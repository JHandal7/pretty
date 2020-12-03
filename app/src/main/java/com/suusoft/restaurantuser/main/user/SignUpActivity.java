package com.suusoft.restaurantuser.main.user;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.retrofit.ApiUtils;
import com.suusoft.restaurantuser.retrofit.respone.ResponeRegister;
import com.suusoft.restaurantuser.retrofit.respone.ResponeUser;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.StringUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suusoft on 14/07/2017.
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private EditText edtEmail, edtPassword, edtConfirmPass, edtName, edtAddress, edtPhone, edtLastName;
    private TextView tvSignUp, tvSignIn;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    protected void initView() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfirmPass = (EditText) findViewById(R.id.edt_password_confirm);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        tvSignUp = (TextView) findViewById(R.id.tv_signup);
        tvSignIn = (TextView) findViewById(R.id.tv_sign_in);
    }

    @Override
    protected void onViewCreated() {
        tvSignUp.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);

//        tvTerm.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        SpannableString spannableStringObject = new SpannableString(getString(R.string.terms_and_conditions));
//        spannableStringObject.setSpan(new UnderlineSpan(), 43, spannableStringObject.length(), 0);
//        tvTerm.setText(spannableStringObject);
    }

    @Override
    public void onClick(View v) {
        if (v == tvSignUp) {
            signUp();
        } else if (v == tvSignIn) {
            switchToSignIn();
        }
    }

    private void signUp() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPass.getText().toString();
        String name = edtName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (!StringUtil.isValidEmail(email)) {
            showSnackBar(R.string.msg_email_required);
            edtEmail.requestFocus();
            return;
        } else if (!StringUtil.isGoodField(password)) {
            showSnackBar(R.string.msg_password_not_valid);
            edtPassword.requestFocus();
            return;
        } else if (!confirmPassword.equals(password)) {
            showSnackBar(R.string.msg_password_not_match);
            edtConfirmPass.requestFocus();
            return;
        }
        if (StringUtil.isEmpty(name)) {
            showSnackBar(R.string.msg_name_required);
            edtName.requestFocus();
            return;
        }

        if (StringUtil.isEmpty(address)) {
            showSnackBar(R.string.msg_address_required);
            edtAddress.requestFocus();
        }

        if (StringUtil.isEmpty(phone)) {
            showSnackBar(R.string.msg_phone_required);
            edtPhone.requestFocus();
        }
        requestToserver(email, password, name, address, phone);
    }

    private void requestToserver(String email, String password, String name,String address, String phone) {
        showProgress(true);
        ApiUtils.getAPIService().register(email, password, name, address, phone).enqueue(new Callback<ResponeRegister>() {
            @Override
            public void onResponse(Call<ResponeRegister> call, Response<ResponeRegister> response) {
                if (response.isSuccessful()) {
                    showToast(R.string.msg_register_success);
                    startActivity(LoginActivity.class);
                    finish();
                }
                showProgress(false);
            }

            @Override
            public void onFailure(Call<ResponeRegister> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
                showProgress(false);
            }
        });
        
        //        RequestManager.register(this, email, password, name,  address, phone, new BaseRequest.CompleteListener() {
//
//            @Override
//            public void onSuccess(ApiResponse response) {
//                if (!response.isError()) {
//                    showToast(R.string.msg_register_success);
//                    startActivity(LoginActivity.class);
//                    finish();
//                }
//
//            }
//
//            @Override
//            public void onError(String msg) {
//                showSnackBar(msg);
//            }
//        });
    }

    private void switchToSignIn() {
        AppUtil.startActivity(this, LoginActivity.class);
        finish();
    }

    private void showDialogTermConditions() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_term_and_condition_dialog);
        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        root.getLayoutParams().height = AppUtil.getScreenHeight(this) / 2;
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
}
