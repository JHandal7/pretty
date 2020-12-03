package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.suusoft.restaurantuser.base.model.BaseModel;

/**
 * Created by Suusoft on 21/08/2017.
 */

public class OpenHour implements Parcelable {
    private String id;
    private String open_time;
    private String close_time;
    private String weekday;
    private int is_active;


    protected OpenHour(Parcel in) {
        id = in.readString();
        open_time = in.readString();
        close_time = in.readString();
        weekday = in.readString();
        is_active = in.readInt();

    }

    public static final Creator<OpenHour> CREATOR = new Creator<OpenHour>() {
        @Override
        public OpenHour createFromParcel(Parcel in) {
            return new OpenHour(in);
        }

        @Override
        public OpenHour[] newArray(int size) {
            return new OpenHour[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public boolean getIs_active() {
        return is_active == 1 ? true : false;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(open_time);
        parcel.writeString(close_time);
        parcel.writeString(weekday);
        parcel.writeInt(is_active);

    }
}
