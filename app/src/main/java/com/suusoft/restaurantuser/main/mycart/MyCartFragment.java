package com.suusoft.restaurantuser.main.mycart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseFragment;
import com.suusoft.restaurantuser.base.view.SingleAdapter;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.main.myaccount.addpromocode.AddPromoCodeActivity;
import com.suusoft.restaurantuser.model.Food;
import com.suusoft.restaurantuser.model.OpenHour;
import com.suusoft.restaurantuser.model.PromoCode;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;
import com.suusoft.restaurantuser.widgets.dialog.MyProgressDialog;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Suusoft on 11/10/2017.
 */

public class MyCartFragment extends BaseFragment implements View.OnClickListener {
    private static final int RQ_PROMO_CODE = 909;
    private ImageView imgClearCart;
    private RecyclerView rcvItemCart;
    private TextView tvPromoCode;
    private TextView tvCode;
    private TextView tvSalePromoCode;
    private TextView tvOrder;
    private TextView tvTotalPrice;
    private ViewGroup rootDetailPromoCode;

    private SingleAdapter myCartAdapter;
    private ArrayList<Food> listItemsCart;
    private BroadcastReceiver broadcastCartItemChange;
    private PromoCode promoCode;
    private String code;

    public static MyCartFragment newInstance() {

        Bundle args = new Bundle();

        MyCartFragment fragment = new MyCartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_my_cart_v2;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {

        imgClearCart = (ImageView) view.findViewById(R.id.img_clear_cart);
        rcvItemCart = (RecyclerView) view.findViewById(R.id.rcv_items);
        tvPromoCode = (TextView) view.findViewById(R.id.tv_promo_code);
        tvSalePromoCode = (TextView) view.findViewById(R.id.tv_sale_promo_code);
        tvTotalPrice = (TextView) view.findViewById(R.id.tv_total_price);
        tvTotalPrice.setSelected(true);
        tvOrder = (TextView) view.findViewById(R.id.tv_order);
        tvCode = (TextView) view.findViewById(R.id.tv_code);
        rootDetailPromoCode = (ViewGroup) view.findViewById(R.id.ll_root_detail_promo_code);

        initRecyclerView(rcvItemCart);

        broadcastCartItemChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                myCartAdapter.notifyDataSetChanged();
                showTotalPrice();
            }
        };
        AppController.getInstance().cartManager.registerReceiverCartChange(broadcastCartItemChange);

