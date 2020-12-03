package com.suusoft.restaurantuser.main.home;

import android.content.Context;

import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 8/30/2016.
 */
public class CustomListVM extends BaseViewModelList {

    public CustomListVM(Context binding) {
        super(binding);
        getData(1);
    }

    @Override
    public void getData(int page) {
        List<Product> list = new ArrayList<>();

        Product product = new Product();

        product.setId("1");
        product.setName("Product 1");
        product.setImage("http://3.bp.blogspot.com/-BmZQ7WsVZhA/VHcKO8i442I/AAAAAAAAk5s/2d8mZrEInI0/s1600/cachorro-quente.png");
        product.setAddress("Ha noi Viet nam");
        product.setEmail("sample@gmail.com");
        product.setDescription("yeah, good good good");
        product.setTitle("Fast food WOW");

        Product product1 = new Product();
        product1.setId("2");
        product1.setName("Product 2");
        product1.setImage("http://3.bp.blogspot.com/-BmZQ7WsVZhA/VHcKO8i442I/AAAAAAAAk5s/2d8mZrEInI0/s1600/cachorro-quente.png");
        product1.setAddress("Ha noi");
        product1.setEmail("sample1@gmail.com");
        product1.setDescription("yeah, good good good");
        product1.setTitle("Fast food 1 WOW");

        list.add(product);
        list.add(product1);

        addListData(list);
        checkLoadingMoreComplete(1);
    }
}
