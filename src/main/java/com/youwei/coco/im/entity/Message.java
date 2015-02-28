package com.youwei.coco.im.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String conts;
	
	public Date sendtime;
	
	public Integer senderId;
	
	public Integer receiverId;
	
//	public Integer receiverType;
	
	public Integer hasRead;
	
	public String chatId;
}
