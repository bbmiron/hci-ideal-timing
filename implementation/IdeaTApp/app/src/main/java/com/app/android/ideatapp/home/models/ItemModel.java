package com.app.android.ideatapp.home.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemModel implements Parcelable {

    private String title;
    private String subtitle;
    private String date;
    private String time;

    public ItemModel() {}

    public ItemModel(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        date = in.readString();
        time = in.readString();
    }

    public ItemModel(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public ItemModel(String title, String subtitle, String date, String time) {
        this.title = title;
        this.subtitle = subtitle;
        this.date = date;
        this.time = time;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(subtitle);
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
}
