/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.suusoft.restaurantuser.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.main.MainCustomizeActivity;
import com.suusoft.restaurantuser.main.SplashActivityOne;
import com.suusoft.restaurantuser.util.DialogUtil;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        sendNotification(remoteMessage.getData());
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    public static final String KEY_MSG = "message";
    public static final String KEY_ORDER_ID = "order_id";

    private void sendNotification(Map<String, String> messageBody) {
        String order_id = messageBody.get(KEY_ORDER_ID);
        String id_promotion = messageBody.get("promotion_id");
        String force_login = messageBody.get("force_login");

        boolean isForceLoin = handleLoginForce(force_login);
        if (isForceLoin) {
            return;
        }

        Intent intent = null;
        if (DataStoreManager.getUser() != null) {
            intent = buildIntent(order_id, id_promotion, MainCustomizeActivity.class);

        } else {
            intent = buildIntent(order_id, id_promotion, SplashActivityOne.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody.get(KEY_MSG)))
                .setContentText(messageBody.get(KEY_MSG))
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

    private Intent buildIntent(String id, String promotionId, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        if (id != null && !id.isEmpty() && !id.equals("0")) {
            bundle.putString(Constant.KEY_ORDER_ID, id);
        }
        if (promotionId != null && !promotionId.isEmpty() && !promotionId.equals("0")) {
            bundle.putString(Constant.KEY_PROMOTION_ID, promotionId);
        }
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    private boolean handleLoginForce(String force_login) {
        if (force_login != null && force_login.equals("1")) {
            if (AppController.getInstance() != null && AppController.getInstance().getForceLoginListener() != null) {
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showAlertDialog(MyFirebaseMessagingService.this, R.string.msg_token_miss_matches, false, false, new DialogUtil.IDialogConfirm() {
                            @Override
                            public void onClickOk() {
                                DataStoreManager.removeUser();
                                AppController.getInstance().getForceLoginListener().cleanSystem();
                            }
                        });
                    }
                });

                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
