package com.suusoft.restaurantuser.base.vm;

import android.content.Context;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.suusoft.restaurantuser.listener.IDataChangedListener;
import com.suusoft.restaurantuser.listener.IOnRefreshing;
import com.suusoft.restaurantuser.widgets.recyclerview.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 7/11/2016.
 */
public abstract class BaseViewModelList extends BaseViewModel implements EndlessRecyclerOnScrollListener.OnLoadMoreListener {

    /**
     * show hide textview  No Data
     */
    public ObservableInt isDataAvailable;

    /**
     * list data
     */
    protected List<?> mDatas;

    /**
     * listener when data is changed
     */
    private IDataChangedListener dataListener;
    private IOnRefreshing onRefreshing;

    protected EndlessRecyclerOnScrollListener mOnScrollListener;

    public abstract void getData(int page);

    Bundle bundle;

    /**
     * Constructor with fragment
     *
     * @param context context
     */
    public BaseViewModelList(Context context) {
        super(context);
        initialize();
    }

    public BaseViewModelList(Context context, Bundle bundle) {
        super(context);
        this.bundle = bundle;
        initialize();
    }

    /**
     * initialize all component
     */
    private void initialize() {
        mOnScrollListener = new EndlessRecyclerOnScrollListener(this);
        mDatas = new ArrayList<>();
        isDataAvailable = new ObservableInt();
        isDataAvailable.set(View.GONE);
    }

    public EndlessRecyclerOnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(self);
    }

    @Override
    public void onLoadMore(int page) {
        getData(page);
    }

    @Override
    public void destroy() {
        dataListener = null;
        self = null;
    }

    /**
     * Notify that the view is needed to update
     */
    protected void notifyDataChanged() {
        if (dataListener != null) {
            dataListener.onListDataChanged(mDatas);
        }
    }


    public List<?> getListData() {
        if (mDatas == null)
            mDatas = new ArrayList<>();
        return mDatas;
    }

    /**
     * add to list data and notify that need update
     *
     * @param data list data
     */
    public void addListData(List<?> data) {
        this.mDatas = data;
        notifyDataChanged();
        if (mDatas.size() > 0)
            isDataAvailable.set(View.GONE);
        else isDataAvailable.set(View.VISIBLE);

    }

    /**
     * Check loading more. Using this when call recyclerView.addOnScrollListener
     *
     * @param totalPage get in json
     */
    protected void checkLoadingMoreComplete(int totalPage) {
        // set status loadmore
        mOnScrollListener.onLoadMoreComplete();
        mOnScrollListener.setEnded(mOnScrollListener.getCurrent_page() >= totalPage);
    }

    /**
     * set is ended loading more
     */
    protected void setIsEndedLoadMore() {
        mOnScrollListener.onLoadMoreComplete();
        mOnScrollListener.setEnded(true);
    }

    public IDataChangedListener getDataListener() {
        return dataListener;
    }

    public void setDataListener(IDataChangedListener listener) {
        this.dataListener = listener;
    }

    public void setOnRefreshing(IOnRefreshing onRefreshing) {
        this.onRefreshing = onRefreshing;
    }

    protected void setRefreshing(boolean isShow) {
        onRefreshing.setRefreshing(isShow);
    }
}
