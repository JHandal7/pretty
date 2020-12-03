/*
 * Name: $RCSfile: NetworkUtility.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 3:57:18 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.suusoft.restaurantuser.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.suusoft.restaurantuser.AppController;

/**
 * NetworkUtility checks available network
 *
 * @author Lemon
 */
public class NetworkUtility {

    /**
     * Check network connection
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager conMgr = (ConnectivityManager) AppController.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null) {
            return false;
        }
        if (!i.isConnected()) {
            return false;
        }
        if (!i.isAvailable()) {
            return false;
        }
        return true;
    }


}
