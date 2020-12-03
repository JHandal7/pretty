package com.suusoft.restaurantuser.main.mycart;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.main.MainActivity;
import com.suusoft.restaurantuser.model.OpenHour;
import com.suusoft.restaurantuser.model.Order;
import com.suusoft.restaurantuser.model.PromoCode;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.paypal.Paypal;
import com.suusoft.restaurantuser.retrofit.APIService;
import com.suusoft.restaurantuser.retrofit.ApiUtils;
import com.suusoft.restaurantuser.retrofit.respone.ResponeOrder;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DateTimeUtility;
import com.suusoft.restaurantuser.util.DialogUtil;
import com.suusoft.restaurantuser.util.NetworkUtility;
import com.suusoft.restaurantuser.util.StringUtil;
import com.suusoft.restaurantuser.widgets.dialog.MyProgressDialog;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suusoft on 12/10/2017.
 */

public class ShippingPaymentActivity extends BaseActivity implements View.OnClickListener {
    private static final String FORMAT_TIME = "HH:mm aa";
    private static final String FORMAT_DATE = "EEE, dd-MMM-yyyy";
    private EditText edtName, edtEmail, edtAddress;
    private TextView tvTime, tvDate, tvTotalProduct, tvDiscount, tvShipping, tvTotal, tvCompleteOrder;
    private RadioButton rabPaypal, rabCash;

    private double shippingFee;
    private int discount;

    private int hour;
    private int min;
    private int day;
    private int month;
    private int year;
    private long timeStamp;

    private Calendar cal;

