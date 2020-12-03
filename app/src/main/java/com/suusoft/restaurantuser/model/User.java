package com.suusoft.restaurantuser.model;

import com.google.gson.annotations.SerializedName;
import com.suusoft.restaurantuser.base.model.BaseModel;

/**
 * Created by Suusoft on 14/07/2017.
 */
public class User extends BaseModel {
    public static final String NORMAL = "n";
    public static final String SOCIAL = "s";

    public String id, rate;
    public String name, title, address, email, description, status, type;
    public String username;
    @SerializedName("avatar")
    public String image;
    public String firstName, phone;
    @SerializedName("last_name")
    public String lastName;
    private String premium;

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return name;
    }

    public void setFirstName(String firstName) {
        this.name = firstName;
    }

    public String getPhone() {
        if (phone == null)
            return "";
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
