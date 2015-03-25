package com.youwei.coco.user.entity;


/**
 * 终端用户
 */
public interface User {

	public String getId();
	
	public String getName();

	public String getAvatar();
	
	public String getType();
	
	public String getSign();
	
	public void setSign(String sign);
	
	public void setAvatar(String avatar);
}
