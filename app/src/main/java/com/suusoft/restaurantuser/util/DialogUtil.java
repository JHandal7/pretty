package com.suusoft.restaurantuser.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.listener.IConfirmation;

/**
 * Created by Suusoft on 6/29/2016.
 */
public class DialogUtil {

    public interface IDialogConfirm {
        void onClickOk();
    }

    public static void showAlertDialog(Context context, int message, final IDialogConfirm iDialogConfirm) {
        showAlertDialog(context, message, true, true, iDialogConfirm);
    }

    public static void showAlertDialog(Context context, int message, boolean isCancel, final IDialogConfirm iDialogConfirm) {
        showAlertDialog(context, message, false, isCancel, iDialogConfirm);
    }

    public static void showAlertDialog(final Context context, int message, boolean isButtonCancel, boolean isCancel, final IDialogConfirm iDialogConfirm) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setTitle("Alert");
        builder.setMessage(message);
        builder.setCancelable(isCancel);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                iDialogConfirm.onClickOk();
            }
        });
        if (isButtonCancel) {
            builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

        }
        AlertDialog dialog = builder.create();
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    public static void showImageDialog(Context context, String imageUrl) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imageView = new ImageView(context);
        ImageUtil.setImage(imageView, imageUrl);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    public static void showConfirmationDialog(Context context, String msg, String positive, String negative,
                                              boolean isCancelable, final IConfirmation iConfirmation) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setContentView(R.layout.layout_dialog_confirmation);

        TextView lblMsg = (TextView) dialog.findViewById(R.id.lbl_msg);
        TextView lblNegative = (TextView) dialog.findViewById(R.id.lbl_negative);
        TextView lblPositive = (TextView) dialog.findViewById(R.id.lbl_positive);

        lblMsg.setText(msg);
        lblNegative.setText(negative);
        lblPositive.setText(positive);

        lblNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                iConfirmation.onNegative();
            }
        });

        lblPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                iConfirmation.onPositive();
            }
        });

        dialog.setCancelable(isCancelable);

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

}
