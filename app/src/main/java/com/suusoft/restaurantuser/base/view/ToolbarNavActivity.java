package com.suusoft.restaurantuser.base.view;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.util.DialogUtil;

/**
 * Created by Suusoft on 2/25/2017.
 */

public abstract class ToolbarNavActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    protected Menu menu;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.MENU_LEFT;
    }

    @Override
    protected void initBeforeChildView() {
        initNavigationView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initNavigationView(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                switchMenu();
                switchScreen();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    /**
     * Menu left onClick and switch fragment
     * using for child view which extend from this
     */
    public void switchScreen() {
        // TODO: 2/25/2017
    }

    /**
     * Set menu for screen
     * using for child view which extend from this
     */
    public void switchMenu() {
        // TODO: 2/25/2017
    }

    public void showDialogWhenExit() {
        DialogUtil.showAlertDialog(this, R.string.msg_confirm_exit, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                DataStoreManager.removeUser();
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showDialogWhenExit();
        }

    }

}
