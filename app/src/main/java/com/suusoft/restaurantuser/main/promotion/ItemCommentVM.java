package com.suusoft.restaurantuser.main.promotion;

import android.content.Context;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.model.Comment;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DateTimeUtility;

/**
 * Created by Suusoft on 11/08/2017.
 */

public class ItemCommentVM extends BaseAdapterVM {
    private Comment model;
    private int colorBackground;
    private static boolean checkItem = true;

    public ItemCommentVM(Context self, Object o) {
        super(self);
        model = (Comment) o;
        int id = Integer.parseInt(model.getId());
        checkItem = !checkItem;
        if (checkItem) {
            colorBackground = AppUtil.getColor(self, R.color.white_transparent_20);
        } else {
            colorBackground = AppUtil.getColor(self, R.color.grey_transparent_20);
        }
    }

    @Override
    public void setData(Object object) {
        model = (Comment) object;
        notifyChange();
    }

    public String getNameUser() {
        return model.getNameUser();
    }

    public String getCreatedDate() {

        return DateTimeUtility.convertStringToDate(model.getCreated_date(), "yyyy-MM-dd HH:mm:ss", "hh:mm aa dd/MM/yyyy");
    }

    public String getContent() {
        return model.getContent();
    }

    public String getImage() {
        return model.getImage();
    }

    public int getColorBackground() {
        return colorBackground;
    }
}
