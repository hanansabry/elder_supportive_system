package com.android.elderlysupportivesystem.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class Medicine implements Parcelable {

    private String id;
    private String name;
    private String desc;
    private HashMap<String, Conflict> conflicts;

    public Medicine() {
    }

    protected Medicine(Parcel in) {
        id = in.readString();
        name = in.readString();
        desc = in.readString();
        conflicts = (HashMap<String, Conflict>) in.readHashMap(Conflict.class.getClassLoader());
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public HashMap<String, Conflict> getConflicts() {
        return conflicts;
    }

    public void setConflicts(HashMap<String, Conflict> conflicts) {
        this.conflicts = conflicts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeMap(conflicts);
    }
}
