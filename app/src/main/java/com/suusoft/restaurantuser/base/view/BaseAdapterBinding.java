package com.suusoft.restaurantuser.base.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.suusoft.restaurantuser.BR;
import com.suusoft.restaurantuser.base.model.BaseModel;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;

import java.util.List;

/**
 * Created by Suusoft on 7/12/2016.
 */
public abstract class BaseAdapterBinding extends RecyclerView.Adapter<BaseAdapterBinding.ViewHolder> {

    protected Context context;
    protected List<? extends BaseModel> listData;

    public BaseAdapterBinding(Context context, List list) {
        this.context = context;
        listData = list;
    }

    /**
     * shortcut for binding view
     *
     * @param parent is viewgroup
     * @param layout is layout
     */
    protected ViewDataBinding getViewBinding(ViewGroup parent, int layout) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                layout, parent, false);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    /**
     * Add list items to list and notify
     *
     * @param list list need be added
     */
    public void setItems(List list) {
        final int positionStart = listData.size();
        listData.addAll(list);
        notifyItemRangeInserted(positionStart, list.size());
    }

    public void clear() {
        listData.clear();
        notifyDataSetChanged();
    }

    /**
     * class view holder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BaseAdapterVM adapterVM) {
            binding.setVariable(BR.viewModel, adapterVM);
        }
    }

}
