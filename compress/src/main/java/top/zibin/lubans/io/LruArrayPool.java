package top.zibin.lubans.io;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * @author：luck
 * @date：2021/8/26 3:15 下午
 * @describe：LruArrayPool
 */
public final class LruArrayPool implements ArrayPool {
    // 4MB.
    public static final int DEFAULT_SIZE = 4 * 1024 * 1024;

    /**
     * The maximum number of times larger an int array may be to be than a requested size to eligible
     * to be returned from the pool.
     */
    static final int MAX_OVER_SIZE_MULTIPLE = 8;
    /**
     * Used to calculate the maximum % of the total pool size a single byte array may consume.
     */
    private static final int SINGLE_ARRAY_MAX_SIZE_DIVISOR = 2;

    private final GroupedLinkedMap<Key, Object> groupedMap = new GroupedLinkedMap<>();
    private final KeyPool keyPool = new KeyPool();
    private final Map<Class<?>, NavigableMap<Integer, Integer>> sortedSizes = new HashMap<>();
    private final Map<Class<?>, ArrayAdapterInterface<?>> adapters = new HashMap<>();
    private final int maxSize;
    private int currentSize;

    public LruArrayPool() {
        maxSize = DEFAULT_SIZE;
    }

    /**
     * Constructor for a new pool.
     *
     * @param maxSize The maximum size in integers of the pool.
     */
    public LruArrayPool(int maxSize) {
        this.maxSize = maxSize;
    }

    @Deprecated
    @Override
    public <T> void put(T array, Class<T> arrayClass) {
        put(array);
    }

    @Override
    public synchronized <T> void put(T array) {
        @SuppressWarnings("unchecked")
        Class<T> arrayClass = (Class<T>) array.getClass();

        ArrayAdapterInterface<T> arrayAdapter = getAdapterFromType(arrayClass);
        int size = arrayAdapter.getArrayLength(array);
        int arrayBytes = size * arrayAdapter.getElementSizeInBytes();
        if (!isSmallEnoughForReuse(arrayBytes)) {
            return;
        }
        Key key = keyPool.get(size, arrayClass);

        groupedMap.put(key, array);
        NavigableMap<Integer, Integer> sizes = getSizesForAdapter(arrayClass);
        Integer current = sizes.get(key.size);
        sizes.put(key.size, current == null ? 1 : current + 1);
        currentSize += arrayBytes;
        evict();
    }


    @Override
    public synchronized <T> T get(int size, Class<T> arrayClass) {
        Integer possibleSize = getSizesForAdapter(arrayClass).ceilingKey(size);
        final Key key;
        if (mayFillRequest(size, possibleSize)) {
            key = keyPool.get(possibleSize, arrayClass);
        } else {
            key = keyPool.get(size, arrayClass);
        }
        return getForKey(key, arrayClass);
    }

    private <T> T getForKey(Key key, Class<T> arrayClass) {
        ArrayAdapterInterface<T> arrayAdapter = getAdapterFromType(arrayClass);
        T result = getArrayForKey(key);
        if (result != null) {
            currentSize -= arrayAdapter.getArrayLength(result) * arrayAdapter.getElementSizeInBytes();
            decrementArrayOfSize(arrayAdapter.getArrayLength(result), arrayClass);
        }

        if (result == null) {
            if (Log.isLoggable(arrayAdapter.getTag(), Log.VERBOSE)) {
                Log.v(arrayAdapter.getTag(), "Allocated " + key.size + " bytes");
            }
            result = arrayAdapter.newArray(key.size);
        }
        return result;
    }

    // Our cast is safe because the Key is based on the type.
    @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
    private <T> T getArrayForKey(Key key) {
        return (T) groupedMap.get(key);
    }

    private boolean isSmallEnoughForReuse(int byteSize) {
        return byteSize <= maxSize / SINGLE_ARRAY_MAX_SIZE_DIVISOR;
    }

    private boolean mayFillRequest(int requestedSize, Integer actualSize) {
        return actualSize != null
                && (isNoMoreThanHalfFull() || actualSize <= (MAX_OVER_SIZE_MULTIPLE * requestedSize));
    }

