package com.interfacciabili.benessere.model;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Esperto implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
