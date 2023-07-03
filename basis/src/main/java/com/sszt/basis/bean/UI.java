package com.sszt.basis.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UI implements Parcelable {
    private String name;

    public UI(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
    }

    protected UI(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<UI> CREATOR = new Creator<UI>() {
        @Override
        public UI createFromParcel(Parcel source) {
            return new UI(source);
        }

        @Override
        public UI[] newArray(int size) {
            return new UI[size];
        }
    };
}
