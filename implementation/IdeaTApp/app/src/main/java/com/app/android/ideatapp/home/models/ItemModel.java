package com.app.android.ideatapp.home.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemModel implements Parcelable {

    private long id;
    private String title;
    private String date;
    private String time;
    private String status;
    private String source;

    public ItemModel() {}

    public ItemModel(Parcel in) {
        this.title = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.status = "PENDING";
    }

    public ItemModel(String title, String source) {
        this.title = title;
        this.source = source;
        this.status = "PENDING";
    }

    public ItemModel(String title, String date, String time, String source) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.status = "PENDING";
        this.source = source;
    }

    public ItemModel(String title, String source, String status, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.status = status;
        this.source = source;
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(time);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
