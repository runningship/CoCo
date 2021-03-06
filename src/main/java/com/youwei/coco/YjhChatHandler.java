package com.youwei.coco;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;

import com.youwei.coco.im.entity.Group;
import com.youwei.coco.im.entity.UserGroup;
import com.youwei.coco.im.entity.UserGroupStatus;
import com.youwei.coco.user.entity.Buyer;
import com.youwei.coco.user.entity.Seller;
import com.youwei.coco.user.entity.User;
import com.youwei.coco.util.DataHelper;

public class YjhChatHandler implements IMChatHandler{

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Override
	public List<Map> getSingleChatUnReads(String userId) {
		List<Map> list = dao.listAsMap("select senderId as senderId ,COUNT(*) as total from Message where  receiverId=? and hasRead=0 group by senderId", userId);
		for(Map map : list){
			User user = DataHelper.getPropUser((String)map.get("senderId"));
			map.put("contactName", user.getName());
		}
		return list;
	}

	@Override
	public List<Map> getGroupChatUnReads(String userId) {
		List<UserGroup> ugList = dao.listByParams(UserGroup.class, "from UserGroup where uid=?", userId);
		List<Map> groupList = new ArrayList<Map>();
		for(UserGroup ug : ugList){
			groupList.add(getUserGroupUnReads(userId , ug.groupId));
		}
		return groupList;
	}

	private Map getUserGroupUnReads(String userId , String groupId){
		Date lasttime = getLastActivetimeOfGroup(userId , groupId);
		long count = dao.countHql("select count(*) from GroupMessage where groupId=? and sendtime>?", groupId , lasttime);
		Map<String ,Object> map = new HashMap<String , Object>();
		Group g = dao.get(Group.class, groupId);
		map.put("groupId", groupId);
		map.put("groupName", g.name);
		map.put("total", count);
		return map;
	}
	
	private Date getLastActivetimeOfGroup(String userId , String groupId){
		UserGroupStatus ugs = dao.getUniqueByParams(UserGroupStatus.class, new String[]{"groupId" , "receiverId"}, new Object[]{groupId , userId});
		if(ugs==null){
			try {
				return DataHelper.sdf.parse("1970-01-01 00:00:00");
			} catch (ParseException e) {
				return new Date();
			}
		}else{
			return ugs.lasttime;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getWebUnReadChats(String userId) {
		List<Map> list = dao.listAsMap("select senderId as senderId ,COUNT(*) as total from Message where  receiverId=? and hasRead=0 group by senderId", userId);
		User u = ThreadSessionHelper.getUser();
		List<Map> removed = new ArrayList<Map>();
		for(Map map : list){
			String uid = (String)map.get("senderId");
			User po = null;
			if(KeyConstants.User_Type_Buyer.equals(u.getType())){
				po = dao.get(Seller.class, uid);
			}else{
				po = dao.get(Buyer.class, uid);
			}
			if(po==null){
				removed.add(map);
				continue;
			}
			map.put("senderName", po.getName());
			map.put("uid", po.getId());
			map.put("senderAvatar", po.getAvatar());
		}
		list.removeAll(removed);
		return list;
	}

	@Override
	public List<Map> getRecentChats(String userType, String uid) {
		//视野分开
//		List<Map> contacts = null;
//		if(KeyConstants.User_Type_Buyer.equals(userType)){
//			contacts = dao.listAsMap("select seller.companyName as name , seller.sellerId as uid "
//					+ "from RecentContact rc ,Seller seller where rc.contactId=seller.sellerId  and rc.uid=?" ,uid);
//		}else{
//			contacts = dao.listAsMap("select buyer.name as name , buyer.buyerId as uid "
//					+ "from RecentContact rc ,Buyer buyer where rc.contactId=buyer.buyerId and rc.uid=? " ,uid);
//		}
		
		//视野合并
		List<Map> contacts = new ArrayList<Map>();
		List<Map> sellers = dao.listAsMap("select seller.companyName as name ,seller.avatar as avatar, seller.sellerId as uid ,rc.userType as type "
				+ "from RecentContact rc ,Seller seller where rc.contactId=seller.sellerId  and rc.uid=? and userType='seller' " ,uid);
		List<Map> buyers = dao.listAsMap("select buyer.name as name ,buyer.avatar as avatar, buyer.buyerId as uid,rc.userType as type "
				+ "from RecentContact rc ,Buyer buyer where rc.contactId=buyer.buyerId and rc.uid=? and userType='buyer' " ,uid);
		List<Map> admins = dao.listAsMap("select admin.name as name ,admin.avatar as avatar, admin.id as uid ,rc.userType as type "
				+ "from RecentContact rc ,Admin admin where rc.contactId=admin.id and rc.uid=? and userType='admin' " ,uid);
		List<Map> groups = dao.listAsMap("select g.name as name , g.id as uid ,rc.userType as type "
				+ "from RecentContact rc ,Group g where rc.contactId=g.id and rc.uid=? and userType='group' order by rc.lasttime desc" ,uid);
		
		contacts.addAll(sellers);
		contacts.addAll(buyers);
		contacts.addAll(admins);
		contacts.addAll(groups);
		return contacts;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getUserGroups(String uid) {
		//统计
		List<UserGroup> groups = dao.listByParams(UserGroup.class, "from UserGroup where uid=?", uid);
//		List<Map> groups = dao.listSql("select count(*) as total, groupId , g.`name` as gname from im_user_group  ug , im_group g where ug.groupId=g.id and ug.uid=? group by groupId ", uid);
		List<Map> depts = new ArrayList<Map>();
		for(UserGroup group : groups){
			List<Map> users = getGroupMembers(group.groupId);
			Group po = dao.get(Group.class, group.groupId);
			Map dept = new HashMap();
			dept.put("totalUsers", users.size());
//			dept.put("type", "部门");
			dept.put("gid", group.groupId);
			dept.put("isOwner", group.isOwner);
			dept.put("dname", po.name);
			dept.put("users", users);
			depts.add(dept);
		}
		return depts;
	}

	public List<Map> getGroupMembers(String groupId) {
		List<UserGroup> list = dao.listByParams(UserGroup.class, "from UserGroup where groupId=?", groupId);
		List<Map> users = new ArrayList<Map>();
		for(UserGroup ug : list){
			Map<String,Object> user = new HashMap<String,Object>();
			user.put("avatar", ug.avatar);
			user.put("uname", ug.uname);
			user.put("uid", ug.uid);
//			ass.put("online", true);
			users.add(user);
		}
		return users;
	}

}
