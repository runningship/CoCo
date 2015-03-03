package com.youwei.coco;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ZjbContactHandler implements IMContactHandler{

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Override
	public JSONArray getUserTree() {
		JSONArray result = new JSONArray();
		getComps(result);
		return result;
	}

	private void getComps(JSONArray result) {
		List<Map> depts = dao.listAsMap("select id as did , fid as fid ,namea as dname from Department where fid=0");
		for(Map d : depts){
			JSONObject json = new JSONObject();
			json.put("id", "comp"+"_"+d.get("did"));
			json.put("pId", 0);
			json.put("name", d.get("dname"));
			json.put("type", "comp");
			json.put("isParent", true);
			result.add(json);
			getDepts(result,(Integer)d.get("did"));
		}
	}
	private void getDepts(JSONArray result, Integer fid) {
		List<Map> depts = dao.listAsMap("select id as did , fid as fid ,namea as dname from Department where fid=?", fid);
		for(Map d : depts){
			JSONObject json = new JSONObject();
			json.put("id", "comp"+"_"+d.get("did"));
			json.put("pId", "comp_"+d.get("fid"));
			json.put("name", d.get("dname"));
			json.put("type", "comp");
			json.put("isParent", true);
			result.add(json);
			getUsers(result , (Integer)d.get("did"));
		}
	}
	private void getUsers(JSONArray result, Integer did) {
		List<Map> users = dao.listAsMap("select avatar as avatar ,uname as uname ,id as uid from User where did=?",did);
		Random r = new Random();
		for(Map u : users){
			JSONObject json = new JSONObject();
			json.put("id", "user"+"_"+u.get("uid"));
			json.put("uid", u.get("uid"));
			json.put("pId", "comp_"+did);
			json.put("name", u.get("uname"));
			json.put("type", "user");
			json.put("avatar", u.get("avatar"));
			json.put("status", KeyConstants.User_Status_Online);
			json.put("avatar", r.nextInt(150));
			result.add(json);
		}
	}

	@Override
	public JSONArray getChildren(String pid, String parentType) {
		JSONArray result = new JSONArray();
		
		if(StringUtils.isEmpty(pid)){
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
		}else if("comp".equals(parentType)){
			int intId = getIntId(pid , parentType);
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
		return result;
	}
	
	private int getIntId(String id , String type){
		String tmp = id.replace(type+"_", "");
		return Integer.valueOf(tmp);
	}
}
