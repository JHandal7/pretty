package com.suusoft.restaurantuser.model;


import com.google.gson.annotations.SerializedName;
import com.suusoft.restaurantuser.base.model.BaseModel;

/**
 * Created by Suusoft on 01/06/2016.
 */
public class Task extends BaseModel {

    public static final String PENDING = "pending";
    public static final String DONE = "done";
    public static final String RESUME = "resume";
    public static final String ASSIGNED = "assigned";
    public static final String JUST_POSTED = "new";
    public static final String OFFER_TASK = "offerTask";
    public static final String CANCEL = "cancel";
    public static final String FINISH = "finish";
    public static final String PAUSE = "pause";
    public static final String POSTED = "posted";


    public static final String START = "start";

    public static final String DELIVERY = "delivery";
    public static final String HANDY_WORK = "handy work";
    public static final String HOUSE_WORK = "house work";
    public static final String OTHER = "other";

    public static final String ALL = "";
    public static final String PROCESSING = "processing";
    public static final String FINISHED = "finished";

    public static final String TOTAL = "total";
    public static final String HOURLY_RATE = "hourly rate";

    public String id, rate;
    public String name, title, address, email, description, status, type;
    @SerializedName("tasker_image")
    public String image;
    private String taskerName, avatar , comments, offers, budget, dateCreated;
    private String notice, packImage, destination, taskerId, helperId, deliveryCode, donated, dateTime;
    private double latitude, longitude;
    private boolean isFollowedTasker, isFollowedTask, isRated, isOffered;
    private String helperLat, helperLong, duration, helperName, helperRate, helperImage;

    public Task() {
    }

    public String getHelperName() {
        return helperName;
    }

    public void setHelperName(String helperName) {
        this.helperName = helperName;
    }

    public String getHelperRate() {
        return helperRate;
    }

    public void setHelperRate(String helperRate) {
        this.helperRate = helperRate;
    }

    public String getHelperImage() {
        return helperImage;
    }

    public void setHelperImage(String helperImage) {
        this.helperImage = helperImage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getHelperLat() {
        return helperLat;
    }

    public void setHelperLat(String helperLat) {
        this.helperLat = helperLat;
    }

    public String getHelperLong() {
        return helperLong;
    }

    public void setHelperLong(String helperLong) {
        this.helperLong = helperLong;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getPackImage() {
        return packImage;
    }

    public void setPackImage(String packImage) {
        this.packImage = packImage;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getTaskerName() {
        return taskerName;
    }

    public void setTaskerName(String taskerName) {
        this.taskerName = taskerName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
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

    public boolean isFollowedTasker() {
        return isFollowedTasker;
    }

    public void setFollowedTasker(boolean isFav) {
        this.isFollowedTasker = isFav;
    }

    public boolean isFollowedTask() {
        return isFollowedTask;
    }

    public void setFollowedTask(boolean followedTask) {
        isFollowedTask = followedTask;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public boolean isOffered() {
        return isOffered;
    }

    public void setOffered(boolean offered) {
        isOffered = offered;
    }

    public String getHelperId() {
        return helperId;
    }

    public void setHelperId(String helperId) {
        this.helperId = helperId;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

}
