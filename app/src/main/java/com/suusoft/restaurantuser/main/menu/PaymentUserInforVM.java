package com.suusoft.restaurantuser.main.menu;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.view.View;
import android.widget.Switch;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.StringUtil;

/**
 * Created by Suusoft on 7/28/2016.
 *
 */
public class PaymentUserInforVM extends BaseViewModel {

    public final ObservableBoolean isEnableFields = new ObservableBoolean();

    private User user;
    private String note;

    public PaymentUserInforVM(Context activity) {
        super(activity);
        user = DataStoreManager.getUser();
        isEnableFields.set(false);

    }

    @Override
    public void destroy() {

    }

    @Override
    public void getData(int page) {

    }

    private boolean isValidate(){
        if (StringUtil.isEmpty(user.firstName) || StringUtil.isEmpty(user.lastName)  || StringUtil.isEmpty(user.phone)
                || StringUtil.isEmpty(user.address)){
            AppUtil.showToast(self, R.string.msg_enter_all_field);
            return false;
        }
        if (!StringUtil.isValidEmail(user.email)){
            AppUtil.showToast(self, R.string.msg_email_required);
            return false;
        }
        return true;
    }

    public void onClickContinue(View view) {
        if (isValidate()){
        }
    }

    public void onClickCancel(View view){

    }

    public void onEditCheckedChange(View v){
        isEnableFields.set(((Switch)v).isChecked());
    }

    public void setFirstName(String name) {
        user.firstName = name;
    }

    public void setLastName(String name) {
        user.lastName = name;
    }

    public void setPhone(String phone) {
        user.phone = phone;
    }

    public void setEmail(String email) {
        user.email = email;
    }

    public void setAddress(String address) {
        user.address = address;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return user.email;
    }

    public String getPhone() {
        return user.phone;
    }

    public String getFirstName() {
        return user.firstName;
    }

    public String getLastName() {
        return user.lastName;
    }

    public String getAddress(){
        return user.address;
    }

    public String getNote(){
        return note;
    }

}
