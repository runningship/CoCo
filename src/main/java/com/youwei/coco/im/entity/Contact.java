package com.youwei.coco.im.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer ownerId;
	
	public Integer contactId;
	
	public String ugroup;

	public Contact(){
		
	}
}
