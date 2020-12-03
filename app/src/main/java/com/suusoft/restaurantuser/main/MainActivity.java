package com.suusoft.restaurantuser.main;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.SingleFragment;
import com.suusoft.restaurantuser.base.view.ToolbarNavActivity;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.main.about.AboutVM;
import com.suusoft.restaurantuser.main.feedback.FeedBackVM;
import com.suusoft.restaurantuser.main.help.HelpCenterFragment;
import com.suusoft.restaurantuser.main.user.LoginActivity;
import com.suusoft.restaurantuser.main.home.HomeFragment;
import com.suusoft.restaurantuser.main.menu.PaymentUserInforVM;
import com.suusoft.restaurantuser.main.settings.SettingsVM;

public class MainActivity extends ToolbarNavActivity implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener{

    private Fragment mCurrenFragment;
    private Menu mMenu;

    /**
     * Position of fragment selected. set when item of left menu clicked
     */
    private FragmentPosition mCurFragSeleted = FragmentPosition.FRAGMENT_HOME;

    /**
     *  List of fragment by position
     */
    private enum FragmentPosition{
        FRAGMENT_HOME,
        FRAGMENT_CATEGORY,
        FRAGMENT_PROFILE,
        FRAGMENT_SETTINGS,
        FRAGMENT_MORE,
        FRAGMENT_FEEDBACK,
        FRAGMENT_ABOUT,
        FRAGMENT_HElP
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.home);
        switchScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        if (mCurFragSeleted == FragmentPosition.FRAGMENT_HOME) {
            MenuItem searchItem = menu.findItem(R.id.action_seach);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(this);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        showToast("starting search");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     * Menu left onClick and switch fragment
     */
    @Override
    public void switchScreen() {
        String tag = "frag_" + mCurFragSeleted;
        mCurrenFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (mCurrenFragment == null) {
            if (mCurFragSeleted == FragmentPosition.FRAGMENT_HOME) {
                mCurrenFragment = HomeFragment.newInstance();
            } else if (mCurFragSeleted == FragmentPosition.FRAGMENT_CATEGORY) {

            } else if (mCurFragSeleted == FragmentPosition.FRAGMENT_MORE) {
                mCurrenFragment = SingleFragment.newInstance(R.layout.layout_payment_shipping, PaymentUserInforVM.class);
            } else if (mCurFragSeleted == FragmentPosition.FRAGMENT_SETTINGS) {
                mCurrenFragment = SingleFragment.newInstance(R.layout.fragment_settings, SettingsVM.class);
            }else if (mCurFragSeleted == FragmentPosition.FRAGMENT_FEEDBACK) {
                mCurrenFragment = SingleFragment.newInstance(R.layout.fragment_feedback, FeedBackVM.class);
            }else if (mCurFragSeleted == FragmentPosition.FRAGMENT_HElP) {
                mCurrenFragment = HelpCenterFragment.newInstance();
            } else if (mCurFragSeleted == FragmentPosition.FRAGMENT_ABOUT) {
                mCurrenFragment = SingleFragment.newInstance(R.layout.fragment_about, AboutVM.class);
            }
        }
        switchFragment(tag,mCurrenFragment);
    }

    /**
     * Set menu for screen
     */
    @Override
    public void switchMenu() {
        if (mMenu != null) {
            mMenu.clear();
            if (mCurFragSeleted == FragmentPosition.FRAGMENT_HOME) {
                getMenuInflater().inflate(R.menu.main, mMenu);
            } else if (mCurFragSeleted == FragmentPosition.FRAGMENT_CATEGORY) {

            } else if (mCurFragSeleted == FragmentPosition.FRAGMENT_MORE) {
                getMenuInflater().inflate(R.menu.profile, mMenu);
            } else if (mCurFragSeleted == FragmentPosition.FRAGMENT_SETTINGS) {
                getMenuInflater().inflate(R.menu.profile, mMenu);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            setToolbarTitle(R.string.home);
            mCurFragSeleted = FragmentPosition.FRAGMENT_HOME;
        } else if (id == R.id.nav_profile) {
            setToolbarTitle(R.string.profile);
            mCurFragSeleted = FragmentPosition.FRAGMENT_HOME;
        } else if (id == R.id.nav_settings) {
            setToolbarTitle(R.string.settings);
            mCurFragSeleted = FragmentPosition.FRAGMENT_SETTINGS;
        } else if (id == R.id.nav_feedback){
            setToolbarTitle(R.string.feedback);
            mCurFragSeleted = FragmentPosition.FRAGMENT_FEEDBACK;
        }else if (id == R.id.nav_about_us){
            setToolbarTitle(R.string.about_us);
            mCurFragSeleted = FragmentPosition.FRAGMENT_ABOUT;
        }else if (id == R.id.nav_help){
            setToolbarTitle(R.string.help);
            mCurFragSeleted = FragmentPosition.FRAGMENT_HElP;
        }else if (id == R.id.nav_more) {
            setToolbarTitle("Two ways & Observerable");
            mCurFragSeleted = FragmentPosition.FRAGMENT_MORE;
        }else if (id == R.id.nav_logout){
            logOut();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        DataStoreManager.removeUser();
        startActivity(LoginActivity.class);
        finish();
    }


}
