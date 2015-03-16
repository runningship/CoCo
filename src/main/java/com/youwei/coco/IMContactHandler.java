package com.youwei.coco;

import java.util.List;

import com.youwei.coco.im.entity.Group;

import net.sf.json.JSONArray;

public interface IMContactHandler {

	public JSONArray getUserTree();
	
	public JSONArray getChildren(String pid , String parentType);
	
	public void createGroup(String creatorUid , Group group);
	
	public void removeGroup(Integer groupId);
	
	public void addMembersToGroup(Integer groupId , List<String> uidList);
	
	public void kickMemberFromGroup(String uid , Integer groupId);
	
	public boolean allowToKickGroupMemeger(String kicker , String kicked , Integer groupId);
	
	public boolean allowToRemoveGroup(String uid, Integer groupId);
}
