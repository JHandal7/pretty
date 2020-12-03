package com.suusoft.restaurantuser.main;

import android.graphics.Color;
import android.support.v4.view.ViewPager;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.main.adapter.HomePagerAdapter;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

/**
 * Created by Suusoft on 11/21/2017.
 */

public class HomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private NavigationTabBar navigationTabBar;
    private HomePagerAdapter adapter;
    private ArrayList<NavigationTabBar.Model> models;
    private String[] colors, title;


    @Override
    protected int getLayoutInflate() {
        return R.layout.layout_tab_viewpager;
    }

    @Override
    protected void initView() {
        initViews();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.view_pagger);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.nav_tab_bar);
        adapter = new HomePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        initModels();

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);

        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setIsBadged(true);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsTinted(true);
        navigationTabBar.setIsBadgeUseTypeface(true);
        navigationTabBar.setBadgeBgColor(Color.RED);
        navigationTabBar.setBadgeTitleColor(Color.WHITE);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(Color.BLACK);
        navigationTabBar.setBadgeSize(10);
        navigationTabBar.setTitleSize(10);
        navigationTabBar.setIconSizeFraction(0.5f);

    }

    private void initModels() {
        colors = getResources().getStringArray(R.array.color_tabs);
        title = getResources().getStringArray(R.array.title_tabs);

        models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_promo),
                        Color.parseColor(colors[0])
                ).title(title[0])
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_search),
                        Color.parseColor(colors[1])
                ).title(title[1])
                        .badgeTitle("NTB1")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu),
                        Color.parseColor(colors[2])
                ).title(title[2])
                        .badgeTitle("NTB2")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_cart),
                        Color.parseColor(colors[3])
                ).title(title[3])
                        .badgeTitle("NTB3")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_account),
                        Color.parseColor(colors[4])
                ).title(title[4])
                        .badgeTitle("NTB3")
                        .build()
        );
    }

    @Override
    protected void onViewCreated() {

    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NORMAL;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch(position){
            case 0:
                setToolbarTitle(R.string.promotion);
                break;

            case 1:
                setToolbarTitle(R.string.search);
                break;

            case 2:
                setToolbarTitle(R.string.menu);
                break;

            case 3:
                setToolbarTitle(R.string.my_cart);
                break;

            case 4:
                setToolbarTitle(R.string.profile);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
