package com.youwei.coco.im.entity;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bc.sdak.SimpDaoTool;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * fid为0 表示公司，其他表示分公司
 */
@Entity
@Table(name="uc_comp")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Department {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 公司id
	 */
	public Integer fid;
	
	@Column(nullable=false)
	public String namea;
	
	//无用
	@Column(name="code_num")
	public String codeNum;
	
	@Column(name="auth_code")
	public String authCode;
	
	/**
	 * 公司代码
	 */
	public String cnum;
	
	/**
	 * 机器数
	 */
	public Integer pcnum;
	
	public String lxr;
	
	public String tel;
	
	public String beizhu;
	
	public Date addtime;
	
	//似乎无用，全为1
	public Integer flag;
	
	/**
	 * 0锁定
	 */
	public Integer sh;
	
	public Integer share;
	
	public Integer dgroup;
	
	/**
	 * 季度价格
	 */
	public Integer price;
	
	public Integer useIm;
	
	//服务过期时间
	public Date deadline;
	
	public void Group(){
		
	}
	
	public Department Company(){
		return SimpDaoTool.getGlobalCommonDaoService().get(Department.class,fid);
	}
	
}
