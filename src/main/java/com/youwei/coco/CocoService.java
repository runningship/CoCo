package com.youwei.coco;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ThreadSession;
import org.bc.web.WebMethod;
import org.jsoup.helper.DataUtil;

import com.youwei.coco.cache.ConfigCache;
import com.youwei.coco.im.IMServer;
import com.youwei.coco.im.entity.IMLog;
import com.youwei.coco.im.entity.UserSign;
import com.youwei.coco.user.entity.Admin;
import com.youwei.coco.user.entity.Buyer;
import com.youwei.coco.user.entity.RecentContact;
import com.youwei.coco.user.entity.Seller;
import com.youwei.coco.user.entity.Token;
import com.youwei.coco.user.entity.User;
import com.youwei.coco.util.DataHelper;
import com.youwei.coco.util.SecurityHelper;

@Module(name="/")
public class CocoService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public static final int AssistantUid=-999;
	public static final int AssistantAvatar=116;
	public static final String AssistantName="小助手";
	
	private IMContactHandler contactHandler = TransactionalServiceHelper.getTransactionalService(YjhContactHandler.class);
	IMChatHandler chatHandler = TransactionalServiceHelper.getTransactionalService(YjhChatHandler.class);
	@WebMethod
	public ModelAndView myProfile(){
		ModelAndView mv = new ModelAndView();
		User u = (User)ThreadSessionHelper.getUser();
		mv.data.put("me", DataHelper.toCommonUser(u));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getSeller(String sellerId){
		ModelAndView mv = new ModelAndView();
		Seller po = dao.get(Seller.class, sellerId);
		mv.data.put("seller", DataHelper.toCommonUser(po));
		return mv;
	}
	
	@WebMethod
	public ModelAndView auth(String token){
		ModelAndView mv = new ModelAndView();
		System.out.println("auth:"+token);
		User session_user = (User)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_User);
		Token po = dao.getUniqueByKeyValue(Token.class, "data", token);
		if(po!=null){
			User u = DataHelper.getUser(po.userType , po.uid);
			if(u==null){
				mv.data.put("result", "-1");
			}else{
				if(session_user==null){
					IMLog imLog = new IMLog();
					imLog.action = KeyConstants.IM_Action_Login;
					imLog.uid = u.getId();
					imLog.utype = u.getType();
					imLog.actiontime = new Date();
					dao.saveOrUpdate(imLog);
				}
				
				ThreadSession.getHttpSession().setAttribute(KeyConstants.Session_User, u);
				mv.data.put("me", DataHelper.toCommonUser(u));
				//get recent chat
				List<Map> contacts = chatHandler.getRecentChats(u.getType(), u.getId());
				getUserStatus(contacts);
				mv.data.put("contacts", JSONHelper.toJSONArray(contacts));
				List<Map> unReadChats = chatHandler.getWebUnReadChats(u.getId());
				getUserStatus(unReadChats);
				mv.data.put("unReadChats", JSONHelper.toJSONArray(unReadChats));
			}
		}else{
			mv.data.put("result", "-1");
		}
		return mv;
	}
	
	private void getUserStatus(List<Map> chats){
		for(Map chat : chats){
			String uid = chat.get("uid").toString();
			chat.put("status" , DataHelper.getUserStatus(uid));
		}
	}
	@WebMethod
	public ModelAndView home(){
		ModelAndView mv = new ModelAndView();
		User u = (User)ThreadSessionHelper.getUser();
		mv.jspData.put("me",DataHelper.toCommonUser(u));
		mv.jspData.put("groups",chatHandler.getUserGroups(u.getId()));
		mv.jspData.put("domainName", ConfigCache.get("domainName" , "www.zhongjiebao.com"));
		return mv;
	}
	
	
	
	@WebMethod
	public ModelAndView yjh(String token,String userType){
		ModelAndView mv = new ModelAndView();
		mv.jspData.put("token",token);
		mv.jspData.put("userType",userType);
		return mv;
	}
	
	@WebMethod
	public ModelAndView yjh_buyer(String token){
		ModelAndView mv = new ModelAndView();
		mv.jspData.put("token",token);
		return mv;
	}
	
	@WebMethod
	public ModelAndView yjh_seller(String token){
		ModelAndView mv = new ModelAndView();
		mv.jspData.put("token",token);
		return mv;
	}
	
	@WebMethod
	public ModelAndView webchat(String token , String sellerId){
		ModelAndView mv = new ModelAndView();
//		mv.jspData.put("token", token);
//		Seller seller = dao.get(Seller.class, sellerId);
//		mv.jspData.put("token", token);
//		mv.jspData.put("seller", seller);
		User u = (User)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_User);
		if(u!=null){
			mv.jspData.put("myUid", u.getId());
			mv.jspData.put("myName", u.getName());
		}
		mv.jspData.put("resource", UUID.randomUUID().toString());
		mv.jspData.put("domainName", ConfigCache.get("domainName" , "www.zhongjiebao.com"));
		return mv;
	}
	
	@WebMethod
	public ModelAndView addRecentContact(String contactId , String userType){
		ModelAndView mv = new ModelAndView();
		String uid =ThreadSessionHelper.getUser().getId();
		RecentContact po = dao.getUniqueByParams(RecentContact.class, new String[]{"uid" , "contactId"}, new Object[]{uid , contactId});
		if(po!=null){
			po.lasttime = new Date();
		}else{
			po = new RecentContact();
			po.uid = uid;
			po.contactId = contactId;
			po.lasttime = new Date();
			User u = DataHelper.getPropUser(contactId);
			if(u!=null){
				po.userType = u.getType();
			}
			if(StringUtils.isNotEmpty(userType)){
				po.userType = userType;
			}
		}
		dao.saveOrUpdate(po);
		return mv;
	}
	
	
	@WebMethod
	public ModelAndView removeRecentContact(String contactId){
		ModelAndView mv = new ModelAndView();
		String uid =ThreadSessionHelper.getUser().getId();
		RecentContact po = dao.getUniqueByParams(RecentContact.class, new String[]{"uid" , "contactId"}, new Object[]{uid , contactId});
		if(po!=null){
			dao.delete(po);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView open(){
		ModelAndView mv = new ModelAndView();
		mv.jspData.put("host", ConfigCache.get("domainName", ""));
		return mv;
	}
	@WebMethod
	public ModelAndView login(String name , String pwd ,String type){
		//type :buyer,seller,admin
		pwd = SecurityHelper.Md5(pwd);
		User u = dao.getUniqueByParams(Admin.class, new String[]{"username" , "password"}, new Object[]{name , pwd});
		if(u==null){
			u = dao.getUniqueByParams(Seller.class, new String[]{"loginCode" , "pwd"}, new Object[]{name,pwd});
		}
		if(u==null){
			u  =  dao.getUniqueByParams(Buyer.class, new String[]{"loginCode" , "buyerPwd"}, new Object[]{name ,pwd});
		}
		
//		if(KeyConstants.User_Type_Buyer.equals(type)){
//			u = dao.getUniqueByParams(Buyer.class, new String[]{"loginCode" , "buyerPwd"}, new Object[]{name ,pwd});
//		}else if(KeyConstants.User_Type_Seller.equals(type)){
//			u = dao.getUniqueByParams(Seller.class, new String[]{"loginCode" , "pwd"}, new Object[]{name,pwd});
//		}else if(KeyConstants.User_Type_Admin.equals(type)){
//			u = dao.getUniqueByParams(Admin.class, new String[]{"username" , "password"}, new Object[]{name , pwd});
//		}
		
		if(u==null){
			throw new GException(PlatformExceptionType.BusinessException, "用户或密码错误");
		}
		if(StringUtils.isEmpty(u.getAvatar())){
			u.setAvatar(String.valueOf(new Random().nextInt(KeyConstants.Max_Avatar_Index)));
			dao.saveOrUpdate(u);
		}
		ThreadSession.getHttpSession().setAttribute(KeyConstants.Session_User, u);
		ModelAndView mv = new ModelAndView();
//		mv.data.put("me", JSONHelper.toJSON(u));
		return mv;
	}
	
	@WebMethod
	public ModelAndView logout(){
		ModelAndView mv = new ModelAndView();
		ThreadSession.getHttpSession().removeAttribute(KeyConstants.Session_User);
		return mv;
	}
	
	@WebMethod
	public ModelAndView getUserTree(){
		ModelAndView mv = new ModelAndView();
		User me = ThreadSessionHelper.getUser();
		JSONArray result = contactHandler.getUserTree(me.getId() , me.getType());
		mv.data.put("result", JSONHelper.toJSONArray(result));
		return mv;
	}
	
	
	@WebMethod
	public ModelAndView getChildren(String id , String type){
		ModelAndView mv = new ModelAndView();
		mv.returnText = contactHandler.getChildren(id , type).toString();
		return mv;
	}
	
	
	private List<Map> getBuddyList(Integer parent){
		List<Map> users = new ArrayList<Map>();
		Map<String,Object> ass = new HashMap<String,Object>();
		ass.put("avatar", AssistantAvatar);
		ass.put("uname", AssistantName);
		ass.put("dname", "系统服务中心");
		ass.put("uid", AssistantUid);
		ass.put("online", true);
		users.add(ass);
		return users;
	}
	
	public List<Map> getGroupList(){
		List<Map> depts = new ArrayList<Map>();
		Map dept = new HashMap();
		dept.put("totalUsers", 12);
		dept.put("type", "部门");
		dept.put("did", "12");
		dept.put("dname", "测试部门");
		dept.put("users", getBuddyList(null));
		depts.add(dept);
		return depts;
	}
}
