package com.example.ananttandon.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jim.
 */

public final class ModuleInfo implements Parcelable{
    public static final Creator<ModuleInfo> CREATOR = new ClassLoaderCreator<ModuleInfo>() {
        @Override
        public ModuleInfo createFromParcel(Parcel source, ClassLoader loader) {
            return null;
        }

        @Override
        public ModuleInfo createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public ModuleInfo[] newArray(int size) {
            return new ModuleInfo[0];
        }
    };
    private final String mModuleId;
    private final String mTitle;
    private boolean mIsComplete = false;

    public ModuleInfo(String moduleId, String title) {
        this(moduleId, title, false);
    }

    public ModuleInfo(String moduleId, String title, boolean isComplete) {
        mModuleId = moduleId;
        mTitle = title;
        mIsComplete = isComplete;
    }

    public String getModuleId() {
        return mModuleId;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isComplete() {
        return mIsComplete;
    }

    public void setComplete(boolean complete) {
        mIsComplete = complete;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModuleInfo that = (ModuleInfo) o;

        return mModuleId.equals(that.mModuleId);
    }

    @Override
    public int hashCode() {
        return mModuleId.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
