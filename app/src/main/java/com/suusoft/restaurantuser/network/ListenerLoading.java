package com.suusoft.restaurantuser.network;

import com.suusoft.restaurantuser.listener.IBaseListener;

/**
 * Created by suusoft.com on 1/26/18.
 */

public interface ListenerLoading extends IBaseListener {
    void onLoadingIsProcessing();
    void onLoadingIsCompleted();
}
