package com.suusoft.restaurantuser.main.detail;

import android.content.Context;
import android.view.ViewGroup;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseAdapterBinding;
import com.suusoft.restaurantuser.model.User;

import java.util.ArrayList;

/**
 * Created by Suusoft on 5/16/2016.
 */
public class OfferUserAdapter extends BaseAdapterBinding {


    public OfferUserAdapter(Context context) {
        super(context, new ArrayList());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getViewBinding(parent,R.layout.item_task));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(new ItemUserOfferVM(context, (User) listData.get(position)));
    }


}
