package com.suusoft.restaurantuser.main.myaccount.myorder;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.model.Order;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.retrofit.APIService;
import com.suusoft.restaurantuser.retrofit.ApiUtils;
import com.suusoft.restaurantuser.retrofit.respone.ResponeListOrder;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.widgets.recyclerview.RecyclerSectionItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends BaseActivity implements RecyclerSectionItemDecoration.SectionCallback, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rcvData;
    private LinearLayoutManager linearLayoutManager;
    private MyOrdersAdapter myOrdersAdapter;
    private ArrayList<Order> listOrders;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_my_orders;
    }

    @Override
    protected void initView() {
        setToolbarTitle(R.string.my_orders);
        initUI();
    }

    @Override
    protected void onViewCreated() {
        setUpRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(this);
        getData();
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }


    @Override
    public boolean isSection(int position) {
        return position == 0 || !listOrders.get(position).getStatus().equals(listOrders.get(position - 1).getStatus());
    }

    @Override
    public CharSequence getSectionHeader(int position) {
        return listOrders.get(position).getStatus();
    }

    @Override
    public void onRefresh() {
        getData();
    }

    private void initUI() {
        rcvData = (RecyclerView) findViewById(R.id.rcv_data);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    private void setUpRecyclerView() {
        listOrders = new ArrayList<>();
        myOrdersAdapter = new MyOrdersAdapter(this, listOrders);
        linearLayoutManager = new LinearLayoutManager(this);
        rcvData.setLayoutManager(linearLayoutManager);
        rcvData.setAdapter(myOrdersAdapter);
        rcvData.addItemDecoration(new RecyclerSectionItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_section_header_height),
                true, this));
    }

    private void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    private void getData() {
        setRefreshing(true);
        listOrders.clear();
        myOrdersAdapter.notifyDataSetChanged();

        /*tạm thời comment khi reskin, lúc này chưa có backend*/
//        RequestManager.getListOrder(new BaseRequest.CompleteListener() {
//            @Override
//            public void onSuccess(ApiResponse response) {
//                if (!response.isError()) {
//                    ArrayList<Order> arrOrders = (ArrayList<Order>) response.getDataList(Order.class);
//                    Log.e("order = ", new Gson().toJson(arrOrders));
//                    if (arrOrders != null && !arrOrders.isEmpty()) {
//                        listOrders.addAll(arrOrders);
//                        myOrdersAdapter.notifyDataSetChanged();
//                    }
//                    setRefreshing(false);
//                }
//            }
//
//            @Override
//            public void onError(String message) {
//                setRefreshing(false);
//            }
//        });

        APIService apiService = ApiUtils.getAPIService();
        apiService.getListOder(DataStoreManager.getToken()).enqueue(new Callback<ResponeListOrder>() {
            @Override
            public void onResponse(Call<ResponeListOrder> call, Response<ResponeListOrder> response) {
                if (response.body() != null) {
                    listOrders.addAll(response.body().getData());
                    myOrdersAdapter.notifyDataSetChanged();
                }
                setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResponeListOrder> call, Throwable t) {
                AppUtil.showToast(self, t.getMessage());
                setRefreshing(false);
            }
        });
    }


}
