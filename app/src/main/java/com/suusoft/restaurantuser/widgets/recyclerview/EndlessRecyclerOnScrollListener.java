package com.suusoft.restaurantuser.widgets.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private boolean isLoading = false; // True if we are still waiting for the last set of data to load.
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1; // current page

    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    private boolean isEnded; // end of list

    private OnLoadMoreListener mLoadMoreListener;

    /**
     * Interface definition for a callback to be invoked when list reaches the
     * last item (the user load more items in the list)
     */
    public interface OnLoadMoreListener {
        /**
         * Called when the list reaches the last item (the last item is visible
         * to the user)
         */
        public void onLoadMore(int page);
    }


    public EndlessRecyclerOnScrollListener(OnLoadMoreListener onLoadMoreListener) {
        mLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!isEnded) {
            visibleItemCount = recyclerView.getChildCount();
            getItemInfor(recyclerView);
            if (visibleItemCount == totalItemCount && !isLoading) {
                Log.d(TAG, "return");
                return;
            }

            boolean loadMore = visibleItemCount + firstVisibleItem >= totalItemCount - 2 ;
            if (!isLoading && loadMore) {
                onLoadMore();
            }

        }

    }

    private void getItemInfor(RecyclerView recyclerView) {

        if (mLinearLayoutManager != null){
            firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
            totalItemCount = mLinearLayoutManager.getItemCount();
        }else if (mGridLayoutManager != null){
            firstVisibleItem = mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
            totalItemCount = mGridLayoutManager.getItemCount();
        }else if (mStaggeredGridLayoutManager != null){
            firstVisibleItem = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0];
            totalItemCount = mStaggeredGridLayoutManager.getItemCount();
        }else{
            if (recyclerView.getLayoutManager() instanceof  LinearLayoutManager) {
                mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            }else if (recyclerView.getLayoutManager() instanceof  GridLayoutManager){
                mGridLayoutManager =(GridLayoutManager) recyclerView.getLayoutManager();
            }else if (recyclerView.getLayoutManager() instanceof  StaggeredGridLayoutManager){
                mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            }
        }


    }

    private void onLoadMore(){
        current_page++;
        isLoading = true;
        mLoadMoreListener.onLoadMore(current_page);
    }

    public void onLoadMoreComplete(){
        isLoading = false;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public int getCurrent_page() {
        return current_page;
    }

}