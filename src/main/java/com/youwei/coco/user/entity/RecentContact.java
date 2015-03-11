package com.youwei.coco.user.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.youwei.coco.KeyConstants;

/**
 * 最近联系人,web用
 */
@Entity
@Table(name="im_recentcontact")
public class RecentContact {

	@Id
	public int id;
	
	public String uid;
	
	//账号
	public String contactId;
	
	//联系人类别
	public String userType;
	
	public Date lasttime;
}
