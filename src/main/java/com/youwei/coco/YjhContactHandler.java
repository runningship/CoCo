package com.youwei.coco;

import java.util.ArrayList;
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
import org.bc.web.PlatformExceptionType;

import com.youwei.coco.im.entity.Group;
import com.youwei.coco.im.entity.UserGroup;
import com.youwei.coco.user.entity.BigArea;
import com.youwei.coco.user.entity.User;
import com.youwei.coco.util.DataHelper;

public class YjhContactHandler implements IMContactHandler{

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Override
	public JSONArray getUserTree(String uid , String userType) {
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
			getUsers(result , area.id ,uid , userType);
		}
		return result;
	}

	private void getUsers(JSONArray result, String pid , String uid , String userType) {
		List<Map> users = new ArrayList<Map>();
		if(KeyConstants.User_Type_Buyer.equals(userType)){
			users = dao.listAsMap("select seller.sellerId as uid,seller.companyName as name ,seller.avatar as avatar , seller.signature as sign from BigAreaCity city ,Seller seller where city.areaCode=seller.cityId and city.bigareaId=?",pid );
			List<Map> admins = dao.listAsMap("select id as uid,name as name ,avatar as avatar ,signature as sign,1 as isAdmin from Admin   where area=?",pid );
			users.addAll(admins);
		}else if(KeyConstants.User_Type_Seller.equals(userType)){
			//卖家只能看到管理员
			users = dao.listAsMap("select id as uid,name as name ,avatar as avatar ,signature as sign ,1 as isAdmin from Admin  where area=?",pid );
		}else if(KeyConstants.User_Type_Admin.equals(userType)){
			users = dao.listAsMap("select id as uid,name as name ,avatar as avatar , signature as sign,1 as isAdmin from Admin  where area=?",pid );
			//还有大区下的卖家
			List<Map> sellers = dao.listAsMap("select seller.sellerId as uid,seller.companyName as name ,seller.avatar as avatar , seller.signature as sign from BigAreaCity city ,Seller seller where city.areaCode=seller.cityId and city.bigareaId=?",pid );
			users.addAll(sellers);
		}
		Random r = new Random();
		for(Map u : users){
			JSONObject json = new JSONObject();
			if(uid.equals(u.get("uid").toString())){
				continue;
			}
			String uname = (String)u.get("name");
			if(StringUtils.isEmpty(uname)){
				uname="";
			}
			json.put("id", u.get("uid"));
			json.put("uid", u.get("uid"));
			json.put("pId", pid);
			json.put("name", uname);
			if(u.containsKey("isAdmin")){
				json.put("isAdmin", 1);
			}
			json.put("namePy",DataHelper.toPinyin(uname));
			json.put("namePyShort",DataHelper.toPinyinShort(uname));
			json.put("type", "user");
			json.put("sign", u.get("sign"));
			json.put("status", DataHelper.getUserStatus(u.get("uid").toString()));
//			if(StringUtils.isEmpty((String)u.get("avatar"))){
//				json.put("avatar", KeyConstants.Default_Avatar);
//			}else{
//				json.put("avatar", u.get("avatar"));
//			}
			json.put("avatar", u.get("avatar"));
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
		if(po==null){
			if(StringUtils.isEmpty(group.id)){
				group.id = UUID.randomUUID().toString().replace("-", "");
			}
			dao.saveOrUpdate(group);
			UserGroup ug = new UserGroup();
			ug.groupId = group.id;
			ug.uid = creatorUid;
			ug.isOwner = 1;
			
			User user = DataHelper.getPropUser(creatorUid);
			if(user!=null){
				ug.avatar = user.getAvatar();
				ug.uname = user.getName();
			}
			dao.saveOrUpdate(ug);
		}else{
			throw new GException(PlatformExceptionType.BusinessException , "存在相同名称的群组，请修改后重试");
		}
	}

	@Override
	public void removeGroup(String groupId) {
		Group po = dao.get(Group.class, groupId);
		if(po==null){
			return;
		}
		dao.delete(po);
		dao.execute("delete from UserGroup where groupId=?", groupId);
		//remove recent chat
		dao.execute("delete from RecentContact where contactId=? and userType=?", groupId , "group");
		//remove group message
		dao.execute("delete from GroupMessage where groupId=? ", groupId );
	}

	@Override
	public void addMembersToGroup(String groupId ,List<String> uidList) {
		for(String uid : uidList){
			UserGroup po = dao.getUniqueByParams(UserGroup.class, new String[]{"groupId" , "uid"}, new Object[]{groupId , uid});
			if(po==null){
				po = new UserGroup();
				po.groupId = groupId;
				po.uid = uid;
				po.isOwner = 0;
				User u = DataHelper.getPropUser(uid);
				po.avatar = u.getAvatar();
				po.uname = u.getName();
				dao.saveOrUpdate(po);
			}
		}
	}

	@Override
	public void kickMemberFromGroup(String uid, String groupId) {
		UserGroup po = dao.getUniqueByParams(UserGroup.class, new String[]{"groupId" , "uid"}, new Object[]{groupId , uid});
		if(po!=null){
			dao.delete(po);
		}
	}

	@Override
	public boolean allowToKickGroupMemeger(String kicker, String kicked, String groupId) {
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
	public boolean allowToRemoveGroup(String uid, String groupId) {
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
