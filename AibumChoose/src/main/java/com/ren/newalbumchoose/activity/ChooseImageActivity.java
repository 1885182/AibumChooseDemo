package com.ren.newalbumchoose.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.permissionx.guolindev.PermissionX;
import com.ren.newalbumchoose.R;
import com.ren.newalbumchoose.adapter.RecyclerAdapter;
import com.ren.newalbumchoose.adapter.RecyclerTopAdapter;
import com.ren.newalbumchoose.bean.ImageBean;
import com.ren.newalbumchoose.bean.ImageFolder;
import com.ren.newalbumchoose.utils.BarUtils;
import com.ren.newalbumchoose.utils.CustomCrop;
import com.ren.newalbumchoose.utils.DividerGridItemDecoration;
import com.ren.newalbumchoose.utils.ImageRotateUtil;
import com.ren.newalbumchoose.utils.OtherUtils;
import com.ren.newalbumchoose.utils.PhotoUtil;
import com.ren.newalbumchoose.utils.UriParse;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import top.zibin.lubans.Luban;
import top.zibin.lubans.OnNewCompressListener;

/**
 * 选择图片、视频
 */
public class ChooseImageActivity extends AppCompatActivity {

    //拍照回调
    static final int REQUEST_TAKE_PHOTO = 10086;
    //状态返回码
    public static final int CODE = 50;
    public static final int CODE_VIDEO = 51;
    //返回参数key
    public static final String NAME = "images";

    private RecyclerView choose_image_rv;
    private RecyclerAdapter adapterRv;
    private ImageBean selectImageBean;

    private TextView choose_image_title_text;
    private RelativeLayout choose_image_title_frame;
    private Map<String, ImageFolder> mFolderMap;
    private List<ImageBean> mPhotoLists = new ArrayList<>();
    private final static String ALL_PHOTO = "所有图片";
    private final static String ALL_VIDEO = "所有视频";
    private final static String ALL_PHOTO_AND_VIDEO = "所有图片和视频";

    private List<ImageFolder> imageFolders;

    // 0 全显示 1 显示图片 2 显示视频
    private int SHOW_MODE;
    private boolean isSingleChoose = true;//是否单选。默认为true
    private boolean isShowCamera = true;//是否显示相机按钮
    private TextView btn;

    private int degree = 0; //图片现在的旋转方向
    private Uri tempUri;    //选择的图片URi 和拍照URI
    private Uri outUri;     //裁剪后保存的URi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
        initIntentData();
        findViewById();

