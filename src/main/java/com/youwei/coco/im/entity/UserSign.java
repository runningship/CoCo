package com.youwei.coco.im.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 不用了，在用户表里分别加入signature字段,查询是方便
 */
@Entity
@Table(name="im_UserSign")
public class UserSign {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int id;
	
	public String uid;
	
	public String sign;
	
	public String userType;
	
}
