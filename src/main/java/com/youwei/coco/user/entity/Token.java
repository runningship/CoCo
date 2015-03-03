package com.youwei.coco.user.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.youwei.coco.KeyConstants;

/**
 * 买家
 */
@Entity
public class Token {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public String id;
	
	public String data;
	
	public Date addtime;
	
	public String userType;
	
	public String uid;
}
