package com.youwei.coco.im.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="im_group")
public class Group {
	
	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
	public String id;
	
	public String name;
}
