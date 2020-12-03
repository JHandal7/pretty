package com.suusoft.restaurantuser.cart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.SingleAdapter;
import com.suusoft.restaurantuser.configs.Apis;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.main.adapter.PropertyAdapter;
import com.suusoft.restaurantuser.main.adapter.ToppingAdapter;
import com.suusoft.restaurantuser.model.Food;
import com.suusoft.restaurantuser.model.ProductOption;
import com.suusoft.restaurantuser.model.ProductVersion;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.ImageUtil;

import java.util.ArrayList;

/**
 * Created by Suusoft on 26/07/2017.
 */

public class Dialog {
    private int indexProperty = 0;
    private TextView tvPrice;
    private TextView tvPriceOptions;
    private TextView tvOptions;
    private TextView tvProperties;
    private ProductVersion mCurrentProductVersion;
    private Context self;
    private Food model;
    private SingleAdapter versionAdapter;
    private SingleAdapter optionsAdapter;

    private Dialog(Builder builder) {
        this.self = builder.context;
        model = builder.model;
    }

    public static class Builder {
        private Food model;
        private Context context;

        public Builder setFood(Food model) {
            this.model = model;
            return this;
        }

        public Dialog build(Context context) {
            this.context = context;
            return new Dialog(this);
        }
    }

    public void showDialogChooseFood() {
        final android.app.Dialog dialog = new android.app.Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_select_food);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // view
        ImageView img_close = (ImageView) dialog.findViewById(R.id.ic_cancel);
        TextView tvSave = (TextView) dialog.findViewById(R.id.lblSave);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tvFoodName = (TextView) dialog.findViewById(R.id.lblFoodName);
        TextView tvDescriptions = (TextView) dialog.findViewById(R.id.tv_description);
        tvPriceOptions = (TextView) dialog.findViewById(R.id.tv_price_option);
        final TextView tvPriceVersion = (TextView) dialog.findViewById(R.id.tv_price_version);
        tvFoodName.setSelected(true);
        TextView tvCategory = (TextView) dialog.findViewById(R.id.lblCategory);
        ImageView imgAddFood = (ImageView) dialog.findViewById(R.id.img_add);
        final TextView tvQuantityFoods = (TextView) dialog.findViewById(R.id.tv_quantity_foods);
        ImageView imgSubFood = (ImageView) dialog.findViewById(R.id.img_sub);
        LinearLayout llRootOptions = (LinearLayout) dialog.findViewById(R.id.ll_root_options);

        RecyclerView rcvVersions = (RecyclerView) dialog.findViewById(R.id.rcv_size);
        final RecyclerView rcvOptions = (RecyclerView) dialog.findViewById(R.id.rcv_options);
        rcvOptions.setLayoutManager(new GridLayoutManager(self, 3));
        rcvVersions.setLayoutManager(new GridLayoutManager(self, 3));


//        tvPrice = (TextView) dialog.findViewById(R.id.lblPrice);
//        tvPriceOptions = (TextView) dialog.findViewById(R.id.lblToppingPrice);
//        tvOptions = (TextView) dialog.findViewById(R.id.btnAddTopping);
//        tvProperties = (TextView) dialog.findViewById(R.id.lblProperties);
        final ImageView imgFood = (ImageView) dialog.findViewById(R.id.imgFood);
        // Set value
        tvFoodName.setText(model.getName());
        tvCategory.setText(model.getNameCategory());
        ImageUtil.setImage(imgFood, model.getImage());
        tvDescriptions.setText(model.getDescription());
        if (model.getImage().endsWith(Apis.URL_IMAGE_DEFAUNT)) {
            imgFood.setVisibility(View.GONE);
        } else {
            imgFood.setVisibility(View.VISIBLE);
        }


        final Food clone = (Food) AppUtil.clone(model, Food.class);

