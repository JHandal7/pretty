package com.suusoft.restaurantuser.social.facebook;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Suusoft on 6/16/2016.
 */
public interface IFacebookConfig {

    /**
     * login permission
     */
    public static final List<String> LOGIN_PERMISSIONS = Arrays.asList("public_profile");
    public static final String KEY_VALUES_PROFILE = "id,name,last_name,first_name,email,picture,gender";

}
