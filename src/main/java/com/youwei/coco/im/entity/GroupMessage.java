package com.youwei.coco.im.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="im_groupmessage")
public class GroupMessage {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String conts;
	
	public Date sendtime;
	
	public String senderId;
	
	public String groupId;
}
