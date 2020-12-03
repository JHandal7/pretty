package com.suusoft.restaurantuser.main.myaccount.deliveryaddress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivityBinding;
import com.suusoft.restaurantuser.base.view.SingleListFragment;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.DeliveryAddress;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;

public class DeliveryAddressActivity extends BaseActivityBinding {
    private static final String TAG = DeliveryAddressActivity.class.getName();
    private DeliveryAddressVM viewModel;
    private SingleListFragment listAddressFragment;
    private BroadcastReceiver broadcastDelAddress;

    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new DeliveryAddressVM(this);
        return viewModel;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_delivery_address;
    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.delivery_address);
        listAddressFragment = SingleListFragment.newInstance(ListAddressVM.class, R.layout.item_address, ItemAddressVM.class);
        showListAddress();
        broadcastDelAddress = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                listAddressFragment.adapter.clear();
                listAddressFragment.viewModel.getData(1);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastDelAddress, new IntentFilter(Constant.MSG_DEL_ADDRESS));
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtil.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                DeliveryAddress address = new DeliveryAddress(place.getName().toString(), place.getLatLng().latitude, place.getLatLng().longitude);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_ADDRESS, address);
                setResult(Constant.RESULT_ADDRESS, intent);
                addNewAddress(address.getAddress(), String.valueOf(address.getLat()), String.valueOf(address.getLongitude()));
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastDelAddress);
    }

    private void showListAddress() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_root_content, listAddressFragment).commit();
    }

    public void addNewAddress(String address, String lat, String longitude) {
        RequestManager.addNewAddress(address, lat, longitude, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    listAddressFragment.adapter.clear();
                    listAddressFragment.viewModel.getData(1);
                    AppUtil.showToast(getApplicationContext(), R.string.msg_add_address_success);
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
