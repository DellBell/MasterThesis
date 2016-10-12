package com.thesis.dell.materialtest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.thesis.dell.materialtest.log.L;

import java.util.Date;

/**
 * Created by DellMain on 15.09.2015.
 */
public class Datastream implements Parcelable {

    private String current_value;
    private Date current_valueAt;

    public Datastream(){

    }

    public Datastream(Parcel input){

        current_value = input.readString();
        long dateMillis = input.readLong();
        current_valueAt = (dateMillis == -1 ? null : new Date(dateMillis));
    }

    public Datastream(String current_value, Date current_valueAt)
    {
        this.current_value = current_value;
        this.current_valueAt = current_valueAt;
    }

    public String getCurrent_value() {
        return current_value;
    }

    public Date getCurrent_valueAt() {
        return current_valueAt;
    }

    public void setCurrent_value(String current_value) {
        this.current_value = current_value;
    }

    public void setCurrent_valueAt(Date current_valueAt) {
        this.current_valueAt = current_valueAt;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Datastream> CREATOR = new Parcelable.Creator<Datastream>()
    {
        public Datastream createFromParcel(Parcel in) {
            L.m("create from parcel :Datastream");
            return new Datastream(in);
        }

        public Datastream[] newArray(int size) {
            return new Datastream[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(current_value);
        dest.writeLong(current_valueAt == null ? -1 : current_valueAt.getTime());
    }


}
