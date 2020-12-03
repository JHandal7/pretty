package com.suusoft.restaurantuser.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.listener.IResponse;
import com.suusoft.restaurantuser.retrofit.Param;
import com.suusoft.restaurantuser.widgets.dialog.MyProgressDialog;

import org.json.JSONObject;

/**
 * Manager for the queue
 *
 */
public class VolleyRequestManager {
	private static RequestQueue mRequestQueue;
	private Context context;
	private boolean isShowWaitingDialog;
	private MyProgressDialog dialog;

	//9-7-2020
	public VolleyRequestManager(Context context, boolean showProgress, boolean cancelable) {
		this.context = context;
		this.isShowWaitingDialog = showProgress;

		if (isShowWaitingDialog) {
			try {
				// Show progress bar
				dialog = new MyProgressDialog(context);
				if (!dialog.isShowing()) {
					dialog.show();
					dialog.setCancelable(cancelable);
				}
			} catch (Exception ex) {
				// Dismiss the progress bar.
				if (isShowWaitingDialog && dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				ex.printStackTrace();
			}
		}
	} 

	/**
	 * @param context
	 * 			application context
	 */
	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	/**
	 * @return
	 * 		instance of the queue
	 * @throws
	 * 	 if initialize has not yet been called
	 */
	public static RequestQueue getRequestQueue() {
	    if (mRequestQueue != null) {
	        return mRequestQueue;
	    } else {
	        throw new IllegalStateException("Not initialized");
	    }
	}


	//9-7-2020
	public void getJSONObject(Uri.Builder builder, final RequestQueue volleyQueue, final IResponse listener) {
		final String url = builder.toString();
		try {
			JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject result) {
					try {
						if (result != null) {
							listener.onResponse(result);

							// Dismiss the progress bar.
							if (isShowWaitingDialog && dialog != null && dialog.isShowing()) {
								dialog.dismiss();
							}

							// Cancel the request.
							volleyQueue.cancelAll(url);
						}
					} catch (Exception e) {
						// Dismiss the progress bar.
						if (isShowWaitingDialog && dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						Log.e("xx", "Err: "+e.getMessage());
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					// Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
					// For AuthFailure, you can re login with user credentials.
					// For ClientError, 400 & 401, Errors happening on client side when sending api request.
					// In this case you can check how client is forming the api and debug accordingly.
					// For ServerError 5xx, you can do retry or handle accordingly.
					if (error instanceof NetworkError) {
					} else if (error instanceof ServerError) {
					} else if (error instanceof AuthFailureError) {
					} else if (error instanceof ParseError) {
					} else if (error instanceof NoConnectionError) {
					} else if (error instanceof TimeoutError) {
						Toast.makeText(context, context.getString(R.string.msg_request_time_out), Toast.LENGTH_SHORT).show();
					}

					// Dismiss the progress bar.
					if (isShowWaitingDialog && dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}

					// Cancel the request.
					volleyQueue.cancelAll(url);

					Log.e("xx", "Err");
				}
			});

			//Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
			jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(Param.REQUEST_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			jsonObjRequest.setTag(url);
			volleyQueue.add(jsonObjRequest);

		} catch (Exception ex) {
			ex.printStackTrace();
			// Dismiss the progress bar.
			if (isShowWaitingDialog && dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			Log.e("xx", "ERRRR");
			// Cancel the request.
			volleyQueue.cancelAll(url);
		}
	}
}