package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.suusoft.restaurantuser.AppController;

import java.util.ArrayList;

/**
 * Created by Suusoft on 21/07/2017.
 */

public class Food implements Parcelable {
    private String id;
    private String name;
    @SerializedName("overview")
    private String description;
    private String content;
    private String price;
    private int quantity;
    @SerializedName("image")
    private String image;
    @SerializedName("name_category")
    private String nameCategory;
    private boolean isSelected;
    @SerializedName("is_top")
    private int isTop;
    @SerializedName("properties")
    private ArrayList<ProductVersion> product_version;
    private String object_name;
    private double total;
    private String category_id;
    private int discountPromoCode;
    private double price_total;
    private double price_discount;
    private String title_topping;


    public Food() {
    }

    public Food(String id, String name, String description, String price, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }


    protected Food(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        content = in.readString();
        price = in.readString();
        quantity = in.readInt();
        image = in.readString();
        nameCategory = in.readString();
        isSelected = in.readByte() != 0;
        isTop = in.readInt();
        product_version = in.createTypedArrayList(ProductVersion.CREATOR);
        object_name = in.readString();
        total = in.readDouble();
        category_id = in.readString();
        discountPromoCode = in.readInt();
        price_total = in.readDouble();
        price_discount = in.readDouble();
        title_topping = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(content);
        parcel.writeString(price);
        parcel.writeInt(quantity);
        parcel.writeString(image);
        parcel.writeString(nameCategory);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeInt(isTop);
        parcel.writeTypedList(product_version);
        parcel.writeString(object_name);
        parcel.writeDouble(total);
        parcel.writeString(category_id);
        parcel.writeInt(discountPromoCode);
        parcel.writeDouble(price_total);
        parcel.writeDouble(price_discount);
        parcel.writeString(title_topping);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

    public String getObject_name() {
        if (object_name == null) {
            return "";
        }
        return object_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        if (price == null) {
            return "0.0";
        }
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (this.quantity == 0) {
            isSelected = false;
            AppController.getInstance().cartManager.removeFromCart(this);
        }
        AppController.getInstance().cartManager.notifiCartChange();
    }

    public String getImage() {
        if (image == null) {
            return "";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected == true)
            quantity = 1;
    }

    public ArrayList<ProductVersion> getProduct_version() {
        if (product_version == null) {
            product_version = new ArrayList<>();
        }
        return product_version;
    }

    public void setProduct_version(ArrayList<ProductVersion> product_version) {
        this.product_version = product_version;
    }

    public String getNameCategory() {
        if (nameCategory == null) {
            return "";
        }
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public boolean getIsTop() {
        return isTop == 1 ? true : false;
    }

    public String getContent() {
        if (content == null) {
            content = "";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getTotal() {
        return total;
    }

    public boolean isHasVersion() {
        if (product_version.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public String getCategory_id() {
        return category_id;
    }

    public int getDiscountPromoCode() {
        return discountPromoCode;
    }

    public void setDiscountPromoCode(int discountPromoCode) {
        this.discountPromoCode = discountPromoCode;
    }

    public boolean isSale() {
        return discountPromoCode == 0 ? false : true;
    }

    public double getPriceSale() {
        double p = Double.parseDouble(price);
        if (discountPromoCode > 0) {
            double priceSale = p - ((discountPromoCode * p) / 100);
            return priceSale;
        } else {
            return p;
        }
    }

    public double getPrice_total() {
        price_total = AppController.getInstance().cartManager.calculatePriceItem(this);
        return price_total;
    }

    public void setPrice_total(double price_total) {
        this.price_total = price_total;
    }

    public double getPrice_discount() {
        price_discount = AppController.getInstance().cartManager.calculatePriceItemSale(this);
        return price_discount;
    }

    public void setPrice_discount(double price_discount) {
        this.price_discount = price_discount;
    }

    public String getTitle_topping() {
        return title_topping;
    }
}
