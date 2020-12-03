package com.suusoft.restaurantuser.main.user;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.listener.ModelManagerListener;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.modelmanager.ModelManager;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.social.facebook.FaceBookManager;
import com.suusoft.restaurantuser.social.facebook.FbUser;
import com.suusoft.restaurantuser.social.googleplus.GUser;
import com.suusoft.restaurantuser.social.googleplus.GooglePlusManager;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;
import com.suusoft.restaurantuser.util.NetworkUtility;
import com.suusoft.restaurantuser.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suusoft on 14/07/2017.
 */
public class LoginVM extends BaseViewModel implements FaceBookManager.ICallbackLoginFacebook,
        GooglePlusManager.ICallbackGoogleLogin {

    private ISignInListener mISignInListener;
    public GooglePlusManager googlePlusManager;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private RequestQueue mRequestQueue;

    public interface ISignInListener {
        void onSignInComplete();

        void onSignInError(int message);

        void onClickForgotpass();

        void onClickSignUp();
    }

    public LoginVM(Context context, ISignInListener signInListener) {
        super(context);
        mISignInListener = signInListener;

        FaceBookManager.initSdk(self);
        googlePlusManager = new GooglePlusManager((FragmentActivity) self, this);
    }

    private String userName = "", password = "";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void destroy() {
        self = null;
        mISignInListener = null;
    }

    @Override
    public void getData(int page) {

    }

    public void onClickLogin(View view) {
        login("0");
    }

    public void onClickLoginFb(View view) {
        //signInFacebook();
        FaceBookManager.login((Activity) self, this);
    }

    public void onClickLoginGg(View view) {
        googlePlusManager.login();
    }

    public void onClickLoginForgot(View view) {
        showDialogForgotPass();
    }

    public void onClickSignUp(View view) {
        mISignInListener.onClickSignUp();
    }

    private void login(String force_login) {

        //để test
//        mISignInListener.onSignInComplete();

        if (!StringUtil.isValidEmail(userName)) {
            mISignInListener.onSignInError(R.string.msg_email_required);
            return;
        } else if (!StringUtil.isGoodField(password)) {
            mISignInListener.onSignInError(R.string.msg_password_not_valid);
            return;
        }


        RequestManager.login(self, userName, password, force_login, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse responseData) {
                if (!responseData.isError()) {
                    User user = responseData.getDataObject(User.class);
                    AppController.getInstance().setToken(responseData.getValueFromRoot(Constant.LOGIN_TOKEN));
                    DataStoreManager.saveUser(user);
                    DataStoreManager.saveToken(AppController.getInstance().getToken());
                    AppUtil.showToast(self, R.string.msg_login_successfully);
                    DataStoreManager.saveLoginSocial(false);
                    mISignInListener.onSignInComplete();
                }

            }

            @Override
            public void onError(String message) {
                if (message.equals("202")) {
                    DialogUtil.showAlertDialog((AppCompatActivity) self, R.string.msg_your_account_logged_in_on_an_other_device_do_you_sure_continues_log_in, new DialogUtil.IDialogConfirm() {
                        @Override
                        public void onClickOk() {
                            login("1");
                        }
                    });
                } else {
                    AppUtil.showToast(self, message);
                }
            }
        });
    }

    @Override
    public void onLoginFbSuccess(FbUser user) {
        loginSocial(user.getEmail(), user.getName(), user.getLast_name(), user.getPhone(), user.getAvatar(), "0");
        Log.d("facebook", "Information: onLoginFbSuccess " + new Gson().toJson(user) );
    }

    @Override
    public void onLoginFbError() {
        AppUtil.showToast(self, R.string.msg_log_in_face_error);
    }

    @Override
    public void onLoginGgSuccess(GUser user) {
        loginSocial(user.getEmail(), user.getFullname(), user.getFullname(), "", user.getAvatar(), "0");
//        AppUtil.showToast(self, R.string.signed_in_fmt);
    }

    @Override
    public void onLoginGgError() {
        AppUtil.showToast(self, R.string.signed_out);
    }

    private void loginSocial(final String email, final String firstName, final String lastName, final String phone, final String avatar,  String force_login) {
        RequestManager.login(self, email, firstName, lastName, phone, avatar, force_login, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    User user = response.getDataObject(User.class);
                    AppController.getInstance().setToken(response.getValueFromRoot(Constant.LOGIN_TOKEN));
                    DataStoreManager.saveUser(user);
                    DataStoreManager.saveToken(AppController.getInstance().getToken());
                    Log.e("TOKEN", "onSuccess: " + DataStoreManager.getToken() );
                    AppUtil.showToast(self, R.string.msg_login_successfully);
                    DataStoreManager.saveLoginSocial(true);
                    mISignInListener.onSignInComplete();
                }
            }

            @Override
            public void onError(String message) {
                if (message.equals("202")) {
                    DialogUtil.showAlertDialog((AppCompatActivity) self, R.string.msg_your_account_logged_in_on_an_other_device_do_you_sure_continues_log_in, new DialogUtil.IDialogConfirm() {
                        @Override
                        public void onClickOk() {
                            loginSocial(email, firstName, lastName, phone, avatar, "1");
                        }
                    });
                } else {
                    AppUtil.showToast(self, message);
                }
            }
        });
    }

    private void showDialogForgotPass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(self);

        View view = LayoutInflater.from(self).inflate(R.layout.layout_dialog_forgot_password, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        final EditText edtEmail = (EditText) view.findViewById(R.id.edt_email);
        TextView tvReset = (TextView) view.findViewById(R.id.tv_reset);

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtility.isNetworkAvailable()) {
                    String email = edtEmail.getText().toString();
                    if (!StringUtil.isValidEmail(email)) {
                        AppUtil.showToast(self, R.string.msg_email_required);
                        return;
                    }
                    RequestManager.resetPassword(email, new BaseRequest.CompleteListener() {
                        @Override
                        public void onSuccess(ApiResponse object) {
                            if (!object.isError()) {
                                AppUtil.showToast(self, object.getMessage());
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(String message) {
                            AppUtil.showToast(self, message);
                            dialog.dismiss();
                        }

                    });
                } else

                {
                    AppUtil.showToast(self, R.string.msg_connection_network_error);
                    dialog.dismiss();
                }


            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    // Login with Facebook
//    private void signInFacebook(){
//
//        if (NetworkUtility.isNetworkAvailable()) {
//            mCallbackManager = CallbackManager.Factory.create();
//
//            if (AccessToken.getCurrentAccessToken() != null) {
//                LoginManager.getInstance().logOut();
//            }
//
//            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//                @Override
//                public void onSuccess(LoginResult loginResult) {
//                    String accessToken = loginResult.getAccessToken().getToken();
//                    ModelManager.getFacebookInfo(self, mRequestQueue, accessToken, new ModelManagerListener() {
//                        @Override
//                        public void onSuccess(Object object) {
//                            JSONObject jsonObject = (JSONObject) object;
//                            String email    = jsonObject.optString("email");
//                            String name     = jsonObject.optString("name");
//                            String avatar   = "";
//
//                            try {
//                                avatar = jsonObject.getJSONObject("picture").getJSONObject("data").optString("url");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            if (!email.equals("")) {
//                                Log.e("Splash", "onSuccess login fb2");
//                                login2(email, "1");
//                            } else {
//                                Toast.makeText(self, R.string.msg_can_not_get_email, Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onError() {
//                            Log.e("Splash", "onError login fb");
//                            Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancel() {
//                    Log.e("onCancel", "Facebook Login cancel");
//                }
//
//                @Override
//                public void onError(FacebookException error) {
//                    Log.e("onError", "Facebook Login error" + error.getMessage().toString());
//                }
//            });
//            LoginManager.getInstance().logInWithReadPermissions((Activity) self, Arrays.asList("public_profile", "email"));
//        }else {
//            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void login2(String userName, String force_login){
//        RequestManager.login(self, userName, "", force_login, new BaseRequest.CompleteListener() {
//            @Override
//            public void onSuccess(ApiResponse responseData) {
//                if (!responseData.isError()) {
//                    User user = responseData.getDataObject(User.class);
//                    AppController.getInstance().setToken(responseData.getValueFromRoot(Constant.LOGIN_TOKEN));
//                    DataStoreManager.saveUser(user);
//                    DataStoreManager.saveToken(AppController.getInstance().getToken());
//                    AppUtil.showToast(self, R.string.msg_login_successfully);
//                    DataStoreManager.saveLoginSocial(false);
//                    mISignInListener.onSignInComplete();
//                }
//
//            }
//
//            @Override
//            public void onError(String message) {
//                if (message.equals("202")) {
//                    DialogUtil.showAlertDialog((AppCompatActivity) self, R.string.msg_your_account_logged_in_on_an_other_device_do_you_sure_continues_log_in, new DialogUtil.IDialogConfirm() {
//                        @Override
//                        public void onClickOk() {
//                            login("1");
//                        }
//                    });
//                } else {
//                    AppUtil.showToast(self, message);
//                }
//            }
//        });
//    }


}