        final ArrayList<ProductVersion> listProductVersions = clone.getProduct_version();
        clone.setQuantity(1);
        tvQuantityFoods.setText("0" + clone.getQuantity());
        // init property
        mCurrentProductVersion = listProductVersions.get(0);
        for (ProductVersion v : listProductVersions) {
            if (v.isDefault()) {
                mCurrentProductVersion = v;
                break;
            }
        }
        if (!listProductVersions.isEmpty()) {
            mCurrentProductVersion.setSelected(true);
            versionAdapter = new SingleAdapter(self, R.layout.item_version, listProductVersions, ItemVersionVM.class, new IOnItemClickedListener() {
                @Override
                public void onItemClicked(View view) {
                    for (ProductVersion v : listProductVersions) {
                        v.setSelected(false);
                    }

                    mCurrentProductVersion = (ProductVersion) view.getTag();
                    mCurrentProductVersion.setSelected(true);
                    versionAdapter.notifyDataSetChanged();
                    optionsAdapter = getOptionsAdapter();
                    rcvOptions.setAdapter(optionsAdapter);

                    double priceVersion = Double.parseDouble(mCurrentProductVersion.getPrice());
                    String strPriceTopping = "";
                    if (priceVersion > 0) {
                        strPriceTopping = "(+" + self.getString(R.string.currency) + AppUtil.formatCurrency(priceVersion) + ")";
                    }
                    if (strPriceTopping.equals("")) tvPriceVersion.setVisibility(View.GONE); else  tvPriceVersion.setVisibility(View.VISIBLE);
                    tvPriceVersion.setText(strPriceTopping);
                }
            });
            optionsAdapter = getOptionsAdapter();
            rcvVersions.setAdapter(versionAdapter);
            rcvOptions.setAdapter(optionsAdapter);
        }


//        if (listProductVersions.isEmpty()) {
//            tvProperties.setText("" + self.getString(R.string.no_propertis));
//            tvPrice.setText(String.format(self.getString(R.string.format_currency), AppUtil.formatCurrency(Double.parseDouble(clone.getPrice()))));
//        } else {
//            mCurrentProductVersion = listProductVersions.get(0);
//            for (ProductVersion v : listProductVersions) {
//                if (v.isDefault()) {
//                    mCurrentProductVersion = v;
//                    break;
//                }
//            }
//            mCurrentProductVersion.setSelected(true);
//            tvProperties.setText("" + mCurrentProductVersion.getName());
//            tvPrice.setText(String.format(self.getString(R.string.format_currency), AppUtil.formatCurrency(Double.parseDouble(mCurrentProductVersion.getPrice()))));
//        }
//
//
//        tvProperties.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listProductVersions.isEmpty()) {
//                    AppUtil.showToast(self, self.getString(R.string.no_propertis));
//                } else {
//                    showDialogChooseProperty(self, listProductVersions, model);
//                }
//
//            }
//        });
//        tvOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final ProductVersion toppings = (ProductVersion) AppUtil.clone(mCurrentProductVersion, ProductVersion.class);
//                showDialogChooseTopping(self, model, toppings.getProduct_option());
//
//            }
//        });
//        if (!listProductVersions.isEmpty()) {
//            // init topping
////            mCurrentProductVersion = listProductVersions.get(indexProperty);
//            final ProductVersion toppings = (ProductVersion) AppUtil.clone(mCurrentProductVersion, ProductVersion.class);
//            if (toppings != null && !toppings.getProduct_option().isEmpty()) {
//                tvOptions.setEnabled(true);
//                tvOptions.setText(model.getTitle_topping());
//                llRootOptions.setVisibility(View.VISIBLE);
//            } else {
//                llRootOptions.setVisibility(View.GONE);
//                tvOptions.setText(model.getTitle_topping());
//                tvOptions.setEnabled(false);
//            }
//
//        } else {
//            llRootOptions.setVisibility(View.GONE);
//            tvOptions.setText("" + self.getString(R.string.no_topping));
//            tvOptions.setEnabled(false);
//
//        }


        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().cartManager.addToCart(clone);
                dialog.dismiss();
                AppUtil.showToast(view.getContext(),
                        String.format(view.getContext().getString(R.string.add_product_to_cart), clone.getName()));
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imgAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = clone.getQuantity();
                if (quantity < 99) {
                    quantity++;
                    clone.setQuantity(quantity);
                    if (quantity < 10) {
                        tvQuantityFoods.setText("0" + quantity);
                    } else {
                        tvQuantityFoods.setText(String.valueOf(quantity));
                    }

                }
            }
        });
        imgSubFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = clone.getQuantity();
                if (quantity > 1) {
                    quantity--;
                    clone.setQuantity(quantity);
                    if (quantity < 10) {
                        tvQuantityFoods.setText("0" + quantity);
                    } else {
                        tvQuantityFoods.setText(String.valueOf(quantity));
                    }
                }
            }
        });
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

    private SingleAdapter getOptionsAdapter() {
        optionsAdapter = new SingleAdapter(self, R.layout.item_options, mCurrentProductVersion.getProduct_option(), ItemOptionsVM.class, new IOnItemClickedListener() {
            @Override
            public void onItemClicked(View view) {
                ProductOption productOption = (ProductOption) view.getTag();
                if (productOption.isSelected()) {
                    productOption.setSelected(false);
                } else {
                    productOption.setSelected(true);
                }
                double priceTopping = 0;
                String toppings = "";
                if (mCurrentProductVersion.isHasOption()) {
                    ArrayList<ProductOption> options = mCurrentProductVersion.getProduct_option();
                    for (ProductOption option : options) {
                        if (option.isSelected()) {
                            priceTopping = priceTopping + Double.parseDouble(option.getPrice());
                            toppings = toppings + option.getName() + ", ";
                        }
                    }
                    String strPriceTopping = "";
                    if (priceTopping > 0) {
                        strPriceTopping = "(+" + self.getString(R.string.currency) + AppUtil.formatCurrency(priceTopping) + ")";
                    }
                    tvPriceOptions.setText(strPriceTopping);
                }
                optionsAdapter.notifyDataSetChanged();
            }
        });

        return optionsAdapter;
    }

    private void showDialogChooseProperty(final Context self, final ArrayList<ProductVersion> listProductVersions, Food model) {
        final android.app.Dialog dialog = new android.app.Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_choose_property);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().height = AppUtil.getScreenHeight((AppCompatActivity) self) / 2;

        TextView tvName = (TextView) dialog.findViewById(R.id.tv_name_food);
        tvName.setSelected(true);
        tvName.setText(model.getName());
        ListView lvListProductVersions = (ListView) dialog.findViewById(R.id.lv_properties);
        PropertyAdapter adapter = new PropertyAdapter(listProductVersions);
        adapter.setOnItemChecked(new PropertyAdapter.IItemChecked() {
            @Override
            public void onSelected(int position) {

                mCurrentProductVersion = listProductVersions.get(position);

                String name_property = mCurrentProductVersion.getName();
                tvProperties.setText(name_property);
                double priceTopping = 0;
                String toppings = "";
                mCurrentProductVersion.setSelected(true);
                if (mCurrentProductVersion.isHasOption()) {
                    ArrayList<ProductOption> options = mCurrentProductVersion.getProduct_option();
                    for (ProductOption option : options) {
                        if (option.isSelected()) {
                            priceTopping = priceTopping + Double.parseDouble(option.getPrice());
                            toppings = toppings + option.getName() + ", ";
                        }
                    }
                    tvOptions.setEnabled(true);
                    tvOptions.setText(self.getString(R.string.toppings));
                } else {
                    tvOptions.setText(self.getString(R.string.no_topping));
                    tvOptions.setEnabled(false);
                }
                if (!toppings.isEmpty()) {
                    toppings = toppings.substring(0, toppings.lastIndexOf(","));
                    tvPriceOptions.setVisibility(View.VISIBLE);
                } else {
                    tvPriceOptions.setVisibility(View.GONE);
                }
                String strPriceTopping = "";
                if (priceTopping > 0) {
                    strPriceTopping = "(+" + self.getString(R.string.currency) + AppUtil.formatCurrency(priceTopping) + ")";
                }
                tvPriceOptions.setText(toppings);
                tvPrice.setText(String.format(self.getString(R.string.format_currency), AppUtil.formatCurrency(Double.parseDouble(mCurrentProductVersion.getPrice()))) + strPriceTopping);
                dialog.dismiss();
            }
        });
        lvListProductVersions.setAdapter(adapter);


        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void showDialogChooseTopping(final Context self, Food model, final ArrayList<ProductOption> listProductOptions) {
        final android.app.Dialog dialog = new android.app.Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_toppings);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().height = AppUtil.getScreenHeight((AppCompatActivity) self) / 2;


        ListView lvListToppings = (ListView) dialog.findViewById(R.id.lvToppings);
        TextView btnSave = (TextView) dialog.findViewById(R.id.btnSave);
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btnCancel);
        TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(model.getName());
        ToppingAdapter adapter = new ToppingAdapter(listProductOptions);
        lvListToppings.setAdapter(adapter);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ProductOption option : listProductOptions) {
                    for (ProductOption opt : mCurrentProductVersion.getProduct_option()) {
                        if (option.getId().equals(opt.getId())) {
                            opt.setSelected(option.isSelected());
                        }
                    }
                }

                double priceTopping = 0;
                String toppings = "";
                for (ProductOption option : mCurrentProductVersion.getProduct_option()) {
                    if (option.isSelected()) {
                        priceTopping = priceTopping + Double.parseDouble(option.getPrice());
                        toppings = toppings + option.getName() + ", ";
                    }
                }
                if (!toppings.isEmpty()) {
                    toppings = toppings.substring(0, toppings.lastIndexOf(","));
                    tvPriceOptions.setVisibility(View.VISIBLE);
                } else {
                    tvPriceOptions.setVisibility(View.GONE);

                }

                String strPriceTopping = "";
                if (priceTopping > 0) {
                    strPriceTopping = "(+" + self.getString(R.string.currency) + AppUtil.formatCurrency(priceTopping) + ")";
                }
                tvPriceOptions.setText(toppings);
                tvPrice.setText(String.format(self.getString(R.string.format_currency), AppUtil.formatCurrency(Double.parseDouble(mCurrentProductVersion.getPrice()))) + strPriceTopping);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
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
