package com.suusoft.restaurantuser.base.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Suusoft on 3/6/2017.
 */

public class SingleListFragment extends BaseListFragmentBinding {

    private static final String KEY_LAYOUT_INT = "LAYOUT_INT";
    private static final String KEY_CLASS_VM = "CLASS_VM";
    private static final String KEY_CLASS_ITEM_VM = "CLASS_ITEM_VM";
    private static final String KEY_CLASS_ADAPTER = "KEY_CLASS_ADAPTER";
    private static final String KEY_CLASS_ITEM_CLICK = "KEY_CLASS_ITEM_CLICK";
    private static final String KEY_BUNDLE_DATA = "KEY_BUNDLE_DATA";

    public int layout, layoutItem;
    public BaseViewModelList viewModel;
    private Constructor<?> cons, consAdapterList;
    public boolean isNestedScrollingEnabled = true;

    private Class itemViewModel;
    public BaseAdapterBinding adapter;
    private IOnItemClickedListener onItemClickedListener;
    Bundle data;
    private RecyclerView rcvList;

    /**
     * this constructor is using #link{@SingleAdapter}
     *
     * @param viewModelClass view model class for this
     * @param layoutitem     layout of adapter
     * @param itemVm         view model of adapter
     */
    public static SingleListFragment newInstance(Class viewModelClass, int layoutitem, Class itemVm, IOnItemClickedListener onItemClick) {
        return newInstance(viewModelClass, SingleAdapter.class, layoutitem, itemVm, null, onItemClick);
    }

    public static SingleListFragment newInstance(Class viewModelClass, int layoutitem, Class itemVm) {
        return newInstance(viewModelClass, SingleAdapter.class, layoutitem, itemVm, null, null);
    }

    public static SingleListFragment newInstance(Class viewModelClass, int layoutitem, Class itemVm, Bundle bundle) {
        return newInstance(viewModelClass, SingleAdapter.class, layoutitem, itemVm, bundle, null);
    }


    /**
     * this constructor is using for other adapter
     *
     * @param viewModelClass view model class for this
     * @param adapter        adapter class need to be use
     */
    public static SingleListFragment newInstance(Class viewModelClass, Class adapter) {
        return newInstance(viewModelClass, adapter, 0, null, null, null);
    }

    /**
     * this constructor is using an adapter
     *
     * @param viewModelClass view model class for this
     * @param adapter        adapter class
     * @param layoutitem     layout of adapter
     * @param itemVm         view model of adapter
     */
    public static SingleListFragment newInstance(Class viewModelClass, Class adapter, int layoutitem, Class itemVm, Bundle bundle, IOnItemClickedListener itemClickedListener) {

        Bundle args = new Bundle();
        SingleListFragment fragment = new SingleListFragment();

        args.putInt(KEY_LAYOUT_INT, layoutitem);
        args.putString(KEY_CLASS_VM, viewModelClass.getName());
        args.putString(KEY_CLASS_ADAPTER, adapter.getName());
        if (itemClickedListener != null)
            fragment.onItemClickedListener = itemClickedListener;

        if (bundle != null)
            args.putBundle(KEY_BUNDLE_DATA, bundle);
        if (itemVm != null)
            args.putString(KEY_CLASS_ITEM_VM, itemVm.getName());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initialize() {
        try {
            Bundle bundle = getArguments();
            layoutItem = bundle.getInt(KEY_LAYOUT_INT);

            Class viewModelClass = Class.forName(bundle.getString(KEY_CLASS_VM));
            Class adapter = Class.forName(bundle.getString(KEY_CLASS_ADAPTER));

            if (bundle.containsKey(KEY_CLASS_ITEM_VM))
                itemViewModel = Class.forName(bundle.getString(KEY_CLASS_ITEM_VM));

            data = bundle.containsKey(KEY_BUNDLE_DATA) ? bundle.getBundle(KEY_BUNDLE_DATA) : null;
            if (data != null) {
                cons = viewModelClass.getConstructor(Context.class, Bundle.class);
            } else {
                cons = viewModelClass.getConstructor(Context.class);
            }
            if (layoutItem == 0) {
                consAdapterList = adapter.getConstructor(Context.class, List.class);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseViewModel getViewModel() {
        try {
            if (data != null) {
                viewModel = (BaseViewModelList) cons.newInstance(self, data);
            } else {
                viewModel = (BaseViewModelList) cons.newInstance(self);
            }

        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return viewModel;
    }

    @Override
    protected void setUpRecyclerView(RecyclerView recyclerView) {
        if (layoutItem != 0) {

            if (onItemClickedListener != null) {
                adapter = new SingleAdapter(self, layoutItem, viewModel.getListData(), itemViewModel, onItemClickedListener);
            } else {
                adapter = new SingleAdapter(self, layoutItem, viewModel.getListData(), itemViewModel);
            }

        } else {
            try {
                adapter = (BaseAdapterBinding) consAdapterList.newInstance(self, viewModel.getListData());
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(viewModel.getOnScrollListener());
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        rcvList = recyclerView;
    }

    @Override
    public boolean isEnableRefresh() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel = null;
    }

    public void handleChangeTypeListItems() {
        rcvList.setLayoutManager(viewModel.getLayoutManager());
        ((SingleAdapter) adapter).mLayout = layoutItem;
        rcvList.setAdapter(adapter);
    }
}
