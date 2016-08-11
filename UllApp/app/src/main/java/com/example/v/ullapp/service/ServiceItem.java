package com.example.v.ullapp.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 11/08/2016.
 */
public class ServiceItem implements Parcelable {
    int courtId;
    String courtType, courtName;

    public ServiceItem(int id, String type, String name){
        this.courtId = id;
        this.courtType = type;
        this.courtName = name;
    }

    public int getCourtId(){return courtId;}
    public String getCourtType(){return courtType;}
    public String getCourtName(){return courtName;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(courtId);
        dest.writeString(courtType);
        dest.writeString(courtName);
    }

    public static final Parcelable.Creator<ServiceItem> CREATOR = new Parcelable.Creator<ServiceItem>() {

        @Override
        public ServiceItem createFromParcel(Parcel source) {
            return new ServiceItem(source);
        }

        @Override
        public ServiceItem[] newArray(int size) {
            return new ServiceItem[size];
        }

    };

    private ServiceItem(Parcel source) {
        this.courtId = source.readInt();
        this.courtType = source.readString();
        this.courtName = source.readString();
    }

}
