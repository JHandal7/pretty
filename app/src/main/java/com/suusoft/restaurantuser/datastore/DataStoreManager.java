package com.suusoft.restaurantuser.datastore;

import com.google.gson.Gson;
import com.suusoft.restaurantuser.model.User;

/**
 * Created by Suusoft on 9/13/2016.
 */

public class DataStoreManager extends BaseDataStore {


    // ============== User ============================
    private static final String PREF_USER = "PREF_USER";
    private static final String PREF_TOKEN_USER = "PREF_TOKEN_USER";

    // ============== Settings ============================
    private static final String PREF_SETTINGS_NOTIFY = "PREF_SETTINGS_NOTIFY";
    private static final String PREF_SETTINGS_NOTIFY_MESSAGE = "PREF_SETTINGS_NOTIFY_MESSAGE";
    private static final String PREF_IS_LOGIN_SOCIAL = "PREF_IS_LOGIN_SOCIAL";

    /**
     * save and get user
     */
    public static void saveUser(User user) {
        if (user != null) {
            String jsonUser = user.toJSon();
            getInstance().sharedPreferences.putStringValue(PREF_USER, jsonUser);
        } else {
            getInstance().sharedPreferences.putStringValue(PREF_USER, null);
        }
    }

    public static void removeUser() {
        getInstance().sharedPreferences.putStringValue(PREF_USER, null);
    }

    public static User getUser() {
        String jsonUser = BaseDataStore.getInstance().sharedPreferences.getStringValue(PREF_USER);
        User user = new Gson().fromJson(jsonUser, User.class);
        return user;
    }

    /**
     * save and get user's token
     *
     */
    public static void saveToken(String token) {
        getInstance().sharedPreferences.putStringValue(PREF_TOKEN_USER, token);
    }
    public static String getToken() {
        return getInstance().sharedPreferences.getStringValue(PREF_TOKEN_USER);
    }


    /**
     * =======================================================
     * Settings screen
     *
     */
    public static void saveSettingsNotify(boolean isChecked){
        getInstance().sharedPreferences.putBooleanValue(PREF_SETTINGS_NOTIFY, isChecked);
    }

    public static boolean getSettingsNotify(){
        return getInstance().sharedPreferences.getBooleanValue(PREF_SETTINGS_NOTIFY);
    }

    public static void saveSettingsNotifyMessage(boolean isChecked){
        getInstance().sharedPreferences.putBooleanValue(PREF_SETTINGS_NOTIFY_MESSAGE, isChecked);
    }

    public static boolean getSettingsNotifyMessage(){
        return getInstance().sharedPreferences.getBooleanValue(PREF_SETTINGS_NOTIFY_MESSAGE);
    }


    //save is login social
    public static void saveLoginSocial(boolean isLoginSocial){
        getInstance().sharedPreferences.putBooleanValue(PREF_IS_LOGIN_SOCIAL, isLoginSocial);
    }

    public static boolean isLoginSocial(){
        return getInstance().sharedPreferences.getBooleanValue(PREF_IS_LOGIN_SOCIAL);
    }

}
