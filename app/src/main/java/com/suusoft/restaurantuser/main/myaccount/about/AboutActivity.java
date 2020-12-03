package com.suusoft.restaurantuser.main.myaccount.about;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivityBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;

public class AboutActivity extends BaseActivityBinding implements OnMapReadyCallback {
    private AboutVM viewModel;
    private GoogleMap map;
    private double latitude = 21.025650;
    private double longitude = 105.851899;

    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new AboutVM(this);
        return viewModel;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_about_v2;
    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.about);
        ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMapAsync(this);

    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        moveCameraTo(latitude, longitude);
        addMarker(latitude, longitude, viewModel.ADDRESS);
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
}
