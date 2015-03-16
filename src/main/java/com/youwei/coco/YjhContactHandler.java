package com.youwei.coco;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.PlatformExceptionType;

import com.youwei.coco.im.entity.Group;
import com.youwei.coco.im.entity.UserGroup;
import com.youwei.coco.user.entity.BigArea;
import com.youwei.coco.user.entity.User;
import com.youwei.coco.util.DataHelper;

public class YjhContactHandler implements IMContactHandler{

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Override
	public JSONArray getUserTree() {
		//两级,大区-->用户
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
			users = dao.listAsMap("select seller.sellerId as uid,seller.companyName as name ,seller.avatar as avatar , seller.signature as sign from BigAreaCity city ,Seller seller where city.areaCode=seller.cityId and city.bigareaId=?",pid );
		}else if(KeyConstants.User_Type_Seller.equals(user.getType())){
			//卖家只能看到管理员
			users = dao.listAsMap("select id as uid,name as name ,avatar as avatar ,signature as sign from Admin  where area=?",pid );
		}else if(KeyConstants.User_Type_Admin.equals(user.getType())){
			users = dao.listAsMap("select id as uid,name as name ,avatar as avatar , signature as sign from Admin  where area=?",pid );
			//还有大区下的卖家
			List<Map> sellers = dao.listAsMap("select seller.sellerId as uid,seller.companyName as name ,seller.avatar as avatar , seller.signature as sign from BigAreaCity city ,Seller seller where city.areaCode=seller.cityId and city.bigareaId=?",pid );
			users.addAll(sellers);
		}
		Random r = new Random();
		for(Map u : users){
			JSONObject json = new JSONObject();
			json.put("id", u.get("uid"));
			json.put("uid", u.get("uid"));
			json.put("pId", pid);
			json.put("name", u.get("name"));
			json.put("namePy",DataHelper.toPinyin((String)u.get("name")));
			json.put("namePyShort",DataHelper.toPinyinShort((String)u.get("name")));
			json.put("type", "user");
			json.put("sign", u.get("sign"));
			json.put("status", DataHelper.getUserStatus(u.get("uid").toString()));
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

	@Override
	public void createGroup(String creatorUid , Group group) {
		Group po = dao.getUniqueByKeyValue(Group.class, "name", group.name);
		if(po!=null){
			dao.saveOrUpdate(group);
		}else{
			throw new GException(PlatformExceptionType.BusinessException , "存在相同名称的群组，请修改后重试");
		}
		UserGroup ug = new UserGroup();
		ug.groupId = po.id;
		ug.uid = creatorUid;
		ug.isOwner = 1;
		dao.saveOrUpdate(ug);
	}

	@Override
	public void removeGroup(Integer groupId) {
		Group po = dao.get(Group.class, groupId);
		if(po==null){
			return;
		}
		dao.delete(po);
		dao.execute("delete from UserGroup where groupId=?", groupId);
	}

	@Override
	public void addMembersToGroup(Integer groupId ,List<String> uidList) {
		for(String uid : uidList){
			UserGroup po = dao.getUniqueByParams(UserGroup.class, new String[]{"groupId" , "uid"}, new Object[]{groupId , uid});
			if(po==null){
				po = new UserGroup();
				po.groupId = groupId;
				po.uid = uid;
				po.isOwner = 0;
			}
		}
	}

	@Override
	public void kickMemberFromGroup(String uid, Integer groupId) {
		UserGroup po = dao.getUniqueByParams(UserGroup.class, new String[]{"groupId" , "uid"}, new Object[]{groupId , uid});
		if(po!=null){
			dao.delete(po);
		}
	}

	@Override
	public boolean allowToKickGroupMemeger(String kicker, String kicked, Integer groupId) {
		if(kicker.equals(kicked)){
			//自己
			return true;
		}
		UserGroup po = dao.getUniqueByParams(UserGroup.class, new String[]{"groupId" , "uid"}, new Object[]{groupId , kicker});
		if(po==null){
			return false;
		}
		if(po.isOwner==1){
			//创建者
			return true;
		}
		return false;
	}

	@Override
	public boolean allowToRemoveGroup(String uid, Integer groupId) {
		UserGroup po = dao.getUniqueByParams(UserGroup.class, new String[]{"groupId" , "uid"}, new Object[]{groupId , uid});
		if(po==null){
			return false;
		}
		if(po.isOwner==1){
			//创建者
			return true;
		}
		return false;
	}

}
