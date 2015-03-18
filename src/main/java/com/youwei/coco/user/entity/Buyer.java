package com.youwei.coco.user.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.youwei.coco.KeyConstants;

/**
 * 买家
 */
@Entity
@Table(name="b2b_buyer")
public class Buyer implements User{

	@Id
	@Column(name="buyer_id")
	public String buyerId;
	
	public String name;
	
	@Column(name="login_code")
	public String loginCode;
	
	@Column(name="buyer_pwd")
	public String buyerPwd;
	
	@Column(name="last_login_time")
	public Date lastLoginTime;
	
	@Column(name="city_id")
	public String cityId;
	
	public String avatar;
	
	public String signature;

	@Override
	public String getId() {
		return buyerId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAvatar() {
		return avatar;
	}

	@Override
	public String getType() {
		return KeyConstants.User_Type_Buyer;
	}
	
	@Override
	public String getSign() {
		return signature;
	}
	
	@Override
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
