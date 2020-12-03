package com.suusoft.restaurantuser.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.listener.IListenerVisibleKeybroad;
import com.suusoft.restaurantuser.listener.IListenerVisibleView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by Suusoft on 1/8/2016.
 */
public class AppUtil {

    private static final String TAG = "AppUtil";

    ///////////////////////////////////////////////////////////////////////////
    // Start activity
    ///////////////////////////////////////////////////////////////////////////
    public static void startActivity(Context act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
    }

    public static void startActivity(Context act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        act.startActivity(intent);
    }

    public static void enableStrictMode() {
        // Allow strict mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static void startActivityLTR(Activity act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static void startActivityLTR(Activity act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static void startActivityRTL(Activity act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void startActivityRTL(Activity act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, 9000)
                        .show();
            } else {
                Log.i("eee", "This device is not supported.");
                Toast.makeText(activity, "This device is not supported google play", Toast.LENGTH_LONG).show();
                activity.finish();
            }
            return false;
        }
        return true;
    }


    public static boolean checkApi(int api) {
        return Build.VERSION.SDK_INT >= api;
    }

    // Request runtime permissions
    public static boolean checkPermission(Context ctx, String permission) {
        if (checkApi(Build.VERSION_CODES.M)) {
            return ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    /**
     * get IMEI of device
     *
     * @param context
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getMacAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    public static void getFacebookKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e(TAG, "Key hash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * Show hide keyboard
     */
    public static void showKeyboard(Context ctx, EditText editText) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * This method allow closing keyboard when users click out-side(out of edittext)
     *
     * @param act  context
     * @param view root view
     */
    public static void closeKeyboardWhenTouchOutside(final Activity act, View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(act);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                closeKeyboardWhenTouchOutside(act, innerView);
            }
        }
    }

    /**
     * Show toast notify.
     *
     * @param message can hard text or from string file
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, int message) {
        Toast.makeText(context, context.getString(message), Toast.LENGTH_LONG).show();
    }

    /**
     * Get color
     */
    public static int getColor(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static int convertPixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }

    public static int getScreenWidth(Activity act) {
        /* getWidth() is deprecated */
        // Display display = act.getWindowManager().getDefaultDisplay();
        // return display.getWidth();

        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static double getScreenWidthAsInch(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        return Math.sqrt(x + y);
    }

    public static void sendEmail(Context ctx, String email) {
        if (!email.isEmpty() && !email.equals("null")) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            i.putExtra(Intent.EXTRA_SUBJECT, "");
            i.putExtra(Intent.EXTRA_TEXT, "");
            try {
                ctx.startActivity(Intent.createChooser(i, ctx.getString(R.string.send_email)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ctx, ctx.getString(R.string.msg_no_email_client), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ctx, ctx.getString(R.string.msg_no_email), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * call a phone number
     *
     * @param ctx         context.
     * @param phoneNumber phone
     */
    public static void call(Context ctx, String phoneNumber) {
        if (!phoneNumber.isEmpty() && !phoneNumber.equals("null")) {
            String number = "tel:" + phoneNumber;
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
            ctx.startActivity(callIntent);
        } else {
            Toast.makeText(ctx, ctx.getString(R.string.msg_no_phone_number), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Add shortcut to home
     *
     * @param context Context
     * @param tClass  class is called(should be first class of app)
     */
    public static void addShortcut(Context context, Class tClass) {
        //on Home screen
        Intent shortcutIntent = new Intent(context, tClass);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);

        DataStoreManager.setInstalled(true);

    }


    public static void openWebView(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static Object clone(Object o, Class<?> cls) {
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return gson.fromJson(json, cls);
    }

    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public static void searchAddress(Activity activity) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(activity);
            activity.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {

        } catch (GooglePlayServicesNotAvailableException e) {

        }
    }

    public static String formatCurrency(double price) {
        return String.format(Locale.ENGLISH, "%.2f", price);
    }

    /**
     * get height status bar
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * show size screen
     */

    public static void logSizeMultiScreen(Activity activity){
        int w = AppUtil.getScreenWidth(activity);
        int h = AppUtil.getScreenHeight(activity);

        Log.e("screen", "width/height = " + AppUtil.convertPixelsToDp(activity, w)
                + "/" + AppUtil.convertPixelsToDp(activity, h)
                + " dp :::: w/h = " + w + "/" + h + "px --- dpi = "  + (w*160)/AppUtil.convertPixelsToDp(activity, w));
    }

    public static int getWidthDp(Activity activity){
        return convertPixelsToDp(activity, getScreenWidth(activity));
    }

    /*Check show or hide of view*/
    public static void checkViewIsShow(final View viewCheck , final Context context , final IListenerVisibleView iListenerVisibleView){
        viewCheck.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = viewCheck.getRootView().getHeight() - viewCheck.getHeight();
                Log.e("viewCheck", "viewCheck.getRootView().getHeight() " +viewCheck.getRootView().getHeight() + "  viewCheck.getRootView().getHeight() " + viewCheck.getHeight() );

                if (heightDiff > 20) {
                    // ... do something here
                    Log.e("visible", "view Visible");
                    iListenerVisibleView.onVisibleView();
                }else{
                    iListenerVisibleView.onGoneView();
                    Log.e("visible", "view Gone");
                }
            }
        });
    }

    /*Check show or hide of keybroad*/
    public static void checkKeybroadIsShow(final View viewCheck , final Context context , final IListenerVisibleKeybroad iListenerVisibleKeybroad){
        viewCheck.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = viewCheck.getRootView().getHeight() - viewCheck.getHeight();
                Log.e("checkKeybroadIsShow", "viewCheck.getRootView().getHeight() " +viewCheck.getRootView().getHeight() + "  viewCheck.getRootView().getHeight() " + viewCheck.getHeight() );
                if (heightDiff > AppUtil.convertDpToPixel(context, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    iListenerVisibleKeybroad.onShowKeybroad();
                }else iListenerVisibleKeybroad.onHideKeybroad();
            }
        });
    }


}