        new PermissionX().init(this)
                .permissions( Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        getPhotosTask.execute();
                    } else {
                        Toast.makeText(this, "缺少必要权限,功能无法使用", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    Log.e("tagggggg",allGranted+"__--"+deniedList.size()+"---"+deniedList.toString());
                });


    }



    private void initIntentData() {
        SHOW_MODE = getIntent().getIntExtra("showVideo", 1);
        isSingleChoose = getIntent().getBooleanExtra("singleChoose", true);
        isShowCamera = getIntent().getBooleanExtra("isShowCamera", true);
    }

    private int selectPos = -1;

    private void findViewById() {
        choose_image_rv = (RecyclerView) findViewById(R.id.choose_image_rv);

        choose_image_rv.addItemDecoration(new DividerGridItemDecoration(this, 3));

        choose_image_rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapterRv = new RecyclerAdapter(this);
        choose_image_rv.setAdapter(adapterRv);

        adapterRv.setOnItemClick(position -> {

            if (mPhotoLists.get(position).getType() == 2) {
                openCamera();
                return;
            }

            if (selectPos == position) return;
            if (selectPos != -1) {
                mPhotoLists.get(selectPos).setChecked(false);
                adapterRv.notifyItemChanged(selectPos);
            }
            mPhotoLists.get(position).setChecked(true);
            selectImageBean = mPhotoLists.get(position);
            adapterRv.notifyItemChanged(position);
            selectPos = position;
        });


        choose_image_title_text = (TextView) findViewById(R.id.choose_image_title_text);
        choose_image_title_frame = (RelativeLayout) findViewById(R.id.choose_image_title_frame);
        findViewById(R.id.titleBack).setOnClickListener(v -> {
            finish();
        });

        btn = (TextView) findViewById(R.id.btn);

        OtherUtils.addMarginTopEqualStatusBarHeight(choose_image_title_frame);
        BarUtils.setStatusBarColor(this, Color.parseColor("#3292ff"));

        choose_image_title_text.setOnClickListener(view -> {

            if (imageFolders.size() > 1) {
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                } else {
                    showFolderPopopWindow();
                }
            }
        });
        btn.setOnClickListener(view -> {

            if (selectImageBean == null) {
                return;
            }
            /*if (SHOW_MODE == 1) {
                tempUri = Uri.fromFile(new File(selectImageBean.getPath()));
                toChoice(tempUri);
//                new CustomCrop(selectImageBean.getPath(), null, null).startResult(this);
            } else {*/

            luBan(selectImageBean.getPath());
                /*setResult(Activity.RESULT_OK, new Intent().putExtra(NAME, selectImageBean.getPath()));
                finish();*/
            //}
        });
    }

    private void openCamera() {

        new PermissionX().init(this)
                .permissions(Manifest.permission.CAMERA)
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        dispatchTakePictureIntent();
                    } else {

                        Toast.makeText(ChooseImageActivity.this, "缺少必要权限,功能无法使用", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }


    /**
     * 掉用裁剪方法
     *
     * @param headFile
     */
    public void toChoice(Uri headFile) {
        outUri = UriParse.getTempUri(this);
        if (outUri == null) return;
        Log.e("toChoice", "toChoice: " + outUri.getEncodedPath() + "" + outUri.getPath());

        Crop.of(headFile, outUri).asPng(true)/*
                .asSquare().withAspect(500, 500).withMaxSize(500, 500)*/.start(this, Crop.REQUEST_CROP);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Crop.REQUEST_CROP) {
                if (tempUri != null) {
                    String imagePath = UriParse.parseOwnUri(this, tempUri);
                    degree = ImageRotateUtil.of().getBitmapDegree(imagePath);
                }
                ImageRotateUtil.of().correctImage(this, outUri, degree);
                try {
                    String filePathWithUri = UriParse.getFilePathWithUri(outUri, this);

                    luBan(filePathWithUri);
                    /*setResult(Activity.RESULT_OK, new Intent().putExtra(NAME, filePathWithUri));
                    finish();*/
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "图片转换失败" + e, Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == Crop.REQUEST_PICK) {
                Toast.makeText(this, "REQUEST_PICK", Toast.LENGTH_SHORT).show();
            } else if (requestCode == Crop.RESULT_ERROR) {
                Toast.makeText(this, "RESULT_ERROR", Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                String filePathWithUri = null;
                try {
                    filePathWithUri = UriParse.getFilePathWithUri(tempUri, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                luBan(filePathWithUri);
                /*setResult(Activity.RESULT_OK, new Intent().putExtra(NAME, filePathWithUri));
                finish();*/
                /*try {
                    outUri = FileProvider.getUriForFile(this,
                            getApplication().getPackageName() + ".fileprovider",
                            createImageFile());
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "拍照uir获取失败" + e, Toast.LENGTH_SHORT).show();
                }
                if (tempUri == null) return;

                ImageRotateUtil.of().correctImage(this, tempUri);*/
                /* toChoice(tempUri);*/


            } else if (requestCode == CustomCrop.Companion.getCUSTOM_CROP_CODE()) {
                String resultPath = data.getStringExtra("resultPath");
                /*setResult(Activity.RESULT_OK, new Intent().putExtra(NAME, resultPath));
                finish();*/
                luBan(resultPath);
            }

        }

    }

    private void luBan(String path){
        Luban.with(this)  //context
                .load(new File(path))  // 需要压缩的图片file
                .ignoreBy(100)   //压缩率 ，默认100

                .setCompressListener(new OnNewCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI


                    }

                    @Override
                    public void onSuccess(String s,File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
// 这个不是在主线程，跟新ui ，切换到主线程
                        setResult(Activity.RESULT_OK, new Intent().putExtra(NAME, file.getPath()));
                        finish();
                    }

                    @Override
                    public void onError(String s,Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("TAG", "dispatchTakePictureIntent: " + ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                tempUri = FileProvider.getUriForFile(this,
                        getApplication().getPackageName() + ".fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    /**
     * 获取照片的异步任务
     */
    private AsyncTask getPhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
//            mProgressDialog = ProgressDialog.show(PhotoPickerActivity.this, null, "loading...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if (SHOW_MODE == 0) {//显示全部
                mFolderMap = PhotoUtil.getPhotosAndVideos(ChooseImageActivity.this.getApplicationContext());
            } else if (SHOW_MODE == 1) {//仅图片
                mFolderMap = PhotoUtil.getPhotos(ChooseImageActivity.this.getApplicationContext());
            } else if (SHOW_MODE == 2) {//仅视频
                mFolderMap = PhotoUtil.getVideos(ChooseImageActivity.this.getApplicationContext());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };

    /**
     * 加载照片成功
     */
    private void getPhotosSuccess() {

        if (isShowCamera) {
            ImageBean imageBean = new ImageBean("");
            imageBean.setType(2);
            mPhotoLists.add(imageBean);
        }
        if (SHOW_MODE == 0) {//图片和视频
            choose_image_title_text.setText(ALL_PHOTO_AND_VIDEO);
            mPhotoLists.addAll(mFolderMap.get(ALL_PHOTO_AND_VIDEO).getPhotoList());
        } else if (SHOW_MODE == 1) {//仅图片
            choose_image_title_text.setText(ALL_PHOTO);
            mPhotoLists.addAll(mFolderMap.get(ALL_PHOTO).getPhotoList());
        } else if (SHOW_MODE == 2) {//仅视频
            choose_image_title_text.setText(ALL_VIDEO);
            mPhotoLists.addAll(mFolderMap.get(ALL_VIDEO).getPhotoList());
        }

        adapterRv.setData(mPhotoLists);

        Set<String> keys = mFolderMap.keySet();
        final List<ImageFolder> folders = new ArrayList<>();
        for (String key : keys) {
            if (ALL_PHOTO.equals(key) || ALL_PHOTO_AND_VIDEO.equals(key) || ALL_VIDEO.equals(key)) {
                ImageFolder folder = mFolderMap.get(key);
                folder.setIsSelected(true);
                folders.add(0, folder);
            } else {
                folders.add(mFolderMap.get(key));
            }
        }
        imageFolders = folders;
    }

    private BottomSheetBehavior mDialogBehavior;
    private BottomSheetDialog bottomSheetDialog;

    /**
     * 显示相册文件夹popupWindow
     */
    private void showFolderPopopWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.activity_choose_image_list_pop_dir, null);
        bottomSheetDialog = new BottomSheetDialog(this);

        bottomSheetDialog.setContentView(popupWindowView);
        RecyclerView list_pop_rv = (RecyclerView) popupWindowView.findViewById(R.id.list_pop_rv);
        RecyclerTopAdapter topAdp = new RecyclerTopAdapter(this);

        list_pop_rv.setLayoutManager(new LinearLayoutManager(this));

        list_pop_rv.setAdapter(topAdp);

        topAdp.setData(imageFolders);

        bottomSheetDialog.show();
        mDialogBehavior = BottomSheetBehavior.from((View) popupWindowView.getParent());
        mDialogBehavior.setPeekHeight(OtherUtils.getScreenHeight(getApplication()) / 2);//dialog的高度
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.dismiss();
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        topAdp.setOnItemClick(position -> {
            ImageFolder folder = imageFolders.get(position);
            mPhotoLists.clear();
            if (isShowCamera) {
                ImageBean imageBean = new ImageBean("");
                imageBean.setType(2);
                mPhotoLists.add(imageBean);
            }
            mPhotoLists.addAll(folder.getPhotoList());

            adapterRv.setData(mPhotoLists);

            choose_image_title_text.setText(folder.getName());
            bottomSheetDialog.dismiss();
        });

    }

    @Override
    public void finish() {
        super.finish();
        if (mFolderMap != null)
            mFolderMap.clear();
        if (mPhotoLists != null)
            mPhotoLists.clear();
    }


}
