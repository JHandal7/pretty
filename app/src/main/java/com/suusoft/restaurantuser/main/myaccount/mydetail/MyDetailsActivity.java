package com.suusoft.restaurantuser.main.myaccount.mydetail;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivityBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.databinding.ActivityMyDetailsV2Binding;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.ImageUtil;
import com.suusoft.restaurantuser.util.StringUtil;

/**
 * Created by Suusoft on 15/08/2017.
 */

public class MyDetailsActivity extends BaseActivityBinding implements MyDetailsVM.IOnUpdateProfile {
    private MyDetailsVM viewModel;
    private ActivityMyDetailsV2Binding myDeailsBinding;

    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new MyDetailsVM(this, DataStoreManager.getUser(), this);
        return viewModel;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_my_details_v2;
    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.my_details);
        myDeailsBinding = (ActivityMyDetailsV2Binding) binding;
        User user = DataStoreManager.getUser();
        if (user != null) {
            ImageUtil.setImageOrginRotate(myDeailsBinding.imgAvatar, user.getImage());
        }
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onUpdate() {
        String name = myDeailsBinding.edtName.getText().toString().trim();
        String address = myDeailsBinding.edtAddress.getText().toString().trim();
        String email = myDeailsBinding.edtEmail.getText().toString().trim();
        String phone = myDeailsBinding.edtPhone.getText().toString().trim();

        if (StringUtil.isEmpty(name)) {
            AppUtil.showToast(this, R.string.msg_name_required);
            myDeailsBinding.edtName.requestFocus();
            return;
        }
        if (StringUtil.isEmpty(address)) {
            AppUtil.showToast(this, R.string.msg_address_required);
            myDeailsBinding.edtAddress.requestFocus();
            return;
        }
        if (!StringUtil.isValidEmail(email)) {
            AppUtil.showToast(this, R.string.msg_email_required);
            myDeailsBinding.edtEmail.requestFocus();
            return;
        }
        if (StringUtil.isEmpty(phone)) {
            AppUtil.showToast(this, R.string.msg_phone_required);
            myDeailsBinding.edtPhone.requestFocus();
            return;
        }
        viewModel.updateProfile(name, address, email, phone);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
