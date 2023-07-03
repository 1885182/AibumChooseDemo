package com.sszt.basis.bean.command;

public class MsgBean {
    private String msg;
    private String data;
    private int code;

    public MsgBean(String msg, String data) {
        this.msg = msg;
        this.data = data;
    }

    public MsgBean(String data, int code) {
        this.data = data;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
