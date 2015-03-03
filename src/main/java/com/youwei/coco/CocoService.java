package com.youwei.coco;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.youwei.coco.user.entity.Buyer;
import com.youwei.coco.user.entity.RecentContact;
import com.youwei.coco.user.entity.Seller;
import com.youwei.coco.user.entity.Token;
import com.youwei.coco.user.entity.User;
import com.youwei.coco.util.DataHelper;

@Module(name="/")
public class CocoService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public static final int AssistantUid=-999;
	public static final int AssistantAvatar=116;
	public static final String AssistantName="小助手";
	
	private IMContactHandler contactHandler = TransactionalServiceHelper.getTransactionalService(YjhContactHandler.class);
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
		mv.data.put("seller", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod
	public ModelAndView auth(String token){
		ModelAndView mv = new ModelAndView();
		Token po = dao.getUniqueByKeyValue(Token.class, "data", token);
		if(po!=null){
			User u = DataHelper.getUser(po.userType , po.uid);
			if(u==null){
				mv.data.put("result", "-1");
			}else{
				ThreadSession.getHttpSession().setAttribute(KeyConstants.Session_User, u);
				mv.data.put("me", DataHelper.toCommonUser(u));
				List<Map> sellers = dao.listAsMap("select seller.companyName as companyName , seller.sellerId as sellerId "
						+ "from RecentContact rc ,Seller seller where rc.contactId=seller.sellerId");
				mv.data.put("sellers", JSONHelper.toJSONArray(sellers));
			}
		}else{
			mv.data.put("result", "-1");
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView home(){
		ModelAndView mv = new ModelAndView();
//		User u = (User)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_User);
//		if(u!=null){
//			mv.jspData.put("me",DataHelper.toCommonUser(u));
//		}
		User u = (User)ThreadSessionHelper.getUser();
		mv.jspData.put("depts",getGroupList());
		mv.jspData.put("domainName", ConfigCache.get("domainName" , "www.zhongjiebao.com"));
		return mv;
	}
	@WebMethod
	public ModelAndView webchat(String token , String sellerId){
		ModelAndView mv = new ModelAndView();
//		mv.jspData.put("token", token);
//		Seller seller = dao.get(Seller.class, sellerId);
//		mv.jspData.put("token", token);
//		mv.jspData.put("seller", seller);
		mv.jspData.put("domainName", ConfigCache.get("domainName" , "www.zhongjiebao.com"));
		return mv;
	}
	
	@WebMethod
	public ModelAndView addRecentContact(String contactId){
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
		}
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView login(String name , String pwd ,String type){
		//type :buyer,seller,admin
		User u  = null;
		if(KeyConstants.User_Type_Buyer.equals(type)){
			u = dao.getUniqueByKeyValue(Buyer.class, "loginCode", name);
		}else if(KeyConstants.User_Type_Seller.equals(type)){
			u = dao.getUniqueByKeyValue(Seller.class, "loginCode", name);
		}
		
		if(u==null){
			throw new GException(PlatformExceptionType.BusinessException, "用户或密码错误");
		}
		ThreadSession.getHttpSession().setAttribute(KeyConstants.Session_User, u);
		ModelAndView mv = new ModelAndView();
//		mv.data.put("me", JSONHelper.toJSON(u));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getUserTree(){
		ModelAndView mv = new ModelAndView();
		JSONArray result = contactHandler.getUserTree();
		mv.data.put("result", result.toArray());
		return mv;
	}
	
	
	@WebMethod
	public ModelAndView getChildren(String id , String type){
		ModelAndView mv = new ModelAndView();
		mv.returnText = contactHandler.getChildren(id , type).toString();
		return mv;
	}
	
	
	private Map getMyProfile() {
		
		Map me = new HashMap();
		me.put("id", 1);
		me.put("avatar", 33);
		me.put("uname", "叶新舟");
		me.put("domain", "hefei");
		return me;
	}

	public List<Map> getBuddyList(Integer parent){
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
