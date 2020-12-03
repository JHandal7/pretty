package com.suusoft.restaurantuser.main.detail;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.suusoft.restaurantuser.base.view.BaseActivityBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.listener.ISetDataListener;
import com.suusoft.restaurantuser.model.Product;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.FileUtil;
import com.suusoft.restaurantuser.util.ImageUtil;
import com.suusoft.restaurantuser.main.demo.CustomListActivity;

/**
 * Created by Suusoft on 7/5/2016.
 */
public class DetailVM extends BaseViewModelList {

    ISetDataListener dataListener;
    private Product product;
    private String pId;

    public String getTitle(){return product.title;}
    public String getTasker_name() {
        return product.tasker_name;
    }
    public String getBudget() {
        return product.budget;
    }
    public String getDateCreated() {
        return product.dateCreated;
    }
    public String getNotice() {
        return product.notice;
    }
    public String getPack_image() {
        return product.pack_image;
    }
    public String getImage() {
        return product.image;
    }
    public String getDescription (){
        return product.description;
    }
    public String getDestination() {
        return product.destination;
    }

    public DetailVM(BaseActivityBinding context, String id, ISetDataListener dataListener) {
        super(context);
        pId = id;
        product = new Product();
        this.dataListener = dataListener;
        getData(1);
    }

    public void onClick(View view) {
        ImageUtil.pickImage((Activity) self, Constant.RQ_CODE_PICK_IMG);
    }

    public void onClickView(View view) {
        AppUtil.startActivity(self, CustomListActivity.class);
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.RQ_CODE_PICK_IMG && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            String[] results = FileUtil.checkAndGetImageFile(self,data.getData());
            if (results != null){
                postFile(results[0], results[1]);
            }
        }
    }

    @Override
    public void getData(int page){
        RequestManager.getDetail(self, pId, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                product = response.getDataObject(Product.class);
                notifyChange();
                dataListener.setListData(product);
            }

            @Override
            public void onError(String message) {
                AppUtil.showToast(self,message);
            }
        });
    }

    public void postFile(String fileAttached, String extention) {
        RequestManager.postFile(self, fileAttached, extention, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                Product list = response.getDataObject(Product.class);

                Toast.makeText(self, "post file success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(self, message, Toast.LENGTH_LONG).show();

            }
        });
    }

}
