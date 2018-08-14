package com.jqscp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.jqscp.Util.RxJavaUtils.RxBusType;


/**
 * RxBus 携带bean对象
 */

public class RxBusBean implements Parcelable {
    private RxBusType type;
    private String content;
    private String flag;
    private int intType;
    private Object mObject;



    public RxBusBean() {
    }
    public RxBusBean(RxBusType type) {
        this.type = type;
    }
    public RxBusBean(RxBusType type, String content) {
        this.type = type;
        this.content = content;
    }

    public RxBusBean(RxBusType type, Object object) {
        this.type = type;
        mObject = object;
    }

    public RxBusBean(RxBusType type, String flag, String content) {
        this.type = type;
        this.content = content;
        this.flag = flag;
    }

    public RxBusBean(RxBusType type, int intType, Object object) {
        this.type = type;
        this.intType = intType;
        mObject = object;
    }

    public RxBusBean(RxBusType type, int intType) {
        this.type = type;
        this.intType = intType;
    }

    public int getIntType() {
        return intType;
    }

    public void setIntType(int intType) {
        this.intType = intType;
    }

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object object) {
        mObject = object;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public RxBusType getType() {
        return type;
    }

    public void setType(RxBusType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.content);
        dest.writeString(this.flag);
    }

    protected RxBusBean(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : RxBusType.values()[tmpType];
        this.content = in.readString();
        this.flag = in.readString();
    }

    public static final Creator<RxBusBean> CREATOR = new Creator<RxBusBean>() {
        @Override
        public RxBusBean createFromParcel(Parcel source) {
            return new RxBusBean(source);
        }

        @Override
        public RxBusBean[] newArray(int size) {
            return new RxBusBean[size];
        }
    };
}
