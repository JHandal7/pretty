package com.suusoft.restaurantuser.main.mycart;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivityBinding;
import com.suusoft.restaurantuser.base.view.SingleAdapter;
import com.suusoft.restaurantuser.base.view.SingleListFragment;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.databinding.ActivityCheckOutBinding;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.main.MainCustomizeActivity;
import com.suusoft.restaurantuser.main.myaccount.deliveryaddress.DeliveryAddressActivity;
import com.suusoft.restaurantuser.main.myaccount.mydetail.MyDetailsActivity;
import com.suusoft.restaurantuser.main.myaccount.myorder.OrderDetailsActivity;
import com.suusoft.restaurantuser.model.DeliveryAddress;
import com.suusoft.restaurantuser.model.MethodPayment;
import com.suusoft.restaurantuser.model.ObjPayment;
import com.suusoft.restaurantuser.model.Order;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.NetworkUtility;
import com.suusoft.restaurantuser.widgets.dialog.MyProgressDialog;
import com.suusoft.restaurantuser.widgets.textview.TextViewLatoMedium;

import java.util.ArrayList;

public class CheckOutActivity extends BaseActivityBinding implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private ActivityCheckOutBinding checkOutBinding;
    private CheckOutVM viewModel;
    private GoogleMap map;
    private double latitude = 21.0425154;
    private double longitude = 105.8002436;
    private Bundle bundle;
    private DeliveryAddress address;
    private MyProgressDialog progressDialog;

    private SingleAdapter addressAdapter;
    private ArrayList<DeliveryAddress> listAddress;
    private SingleAdapter methodPaymentAdapter;
    private ArrayList<MethodPayment> listMethodPayments;
    private SingleListFragment listMethodPayment;
    private Dialog addressDialog;
    private double totalFinal = 0;
    private double riderTip = 0;
    private long timeDelivery;
    private String code;
    private String phone;
    private boolean isHasDeliveryFee;
    private double deliveryFee;

    @Override
    protected BaseViewModel getViewModel() {
        bundle = getIntent().getExtras();
        totalFinal = bundle.getDouble(Constant.KEY_TOTAL_FINAL);
        riderTip = bundle.getDouble(Constant.KEY_RIDER_TIP);
        timeDelivery = bundle.getLong(Constant.KEY_TIME_DELIVERY);
        code = bundle.getString(Constant.KEY_PROMO_CODE);
        isHasDeliveryFee = bundle.getBoolean(Constant.KEY_HAS_DELIVERY_FEE);

        if (isHasDeliveryFee) {
            totalFinal = totalFinal - AppController.getInstance().cartManager.getDeliveryFee();
        }
        viewModel = new CheckOutVM(this, totalFinal, isHasDeliveryFee);
        phone = DataStoreManager.getUser().getPhone();
        return viewModel;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_check_out;
    }

    @Override
    protected void onViewCreated() {
        checkOutBinding = (ActivityCheckOutBinding) binding;
        progressDialog = new MyProgressDialog(this);
        progressDialog.setCancelable(false);

        setToolbarTitle(R.string.check_out);
        ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMapAsync(this);
        initAddressAdapter();
        getListAddress();

        initRecycleViewMethodPayment(checkOutBinding.rcvData);

        checkOutBinding.tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtility.isNetworkAvailable()) {
                    startActivityForResult(new Intent(CheckOutActivity.this, DeliveryAddressActivity.class), 1);
                } else {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_connection_network_error);
                }

            }
        });
        checkOutBinding.tvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddress();
            }
        });
        checkOutBinding.tvPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!viewModel.isRestaurant.get())
                    if (address == null) {
                        AppUtil.showToast(self, R.string.msg_address_delivery_required);
                        return;
                    }

                if (phone == null || phone != null && phone.isEmpty()) {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_phone_update_phone);
                    Intent intent = new Intent(getApplicationContext(), MyDetailsActivity.class);
                    startActivityForResult(intent, Constant.RQ_UPDATE_PROFILE);
                    return;
                }
                if (viewModel.method == null) {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_method_is_required);
                    return;
                }
                if (NetworkUtility.isNetworkAvailable()) {
                    placeOrder();
                } else {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_connection_network_error);
                }

            }
        });
        checkOutBinding.tvPhone.setText(DataStoreManager.getUser().getPhone());


        checkOutBinding.ragOptionDelivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkShowAddress();
            }
        });

    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    private void initMap() {

    }


    private void addMarker(double lat, double longitude, String title) {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, longitude)).title(title).draggable(true);
        map.clear();
        map.addMarker(marker);
    }

    private void moveCameraTo(double lat, double longitude) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(lat, longitude)).zoom(12).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerDragListener(this);
        moveCameraTo(latitude, longitude);
        addMarker(latitude, longitude, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESULT_ADDRESS) {
            getListAddress();
        }
        if (requestCode == Constant.RQ_CONFIRM_PAYMENT && resultCode == Constant.RC_CONFIRM_PAYMENT) {
            Order order = data.getParcelableExtra(Constant.KEY_ORDER);
            viewDetailOrder(order);
        }
        if (requestCode == Constant.RQ_END_PAYMENT && resultCode == Constant.RC_END_PAYMENT) {
            AppUtil.startActivity(CheckOutActivity.this, MainCustomizeActivity.class);
        }
        if (requestCode == Constant.RQ_UPDATE_PROFILE && resultCode == Constant.RC_UPDATE_PROFILE) {
            phone = DataStoreManager.getUser().getPhone();
            checkOutBinding.tvPhone.setText(phone);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng newLocation = marker.getPosition();
        address.setAddress(marker.getTitle());
        address.setLat(newLocation.latitude);
        address.setLongitude(newLocation.longitude);
        moveCameraTo(address.getLat(), address.getLongitude());
        addMarker(address.getLat(), address.getLongitude(), address.getAddress());
    }

    private void bindDataAddress(DeliveryAddress address) {
        moveCameraTo(address.getLat(), address.getLongitude());
        addMarker(address.getLat(), address.getLongitude(), address.getAddress());
        checkOutBinding.tvAddress.setText(address.getAddress());
    }

    private void checkShowAddress() {
        if (!viewModel.isRestaurant.get()) {
            if (listAddress.isEmpty()) {
                checkOutBinding.rltRootAddress.setVisibility(View.GONE);
                checkOutBinding.llRootAddAddress.setVisibility(View.VISIBLE);
            } else {
                checkOutBinding.rltRootAddress.setVisibility(View.VISIBLE);
                checkOutBinding.llRootAddAddress.setVisibility(View.GONE);
            }
        } else {
            checkOutBinding.rltRootAddress.setVisibility(View.GONE);
            checkOutBinding.llRootAddAddress.setVisibility(View.GONE);
        }
    }

    private void initAddressAdapter() {
        listAddress = new ArrayList<>();
        addressAdapter = new SingleAdapter(this, R.layout.item_address_select, listAddress, ItemAddressSelectVM.class, new IOnItemClickedListener() {
            @Override
            public void onItemClicked(View view) {
                refreshStatusAddressSelect();
                address = (DeliveryAddress) view.getTag();
                address.setSelected(true);
                bindDataAddress(address);
                addressDialog.dismiss();
            }
        });
    }

    private void refreshStatusAddressSelect() {
        for (DeliveryAddress address : listAddress) {
            address.setSelected(false);
        }
    }

    private void showDialogAddress() {
        addressDialog = new Dialog(this);
        addressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addressDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        addressDialog.setContentView(R.layout.layout_dialog_select_address);
        addressDialog.setCancelable(true);
        addressDialog.getWindow().getAttributes().height = AppUtil.getScreenHeight(this) / 2;
        TextViewLatoMedium tvAddNewAddress = (TextViewLatoMedium) addressDialog.findViewById(R.id.tv_add_address);
        LinearLayout root = (LinearLayout) addressDialog.findViewById(R.id.root);
        root.getLayoutParams().height = AppUtil.getScreenHeight(this) / 2;
        RecyclerView rcvData = (RecyclerView) addressDialog.findViewById(R.id.rcv_data);
        rcvData.setLayoutManager(new LinearLayoutManager(this));
        rcvData.setAdapter(addressAdapter);
        tvAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtility.isNetworkAvailable()) {
                    startActivityForResult(new Intent(CheckOutActivity.this, DeliveryAddressActivity.class), 1);
                } else {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_connection_network_error);
                }
                addressDialog.dismiss();
            }
        });
        if (!addressDialog.isShowing()) {
            addressDialog.show();
        }

    }

    private void getListAddress() {
        setProgress(true);
        RequestManager.getListAddress(new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    setProgress(false);
                    ArrayList<DeliveryAddress> arrAddress = (ArrayList<DeliveryAddress>) response.getDataList(DeliveryAddress.class);
                    if (arrAddress != null && !arrAddress.isEmpty()) {
                        listAddress.clear();
                        listAddress.addAll(arrAddress);
                        address = listAddress.get(0);
                        address.setSelected(true);
                        bindDataAddress(address);
                        addressAdapter.notifyDataSetChanged();
                    }
                    checkShowAddress();
                }
            }

            @Override
            public void onError(String message) {
                setProgress(false);
            }
        });
    }

    private void placeOrder() {
        String add = "";
        double lat = 0;
        double longitude = 0;
        if (viewModel.isRestaurant.get()) {
            add = Constant.SEA_PALACE;
            lat = Constant.LATITUDE_RESTAURANT;
            longitude = Constant.LONGITUDE_RESTAURANT;
            deliveryFee = 0;
        } else {
            double amount_lowest = AppController.getInstance().amount_lowest;
            if (AppController.getInstance().cartManager.getTotal() >= amount_lowest) {
                deliveryFee = 0;
            } else {
                deliveryFee = AppController.getInstance().cartManager.getDeliveryFee();
            }
            add = address.getAddress();
            lat = address.getLat();
            longitude = address.getLongitude();
        }
        setProgress(true);
        RequestManager.order(this, add, String.valueOf(lat), String.valueOf(longitude), code, String.valueOf(riderTip), String.valueOf(deliveryFee), String.valueOf(timeDelivery), viewModel.getMethodPayment(), String.valueOf(totalFinal), new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    Order order = response.getDataObject(Order.class);
                    setProgress(false);
                    if (totalFinal > 0) {
                        payMollie(order.getId());
                    } else {
                        viewDetailOrder(order);
                    }

                }
            }

            @Override
            public void onError(String message) {
                setProgress(false);
                AppUtil.showToast(self, message);
            }
        });
    }

    private void setProgress(boolean is) {
        if (is) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }
    }

    private void payMollie(String id) {
        setProgress(true);
        RequestManager.getPaymentMolllie(id, String.valueOf(totalFinal), viewModel.method.getId(), new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                ObjPayment objPayment = response.getDataObject(ObjPayment.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.URL_PAYMENT, objPayment.getUrl_payment());
                Intent intent = new Intent(self, WebViewActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constant.RQ_CONFIRM_PAYMENT);
                AppUtil.startActivity(self, WebViewActivity.class, bundle);
                setProgress(false);
            }

            @Override
            public void onError(String message) {
                setProgress(false);
                AppUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void viewDetailOrder(Order model) {
        AppUtil.showToast(self, R.string.msg_order_success);
        AppController.getInstance().cartManager.clearCart();
        Intent intent = new Intent(self, OrderDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.KEY_ORDER, model);
        intent.putExtras(bundle);
        startActivityForResult(intent, Constant.RQ_END_PAYMENT);
    }

    private void initRecycleViewMethodPayment(RecyclerView rcvData) {
        listMethodPayments = new ArrayList<>();
        methodPaymentAdapter = new SingleAdapter(this, R.layout.item_method_payment, listMethodPayments, ItemMethodPaymentVM.class, new IOnItemClickedListener() {
            @Override
            public void onItemClicked(View view) {
                viewModel.method = (MethodPayment) view.getTag();
                viewModel.setMethodPayment(viewModel.method.getDescription(), viewModel.method.getImage().getNormal());
            }
        });
        rcvData.setLayoutManager(new LinearLayoutManager(this));
        rcvData.setAdapter(methodPaymentAdapter);
        rcvData.setNestedScrollingEnabled(false);
        getListMethodPayment();
    }

    private void getListMethodPayment() {
        setProgress(true);
        RequestManager.getListPaymentMolllie(new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {

                ArrayList<MethodPayment> arrMethodPayment = (ArrayList<MethodPayment>) response.getDataList(MethodPayment.class);
                if (arrMethodPayment != null && !arrMethodPayment.isEmpty()) {
                    listMethodPayments.addAll(arrMethodPayment);
                }
                methodPaymentAdapter.notifyDataSetChanged();
                setProgress(false);
            }

            @Override
            public void onError(String message) {
                AppUtil.showToast(getApplicationContext(), message);
                setProgress(false);
            }
        });
    }
}
