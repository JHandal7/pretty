package com.suusoft.restaurantuser.base.view;

import android.content.Context;
import android.view.ViewGroup;


import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Suusoft on 11/27/2016.
 */

public class SingleAdapter extends BaseAdapterBinding {

    public int mLayout;
    private BaseAdapterVM mItemVM;
    private Constructor<?> cons;
    private IOnItemClickedListener listener;


    public SingleAdapter(Context context, int layout, List<?> listData, Class itemVMClass) {
        super(context, listData);
        mLayout = layout;
        try {
            cons = itemVMClass.getConstructor(Context.class, Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public SingleAdapter(Context context, int layout, List<?> listData, Class itemVMClass, IOnItemClickedListener listener) {
        this(context, layout, listData, itemVMClass);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getViewBinding(parent, mLayout));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object item = listData.get(position);
        if (item != null) {
            try {
                mItemVM = (BaseAdapterVM) cons.newInstance(context, item);
                if (listener != null)
                    mItemVM.setListener(listener);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            holder.bind(mItemVM);
        }

    }

}
