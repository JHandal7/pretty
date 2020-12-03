package com.suusoft.restaurantuser.listener;

import android.support.annotation.NonNull;

/**
 * Created by Suusoft on 12/09/2017.
 */

public interface IRequestPermissions {
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
