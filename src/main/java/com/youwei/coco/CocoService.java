package com.youwei.coco;

import java.util.ArrayList;
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

import com.youwei.coco.cache.ConfigCache;
import com.youwei.coco.user.entity.User;

@Module(name="/")
public class CocoService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public static final int AssistantUid=-999;
	public static final int AssistantAvatar=116;
	public static final String AssistantName="小助手";
	
	@WebMethod
	public ModelAndView myProfile(){
		ModelAndView mv = new ModelAndView();
		User u = (User)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_User);
		mv.data.put("me", JSONHelper.toJSON(u));
		return mv;
	}
	@WebMethod
	public ModelAndView home(){
		ModelAndView mv = new ModelAndView();
//		List<Map> users = dao.listAsMap("select u.avatar as avatar, u.uname as uname , d.namea as dname ,u.id as uid from User u, Department d where u.cid=? and u.lock=1 and u.did=d.id order by u.uname", ThreadSessionHelper.getUser().cid);
		
		List<Map> users = getBuddyList(null);
		
		User u = (User)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_User);
		mv.jspData.put("me", u);
		//按在线优先排序
//		mv.jspData.put("contacts",users);
		
		mv.jspData.put("depts",getGroupList());
		
		mv.jspData.put("domainName", ConfigCache.get("domainName" , "www.zhongjiebao.com"));
//		mv.jspData.put("use_im", me.Company().useIm);
		
//		StringBuilder auths = new StringBuilder();
//		if(me.getRole()!=null){
//			for(RoleAuthority ra : me.getRole().Authorities()){
//				auths.append(ra.name).append(",");
//			}
//		}
//		mv.jspData.put("auths", auths);
//		auths.toString().indexOf("cw_on");
		
		return mv;
	}
	
	@WebMethod
	public ModelAndView login(String name , String pwd){
		User u = dao.getUniqueByKeyValue(User.class, "lname", name);
		if(u==null){
			throw new GException(PlatformExceptionType.BusinessException, "用户或密码错误");
		}
		ThreadSession.getHttpSession().setAttribute(KeyConstants.Session_User, u);
		ModelAndView mv = new ModelAndView();
		mv.data.put("me", JSONHelper.toJSON(u));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getChildren(String id , String type){
		ModelAndView mv = new ModelAndView();
		JSONArray result = new JSONArray();
		
		if(StringUtils.isEmpty(id)){
			List<Map> depts = dao.listAsMap("select id as did , fid as fid ,namea as dname from Department where fid=0");
			for(Map d : depts){
				JSONObject json = new JSONObject();
				json.put("id", "comp"+"_"+d.get("did"));
				json.put("pId", 0);
				json.put("name", d.get("dname"));
				json.put("type", "comp");
				json.put("isParent", true);
				result.add(json);
			}
		}else if("comp".equals(type)){
			int intId = getIntId(id , type);
			//get sub group
			List<Map> depts = dao.listAsMap("select id as did , fid as fid ,namea as dname from Department where fid=?", intId);
			for(Map d : depts){
				JSONObject json = new JSONObject();
				json.put("id", "comp"+"_"+d.get("did"));
				json.put("pId", "comp_"+d.get("fid"));
				json.put("name", d.get("dname"));
				json.put("type", "comp");
				json.put("isParent", true);
				result.add(json);
			}
			
			//get direct children
			List<Map> users = dao.listAsMap("select did as did , avatar as avatar ,uname as uname ,id as uid from User where did=?",intId);
			Random r = new Random();
			for(Map u : users){
				JSONObject json = new JSONObject();
				json.put("id", "user"+"_"+u.get("uid"));
				json.put("uid", u.get("uid"));
				json.put("pId", "dept_"+intId);
				json.put("name", u.get("uname"));
				json.put("type", "user");
				json.put("avatar", u.get("avatar"));
				json.put("status", KeyConstants.User_Status_Online);
				json.put("avatar", r.nextInt(150));
				result.add(json);
			}
		}
//		mv.data.put("result", result.toArray());
		mv.returnText = result.toString();
		return mv;
	}
	
	private int getIntId(String id , String type){
		String tmp = id.replace(type+"_", "");
		return Integer.valueOf(tmp);
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
		users =dao.listAsMap("select did as did , avatar as avatar ,uname as uname ,id as uid from User where cid=1");
		
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
