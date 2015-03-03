package com.youwei.coco.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.youwei.coco.KeyConstants;

/**
 * 买家
 */
@Entity
@Table(name="b2b_company")
public class Seller implements User{

	@Id
	@Column(name="company_id")
	public String sellerId;
	
	public String name;
	
	@Column(name="company_name")
	public String companyName;
	
	@Column(name="login_code")
	public String loginCode;
	
	@Column(name="company_pwd")
	public String pwd;
	
//	@Column(name="last_login_time")
//	public Date lastLoginTime;
	
	@Column(name="city_id")
	public String cityId;
	
	public transient int avatar;

	@Override
	public String getId() {
		return sellerId;
	}

	@Override
	public String getName() {
		return companyName;
	}
	
	@Override
	public int getAvatar() {
		return avatar;
	}
	
	@Override
	public String getType() {
		return KeyConstants.User_Type_Seller;
	}
}
