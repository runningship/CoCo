package com.youwei.coco.user.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 大区
 */
@Entity
@Table(name="b2b_bigarea")
public class BigArea {

	@Id
	public String id;
	
	public String name;
	
	public String code;
	
}
