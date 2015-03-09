package com.youwei.coco;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ThreadSession;

import com.youwei.coco.user.entity.BigArea;
import com.youwei.coco.user.entity.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class YjhContactHandler implements IMContactHandler{

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Override
	public JSONArray getUserTree() {
		//获取大区
		List<BigArea> areas = dao.listByParams(BigArea.class, "from BigArea where 1=1");
		JSONArray result = new JSONArray();
		for(BigArea area : areas){
			JSONObject json = new JSONObject();
			json.put("id", area.id);
			json.put("pId", 0);
			json.put("name", area.name);
			json.put("type", "comp");
			json.put("isParent", true);
			result.add(json);
			getUsers(result , area.id);
		}
		return result;
	}

	private void getUsers(JSONArray result, String pid) {
		User user = ThreadSessionHelper.getUser();
		if(user==null){
			return;
		}
		List<Map> users = new ArrayList<Map>();
		if(KeyConstants.User_Type_Buyer.equals(user.getType())){
			users = dao.listAsMap("select seller.sellerId as uid,seller.companyName as name ,seller.avatar as avatar from BigAreaCity city ,Seller seller where city.areaCode=seller.cityId and city.bigareaId=?",pid );
		}else if(KeyConstants.User_Type_Seller.equals(user.getType())){
			users = dao.listAsMap("select id as uid,name as name ,avatar as avatar from Admin  where area=?",pid );
		}else if(KeyConstants.User_Type_Admin.equals(user.getType())){
			users = dao.listAsMap("select id as uid,name as name ,avatar as avatar from Admin  where area=?",pid );
		}
		Random r = new Random();
		for(Map u : users){
			JSONObject json = new JSONObject();
			json.put("id", u.get("uid"));
			json.put("uid", u.get("uid"));
			json.put("pId", pid);
			json.put("name", u.get("name"));
			json.put("type", "user");
			json.put("status", KeyConstants.User_Status_Online);
			if(StringUtils.isEmpty((String)u.get("avatar"))){
				json.put("avatar", KeyConstants.Default_Avatar);
			}else{
				json.put("avatar", u.get("avatar"));
			}
			result.add(json);
		}
	}

	@Override
	public JSONArray getChildren(String pid, String parentType) {
		return null;
	}

}
