package com.suusoft.restaurantuser.main.mycart;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.base.view.SingleAdapter;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.main.myaccount.addpromocode.AddPromoCodeActivity;
import com.suusoft.restaurantuser.model.Food;
import com.suusoft.restaurantuser.model.OpenHour;
import com.suusoft.restaurantuser.model.PromoCode;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;
import com.suusoft.restaurantuser.util.NetworkUtility;
import com.suusoft.restaurantuser.widgets.dialog.MyProgressDialog;
import com.suusoft.restaurantuser.widgets.textview.TextViewLatoBold;
import com.suusoft.restaurantuser.widgets.textview.TextViewLatoHeavy;
import com.suusoft.restaurantuser.widgets.textview.TextViewLatoMedium;
import com.suusoft.restaurantuser.widgets.textview.TextViewLatoRegular;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyCartActivity extends BaseActivity implements View.OnClickListener {
    private static final int RQ_PROMO_CODE = 123;
    private ImageView imgBack;
    private TextViewLatoHeavy tvTitle;
    private TextViewLatoRegular tvChangeTimeOrdering, tvTimeOrdering;
    private RecyclerView rcvData;
    private TextViewLatoBold tvSubTotal;
    private TextViewLatoBold tvDeliveryFee;
    private TextViewLatoRegular tvAddPromoCode;
    private TextViewLatoRegular tvSalePromoCode;
    private TextViewLatoRegular tvRiderTipPrice;
    private TextViewLatoRegular tvChangeRiderTipPrice;
    private TextViewLatoBold tvOrderTotalPrice;
    private TextViewLatoBold tvCheckOut;

    private LinearLayout llRootAddPromoCode;
    private RelativeLayout rltRootPromoCode;
    private TextViewLatoRegular tvPromoCode, tvChangePromoCode;
    private TextViewLatoMedium tvNoteUsePromotion;

    private RelativeLayout rltRootDelivery;
    private LinearLayout llRootRiderTip;

    private SingleAdapter myCartAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Food> listFoods;


    private SingleAdapter timeDeliveryAdapter;
    private ArrayList<String> listTimeDeliveries;
    private ArrayList<String> listTimeDeliveriesToday;
    private ArrayList<String> listTimeDeliveriesTomorrow;
    private ArrayList<OpenHour> listOpenHours;
    private Dialog timeDeliverydialog;

    private SingleAdapter promoCodeAdapter;
    private ArrayList<PromoCode> listPromoCodes;
    private Dialog promoCodeDialog;

    private SingleAdapter riderTipAdapter;
    private double[] listRiderTip;
    private Dialog riderTipDialog;


    private MyProgressDialog progressDialog;

    private boolean isToday;
    private String timeDelivery;
    private boolean isRemoveItem;
    private int salePromoCode = 0;
    private double riderTip = 0;
    private double totalFinal = 0;
    private long timeStampDelivery = 0;
    private String code = "";
    private PromoCode promoCode;
    private boolean isHasDeliveryFee;

    private BroadcastReceiver broadcastReceiver;


    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_my_cart;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        listOpenHours = bundle.getParcelableArrayList(Constant.KEY_LIST_OPEN_HOUR);
        listRiderTip = bundle.getDoubleArray(Constant.KEY_RIDER_TIP);
        listTimeDeliveriesToday = getListTimesOpen(listOpenHours.get(0));
        listTimeDeliveriesTomorrow = getListTimesOpen(listOpenHours.get(1));
        initUI();

        FrameLayout root = (FrameLayout) toolbar.findViewById(R.id.fr_root);
        root.setVisibility(View.GONE);
    }

    @Override
    protected void onViewCreated() {
        initControl();
        setUpRecyclerView();
        bindDataIntoView();

        initTimeDeliveryAdapter();
        setTimeDelivery(true, listTimeDeliveriesToday);
        timeDelivery = "ASAP";
        tvTimeOrdering.setText(listTimeDeliveries.get(0));

        initRiderTipAdapter();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                myCartAdapter.notifyDataSetChanged();
                bindDataIntoView();
            }
        };

        AppController.getInstance().cartManager.registerReceiverRemoveItem(broadcastReceiver);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestRefreshListFood();
                finish();
            }
        });

        tvNoteUsePromotion.setText(String.format(getString(R.string.note_use_promo_code), AppUtil.formatCurrency(AppController.getInstance().amount_promtion_lowest)));
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().cartManager.unregisterReceiverCartChange(broadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_clear_all:
                clearAllMyCart();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        if (view == imgBack) {

        } else if (view == tvChangeTimeOrdering) {
            if (listOpenHours != null) {
                showDialogSelectTimeDelivery();
            }

        } else if (view == tvAddPromoCode) {
            if (AppController.getInstance().cartManager.getCart().isEmpty()) {
                AppUtil.showToast(this, R.string.msg_cart_empty);
                return;
            }
            startActivityForResult(new Intent(this, AddPromoCodeActivity.class), RQ_PROMO_CODE);
        } else if (view == tvCheckOut) {
            if (AppController.getInstance().cartManager.getCart().isEmpty()) {
                AppUtil.showToast(this, R.string.msg_cart_empty);
                return;
            }
            if (isToday) {
                if (listOpenHours.get(0).getIs_active()) {
                    if (timeDelivery.equals("ASAP")) {
                        timeStampDelivery = 0;
                    } else {
                        String[] arrTime = timeDelivery.split(":");
                        int hour = Integer.parseInt(arrTime[0]);
                        int minutes = Integer.parseInt(arrTime[1]);
                        Calendar today = Calendar.getInstance();
                        today.set(Calendar.HOUR_OF_DAY, hour);
                        today.set(Calendar.MINUTE, minutes);
                        if (today.getTimeInMillis() < System.currentTimeMillis()) {
                            AppUtil.showToast(this, R.string.time_delivery_invalid);
                            return;
                        } else {
                            timeStampDelivery = today.getTimeInMillis() / 1000;
                        }
                    }
                } else {
                    DialogUtil.showAlertDialog(this, R.string.msg_restaurant_is_closed, false, new DialogUtil.IDialogConfirm() {
                        @Override
                        public void onClickOk() {

                            return;
                        }
                    });

                }
            } else {
                if (listOpenHours.get(1).getIs_active()) {
                    String[] arrTime = timeDelivery.split(":");
                    int hour = Integer.parseInt(arrTime[0]);
                    int minutes = Integer.parseInt(arrTime[1]);
                    Calendar tomorrow = Calendar.getInstance();
                    tomorrow.add(Calendar.DAY_OF_YEAR, 1);
                    tomorrow.set(Calendar.HOUR_OF_DAY, hour);
                    tomorrow.set(Calendar.MINUTE, minutes);
                    timeStampDelivery = tomorrow.getTimeInMillis() / 1000;
                } else {
                    DialogUtil.showAlertDialog(this, R.string.msg_restaurant_is_closed, false, new DialogUtil.IDialogConfirm() {
                        @Override
                        public void onClickOk() {
                            return;
                        }
                    });

                }
            }
            if (NetworkUtility.isNetworkAvailable()) {
                Bundle bundle = new Bundle();
                bundle.putDouble(Constant.KEY_TOTAL_FINAL, totalFinal);
                bundle.putDouble(Constant.KEY_RIDER_TIP, riderTip);
                bundle.putLong(Constant.KEY_TIME_DELIVERY, timeStampDelivery);
                bundle.putString(Constant.KEY_PROMO_CODE, code);
                bundle.putBoolean(Constant.KEY_HAS_DELIVERY_FEE, isHasDeliveryFee);
                AppUtil.startActivity(this, CheckOutActivity.class, bundle);
            } else {
                AppUtil.showToast(this, R.string.msg_connection_network_error);
            }
        } else if (view == tvChangePromoCode) {
            showDialogPromoCode();
        } else if (view == tvChangeRiderTipPrice) {
            showDialogRiderTip();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_PROMO_CODE && resultCode == Constant.RC_PROMO_CODE) {
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
                bindDataPromoCode(promoCode);
                rltRootPromoCode.setVisibility(View.VISIBLE);
                myCartAdapter.notifyDataSetChanged();
            } else {
                AppUtil.showToast(this, R.string.msg_promo_code_is_not_apply_for_items);
                rltRootPromoCode.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        sendRequestRefreshListFood();
        finish();
    }

    private void initUI() {
        setToolbarTitle(R.string.my_cart);
        tvChangeTimeOrdering = (TextViewLatoRegular) findViewById(R.id.tv_change_time_ordering);
        rcvData = (RecyclerView) findViewById(R.id.rcv_data);
        tvSubTotal = (TextViewLatoBold) findViewById(R.id.tv_sub_total);
        tvDeliveryFee = (TextViewLatoBold) findViewById(R.id.tv_delivery_fee);
        tvAddPromoCode = (TextViewLatoRegular) findViewById(R.id.tv_add_promo_code);
        tvRiderTipPrice = (TextViewLatoRegular) findViewById(R.id.tv_rider_tip_price);
        tvChangeRiderTipPrice = (TextViewLatoRegular) findViewById(R.id.tv_change_rider_tip);
        tvOrderTotalPrice = (TextViewLatoBold) findViewById(R.id.tv_order_total_price);
        tvCheckOut = (TextViewLatoBold) findViewById(R.id.tv_checkout);
        tvTimeOrdering = (TextViewLatoRegular) findViewById(R.id.tv_time_ordering);
        llRootAddPromoCode = (LinearLayout) findViewById(R.id.ll_root_add_promo_code);
        rltRootPromoCode = (RelativeLayout) findViewById(R.id.rlt_root_promo_code);
        tvPromoCode = (TextViewLatoRegular) findViewById(R.id.tv_promo_code);
        tvChangePromoCode = (TextViewLatoRegular) findViewById(R.id.tv_change_promo_code);
        tvSalePromoCode = (TextViewLatoRegular) findViewById(R.id.tv_sale);
        tvNoteUsePromotion = (TextViewLatoMedium) findViewById(R.id.tv_note_promotion);

        rltRootDelivery = (RelativeLayout) findViewById(R.id.rlt_delivery_fee);
        llRootRiderTip = (LinearLayout) findViewById(R.id.ll_root_rider_tip);

        progressDialog = new MyProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void initControl() {
        tvChangeTimeOrdering.setOnClickListener(this);
        tvAddPromoCode.setOnClickListener(this);
        tvCheckOut.setOnClickListener(this);
        tvChangePromoCode.setOnClickListener(this);
        tvChangeRiderTipPrice.setOnClickListener(this);
    }

    private void setUpRecyclerView() {
        listFoods = AppController.getInstance().cartManager.getCart();
        myCartAdapter = new SingleAdapter(self, R.layout.item_my_cart, listFoods, ItemMyCartVM.class, new IOnItemClickedListener() {
            @Override
            public void onItemClicked(View view) {
                Food food = (Food) view.getTag();
                showDialogChangeItemCart(food);
            }
        });
        linearLayoutManager = new LinearLayoutManager(self);
        rcvData.setLayoutManager(linearLayoutManager);
        rcvData.setAdapter(myCartAdapter);
    }

    /**************
     * handle change item cart
     */
    private void showDialogChangeItemCart(final Food model) {
        final Food food = (Food) AppUtil.clone(model, Food.class);
        isRemoveItem = false;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(this).inflate(R.layout.layout_change_item_cart_dialog, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        TextViewLatoHeavy tvNameFood = (TextViewLatoHeavy) view.findViewById(R.id.tv_name);
        ImageView imgSub = (ImageView) view.findViewById(R.id.img_sub);
        final TextViewLatoHeavy tvQuantity = (TextViewLatoHeavy) view.findViewById(R.id.tv_quantity);
        ImageView imgAdd = (ImageView) view.findViewById(R.id.img_add);
        final TextViewLatoMedium tvPrice = (TextViewLatoMedium) view.findViewById(R.id.tv_price);
        final TextViewLatoHeavy tvFunction = (TextViewLatoHeavy) view.findViewById(R.id.tv_function);

        tvNameFood.setText(food.getName());
        tvQuantity.setText(String.valueOf(food.getQuantity()));
        bindDataPrice(tvPrice, food);

        imgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = food.getQuantity();
                if (quantity == 1) {
                    tvFunction.setText(getString(R.string.remove_item));
                    isRemoveItem = true;
                }
                if (quantity > 0) {
                    quantity--;
                    food.setQuantity(quantity);
                    if (quantity < 10) {
                        tvQuantity.setText("0" + food.getQuantity());

                    } else {
                        tvQuantity.setText(String.valueOf(food.getQuantity()));
                    }

                }
                bindDataPrice(tvPrice, food);
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = food.getQuantity();
                if (quantity == 0) {
                    tvFunction.setText(getString(R.string.update));
                    isRemoveItem = false;
                }
                if (quantity < 99) {
                    quantity++;
                    food.setQuantity(quantity);
                    if (quantity < 10) {
                        tvQuantity.setText("0" + food.getQuantity());

                    } else {
                        tvQuantity.setText(String.valueOf(food.getQuantity()));
                    }
                    bindDataPrice(tvPrice, food);
                }
            }
        });

        tvFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRemoveItem) {
                    AppController.getInstance().cartManager.removeFromCart(model);
                } else {
                    model.setQuantity(food.getQuantity());
                    myCartAdapter.notifyDataSetChanged();
                    bindDataIntoView();
                }
                dialog.dismiss();
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void bindDataPrice(TextView textView, Food model) {
        double price = AppController.getInstance().cartManager.calculatePriceItem(model) * model.getQuantity();
        textView.setText(AppUtil.formatCurrency(price));

    }


    private void bindDataIntoView() {
        double total = AppController.getInstance().cartManager.getTotal();
        double delivery = AppController.getInstance().cartManager.getDeliveryFee();
        tvSubTotal.setText(AppUtil.formatCurrency(total));
        tvDeliveryFee.setText(AppUtil.formatCurrency(delivery));
        if (!AppController.getInstance().isChoseAddressDelivery()) {
            rltRootDelivery.setVisibility(View.GONE);
            llRootRiderTip.setVisibility(View.GONE);
        } else {
            rltRootDelivery.setVisibility(View.VISIBLE);
            llRootRiderTip.setVisibility(View.VISIBLE);
        }
        calculateTotalFinal();
    }

    private void calculateTotalFinal() {
        double totalDelivery = AppController.getInstance().cartManager.getTotal();
        double deliveryFee = AppController.getInstance().cartManager.getDeliveryFee();
        double amount_lowest = AppController.getInstance().amount_lowest;
        if (AppController.getInstance().isChoseAddressDelivery()) {
            if (totalDelivery >= amount_lowest) {
                deliveryFee = 0;
                tvDeliveryFee.setText(AppUtil.formatCurrency(deliveryFee));
                isHasDeliveryFee = false;
            } else {
                isHasDeliveryFee = true;
            }
        } else {
            deliveryFee = 0;
        }
//        if (totalDelivery >= AppController.getInstance().amount_promtion_lowest) {
//            totalFinal = totalDelivery - (salePromoCode * totalDelivery) / 100;
//        } else {
//            totalFinal = totalDelivery;
//        }
        totalFinal = totalDelivery;
        totalFinal += riderTip;
        totalFinal += deliveryFee;
        tvOrderTotalPrice.setText(AppUtil.formatCurrency(totalFinal));
    }

    private void showProgressDialog(boolean isShow) {
        if (isShow) {
            if (!progressDialog.isShowing())
                progressDialog.show();
        } else {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    /*******
     * handle delivery time
     */

    private void setTimeDelivery(boolean isToday, ArrayList<String> arrTimeDeliveries) {
        this.isToday = isToday;
        listTimeDeliveries.clear();
        listTimeDeliveries.addAll(arrTimeDeliveries);
        if (this.isToday) {
            listTimeDeliveries.add(0, "ASAP");
        }
        timeDeliveryAdapter.notifyDataSetChanged();
    }

    private void showDialogSelectTimeDelivery() {
        timeDeliverydialog = new Dialog(this);
        timeDeliverydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timeDeliverydialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        timeDeliverydialog.setCancelable(false);
        timeDeliverydialog.setContentView(R.layout.layout_dialog_select_time_delivery);
        final TextViewLatoRegular tvToday = (TextViewLatoRegular) timeDeliverydialog.findViewById(R.id.tv_today);
        final TextViewLatoRegular tvTomorrow = (TextViewLatoRegular) timeDeliverydialog.findViewById(R.id.tv_tomorrow);
        final View vToday = timeDeliverydialog.findViewById(R.id.v_today);
        final View vTomorrow = timeDeliverydialog.findViewById(R.id.v_tomorrow);
        LinearLayout root = (LinearLayout) timeDeliverydialog.findViewById(R.id.root);
        root.getLayoutParams().height = AppUtil.getScreenHeight(this) / 2;
        RecyclerView rcvData = (RecyclerView) timeDeliverydialog.findViewById(R.id.rcv_data);

        rcvData.setLayoutManager(new LinearLayoutManager(this));
        setTimeDelivery(true, listTimeDeliveriesToday);
        rcvData.setAdapter(timeDeliveryAdapter);

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeDelivery(true, listTimeDeliveriesToday);
                vToday.setVisibility(View.VISIBLE);
                vTomorrow.setVisibility(View.INVISIBLE);
                tvToday.setTextColor(AppUtil.getColor(getApplicationContext(), R.color.colorPrimary));
                tvTomorrow.setTextColor(AppUtil.getColor(getApplicationContext(), R.color.textColorSecondary));
            }
        });
        tvTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeDelivery(false, listTimeDeliveriesTomorrow);
                vToday.setVisibility(View.INVISIBLE);
                vTomorrow.setVisibility(View.VISIBLE);
                tvToday.setTextColor(AppUtil.getColor(getApplicationContext(), R.color.textColorSecondary));
                tvTomorrow.setTextColor(AppUtil.getColor(getApplicationContext(), R.color.colorPrimary));
            }
        });

        if (!timeDeliverydialog.isShowing()) {
            timeDeliverydialog.show();
        }
    }

    private ArrayList<String> getListTimesOpen(OpenHour timeSchedule) {
        String openTime = timeSchedule.getOpen_time();
        String closeTime = timeSchedule.getClose_time();
        int timeStart = Integer.parseInt(openTime.split(":")[0]);
        int timeFinish = Integer.parseInt(closeTime.split(":")[0]);
        ArrayList<String> listTimes = new ArrayList<>();
        for (int i = timeStart; i <= timeFinish; i++) {
            if (i < 10) {
                String time = "0" + i + ":00";
                listTimes.add(time);
                time = "0" + i + ":30";
                listTimes.add(time);

            } else {
                String time = i + ":00";
                listTimes.add(time);
                time = i + ":30";
                listTimes.add(time);
            }
        }
        String[] arrTime = openTime.split(":");
        openTime = arrTime[0] + ":" + arrTime[1];
        if (!listTimes.get(0).equals(openTime)) {
            listTimes.remove(0);
        }
        listTimes.remove(0);
        int lastIndex = listTimes.size() - 1;
        if (!listTimes.get(lastIndex).equals(closeTime)) {
            listTimes.remove(lastIndex);
        }
        return listTimes;

    }

    private void initTimeDeliveryAdapter() {
        listTimeDeliveries = new ArrayList<>();
        timeDeliveryAdapter = new SingleAdapter(this, R.layout.item_time_delivery, listTimeDeliveries, ItemTimDeliveryVM.class, new IOnItemClickedListener() {
            @Override
            public void onItemClicked(View view) {
                timeDelivery = (String) view.getTag();
                if (isToday) {
                    tvTimeOrdering.setText(timeDelivery);
                } else {
                    tvTimeOrdering.setText("Tomorrow " + timeDelivery);
                }
                timeDeliverydialog.dismiss();
            }
        });
    }


    /*****************
     * handle promo code
     */
    private void initPromoCodeAdapter() {
        listPromoCodes = new ArrayList<>();
        promoCodeAdapter = new SingleAdapter(this, R.layout.item_promo_code_select, listPromoCodes, ItemPromoCodeSelectVM.class, new IOnItemClickedListener() {
            @Override
            public void onItemClicked(View view) {
                refreshStatusPromoCode();
                promoCode = (PromoCode) view.getTag();
                if (promoCode.getIsActive()) {
                    code = promoCode.getCode();
                    promoCode.setSelected(true);
                    promoCodeAdapter.notifyDataSetChanged();
                    bindDataPromoCode(promoCode);
                    if (promoCodeDialog.isShowing()) {
                        promoCodeDialog.dismiss();
                    }
                } else {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_promo_code_not_active);
                }

            }
        });
    }

    private void refreshStatusPromoCode() {
        for (PromoCode promoCode : listPromoCodes) {
            promoCode.setSelected(false);
        }
    }

    private void showDialogPromoCode() {
        promoCodeDialog = new Dialog(this);
        promoCodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promoCodeDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        promoCodeDialog.setContentView(R.layout.layout_dialog_list_promo_code);
        promoCodeDialog.setCancelable(true);
        progressDialog.getWindow().getAttributes().height = AppUtil.getScreenHeight(this) / 2;
        TextViewLatoMedium tvAddPromoCode = (TextViewLatoMedium) promoCodeDialog.findViewById(R.id.tv_add_promo_code);
        LinearLayout root = (LinearLayout) promoCodeDialog.findViewById(R.id.root);
        root.getLayoutParams().height = AppUtil.getScreenHeight(this) / 2;
        RecyclerView rcvData = (RecyclerView) promoCodeDialog.findViewById(R.id.rcv_data);
        rcvData.setLayoutManager(new LinearLayoutManager(this));
        rcvData.setAdapter(promoCodeAdapter);
        tvAddPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MyCartActivity.this, AddPromoCodeActivity.class), RQ_PROMO_CODE);
            }
        });
        if (!promoCodeDialog.isShowing()) {
            promoCodeDialog.show();
        }

    }

    private void checkShowPromoCode() {
        if (listPromoCodes.isEmpty()) {
            llRootAddPromoCode.setVisibility(View.VISIBLE);
            rltRootPromoCode.setVisibility(View.GONE);
        } else {
            bindDataPromoCode(promoCode);
            llRootAddPromoCode.setVisibility(View.GONE);
            rltRootPromoCode.setVisibility(View.VISIBLE);
        }

    }

    private void bindDataPromoCode(PromoCode promoCode) {
        if (promoCode != null) {
            salePromoCode = promoCode.getSale();
            tvPromoCode.setText(promoCode.getCode());
            tvSalePromoCode.setText(promoCode.getDetailPromoCode());
        }
        bindDataIntoView();
    }

    private void getListPromoCode() {
        showProgressDialog(true);
        RequestManager.getListPromoCode(Constant.ALL_PROMO_CODE_ACTIVE, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    ArrayList<PromoCode> arrPromoCode = (ArrayList<PromoCode>) response.getDataList(PromoCode.class);
                    if (arrPromoCode != null && !arrPromoCode.isEmpty()) {
                        listPromoCodes.clear();
                        listPromoCodes.addAll(arrPromoCode);
                        promoCodeAdapter.notifyDataSetChanged();
//                        filterCodeInvalid(listPromoCodes);
                        checkPromoCodeValid(listPromoCodes);
                    }
                    checkShowPromoCode();
                    showProgressDialog(false);
                }
            }

            @Override
            public void onError(String message) {
                showProgressDialog(false);
                AppUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void checkPromoCodeValid(ArrayList<PromoCode> listPromoCodes) {
        for (PromoCode promoCode : listPromoCodes) {
            if (promoCode.getIsActive()) {
                this.promoCode = promoCode;
                this.promoCode.setSelected(true);
                code = promoCode.getCode();
                break;
            }
        }
    }


    /***************
     * handle rider tip
     */
    private void initRiderTipAdapter() {
        List<Double> list = new ArrayList<>();
        for (Double aDouble : listRiderTip) {
            list.add(aDouble);
        }
        riderTipAdapter = new SingleAdapter(self, R.layout.item_rider_tip, list, ItemRiderTipVM.class, new IOnItemClickedListener() {
            @Override
            public void onItemClicked(View view) {
                riderTip = (double) view.getTag();
                tvRiderTipPrice.setText(AppUtil.formatCurrency(riderTip));
                calculateTotalFinal();
                riderTipDialog.dismiss();
            }
        });
    }

    private void showDialogRiderTip() {
        riderTipDialog = new Dialog(this);
        riderTipDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        riderTipDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        riderTipDialog.setContentView(R.layout.layout_rider_tip_dialog);
        riderTipDialog.setCancelable(true);

        TextView tvClose = (TextView) riderTipDialog.findViewById(R.id.tv_close);
        RelativeLayout root = (RelativeLayout) riderTipDialog.findViewById(R.id.root);
        root.getLayoutParams().height = AppUtil.getScreenHeight(this) / 2;
        RecyclerView rcvData = (RecyclerView) riderTipDialog.findViewById(R.id.rcv_data);
        rcvData.setLayoutManager(new LinearLayoutManager(self));
        rcvData.setAdapter(riderTipAdapter);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                riderTipDialog.dismiss();
            }
        });
        if (!riderTipDialog.isShowing()) {
            riderTipDialog.show();
        }
    }


    /********
     * clear cart
     */
    private void clearAllMyCart() {
        if (!listFoods.isEmpty()) {
            DialogUtil.showAlertDialog(this, R.string.msg_delete_all_item_cart, new DialogUtil.IDialogConfirm() {
                @Override
                public void onClickOk() {
                    AppController.getInstance().cartManager.clearCart();
                    myCartAdapter.notifyDataSetChanged();
                    bindDataIntoView();
                }
            });
        } else {
            AppUtil.showToast(this, R.string.msg_cart_empty);
        }
    }

    private void sendRequestRefreshListFood() {
        setResult(Constant.RC_REQUEST_REFRESH);
    }
}
