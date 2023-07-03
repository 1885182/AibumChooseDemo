package com.ren.newalbumchoose.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by win10 on 2017/6/19.
 */

public class ImageFolder implements Serializable {
    /* 文件夹名 */
    private String name;
    /* 文件夹路径 */
    private String dirPath;
    /* 该文件夹下图片列表 */
    private List<ImageBean> photoList;
    /* 标识是否选中该文件夹 */
    private boolean isSelected;

    private int type;//类型

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public List<ImageBean> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<ImageBean> photoList) {
        this.photoList = photoList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
