package com.suusoft.restaurantuser.main.settings;

import android.content.Context;
import android.databinding.ObservableBoolean;

import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.datastore.DataStoreManager;

/**
 * Created by Suusoft on 9/14/2016.
 */

public class SettingsVM extends BaseViewModel {

    public ObservableBoolean isCheckedNotify, isCheckedMessage;

    public SettingsVM(Context self) {
        super(self);
        isCheckedMessage = new ObservableBoolean();
        isCheckedNotify = new ObservableBoolean();

        isCheckedNotify.set(DataStoreManager.getSettingsNotify());
        isCheckedMessage.set(DataStoreManager.getSettingsNotifyMessage());
    }

    @Override
    public void destroy() {
        DataStoreManager.saveSettingsNotify(isCheckedNotify.get());
        DataStoreManager.saveSettingsNotifyMessage(isCheckedMessage.get());
    }

    @Override
    public void getData(int page) {

    }
}
