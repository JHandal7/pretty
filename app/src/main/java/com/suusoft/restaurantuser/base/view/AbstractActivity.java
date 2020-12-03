package com.suusoft.restaurantuser.base.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.network.ListenerLoading;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.NetworkUtility;

/**
 * Created by Suusoft on 7/10/2016.
 */
public abstract class AbstractActivity extends AppCompatActivity implements ListenerLoading {

    public Context self;

    protected FrameLayout contentLayout;
    protected ProgressBar progressBar;
    // toolbar
    protected Toolbar toolbar;
    protected TextView tvTitle;

    public enum ToolbarType{
        NAVI,    // make screen with toolbar has a button back
        NORMAL, // only toolbar
        MENU_LEFT,// make screen with menu left
        NONE   // none
    }

    /**
     * get toolbar type. Each toolbar type match with a layout
     */
    protected abstract ToolbarType getToolbarType();

    /**
     * This function is called before view is created
     *
     */
    protected abstract void onPrepareCreateView();

    /**
     * Listener network state
     */
    private BroadcastReceiver broadCastNetwork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtility.isNetworkAvailable()){
//                showSnackBar(R.string.msg_network_connected);
            }else{
                showSnackBar(R.string.msg_network_notify);
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self =  this;
        registerNetworkStatesListener();
        BaseRequest.getInstance().setListenerLoading(this);

        onPrepareCreateView();
        setView();
        initViewBase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseRequest.getInstance().setListenerLoading(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCastNetwork);

    }

    @Override
    public void onLoadingIsProcessing() {
        showProgress(true);
    }

    @Override
    public void onLoadingIsCompleted() {
        showProgress(false);
    }

    //================================ view ===============================================

    /**
     * At the moment. There are 4 types layout
     */
    private void setView() {
        if (getToolbarType() == ToolbarType.MENU_LEFT) {
            setContentView(R.layout._base_drawer);
            initToolbar();
        }else if (getToolbarType() == ToolbarType.NAVI){
            setContentView(R.layout._base_nav);
            initToolbar();
            initToolBarNav();
        }else if (getToolbarType() == ToolbarType.NORMAL){
            setContentView(R.layout._base_nav);
            initToolbar();
        }
        else if (getToolbarType() == ToolbarType.NONE){
            setContentView(R.layout._base_content);
        }
    }

    private void initViewBase(){
        contentLayout = (FrameLayout) findViewById(R.id.content_main);
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    /**
     * initialize toolbar
     */
    protected void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) toolbar.findViewById(R.id.tb_tv_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ico_back);

    }

    protected void initToolBarNav(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * set title for toolbar
     */
    public void setToolbarTitle(String title){
        tvTitle.setText(title);
    }

    public void setToolbarTitle(int title){
        tvTitle.setText(getString(title));
    }

    /**
     * Make for toolbar is transparent. and over on view content
     * NOTE: only use for ToolbarType.NAVI
     */
    protected void makeToolbarTransparent(){
        FrameLayout layoutContent = (FrameLayout) findViewById(R.id.lo_content);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layoutContent.getLayoutParams();
        params.setMargins(0,0,0,0);
        toolbar.setBackgroundColor(AppUtil.getColor(self, R.color.transparent));
    }


    /**
     * show snack bar message
     * @param message
     */
    public void showSnackBar(int message){
        Snackbar.make(contentLayout, getString(message), Snackbar.LENGTH_LONG).show();
    }

    public void showSnackBar(String message){
        Snackbar.make(contentLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * show hide progress bar on screen
     * @param isShow true is show
     */
    public void showProgress(boolean isShow){
        if (isShow) {
            if (!progressBar.isShown())
                progressBar.setVisibility(View.VISIBLE);
        }else{
            if (progressBar.isShown())
                progressBar.setVisibility(View.GONE);
        }
    }

    //================================ end view ===========================================

    protected void registerNetworkStatesListener() {
        registerReceiver(broadCastNetwork, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    /**
     * start activity
     * @param clz class name
     */
    public void startActivity(Class<?> clz){
        AppUtil.startActivity(self, clz);
    }

    public void startActivity(Class<?> clz, Bundle bundle){
        AppUtil.startActivity(self, clz, bundle);
    }

    /**
     * show toast message
     * @param message
     */
    public void showToast(String message){
        AppUtil.showToast(self,message);
    }

    public void showToast(int message){
        AppUtil.showToast(self,getString(message));
    }

    /**
     * switch fragment by id default id content
     * @param tag tag of fragment
     * @param fragment destination fragment
     */
    public void switchFragment(String tag, Fragment fragment){
        switchFragment(tag, R.id.content, fragment);
    }
    /**
     * switch fragment
     * @param tag tag of fragment
     * @param content destination view by R.id.
     * @param fragment destination fragment
     */
    public void switchFragment(String tag, int content, Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(content, fragment,tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
}
