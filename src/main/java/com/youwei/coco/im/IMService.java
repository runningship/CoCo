package com.youwei.coco.im;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.coco.ThreadSessionHelper;
import com.youwei.coco.im.entity.Message;
import com.youwei.coco.im.entity.UserGroupStatus;
import com.youwei.coco.user.entity.User;
import com.youwei.coco.util.DataHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Module(name="/im/")
public class IMService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView getHistory(Page<Message> page , Integer contactId) {
		ModelAndView mv = new ModelAndView();
		page.setPageSize(10);
		Integer myId = ThreadSessionHelper.getUser().id;
		page = dao.findPage(page ,"from Message where (senderId=? and receiverId=?) or (senderId=? and receiverId=?) order by sendtime desc", myId , contactId , contactId , myId);
		mv.data.put("history", JSONHelper.toJSONArray(page.getResult() , DataHelper.sdf4.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getGroupHistory(Page<Message> page , Integer groupId) {
		ModelAndView mv = new ModelAndView();
		page.setPageSize(10);
		page = dao.findPage(page ,"from GroupMessage where groupId=? order by sendtime desc", groupId);
		mv.data.put("history", JSONHelper.toJSONArray(page.getResult() , DataHelper.sdf4.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getGroupMembers(Integer groupId) {
		ModelAndView mv = new ModelAndView();
		List<Map> list = dao.listAsMap("select id as uid , avatar as avatar , uname as uname from User where (did=? or cid=?) and lock=1", groupId , groupId);
		mv.data.put("members", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getUnReadChats() {
		ModelAndView mv = new ModelAndView();
		User me = ThreadSessionHelper.getUser();
		mv.data.put("unReadSingleChats", JSONHelper.toJSONArray(getSingleChatUnReads(me.id)));
		
		List<Map> groupList = new ArrayList<Map>();
		//TODO
//		groupList.add(getUserGroupUnReads(me.id , me.Department().id));
//		groupList.add(getUserGroupUnReads(me.id , me.Company().id));
		mv.data.put("unReadGroupChats", JSONHelper.toJSONArray(groupList));
		return mv;
	}
	
	public List<Map> getSingleChatUnReads(int userId){
		List<Map> list = dao.listAsMap("select senderId as senderId ,COUNT(*) as total from Message where  receiverId=? and hasRead=0 group by senderId", userId);
		return list;
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
//	public List<Map> countUnReadMessage(int userId){
//		//需要过滤掉不在自己好友列表里面的
//		String hql = "select count(*) as total, m.senderId as senderId from Message m,Contact c where c.contactId=m.senderId and c.ownerId=? "
//				+ " and m.hasRead=0 and m.receiverId=? group by senderId";
//		List<Map> list = dao.listAsMap(hql, userId , userId);
//		return list;
//	}

	@WebMethod
	public ModelAndView setUserName(String name){
		ModelAndView mv = new ModelAndView();
		ThreadSessionHelper.getUser().uname = name;
		User po = dao.get(User.class, ThreadSessionHelper.getUser().id);
		po.uname = name;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	
	@WebMethod
	public ModelAndView setAvatar(int avatarId){
		ModelAndView mv = new ModelAndView();
		User po = dao.get(User.class, ThreadSessionHelper.getUser().id);
		ThreadSessionHelper.getUser().avatar = avatarId;
		po.avatar = avatarId;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	
//	@WebMethod
//	public ModelAndView addContact(Contact contact){
//		ModelAndView mv = new ModelAndView();
//		Contact po = dao.getUniqueByParams(Contact.class, new String[]{"ownerId", "contactId"}, new Object[]{contact.ownerId,contact.contactId});
//		if(po==null){
//			dao.saveOrUpdate(contact);
//		}
//		
//		Contact po2 = dao.getUniqueByParams(Contact.class, new String[]{"ownerId", "contactId"}, new Object[]{contact.contactId,contact.ownerId});
//		if(po2==null){
//			Contact contact2 = new Contact();
//			contact2.ownerId = contact.contactId;
//			contact2.contactId = contact.ownerId;
//			dao.saveOrUpdate(contact2);
//		}
//		mv.data.put("result", new JSONObject());
//		return mv;
//	}
//	
//	@WebMethod
//	public ModelAndView delContact(int ownerId , int contactId){
//		ModelAndView mv = new ModelAndView();
//		dao.execute("delete from Contact where ownerId=? and contactId=?", ownerId , contactId);
//		mv.data.put("result", 0);
//		return mv;
//	}
	
	@WebMethod
	public ModelAndView setSingleChatRead(int contactId){
		ModelAndView mv = new ModelAndView();
		String hql = "update Message set hasRead=1 where senderId=? and receiverId=? and hasRead=0";
		dao.execute(hql, contactId, ThreadSessionHelper.getUser().id);
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView setGroupChatRead(int groupId){
		//更新最后活跃时间即可
		ModelAndView mv = new ModelAndView();
		Integer myId = ThreadSessionHelper.getUser().id;
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
}
