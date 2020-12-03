package com.suusoft.restaurantuser.main.myaccount.mydetail;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.listener.IConfirmation;
import com.suusoft.restaurantuser.listener.IRequestPermissions;
import com.suusoft.restaurantuser.main.user.LoginActivity;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.retrofit.APIService;
import com.suusoft.restaurantuser.retrofit.ApiUtils;
import com.suusoft.restaurantuser.retrofit.respone.ResponeUser;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;
import com.suusoft.restaurantuser.util.FileUtil;
import com.suusoft.restaurantuser.util.ImagePickerUtil;
import com.suusoft.restaurantuser.util.ImageUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suusoft on 15/08/2017.
 */

public class MyDetailsVM extends BaseViewModel implements IRequestPermissions {
    public static final int RC_CAMERA_PERMISSION = 1;

    public static final int REQUEST_CODE_SELECT_IMAGE = 1221;
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1323;
    private User model;
    private Bitmap bitmap;
    private String filePath;

    private TextView tvEdit, tvSignOut;

    private boolean isEdit = false;
    private boolean enable = false;
    private int isVisible = View.GONE;


    public interface IOnUpdateProfile {
        void onUpdate();
    }

    IOnUpdateProfile onUpdateProfile;

    public MyDetailsVM(Context self, Object o, IOnUpdateProfile onUpdateProfile) {
        super(self);
        model = (User) o;
        this.onUpdateProfile = onUpdateProfile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (data == null) {
                return;
            }
            bitmap = ImagePickerUtil.getImageFromResult(self, resultCode, data);
            String[] results = FileUtil.checkAndGetImageFile(self, data.getData());
            if (results != null) {
                filePath = results[0];
            }
            if (bitmap != null) {
                notifyChange();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_CAMERA_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageUtil.captureImage((AppCompatActivity) self, REQUEST_CODE_CAPTURE_IMAGE);
                } else {
                    showPermissionReminder();
                }
            }
        }
    }

    private void showPermissionReminder() {
        DialogUtil.showConfirmationDialog(self, self.getString(R.string.msg_remind_user_grant_camera_permission),
                self.getString(R.string.allow), self.getString(R.string.no), true, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        if (AppUtil.checkApi(Build.VERSION_CODES.M))
                            ((AppCompatActivity) self).requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
                    }

                    @Override
                    public void onNegative() {
                    }
                });
    }

    public String firstName() {
        return model.getName();
    }

    public String lastName() {
        return model.getLastName();
    }

    public String getPhone() {
        return model.getPhone();
    }

    public String getAddress() {
        return model.getAddress();
    }

    public void setFirstName(String firstName) {
        model.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        model.setLastName(lastName);
    }

    public void setPhone(String phone) {
        model.setPhone(phone);
    }

    public String getNote() {
        return String.format(self.getString(R.string.note_my_details), model.getEmail());
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean isEnable() {
        return enable;
    }

    public int isVisible() {
        return isVisible;
    }
    public String getEmail() {
        return model.getEmail();
    }

    public void save(View view) {
        onUpdateProfile.onUpdate();
    }


    public void updateProfile(final String name, final String address, String email, final String phone) {
        model = new User();

        /*tạm thời comment while reskin*/

//        RequestManager.updateProfile(name, address, email, phone, filePath, new BaseRequest.CompleteListener() {
//            @Override
//            public void onSuccess(ApiResponse response) {
//                if (!response.isError()) {
//                    User user = response.getDataObject(User.class);
//                    DataStoreManager.saveUser(user);
//                    AppUtil.showToast(self, R.string.msg_update_successfully);
//                    ((AppCompatActivity) self).setResult(Constant.RC_UPDATE_PROFILE);
//                }
//            }
//
//            @Override
//            public void onError(String message) {
//                AppUtil.showToast(self, message);
//            }
//        });
        APIService apiService = ApiUtils.getAPIService();
        apiService.updateprofile(name, address, email, phone, DataStoreManager.getToken()).enqueue(new Callback<ResponeUser>() {
            @Override
            public void onResponse(Call<ResponeUser> call, Response<ResponeUser> response) {
                if (response.body() != null) {
                    User user = response.body().getData();
                    DataStoreManager.saveUser(user);
                    AppUtil.showToast(self, R.string.msg_update_successfully);
                    ((AppCompatActivity) self).setResult(Constant.RC_UPDATE_PROFILE);
                }
            }

            @Override
            public void onFailure(Call<ResponeUser> call, Throwable t) {
                AppUtil.showToast(self, t.getMessage());
            }
        });
    }

    public void choseImage(View view) {
        DialogUtil.showConfirmationDialog(self, self.getString(R.string.title_chose_image), self.getString(R.string.from_gallery), self.getString(R.string.from_camera),
                true, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        ImageUtil.pickImage((AppCompatActivity) self, REQUEST_CODE_SELECT_IMAGE);
                    }

                    @Override
                    public void onNegative() {
                        if (AppUtil.checkApi(Build.VERSION_CODES.M)) {
                            if (AppUtil.checkPermission(self, Manifest.permission.CAMERA)) {
                                ImageUtil.captureImage((AppCompatActivity) self, REQUEST_CODE_CAPTURE_IMAGE);
                            } else {
                                ((AppCompatActivity) self).requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
                            }
                        } else {
                            ImageUtil.captureImage((AppCompatActivity) self, REQUEST_CODE_CAPTURE_IMAGE);
                        }
                    }
                });
    }

    public boolean isClickAble(){
        return isEdit;
    }

    public void showDialogOption(View view) {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_option_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);

        tvEdit = (TextView) dialog.findViewById(R.id.tv_edit);
        tvSignOut = (TextView) dialog.findViewById(R.id.tv_sign_out);
        checkEdit();
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = !isEdit;
                checkEdit();
                dialog.dismiss();
                notifyChange();
            }
        });
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void checkEdit() {
        if (isEdit) {
            tvEdit.setTextColor(AppUtil.getColor(self, R.color.colorPrimary));
            enable = true;
            isVisible = View.VISIBLE;
        } else {
            tvEdit.setTextColor(AppUtil.getColor(self, R.color.dividerColor));
            enable = false;
            isVisible = View.GONE;
        }
    }

    public void logOut() {
        DialogUtil.showAlertDialog(self, R.string.msg_confirm_log_out, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                RequestManager.logOut(new BaseRequest.CompleteListener() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        if (!response.isError()) {
                            DataStoreManager.removeUser();
                            ((AppCompatActivity) self).setResult(Constant.RC_EXIT_APP);
                            ((AppCompatActivity) self).finish();
                            AppUtil.startActivity(self, LoginActivity.class);
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

            }
        });
    }
}
