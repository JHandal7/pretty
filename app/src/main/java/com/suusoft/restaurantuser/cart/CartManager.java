package com.suusoft.restaurantuser.cart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;


/**
 * Created by Suusoft on 26/07/2017.
 */

public abstract class CartManager {
    private static final String ACTION_CART_CHANGE = "ACTION_CART_CHANGE";
    private static final String ACTION_REMOVE_ITEM = "ACTION_REMOVE_ITEM";
    private ArrayList<?> cart;
    private LocalBroadcastManager localBroadcastManager;

    public CartManager(Context context) {
        cart = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public abstract double getTotal();

    public abstract double getTotalDeliveryFee();

    public abstract double getDeliveryFee();

    public abstract void setDeliveryFee(double deliveryFee);

    public abstract ArrayList<Integer> getRiderTips();

    public abstract <T extends Object> double calculatePriceItem(T obj);

    public abstract <T extends Object> double calculatePriceItemSale(T obj);

    public void notifiCartChange() {
        localBroadcastManager.sendBroadcast(new Intent(ACTION_CART_CHANGE));
    }

    public void notifiRemoveItem() {
        localBroadcastManager.sendBroadcast(new Intent(ACTION_REMOVE_ITEM));
    }

    public void registerReceiverCartChange(BroadcastReceiver broadcastReceiver) {
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(ACTION_CART_CHANGE));
    }

    public void registerReceiverRemoveItem(BroadcastReceiver broadcastReceiver) {
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(ACTION_REMOVE_ITEM));
    }

    public void unregisterReceiverCartChange(BroadcastReceiver broadcastReceiver) {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    public <T extends Object> void addToCart(T o) {
        ((ArrayList<T>) cart).add(o);
        notifiCartChange();
    }

    public <T extends Object> void removeFromCart(T o) {
        if (cart.contains(o)) {
            cart.remove(o);
            notifiCartChange();
        }
    }

    public void clearCart() {
        cart.clear();
        notifiCartChange();
    }

    public int countItems() {
        return cart.size();
    }

    public <T extends Object> ArrayList<T> getCart() {
        return (ArrayList<T>) cart;
    }

}
