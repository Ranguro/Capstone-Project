package com.example.randall.readit.domain;

import android.os.Parcel;
import android.os.Parcelable;


public class ParcelableScript implements Parcelable {


    private int id;
    private String title;
    private String description;

    public ParcelableScript(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
    }

    public ParcelableScript() {
    }

    protected ParcelableScript(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
    }

    public static final Creator<ParcelableScript> CREATOR = new Creator<ParcelableScript>() {
        @Override
        public ParcelableScript createFromParcel(Parcel source) {
            return new ParcelableScript(source);
        }

        @Override
        public ParcelableScript[] newArray(int size) {
            return new ParcelableScript[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
