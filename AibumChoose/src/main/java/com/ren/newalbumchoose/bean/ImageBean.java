package com.ren.newalbumchoose.bean;

import java.io.Serializable;

/**
 * Created by win10 on 2017/6/19.
 */

public class ImageBean implements Serializable {
    private int id;
    private String path;  //路径
    private boolean isVideo;//是否为视频
    private boolean isChecked = false;//是否选中
    private int type;//类型

    public Object getTragetHolder() {
        return tragetHolder;
    }

    public void setTragetHolder(Object tragetHolder) {
        this.tragetHolder = tragetHolder;
    }

    private Object tragetHolder = null;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public ImageBean(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
