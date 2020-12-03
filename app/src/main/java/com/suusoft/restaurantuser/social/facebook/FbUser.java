package com.suusoft.restaurantuser.social.facebook;


import com.suusoft.restaurantuser.model.User;

public class FbUser {
	String id, name, email, gender,avatar,first_name,last_name,phone;

	public FbUser(String id, String name, String email, String gender, String avatar) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.avatar = avatar;
	}

	public User toUser(){
		User user = new User();
		user.setImage(avatar);
		user.setName(name);
		user.setEmail(email);
		user.setId(id);
		return user;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPhone() {
		if (phone == null) {
			phone = "";
		}
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
