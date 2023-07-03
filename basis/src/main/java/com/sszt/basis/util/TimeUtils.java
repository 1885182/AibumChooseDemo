package com.sszt.basis.util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class TimeUtils {
    private String color;//这里可以修改文字颜色
    WeakReference<TextView> tvCodeWr;//控件软引用，防止内存泄漏
    private CountDownTimer timer;


    public TimeUtils(TextView tvCode, String color) {
        super();
        this.tvCodeWr = new WeakReference(tvCode);
        this.color = color;
    }
    //这是倒计时执行方法
    public void RunTimer() {
        timer = new CountDownTimer(60 * 1000 - 1, 1000) {
            @Override
            public void onFinish() {
                if (tvCodeWr.get() != null) {
                    tvCodeWr.get().setText("重新获取");
                    tvCodeWr.get().setTextColor(Color.parseColor(color));
                    tvCodeWr.get().setClickable(true);
                    tvCodeWr.get().setEnabled(true);
                }

                cancel();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (tvCodeWr.get() != null) {
                    tvCodeWr.get().setClickable(false);
                    tvCodeWr.get().setEnabled(false);
                    tvCodeWr.get().setText(millisUntilFinished / 1000 + "s");
                    tvCodeWr.get().setTextColor(Color.parseColor("#999999"));
                }
            }
        }.start();
    }
    //这个方法可以在activity或者fragment销毁的时候调用，防止内存泄漏
    public void cancle() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
