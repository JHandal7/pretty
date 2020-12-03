package com.suusoft.restaurantuser.main.myaccount.about;

import android.content.Context;

import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.model.OpenHour;

/**
 * Created by Suusoft on 24/08/2017.
 */

public class ItemOpenHourVM extends BaseAdapterVM {
    private OpenHour model;

    public ItemOpenHourVM(Context self, Object o) {
        super(self);
        model = (OpenHour) o;
    }

    @Override
    public void setData(Object object) {
        model = (OpenHour) object;
        notifyChange();
    }

    public String getOpenTime() {
        String hhmm = model.getOpen_time();
        String[] arrHHMM = hhmm.split(":");
        hhmm = arrHHMM[0] + ":" + arrHHMM[1];
        return hhmm;
    }

    public String getCloseTime() {
        String hhmm = model.getClose_time();
        String[] arrHHMM = hhmm.split(":");
        hhmm = arrHHMM[0] + ":" + arrHHMM[1];
        return hhmm;
    }

    public String getStatus() {
        if (model.getIs_active()) {
            return "Active";
        } else {
            return "Inactive";
        }
    }
public String getStatusStore() {
    if (model.getIs_active()) {
        return "Openning";
    } else {
        return "Closed";
    }
}
    public String getDateOfWeek() {
        return model.getWeekday();
    }
}
