package com.youwei.coco;

import java.util.List;
import java.util.Map;

public interface IMChatHandler {

	public List<Map>  getUserGroups(String uid);
	
	public List<Map> getSingleChatUnReads(String userId);
	
	public List<Map> getGroupChatUnReads(String userId);
	
	public List<Map> getWebUnReadChats(String userId);
	
	public List<Map> getRecentChats(String userType,String uid);
	
	 public List<Map> getGroupMembers(String groupId); 
}
