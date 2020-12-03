package com.suusoft.restaurantuser.main.about;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.util.AppUtil;

/**
 * Created by Suusoft on 3/24/17.
 */

public class AboutVM extends BaseViewModel {
// december 4
    public AboutVM(Context self) {
        super(self);
    }

    public void onClickFb(View view){

        AppUtil.openWebView(self, self.getString(R.string.about_fb));

    }

    public void onClickGg(View view){
        AppUtil.openWebView(self, self.getString(R.string.about_fb));

    }

    public void onClickWeb(View view){
        AppUtil.openWebView(self, self.getString(R.string.about_link));

    }

}
