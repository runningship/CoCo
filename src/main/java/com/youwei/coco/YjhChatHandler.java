package com.youwei.coco;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;

import com.youwei.coco.user.entity.Buyer;
import com.youwei.coco.user.entity.Seller;
import com.youwei.coco.user.entity.User;

public class YjhChatHandler implements IMChatHandler{

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Override
	public List<Map> getGroupMembers(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map> getSingleChatUnReads(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map> getGroupChatUnReads(String userId, String groupId) {
		// TODO Auto-generated method stub
		return null;
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
		List<Map> sellers = dao.listAsMap("select seller.companyName as name , seller.sellerId as uid "
				+ "from RecentContact rc ,Seller seller where rc.contactId=seller.sellerId  and rc.uid=?" ,uid);
		List<Map> buyers = dao.listAsMap("select buyer.name as name , buyer.buyerId as uid "
				+ "from RecentContact rc ,Buyer buyer where rc.contactId=buyer.buyerId and rc.uid=? " ,uid);
		List<Map> admins = dao.listAsMap("select admin.name as name , admin.id as uid "
				+ "from RecentContact rc ,Admin admin where rc.contactId=admin.id and rc.uid=? " ,uid);
		contacts.addAll(sellers);
		contacts.addAll(buyers);
		contacts.addAll(admins);
		return contacts;
	}

}
