package com.suusoft.restaurantuser.main.home;

import android.content.Context;
import android.view.ViewGroup;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.model.Product;
import com.suusoft.restaurantuser.base.view.BaseAdapterBinding;

import java.util.List;

/**
 * Created by Suusoft on 6/29/2016.
 */
public class ItemProduct extends BaseAdapterBinding {

    public static int TYPE_OTHER = 0;
    public static int TYPE_NORMAL = 1;

    public ItemProduct(Context context, List<?> datas) {
        super(context, datas);
        this.listData = (List<Product>) datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_OTHER) {
            return new ViewHolder(getViewBinding(parent, R.layout.item_product));
        }else{
            return new ViewHolder(getViewBinding(parent, R.layout.item_product1));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(new ItemProductVM(context,listData.get(position)));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_OTHER;
        else return TYPE_NORMAL;
    }

}