    private boolean isNoMoreThanHalfFull() {
        return currentSize == 0 || (maxSize / currentSize >= 2);
    }

    @Override
    public synchronized void clearMemory() {
        evictToSize(0);
    }


    private void evict() {
        evictToSize(maxSize);
    }

    @SuppressLint("RestrictedApi")
    private void evictToSize(int size) {
        while (currentSize > size) {
            Object evicted = groupedMap.removeLast();
            ArrayAdapterInterface<Object> arrayAdapter = getAdapterFromObject(evicted);
            currentSize -= arrayAdapter.getArrayLength(evicted) * arrayAdapter.getElementSizeInBytes();
            decrementArrayOfSize(arrayAdapter.getArrayLength(evicted), evicted.getClass());
            if (Log.isLoggable(arrayAdapter.getTag(), Log.VERBOSE)) {
                Log.v(arrayAdapter.getTag(), "evicted: " + arrayAdapter.getArrayLength(evicted));
            }
        }
    }

    private void decrementArrayOfSize(int size, Class<?> arrayClass) {
        NavigableMap<Integer, Integer> sizes = getSizesForAdapter(arrayClass);
        Integer current = sizes.get(size);
        if (current == null) {
            throw new NullPointerException(
                    "Tried to decrement empty size" + ", size: " + size + ", this: " + this);
        }
        if (current == 1) {
            sizes.remove(size);
        } else {
            sizes.put(size, current - 1);
        }
    }

    private NavigableMap<Integer, Integer> getSizesForAdapter(Class<?> arrayClass) {
        NavigableMap<Integer, Integer> sizes = sortedSizes.get(arrayClass);
        if (sizes == null) {
            sizes = new TreeMap<>();
            sortedSizes.put(arrayClass, sizes);
        }
        return sizes;
    }

    @SuppressWarnings("unchecked")
    private <T> ArrayAdapterInterface<T> getAdapterFromObject(T object) {
        return (ArrayAdapterInterface<T>) getAdapterFromType(object.getClass());
    }

    @SuppressWarnings("unchecked")
    private <T> ArrayAdapterInterface<T> getAdapterFromType(Class<T> arrayPoolClass) {
        ArrayAdapterInterface<?> adapter = adapters.get(arrayPoolClass);
        if (adapter == null) {
            if (arrayPoolClass.equals(int[].class)) {
                adapter = new IntegerArrayAdapter();
            } else if (arrayPoolClass.equals(byte[].class)) {
                adapter = new ByteArrayAdapter();
            } else {
                throw new IllegalArgumentException(
                        "No array pool found for: " + arrayPoolClass.getSimpleName());
            }
            adapters.put(arrayPoolClass, adapter);
        }
        return (ArrayAdapterInterface<T>) adapter;
    }

    // VisibleForTesting
    int getCurrentSize() {
        int currentSize = 0;
        for (Class<?> type : sortedSizes.keySet()) {
            for (Integer size : sortedSizes.get(type).keySet()) {
                ArrayAdapterInterface<?> adapter = getAdapterFromType(type);
                currentSize += size * sortedSizes.get(type).get(size) * adapter.getElementSizeInBytes();
            }
        }
        return currentSize;
    }

    private static final class KeyPool extends BaseKeyPool<Key> {

        KeyPool() {
        }

        Key get(int size, Class<?> arrayClass) {
            Key result = get();
            result.init(size, arrayClass);
            return result;
        }

        @Override
        protected Key create() {
            return new Key(this);
        }
    }

    private static final class Key implements PoolAble {
        private final KeyPool pool;
        int size;
        private Class<?> arrayClass;

        Key(KeyPool pool) {
            this.pool = pool;
        }

        void init(int length, Class<?> arrayClass) {
            this.size = length;
            this.arrayClass = arrayClass;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Key) {
                Key other = (Key) o;
                return size == other.size && arrayClass == other.arrayClass;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Key{" + "size=" + size + "array=" + arrayClass + '}';
        }

        @Override
        public void offer() {
            pool.offer(this);
        }

        @Override
        public int hashCode() {
            int result = size;
            result = 31 * result + (arrayClass != null ? arrayClass.hashCode() : 0);
            return result;
        }
    }
}
