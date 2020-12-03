package com.suusoft.restaurantuser.main.myaccount.myorder;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseAdapterBinding;
import com.suusoft.restaurantuser.databinding.ItemMyOrdersBinding;
import com.suusoft.restaurantuser.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 14/08/2017.
 */

public class MyOrdersAdapter extends BaseAdapterBinding {
    private ArrayList<Order> listOrders;

    public MyOrdersAdapter(Context context, List list) {
        super(context, list);
        listOrders = (ArrayList<Order>) list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMyOrdersBinding binding = (ItemMyOrdersBinding) getViewBinding(parent, R.layout.item_my_orders);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseAdapterBinding.ViewHolder holder, int position) {
        Order model = listOrders.get(position);
        holder.bind(new ItemMyOrdersVM(context, model));
//        ((ViewHolder) holder).bindDataRecyclerView(model);

    }

    public class ViewHolder extends BaseAdapterBinding.ViewHolder {
        private ItemMyOrdersBinding binding;


        public ViewHolder(ViewDataBinding binding) {
            super(binding);
            this.binding = (ItemMyOrdersBinding) binding;

        }

        public void bindDataRecyclerView(Order model) {
//            binding.rcvData.setLayoutManager(new LinearLayoutManager(context));
//            binding.rcvData.setAdapter(new SingleAdapter(context, R.layout.item_food_my_order, model.getListFoods(), ItemFoodMyOrderVM.class));
        }
    }

}
