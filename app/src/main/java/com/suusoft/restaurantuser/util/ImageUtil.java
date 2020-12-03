package com.suusoft.restaurantuser.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.suusoft.restaurantuser.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Suusoft.
 * Created 06/13/16. v1.0
 */
public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();

    public static int getScreenWidth(Activity act) {
        /* getWidth() is deprecated */
        // Display display = act.getWindowManager().getDefaultDisplay();
        // return display.getWidth();

        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static void calViewRatio(Activity act, View img, int x, int y, int subtract) {
        int w = getScreenWidth(act) - subtract;
        img.getLayoutParams().width = w;
        img.getLayoutParams().height = w * y / x;
    }

    /**
     * get path file from uri.
     */
    public static String getFilePathFromUri(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return "";
    }

    /**
     * Convert from bitmap to base64
     */
    public static final String convertBitmapToBase64(Bitmap bitmap, String extention) {
        if (bitmap != null) {
            Bitmap.CompressFormat ext = extention.equals("jpg") == true ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(ext, 90, stream);
            byte[] imageToByte = stream.toByteArray();
            return Base64.encodeToString(imageToByte, Base64.DEFAULT);
        }

        return "";
    }


    /**
     * convert from base64 to image
     *
     * @param input text base64
     */
    public static Bitmap convertBase64ToBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    // Decode bitmap
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * decode image to a width and high
     *
     * @param res resource of image
     */
    public static Bitmap decodeBitmap(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    /**
     * decode image to a width and high
     *
     * @param imageFile image url
     */
    public static Bitmap decodeBitmap(String imageFile, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFile, options);
    }


    /**
     * decode image to a width and high
     *
     * @param bitmap bitmap
     */
    public static Bitmap decodeBitmap(Bitmap bitmap, String extention, int reqWidth, int reqHeight) {

        Bitmap.CompressFormat ext = extention.equals("jpg") == true ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(ext, 100, blob);
        byte[] bitmapData = blob.toByteArray();

        BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length, options);
    }

    /**
     * resize image to a width and high
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    /**
     * Blur image by Renderscipt
     * Require:
     * + config in .gradle #defaultconfig
     *   {
     *       renderscriptTargetApi 19 renderscriptSupportModeEnabled true
     *   }
     * + import v.8 rederscript
     * @params context context
     * @params image bitmap of image
     *
     */
    /*
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
    */

    /**
     * pick dialog gallery
     */
    public static void pickImage(Fragment context, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(intent, requestCode);
    }

    public static void pickImage(Activity context, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(intent, requestCode);
    }

    public static void captureImage(Activity activity, int requestCode) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(cameraIntent, requestCode);
    }

    public static void captureImage(Fragment fragment, int requestCode) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        fragment.startActivityForResult(cameraIntent, requestCode);
    }

    /**
     * Set image by Picasso
     *
     * @param imageView image destination
     * @param imageUrl  image path
     */
    @BindingAdapter("url")
    public static void setImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.equals("")) {


            if (!imageUrl.endsWith("no_image.jpg")) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(android.R.drawable.stat_notify_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(600, 400)
                        .priority(Priority.HIGH);

//                Picasso.with(imageView.getContext())
//                        .load(imageUrl)
//                        .error(R.mipmap.ic_launcher)
//                        .resize(600, 400)
//                        .into(imageView);
                Glide.with(imageView.getContext())
                        .load(imageUrl)
                        .apply(options)
                        .into(imageView);
            }

        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }


    /**
     * Set image by Picasso from a size
     *
     * @param imageView image destination
     * @param imageUrl  image path
     * @param high      high des
     * @param width     width des
     */
    public static void setImage(ImageView imageView, String imageUrl, int width, int high) {
        if (imageUrl != null && !imageUrl.equals("")) {
            if (!imageUrl.endsWith("no_image.jpg")) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(android.R.drawable.stat_notify_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(width, high)
                        .priority(Priority.HIGH);

                Glide.with(imageView.getContext())
                        .load(imageUrl)
                        .apply(options)
                        .into(imageView);



//                Picasso.with(imageView.getContext())
//                        .load(imageUrl)
//                        .error(R.mipmap.ic_launcher)
//                        .resize(width, high)
//                        .into(imageView);
            }
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @BindingAdapter("setImageOrginRotate")
    public static void setImageOrginRotate(ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.equals("")) {
            if (!imageUrl.endsWith("no_image.jpg")) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(android.R.drawable.stat_notify_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH);
                Glide.with(imageView.getContext())
                        .load(imageUrl)
                        .apply(options)
                        .into(imageView);

                Log.e("setImageOrginRotate", "setImageOrginRotate: "+ imageUrl);

//                Picasso.with(imageView.getContext())
//                        .load(imageUrl)
//                        .error(R.mipmap.ic_launcher)
//                        .placeholder(R.drawable.placeholder)
//                        .into(imageView);
            }
        } else {
//            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }
    @BindingAdapter("setImageOrgin")
    public static void setImageOrgin(ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.equals("")) {
            if (!imageUrl.endsWith("no_image.jpg")) {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(android.R.drawable.stat_notify_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH);
                Glide.with(imageView.getContext())
                        .load(imageUrl)
                        .apply(options)
                        .into(imageView);

                Log.e("setImageOrgin", "setImageOrgin: "+ imageUrl);

//                Picasso.with(imageView.getContext())
//                        .load(imageUrl)
//                        .error(R.mipmap.ic_launcher)
//                        .placeholder(R.drawable.progress_holder)
//                        .into(imageView);
            }
        } else {
//            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    /**
     * Set image by Picasso
     */
    @BindingAdapter("url")
    public static void setImage(ImageView imageView, int res) {
        imageView.setImageResource(res);
    }

    @BindingAdapter("url")
    public static void setImage(ImageView imageView, Bitmap bitmap) {
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
    }

    /**
     * This class get bitmap from gallery and set to imageView. It's running on background
     */
    public static class RunnerLoadBitmap extends AsyncTask<Uri, Void, Bitmap> {
        private ImageView imageView;
        private Context context;

        public RunnerLoadBitmap setImageView(ImageView imageView) {
            this.imageView = imageView;
            this.context = imageView.getContext();
            return this;
        }

        @Override
        protected Bitmap doInBackground(Uri... uris) {
            Bitmap bitmap = null;
            try {
                Uri uri = uris[0];
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
            if (bitmap != null) {
                return ImageUtil.decodeBitmap(bitmap, "jpg", 300, 300);
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                if (imageView != null)
                    imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static void setImageFromUri(ImageView imageBitmap, Uri uri) {
        new RunnerLoadBitmap().setImageView(imageBitmap).execute(uri);
    }


}
