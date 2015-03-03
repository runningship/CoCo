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
public class RecentContact {

	@Id
	public int id;
	
	public String uid;
	
	//账号
	public String contactId;
	
	public Date lasttime;
}
