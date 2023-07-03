package com.sszt.basis.ext

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.sszt.basis.base.BaseParameters
import com.sszt.basis.ext.util.loge
import com.sszt.basis.util.FileRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * RequestBody 请求传参 bizPara 不需要套多成
 *
 */
fun Any.anyToBody(): RequestBody {

    val base = BaseParameters()
    base.bizPara = this
    val json = Gson().toJson(base)

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

}

fun FragmentActivity.emptyToBody(): RequestBody {

    val base = BaseParameters()
    val json = Gson().toJson(base)

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

}

fun Fragment.emptyToBody(): RequestBody {

    val base = BaseParameters()
    val json = Gson().toJson(base)

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

}

/**
 * 分页查询获取数据
 * @param pageIndex 页数
 * @param pageSize 每页几条
 */
fun FragmentActivity.requestForPage(
    pageIndex: Int = 1,
    pageSize: Int = 10,
    vararg bizPara: Pair<String, Any?>
): RequestBody {

    val map = HashMap<String, Int>()
    map["pageIndex"] = pageIndex
    map["pageSize"] = pageSize

    val base = BaseParameters()
    base.bizPara = bizPara.pairToMap()
    base.pagePara = map
    val json = Gson().toJson(base)

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
}

/**
 * 普通传参
 */
fun FragmentActivity.requestForParameter(
    vararg bizPara: Pair<String, Any?>
): RequestBody {

    val base = BaseParameters()
    base.bizPara = bizPara.pairToMap()
    val json = Gson().toJson(base)

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
}

/**
 * 不通传参
 */
fun FragmentActivity.requestForBody(
    vararg bizPara: Pair<String, Any?>
): RequestBody {

    "${bizPara.pairToMap()}".loge()

    val json = Gson().toJson(bizPara.pairToMap())

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
}
/**
 * 不通传参
 */
fun requestForBody(
    vararg bizPara: Pair<String, Any?>
): RequestBody {

    "${bizPara.pairToMap()}".loge()

    val json = Gson().toJson(bizPara.pairToMap())

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
}

/**
 * 用来解决反转移的  /
 * @receiver FragmentActivity
 * @param json String
 * @return RequestBody
 */
fun FragmentActivity.requestForStringBody(
    json: String
): RequestBody {

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
}

/**
 * 不通传参
 */
fun Fragment.requestForParameter(
    vararg bizPara: Pair<String, Any?>
): RequestBody {

    val base = BaseParameters()
    base.bizPara = bizPara.pairToMap()
    val json = Gson().toJson(base)

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
}

/**
 * 分页查询获取数据
 * @param pageIndex 页数
 * @param pageSize 每页几条
 */
fun Fragment.requestForPage(
    pageIndex: Int = 1,
    pageSize: Int = 10,
    vararg bizPara: Pair<String, Any?>
): RequestBody {

    val map = HashMap<String, Int>()
    map.put("pageNo", pageIndex)
    map.put("pageSize", pageSize)

    val base = BaseParameters()
    base.bizPara = bizPara.pairToMap()
    base.pagePara = map
    val json = Gson().toJson(base)

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
}

/**
 * RequestBody 请求传参 bizPara 需要套多成
 */
fun Activity.requestForPage(
    vararg bizPara: Pair<String, Any>
): RequestBody {

    val base = BaseParameters()
    base.bizPara = bizPara.pairToMap()
    val json = Gson().toJson(base)

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

}

/**
 * RequestBody 请求传参 bizPara 需要套多成
 */
fun requestFile(
    file: File
): RequestBody {

    val body: RequestBody =
        RequestBody.create(MediaType.parse("image/*"), file)


    val requestBody =
        MultipartBody.Builder().setType(MultipartBody.FORM)

    requestBody.addFormDataPart("file", file.getName(), body)

    return requestBody.build()

}
/**
 * RequestBody 请求传参 bizPara 需要套多成
 */
fun requestFileAll(
    file: File
): RequestBody {

    val body: RequestBody =
        RequestBody.create(MediaType.parse("multipart/form-data"), file)


    val requestBody =
        MultipartBody.Builder().setType(MultipartBody.FORM)

    requestBody.addFormDataPart("file", file.getName(), body)

    return requestBody.build()

}

/**
 * RequestBody 请求传参 bizPara 需要套多成
 */
fun Activity.requestVideoFile(
    file: File
): RequestBody {

    val body: RequestBody =
        RequestBody.create(MediaType.parse("video/*"), file)
    val requestBody =
        MultipartBody.Builder().setType(MultipartBody.FORM)
    requestBody.addFormDataPart("file", file.getName(), body)

    return requestBody.build()

}

/**
 * RequestBody 请求传参 bizPara 需要套多成
 */
fun Activity.requestFileVideo(
    file: File
): RequestBody {

    val body: RequestBody =
        RequestBody.create(MediaType.parse("video/*"), file)
    val requestBody =
        MultipartBody.Builder().setType(MultipartBody.FORM)
    requestBody.addFormDataPart("file", file.getName(), body)

    return requestBody.build()

}
fun Activity.requestFileAll(
    file: File?
): RequestBody {
    val body: RequestBody =
        RequestBody.create(MediaType.parse("video/*;*/*"), file)
    val requestBody =
        MultipartBody.Builder().setType(MultipartBody.FORM)
    requestBody.addFormDataPart("file", file?.name, body)

    return requestBody.build()

}

/**
 * 上传图片
 */
fun Activity.requestFileBig(
    file: File,
    callBack: (progress: Int) -> Unit
): RequestBody {
    val body: RequestBody =
        RequestBody.create(MediaType.parse("image/*"), file)
    val requestBody =
        MultipartBody.Builder().setType(MultipartBody.FORM)

    requestBody.addFormDataPart("file", file.getName(), body)

    val fileBody = FileRequestBody(
        requestBody.build()
    ) { currentLength, contentLength ->
        Log.e(
            "requestFileBig",
            "onProgress: 进度---$currentLength===$contentLength ${
                currentLength.times(1.0).div(contentLength).times(100.0)
                    .toInt()
            }%"
        )
        callBack.invoke(
            currentLength.times(1.0).div(contentLength).times(100.0)
                .toInt()
        )
    }


    return fileBody

}


/**
 * RequestBody 请求传参 bizPara 需要套多成
 */
fun Fragment.requestForBody(
    vararg bizPara: Pair<String, Any?>
): RequestBody {
    val json = Gson().toJson(bizPara.pairToMap())

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

}

fun Fragment.requestForPage(
    vararg bizPara: Pair<String, Any>
): RequestBody {

    val base = BaseParameters()
    base.bizPara = bizPara.pairToMap()
    val json = Gson().toJson(base)

    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

}

/**
 * Pair 转Map
 */
fun Activity.valueToMap(vararg pair: Pair<String, Any?>) = pair.pairToMap()

/**
 * Pair 转Map
 */
fun Fragment.valueToMap(vararg pair: Pair<String, Any?>) = pair.pairToMap()


/**
 *Pair 转 Map
 */
fun Array<out Pair<String, Any?>>.pairToMap(): Map<String, Any?> {

    val map = HashMap<String, Any?>()
    this.forEach {

        map.put(it.first, it.second)
    }

    return map
}