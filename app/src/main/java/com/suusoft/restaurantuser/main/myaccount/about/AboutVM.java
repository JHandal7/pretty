package com.suusoft.restaurantuser.main.myaccount.about;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.SingleAdapter;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.OpenHour;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.retrofit.APIService;
import com.suusoft.restaurantuser.retrofit.ApiUtils;
import com.suusoft.restaurantuser.retrofit.respone.ResponeOpenHouse;
import com.suusoft.restaurantuser.util.AppUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AboutVM extends BaseViewModel {
    public static final String EMAIL = "suusoft@gmail.com";
    public static final String FACEBOOK = "https://www.facebook.com/suusoft";
    public static final String INSTAGRAM = "https://www.instagram.com/seapalace/";
    public static final String NEWS = "http://suusoft.com";
    public static final String ADDRESS = "Hoan Kiem, Hanoi, Vietnam";
    public static final String PHONE = "+(84) 1658296686";


    private ArrayList<OpenHour> listOpenHours;
    private SingleAdapter openHourAdapter;

    public AboutVM(Context self) {
        super(self);
        listOpenHours = new ArrayList<>();
        openHourAdapter = new SingleAdapter(self, R.layout.item_schedule_store, listOpenHours, ItemOpenHourVM.class);
        getOpenHours();
    }

    public void contact(View view) {
        AppUtil.sendEmail(self, EMAIL);
    }

    public void openFacebook(View view) {
        AppUtil.openWebView(self, FACEBOOK);
    }

    public void openInstagram(View view) {
        AppUtil.openWebView(self, INSTAGRAM);
    }

    public void openNews(View view) {
        AppUtil.openWebView(self, NEWS);
    }

    public void showOpenHour(View view) {
        showDialogOpenHour();
    }

    public void showTermCondition(View view) {
        showDialogTermConditions();
    }

    private void getOpenHours() {
        RequestManager.getOpenHours(true, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    ArrayList<OpenHour> arrOpenHours = (ArrayList<OpenHour>) response.getDataList(OpenHour.class);
                    if (arrOpenHours != null && !arrOpenHours.isEmpty()) {
                        listOpenHours.addAll(arrOpenHours);
                        openHourAdapter.notifyDataSetChanged();
                        AppController.getInstance().cartManager.setDeliveryFee(response.getRoot().optDouble(Constant.KEY_DELIVERY_FEE));
                    }
                }
            }

            @Override
            public void onError(String message) {
                AppUtil.showToast(self, message);
            }
        });
//        APIService apiService = ApiUtils.getAPIService();
//        apiService.openhouse().enqueue(new Callback<ResponeOpenHouse>() {
//            @Override
//            public void onResponse(Call<ResponeOpenHouse> call, Response<ResponeOpenHouse> response) {
//                if (response.body() != null) {
//                    if (response.body().getData() != null) {
//                        listOpenHours.addAll(response.body().getData());
//                        openHourAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponeOpenHouse> call, Throwable t) {
//                AppUtil.showToast(self, t.getMessage());
//            }
//        });
    }

    private void showDialogOpenHour() {
        final android.app.Dialog dialog = new android.app.Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_dialog_open_hour);
        RelativeLayout rltRoot = (RelativeLayout) dialog.findViewById(R.id.rlt_root);
        rltRoot.getLayoutParams().height = AppUtil.getScreenHeight((AppCompatActivity) self) / 2;
        TextView tvClose = (TextView) dialog.findViewById(R.id.tv_close);
        RecyclerView rcvData = (RecyclerView) dialog.findViewById(R.id.rcv_data);
        rcvData.setLayoutManager(new LinearLayoutManager(self));
        rcvData.setAdapter(openHourAdapter);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void showDialogTermConditions() {
        final android.app.Dialog dialog = new android.app.Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_term_and_condition_dialog);
        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        root.getLayoutParams().height = AppUtil.getScreenHeight((AppCompatActivity) self) / 2;
        TextView tvClose = (TextView) dialog.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public String getEmail() {
        return EMAIL;
    }

    public String getAddress() {
        return Constant.ADDRESS_RESTAURANT;
    }

    public String getPhone() {
        return PHONE;
    }
}
