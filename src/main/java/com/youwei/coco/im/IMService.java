package com.youwei.coco.im;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;
import org.java_websocket.WebSocket;

import com.youwei.bosh.BoshConnectionManager;
import com.youwei.coco.IMChatHandler;
import com.youwei.coco.IMContactHandler;
import com.youwei.coco.ThreadSessionHelper;
import com.youwei.coco.YjhChatHandler;
import com.youwei.coco.YjhContactHandler;
import com.youwei.coco.im.entity.Group;
import com.youwei.coco.im.entity.Message;
import com.youwei.coco.im.entity.UserGroupStatus;
import com.youwei.coco.im.entity.UserSign;
import com.youwei.coco.user.entity.Admin;
import com.youwei.coco.user.entity.Buyer;
import com.youwei.coco.user.entity.Seller;
import com.youwei.coco.user.entity.User;
import com.youwei.coco.util.DataHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Module(name="/im/")
public class IMService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	IMChatHandler chatHandler = TransactionalServiceHelper.getTransactionalService(YjhChatHandler.class);
	
	IMContactHandler contactHandler = TransactionalServiceHelper.getTransactionalService(YjhContactHandler.class);
	
	@WebMethod
	public ModelAndView getHistory(Page<Message> page , String contactId) {
		ModelAndView mv = new ModelAndView();
		page.setPageSize(10);
		String myId = ThreadSessionHelper.getUser().getId();
		page = dao.findPage(page ,"from Message where (senderId=? and receiverId=?) or (senderId=? and receiverId=?) order by sendtime desc", myId , contactId , contactId , myId);
		mv.data.put("history", JSONHelper.toJSONArray(page.getResult() , DataHelper.sdf4.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getUserStatus(String contactId) {
		ModelAndView mv = new ModelAndView();
		mv.data.put("senderId", contactId);
		mv.data.put("status", DataHelper.getUserStatus(contactId));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getGroupHistory(Page<Message> page , String groupId) {
		ModelAndView mv = new ModelAndView();
		page.setPageSize(10);
		page = dao.findPage(page ,"from GroupMessage where groupId=? order by sendtime desc", groupId);
		mv.data.put("history", JSONHelper.toJSONArray(page.getResult() , DataHelper.sdf4.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getGroupMembers(Integer groupId) {
		ModelAndView mv = new ModelAndView();
		List<Map> list = chatHandler.getGroupMembers(groupId);
		mv.data.put("members", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getWebUnReadChats() {
		ModelAndView mv = new ModelAndView();
		User me = ThreadSessionHelper.getUser();
		mv.data.put("unReadChats", JSONHelper.toJSONArray(chatHandler.getWebUnReadChats(me.getId())));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getUnReadChats() {
		ModelAndView mv = new ModelAndView();
		User me = ThreadSessionHelper.getUser();
		mv.data.put("unReadSingleChats", JSONHelper.toJSONArray(chatHandler.getSingleChatUnReads(me.getId())));
		
		List<Map> groupList = new ArrayList<Map>();
		//TODO
//		groupList.add(getUserGroupUnReads(me.id , me.Department().id));
//		groupList.add(getUserGroupUnReads(me.id , me.Company().id));
		mv.data.put("unReadGroupChats", JSONHelper.toJSONArray(groupList));
		return mv;
	}
	
	
	private Map getGroupChatUnReads(int userId , int groupId){
		Date lasttime = getLastActivetimeOfGroup(userId , groupId);
		long count = dao.countHql("select count(*) from GroupMessage where groupId=? and sendtime>?", groupId , lasttime);
		Map<String ,Object> map = new HashMap<String , Object>();
		map.put("groupId", groupId);
		map.put("total", count);
		return map;
	}
	
	private Date getLastActivetimeOfGroup(int userId , int groupId){
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
	
	@WebMethod
	public ModelAndView setSingleChatRead(String contactId){
		ModelAndView mv = new ModelAndView();
		String hql = "update Message set hasRead=1 where senderId=? and receiverId=? and hasRead=0";
		dao.execute(hql, contactId, ThreadSessionHelper.getUser().getId());
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView setGroupChatRead(String groupId){
		//更新最后活跃时间即可
		ModelAndView mv = new ModelAndView();
		String myId = ThreadSessionHelper.getUser().getId();
		UserGroupStatus ugs = dao.getUniqueByParams(UserGroupStatus.class, new String[]{"groupId" , "receiverId"}, new Object[]{groupId , myId});
		if(ugs==null){
			ugs = new UserGroupStatus();
			ugs.groupId = groupId;
			ugs.receiverId = myId;
			ugs.lasttime = new Date();
		}else{
			ugs.lasttime = new Date();
		}
		dao.saveOrUpdate(ugs);
		return mv;
	}

	@WebMethod
	public  ModelAndView updateUserSign(String uid , String sign,String type){
//		UserSign us = dao.getUniqueByParams(UserSign.class, new String[]{"uid"}, new Object[]{uid});
		
		Seller seller = dao.get(Seller.class, uid);
		Buyer buyer = dao.get(Buyer.class, uid);
		try{
			Admin admin = dao.get(Admin.class, Integer.valueOf(uid));
			if(admin!=null){
				admin.signature = sign;
				dao.saveOrUpdate(admin);
			}
		}catch(NumberFormatException ex ){
			
		}
		
		if(seller!=null){
			seller.signature = sign;
			dao.saveOrUpdate(seller);
		}
		if(buyer!=null){
			buyer.signature = sign;
			dao.saveOrUpdate(buyer);
		}
		ModelAndView mv = new ModelAndView();
		return mv;
	}
	@WebMethod
	public ModelAndView getRecentChats(){
		ModelAndView mv = new ModelAndView();
		User u = (User)ThreadSessionHelper.getUser();
		mv.data.put("recentChats", JSONHelper.toJSONArray(chatHandler.getRecentChats(u.getType(), u.getId())));
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView createGroupWithUsers(String uids , String groupName) {
		ModelAndView mv = new ModelAndView();
		User me = ThreadSessionHelper.getUser();
		Group group = new Group();
		group.name = groupName;
		contactHandler.createGroup(me.getId(), group);
		List<String> uidList = new ArrayList<String>();
		for(String uid : uids.split(",")){
			uidList.add(uid);
		}
		contactHandler.addMembersToGroup(group.id, uidList);
		return mv;
	}
	
	@WebMethod
	public ModelAndView pushMsgToUser(String msg,String contactId , String senderId , String senderName,String senderAvatar){
		ModelAndView mv = new ModelAndView();
		JSONObject jobj = new JSONObject();
		jobj.put("type", "msg");
		jobj.put("msg", msg);
		jobj.put("senderId", senderId);
		jobj.put("senderName", senderName);
		jobj.put("senderAvatar", senderAvatar);
		jobj.put("sendtime", DataHelper.sdf4.format(new Date()));
		
		Message dbMsg = new Message();
		dbMsg.sendtime = new Date();
		dbMsg.conts = msg;
		dbMsg.senderId = senderId;
		dbMsg.receiverId = contactId;
		dbMsg.hasRead=0;
		try{
			SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(dbMsg);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		WebSocket targetConn = IMServer.getUserSocket(contactId);
		if(targetConn!=null){
			targetConn.send(jobj.toString());
		}
		DataHelper.sendToBosh(contactId, jobj);
		return mv;
	}
}