        showTotalPrice();
        initControl();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppController.getInstance().cartManager.unregisterReceiverCartChange(broadcastCartItemChange);
    }

    @Override
    protected void getData() {

    }

    @Override
    public void onClick(View view) {
        if (view == imgClearCart) {
            clearAllMyCart();
        } else if (view == tvPromoCode) {
            if (AppController.getInstance().cartManager.getCart().isEmpty()) {
                AppUtil.showToast(getActivity(), R.string.msg_cart_empty);
                return;
            }
            startActivityForResult(new Intent(getActivity(), AddPromoCodeActivity.class), RQ_PROMO_CODE);
        } else if (view == tvOrder) {
            if (listItemsCart.isEmpty()) {

                AppUtil.showToast(getActivity(), R.string.msg_cart_empty);
                return;
            }
            switchToShippingPayment();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_PROMO_CODE) {
            if (data != null && data.hasExtra(Constant.KEY_PROMO_CODE)) {
                ArrayList<Food> listFoods = AppController.getInstance().cartManager.getCart();
                promoCode = data.getParcelableExtra(Constant.KEY_PROMO_CODE);
                boolean isHasItemDiscount = false;
                double totalPriceItem = 0;
                if (promoCode.getCategory_id().contains(",")) {
                    String[] arrCate = promoCode.getCategory_id().split(",");
                    for (int j = 0; j < listFoods.size(); j++) {
                        Food item = listFoods.get(j);
                        for (int i = 0; i < arrCate.length; i++) {
                            if (item.getCategory_id().equals(arrCate[i])) {
                                totalPriceItem += AppController.getInstance().cartManager.calculatePriceItem(item) * item.getQuantity();
                                break;
                            }
                        }
                    }


                } else {
                    for (Food item : listFoods) {
                        if (item.getCategory_id().equals(promoCode.getCategory_id())) {
                            totalPriceItem += AppController.getInstance().cartManager.calculatePriceItem(item) * item.getQuantity();
//                    item.setDiscountPromoCode(promoCode.getSale());
                        }
                    }

                }


                if (totalPriceItem >= promoCode.getAmount_lowest()) {
                    if (promoCode.getCategory_id().contains(",")) {
                        String[] arrCate = promoCode.getCategory_id().split(",");
                        for (int j = 0; j < listFoods.size(); j++) {
                            Food item = listFoods.get(j);
                            for (int i = 0; i < arrCate.length; i++) {
                                if (item.getCategory_id().equals(arrCate[i])) {
                                    item.setDiscountPromoCode(promoCode.getSale());
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Food item : listFoods) {
                            if (item.getCategory_id().equals(promoCode.getCategory_id())) {
                                item.setDiscountPromoCode(promoCode.getSale());
                            }
                        }
                    }

                    if (!isHasItemDiscount)
                        isHasItemDiscount = true;
                }

                if (isHasItemDiscount) {
                    code = promoCode.getCode();
                    showDetailPromoCode(promoCode);
                    myCartAdapter.notifyDataSetChanged();
                } else {
                    AppUtil.showToast(getActivity(), R.string.msg_promo_code_is_not_apply_for_items);
                    showDetailPromoCode(null);
                }
            }

        }
        if (requestCode == Constant.RQ_CONFIRM_PAYMENT && resultCode == Constant.RC_END_PAYMENT) {
            showDetailPromoCode(null);
            AppController.getInstance().cartManager.clearCart();
            myCartAdapter.notifyDataSetChanged();
        }
    }

    private void initControl() {
        imgClearCart.setOnClickListener(this);
        tvOrder.setOnClickListener(this);
        tvPromoCode.setOnClickListener(this);
    }

    private void initRecyclerView(RecyclerView rcvItemCart) {
        listItemsCart = AppController.getInstance().cartManager.getCart();
        myCartAdapter = new SingleAdapter(getActivity(), R.layout.item_my_cart_v3, listItemsCart, ItemMyCartVM.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvItemCart.setLayoutManager(linearLayoutManager);
        rcvItemCart.setAdapter(myCartAdapter);
    }

    private void clearAllMyCart() {
        if (!listItemsCart.isEmpty()) {
            DialogUtil.showAlertDialog(getActivity(), R.string.msg_delete_all_item_cart, new DialogUtil.IDialogConfirm() {
                @Override
                public void onClickOk() {
                    AppController.getInstance().cartManager.clearCart();
                    myCartAdapter.notifyDataSetChanged();
                }
            });
        } else {
            AppUtil.showToast(getActivity(), R.string.msg_cart_empty);
        }
    }

    private void showTotalPrice() {
        double total = AppController.getInstance().cartManager.getTotal();
        tvTotalPrice.setText(getString(R.string.total_price_mycart) + " " + AppUtil.formatCurrency(total));
    }

    private void showDetailPromoCode(PromoCode promoCode) {
        if (promoCode != null) {
            tvCode.setText(promoCode.getCode());
            tvSalePromoCode.setText("Sale " + promoCode.getSale() + "%");
            rootDetailPromoCode.setVisibility(View.VISIBLE);
        } else {
            tvCode.setText("");
            tvSalePromoCode.setText("");
            rootDetailPromoCode.setVisibility(View.VISIBLE);
        }
        showTotalPrice();
    }

    private void switchToShippingPayment() {
        getOpenHours();
    }

    private void getOpenHours() {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());
        myProgressDialog.setCancelable(false);
        myProgressDialog.show();
        RequestManager.getOpenHours(true, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    ArrayList<OpenHour> arrOpenHours = (ArrayList<OpenHour>) response.getDataList(OpenHour.class);
                    if (arrOpenHours != null && !arrOpenHours.isEmpty()) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constant.KEY_LIST_OPEN_HOUR, arrOpenHours);
                        JSONArray jsonArray = response.getRoot().optJSONArray(Constant.KEY_RIDER_TIP);
                        double[] listRiderTips = new double[jsonArray.length()];
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                listRiderTips[i] = jsonArray.optDouble(i);
                            }
                        }
                        bundle.putDoubleArray(Constant.KEY_RIDER_TIP, listRiderTips);
                        bundle.putParcelable(Constant.KEY_PROMO_CODE, promoCode);
                        Intent intent = new Intent(getActivity(), ShippingPaymentActivity.class);
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, Constant.RQ_CONFIRM_PAYMENT);

                        AppController.getInstance().cartManager.setDeliveryFee(response.getRoot().optDouble(Constant.KEY_DELIVERY_FEE));
                        AppController.getInstance().amount_lowest = response.getRoot().optDouble(Constant.KEY_AMOUNT_LOWEST);
                        AppController.getInstance().amount_promtion_lowest = response.getRoot().optDouble(Constant.KEY_AMOUNT_PROMOTION_LOWEST);
                        AppController.getInstance().isChoseAddressDelivery = response.getRoot().optInt(Constant.KEY_CHOSE_ADDRESS_DELIVERY);
                    }
                }
                myProgressDialog.dismiss();
            }

            @Override
            public void onError(String message) {
                myProgressDialog.dismiss();
                AppUtil.showToast(getActivity(), message);
            }
        });
    }
}
