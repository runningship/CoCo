package com.youwei.coco.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.youwei.coco.KeyConstants;

/**
 * 管理员
 */
@Entity
@Table(name="xx_admin")
public class Admin implements User{

	@Id
	public int id;
	
	public String name;
	
	//账号
	public String username;
	
	public String password;
	
	//大区
	public String area;
	
	public transient int avatar;

	@Override
	public String getId() {
		return String.valueOf(id);
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getAvatar() {
		return avatar;
	}
	
	@Override
	public String getType() {
		return KeyConstants.User_Type_Admin;
	}
}
