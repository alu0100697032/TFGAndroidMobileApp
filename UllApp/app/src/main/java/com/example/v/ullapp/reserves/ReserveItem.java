package com.example.v.ullapp.reserves;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 13/08/2016.
 */
public class ReserveItem implements Parcelable{
    int reserveId;
    String courtType, courtName, date;

    public ReserveItem(int id, String type, String name, String date){
        this.reserveId = id;
        this.courtType = type;
        this.courtName = name;
        this.date = date;
    }

    public int getReserveId(){return reserveId;}
    public String getCourtType(){return courtType;}
    public String getCourtName(){return courtName;}
    public String getDate(){return date;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reserveId);
        dest.writeString(courtType);
        dest.writeString(courtName);
        dest.writeString(date);
    }

    public static final Parcelable.Creator<ReserveItem> CREATOR = new Parcelable.Creator<ReserveItem>() {

        @Override
        public ReserveItem createFromParcel(Parcel source) {
            return new ReserveItem(source);
        }

        @Override
        public ReserveItem[] newArray(int size) {
            return new ReserveItem[size];
        }

    };

    private ReserveItem(Parcel source) {
        this.reserveId = source.readInt();
        this.courtType = source.readString();
        this.courtName = source.readString();
        this.date = source.readString();
    }
}
