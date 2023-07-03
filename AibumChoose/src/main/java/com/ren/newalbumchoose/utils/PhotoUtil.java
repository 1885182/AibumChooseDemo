package com.ren.newalbumchoose.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.ren.newalbumchoose.bean.ImageBean;
import com.ren.newalbumchoose.bean.ImageFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by win10 on 2017/6/19.
 */

public class PhotoUtil {
    private static Map<String, ImageFolder> folderMap = new HashMap<>();

    /**
     * 扫描所有图片
     *
     * @param context
     * @return
     */
    public static Map<String, ImageFolder> getPhotos(Context context) {
        String allPhotosKey = "所有图片";
        ImageFolder allFolder = new ImageFolder();
        allFolder.setName(allPhotosKey);
        allFolder.setDirPath(allPhotosKey);
        allFolder.setType(0);
        allFolder.setPhotoList(new ArrayList<ImageBean>());
        folderMap.put(allPhotosKey, allFolder);

        getPhoto(context, allPhotosKey);
        return folderMap;
    }

    /**
     * 扫描所有视频
     *
     * @param context
     * @return
     */
    public static Map<String, ImageFolder> getVideos(Context context) {
        String allVideosKey = "所有视频";
        ImageFolder allFolder = new ImageFolder();
        allFolder.setName(allVideosKey);
        allFolder.setType(1);
        allFolder.setDirPath(allVideosKey);
        allFolder.setPhotoList(new ArrayList<ImageBean>());
        folderMap.put(allVideosKey, allFolder);

        getVideo(context, allVideosKey);

        return folderMap;
    }

    /**
     * 扫描所有图片和视频
     *
     * @param context
     * @return
     */
    public static Map<String, ImageFolder> getPhotosAndVideos(Context context) {


        String allKey = "所有图片和视频";
        ImageFolder allVideoFolder = new ImageFolder();
        allVideoFolder.setName(allKey);
        allVideoFolder.setDirPath(allKey);
        allVideoFolder.setType(10);
        allVideoFolder.setPhotoList(new ArrayList<ImageBean>());
        folderMap.put(allKey, allVideoFolder);

        getPhoto(context, allKey);
        getVideo(context, allKey);

        return folderMap;
    }

    public static void getPhoto(Context context, String allPhotosKey) {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();


        String[] projImage = {MediaStore.Images.Media._ID
                , MediaStore.Images.Media.DATA
                , MediaStore.Images.Media.SIZE
                , MediaStore.Images.Media.DISPLAY_NAME};

        // 只查询jpeg和png的图片 //"image/mp4","image/3gp"
        Cursor mCursor = mContentResolver.query(imageUri, projImage,
                MediaStore.Images.Media.MIME_TYPE + " in(?, ?, ?)",
                new String[]{"image/jpeg", "image/png", "image/jpg"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");


        int pathIndex = mCursor
                .getColumnIndex(MediaStore.Images.Media.DATA);

        if (mCursor.moveToFirst()) {
            do {
                // 获取图片的路径
                String path = mCursor.getString(pathIndex);

                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                //文件地址
                String dirPath = parentFile.getAbsolutePath();

                if (folderMap.containsKey(dirPath)) {
                    ImageBean photo = new ImageBean(path);
                    photo.setVideo(false);
                    ImageFolder photoFolder = folderMap.get(dirPath);
                    photoFolder.getPhotoList().add(photo);
                    folderMap.get(allPhotosKey).getPhotoList().add(photo);
                    continue;
                } else {
                    // 初始化imageFolder
                    ImageFolder photoFolder = new ImageFolder();
                    List<ImageBean> photoList = new ArrayList<>();
                    ImageBean photo = new ImageBean(path);
                    photo.setVideo(false);
                    photoList.add(photo);
                    photoFolder.setPhotoList(photoList);
                    photoFolder.setDirPath(dirPath);
                    photoFolder.setName(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1, dirPath.length()));
                    folderMap.put(dirPath, photoFolder);
                    folderMap.get(allPhotosKey).getPhotoList().add(photo);
                }
            } while (mCursor.moveToNext());
        }
        mCursor.close();
    }

    public static void getVideo(Context context, String allVideoSKey) {
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        String[] projVideo = {MediaStore.Video.Thumbnails._ID
                , MediaStore.Video.Thumbnails.DATA
                , MediaStore.Video.Media.DURATION
                , MediaStore.Video.Media.SIZE
                , MediaStore.Video.Media.DISPLAY_NAME
                , MediaStore.Video.Media.DATE_MODIFIED};

        Cursor mCursor = mContentResolver.query(videoUri, projVideo,
                MediaStore.Video.Media.MIME_TYPE + " in(?, ?, ?, ?)",
                new String[]{"video/mp4", "video/3gp", "video/avi", "video/rmvb"},
                MediaStore.Video.Media.DATE_MODIFIED + " desc");

        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                // 获取视频的路径
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                // 获取该视频的父路径名
                String dirPath = new File(path).getParentFile().getAbsolutePath();

                //存储对应关系
                if (folderMap.containsKey(dirPath)) {
                    ImageBean video = new ImageBean(path);
                    video.setVideo(true);
                    video.setPath(path);
                    video.setType(1);
                    ImageFolder photoFolder = folderMap.get(dirPath);
                    photoFolder.setType(1);
                    photoFolder.getPhotoList().add(video);
                    folderMap.get(allVideoSKey).getPhotoList().add(video);
                } else {
                    ImageFolder photoFolder = new ImageFolder();
                    photoFolder.setType(1);
                    List<ImageBean> photoList = new ArrayList<>();
                    ImageBean video = new ImageBean(path);
                    video.setVideo(true);
                    video.setPath(path);
                    video.setType(1);
                    photoList.add(video);
                    photoFolder.setPhotoList(photoList);
                    photoFolder.setDirPath(dirPath);
                    photoFolder.setName(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1, dirPath.length()));
                    folderMap.put(dirPath, photoFolder);
                    folderMap.get(allVideoSKey).getPhotoList().add(video);
                }

            }
            mCursor.close();
        }
    }
}