    private ArrayList<OpenHour> listOpenHours;
    private String code;
    private String methodPayment;
    private double total;
    private String name;
    private String email;
    private String address;
    private OpenHour openHour;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_shipping_payment_v2;
    }

    @Override
    protected void initView() {
        initUI();
        initControl();
    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.shipping_payment);
        getDataBundle();
        initCalendar();
        bindData();
        Paypal.startPaypalService(this);
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }


    @Override
    public void onClick(View view) {
        if (view == tvTime) {
            showDialogSelectTime();
        } else if (view == tvDate) {
            showDialogSelectDate();
        } else if (view == tvCompleteOrder) {
//            sendOrder(name, email, address);
            showDialogConfirmOrder();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Paypal.isConfirm(data)) {
            sendOrder(name, email, address);
        }
    }

    private void getDataBundle() {
        Bundle bundle = getIntent().getExtras();
        PromoCode promoCode = bundle.getParcelable(Constant.KEY_PROMO_CODE);
        listOpenHours = bundle.getParcelableArrayList(Constant.KEY_LIST_OPEN_HOUR);
        if (promoCode != null) {
            shippingFee = promoCode.getSale();
            code = promoCode.getCode();
        } else {
            code = "";
        }
    }

    private void initUI() {
        edtName = (EditText) findViewById(R.id.edt_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDate = (TextView) findViewById(R.id.tv_date);
        rabPaypal = (RadioButton) findViewById(R.id.rab_paypal);
        rabCash = (RadioButton) findViewById(R.id.rab_cash);
        tvTotalProduct = (TextView) findViewById(R.id.tv_total_product);
        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        tvShipping = (TextView) findViewById(R.id.tv_shipping);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        tvCompleteOrder = (TextView) findViewById(R.id.tv_complete);
    }

    private void initControl() {
        tvTime.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvCompleteOrder.setOnClickListener(this);
    }

    private void bindData() {
        User user = DataStoreManager.getUser();
        double totalPriceProduct = AppController.getInstance().cartManager.getTotal();
        shippingFee = AppController.getInstance().cartManager.getDeliveryFee();
        total = totalPriceProduct + shippingFee;
        if (user != null) {
            edtName.setText(user.getName());
            edtAddress.setText(user.getAddress());
            edtEmail.setText(user.getEmail());
        }
        setTimeDisplay();
        setDateDisplay();
        tvDiscount.setText(discount + "%");
        tvShipping.setText(getString(R.string.currency) + AppUtil.formatCurrency(shippingFee));
        tvTotalProduct.setText(getString(R.string.currency) + AppUtil.formatCurrency(totalPriceProduct));
        tvTotal.setText(getString(R.string.currency) + AppUtil.formatCurrency(total));
        rabCash.setChecked(true);
    }

    private void initCalendar() {
        cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        timeStamp = cal.getTimeInMillis() / 1000;
    }

    private long getTimeStamp(int hour, int min, int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTimeInMillis() / 1000;
    }

    private void showDialogSelectTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                hour = hours;
                min = minutes;
                timeStamp = getTimeStamp(hour, min, day, month, year);
                setTimeDisplay();
            }
        }, hour, min, true);
        timePickerDialog.show();

    }

    private void showDialogSelectDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                year = y;
                month = m;
                day = d;
                timeStamp = getTimeStamp(hour, min, day, month, year);
                setDateDisplay();
            }
        }, year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        Calendar maxDay = Calendar.getInstance();
        maxDay.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        datePicker.setMinDate(cal.getTimeInMillis());
        datePicker.setMaxDate(maxDay.getTimeInMillis());
        datePickerDialog.show();
    }

    private void setTimeDisplay() {
        tvTime.setText(DateTimeUtility.convertTimeStampToDate(timeStamp, FORMAT_TIME));
    }

    private void setDateDisplay() {
        tvDate.setText(DateTimeUtility.convertTimeStampToDate(timeStamp, FORMAT_DATE));
    }

    private boolean isValid() {
        name = edtName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        address = edtAddress.getText().toString().trim();
        if (StringUtil.isEmpty(name)) {
            AppUtil.showToast(this, R.string.msg_name_required);
            edtName.requestFocus();
            return false;
        }
        if (!StringUtil.isValidEmail(email)) {
            AppUtil.showToast(this, R.string.msg_email_required);
            edtEmail.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty(address)) {
            AppUtil.showToast(this, R.string.msg_address_required);
            edtAddress.requestFocus();
            return false;
        }
        if (timeStamp < System.currentTimeMillis() / 1000) {
            AppUtil.showToast(this, R.string.time_delivery_invalid);
            return false;
        }
        if (!isValidTimeDelivery(timeStamp)) {
            return false;
        }
        return true;
    }

    private void sendOrder(String name, String email, String address) {
//        final MyProgressDialog myProgressDialog = new MyProgressDialog(this);
//        myProgressDialog.setCancelable(false);
//        myProgressDialog.show();
        showProgress(true);
        if (rabPaypal.isChecked()) {
            methodPayment = getString(R.string.paypal);
        }
        if (rabCash.isChecked()) {
            methodPayment = getString(R.string.cash);
        }
        if ("".equals(DataStoreManager.getUser().getPhone())) {
            AppUtil.showToast(self, "phone is empty");
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

//        RequestManager.order(self, address, String.valueOf(27.5435), String.valueOf(105.4564362), name,
//                DataStoreManager.getUser().getLastName(), String.valueOf(shippingFee), String.valueOf(timeStamp), methodPayment, String.valueOf(total), new BaseRequest.CompleteListener() {
//                    @Override
//                    public void onSuccess(ApiResponse response) {
//                        if (!response.isError()) {
////                            Order order = response.getDataObject(Order.class);
//                            setResult(Constant.RC_END_PAYMENT);
//                            AppUtil.showToast(getApplicationContext(), R.string.msg_order_success);
//                            fileList();
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void onError(String message) {
//                        Log.e("TAG", "onError: "+message );
//                    }
//                });

        APIService apiService = ApiUtils.getAPIService();
        apiService.order(DataStoreManager.getUser().getPhone(), email,
                address, String.valueOf(27.5435), String.valueOf(105.4564362), name,
                DataStoreManager.getUser().getLastName(), String.valueOf(shippingFee),
                gson.toJson(AppController.getInstance().cartManager.getCart()), code, String.valueOf(0), String.valueOf(timeStamp), methodPayment, String.valueOf(System.currentTimeMillis() / 1000), String.valueOf(total), DataStoreManager.getToken()).enqueue(new Callback<ResponeOrder>() {
            @Override
            public void onResponse(Call<ResponeOrder> call, Response<ResponeOrder> response) {
                if (response.body().isSuccess(self)) {
                    if (response.body() != null) {
                        Order order = response.body().getData();
                        Log.e("TAG", "onResponse: "+order.getTotalPrice() );
                        setResult(Constant.RC_END_PAYMENT);
                        AppUtil.showToast(getApplicationContext(), R.string.msg_order_success);
                        //fileList();
                        finish();
                    }
                }
//                myProgressDialog.dismiss();
                showProgress(false);
            }

            @Override
            public void onFailure(Call<ResponeOrder> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.getMessage() );
                AppUtil.showToast(self, t.getMessage());
                showProgress(false);
//                myProgressDialog.dismiss();
            }
        });
    }

    private void showDialogConfirmOrder() {
        DialogUtil.showAlertDialog(this, R.string.msg_confirm_order, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                if (NetworkUtility.isNetworkAvailable()) {
                    if (isValid()) {
                        if (rabPaypal.isChecked()) {
                            Paypal.requestPaypalPayment(ShippingPaymentActivity.this, total);
                        } else if (rabCash.isChecked()) {
                            sendOrder(name, email, address);
                        }
                    }
                } else {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_connection_network_error);
                }
            }
        });
    }

    private int getDayOfWeek(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private boolean isValidTimeDelivery(long timeStamp) {
        switch (getDayOfWeek(timeStamp)) {
            case 1:
                openHour = listOpenHours.get(6);
                break;
            case 2:
                openHour = listOpenHours.get(0);
                break;
            case 3:
                openHour = listOpenHours.get(1);
                break;
            case 4:
                openHour = listOpenHours.get(2);
                break;
            case 5:
                openHour = listOpenHours.get(3);
                break;
            case 6:
                openHour = listOpenHours.get(4);
                break;
            case 7:
                openHour = listOpenHours.get(5);
                break;
            default:
                openHour = listOpenHours.get(6);
                break;
        }
        String openTime = openHour.getOpen_time();
        String closeTime = openHour.getClose_time();
        String[] arrOpenTime = openTime.split(":");
        int openH = Integer.parseInt(arrOpenTime[0]);
        int openM = Integer.parseInt(arrOpenTime[1]);
        String[] arrCloseTime = closeTime.split(":");
        int closeH = Integer.parseInt(arrCloseTime[0]);
        int closeM = Integer.parseInt(arrCloseTime[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        int hourSelect = calendar.get(Calendar.HOUR_OF_DAY);
        int minSelect = calendar.get(Calendar.MINUTE);

        if (hourSelect < openH) {
            AppUtil.showToast(getApplicationContext(), R.string.msg_restaurant_has_not_open_yet);
            return false;
        } else if (hourSelect == openH) {
            if (minSelect < openM) {
                AppUtil.showToast(getApplicationContext(), R.string.msg_restaurant_has_not_open_yet);
                return false;
            }
        }
        if (hourSelect > closeH) {
            AppUtil.showToast(getApplicationContext(), R.string.msg_restaurant_were_closed);
            return false;
        } else if (hourSelect == closeH) {
            if (minSelect > closeM) {
                AppUtil.showToast(getApplicationContext(), R.string.msg_restaurant_were_closed);
                return false;
            }
        }
        return true;
    }
}
