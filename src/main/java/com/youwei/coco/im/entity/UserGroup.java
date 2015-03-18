package com.youwei.coco.im.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="im_user_group")
public class UserGroup {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String uid;
	
	public String uname;
	
	public String avatar;
	
	public String groupId;
	
	public int isOwner;
}
