package com.suusoft.restaurantuser.model;

import com.google.gson.annotations.SerializedName;
import com.suusoft.restaurantuser.base.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 6/29/2016.
 */
public class Product extends BaseModel {
    public String id, rate;
    public String name, title, address, email, description, status, type;
    @SerializedName("tasker_image")
    public String image;
    public String tasker_name, avatar, budget, dateCreated;
    public String notice, pack_image, destination, taskerId, helper_id, deliveryCode, donated, dateTime;
    public double latitude, longitude;

    private List<User> offers;

    public List<User> getListOffers() {
        if (offers == null)
            offers = new ArrayList<>();
        return offers;
    }

    public void setListOffers(List<User> listOffers) {
        this.offers = listOffers;

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

    public String getTasker_name() {
        return tasker_name;
    }

    public void setTasker_name(String tasker_name) {
        this.tasker_name = tasker_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPack_image() {
        return pack_image;
    }

    public void setPack_image(String pack_image) {
        this.pack_image = pack_image;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTaskerId() {
        return taskerId;
    }

    public void setTaskerId(String taskerId) {
        this.taskerId = taskerId;
    }

    public String getHelper_id() {
        return helper_id;
    }

    public void setHelper_id(String helper_id) {
        this.helper_id = helper_id;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getDonated() {
        return donated;
    }

    public void setDonated(String donated) {
        this.donated = donated;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<User> getOffers() {
        return offers;
    }

    public void setOffers(List<User> offers) {
        this.offers = offers;
    }
}
