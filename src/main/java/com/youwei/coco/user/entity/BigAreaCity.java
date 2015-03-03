package com.youwei.coco.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 大区
 */
@Entity
@Table(name="b2b_bigarea_city")
public class BigAreaCity {

	@Id
	public String id;
	
	@Column(name="bigarea_id")
	public String bigareaId;
	
	/**
	 * 城市编号
	 */
	@Column(name="area_code")
	public String areaCode;
	
}
