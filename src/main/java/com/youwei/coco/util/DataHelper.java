package com.youwei.coco.util;

import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.utils.LogUtil;

import com.youwei.bosh.BoshConnection;
import com.youwei.bosh.BoshConnectionManager;
import com.youwei.bosh.RetryMessagePool;
import com.youwei.coco.KeyConstants;
import com.youwei.coco.im.IMServer;
import com.youwei.coco.user.entity.Admin;
import com.youwei.coco.user.entity.Buyer;
import com.youwei.coco.user.entity.Seller;
import com.youwei.coco.user.entity.User;

public class DataHelper {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat sdf4 = new SimpleDateFormat("MM-dd HH:mm:ss");
	public static SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
	public static SimpleDateFormat sdf6 = new SimpleDateFormat("yyyyMMdd");
	public static final String User_Default_Password = "123456";
	private static final HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	static{
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	}
	
	public static String toPinyin(String hanzi){
		try {
			String pinyin="";
			for(int i=0;i<hanzi.length();i++){
				String[] arr = PinyinHelper.toHanyuPinyinStringArray(hanzi.charAt(i), format);
				if(arr!=null && arr.length>0){
					pinyin+=arr[0];
				}else{
//					LogUtil.warning("汉字["+hanzi.charAt(i)+"]转拼音失败,");
					continue;
				}
			}
			return pinyin;
		} catch (Exception e) {
//			LogUtil.log(Level.WARN, "汉字["+hanzi+"]转拼音失败", e);
		}
		return "";
	}
	
	public static String toPinyinShort(String hanzi){
		try {
			String pinyin="";
			for(int i=0;i<hanzi.length();i++){
				String[] arr = PinyinHelper.toHanyuPinyinStringArray(hanzi.charAt(i), format);
				if(arr!=null && arr.length>0){
					if(StringUtils.isNotEmpty(arr[0])){
						pinyin+=arr[0].charAt(0);
					}
				}else{
//					LogUtil.warning("汉字["+hanzi.charAt(i)+"]转拼音失败,");
					continue;
				}
			}
			return pinyin;
		} catch (Exception e) {
//			LogUtil.log(Level.WARN, "汉字["+hanzi+"]转拼音失败", e);
		}
		return "";
	}
	
	public static JSONObject toCommonUser(User u){
		JSONObject jobj = new JSONObject();
		if(u==null){
			return jobj;
		}
		jobj.put("id", u.getId());
		jobj.put("name", u.getName());
		if(StringUtils.isEmpty(u.getAvatar())){
			jobj.put("avatar", KeyConstants.Default_Avatar);
		}else{
			jobj.put("avatar", u.getAvatar());
		}
		jobj.put("sign", u.getSign());
		jobj.put("type", u.getType());
		return jobj;
	}
	
	public static User getUser(String type , String id){
		CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
		if(KeyConstants.User_Type_Buyer.equals(type)){
			return dao.get(Buyer.class, id);
		}else if(KeyConstants.User_Type_Seller.equals(type)){
			return dao.get(Seller.class, id);
		}else if(KeyConstants.User_Type_Admin.equals(type)){
			return dao.get(Admin.class, Integer.valueOf(id));
		}
		return null;
	}
	
	public static User getPropUser(String id){
		CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
		User u = dao.get(Buyer.class, id);
		if(u!=null){
			return u;
		}
		u = dao.get(Seller.class, id);
		if(u!=null){
			return u;
		}
		try{
			u = dao.get(Admin.class, Integer.valueOf(id));
			if(u!=null){
				return u;
			}
		}catch(Exception ex){
			
		}
		return null;
	}
	
	public static int getUserStatus(String contactId){
		boolean webSocketOn = IMServer.isUserOnline(contactId);
		boolean boshOn=false;
		for(String key : BoshConnectionManager.conns.keySet()){
			String target = key.split(KeyConstants.Connection_Resource_Separator)[0];
			if(target.equals(contactId)){
				boshOn = true;
				break;
			}
		}
		if(webSocketOn || boshOn){
			return KeyConstants.User_Status_Online;
		}else{
			return KeyConstants.User_Status_Offline;
		}
	}
	
	public static void sendToBosh(String contactId , JSONObject msg){
		if(!RetryMessagePool.isUserRetryMsgEmpty(contactId)){
			RetryMessagePool.pushMsg(contactId, msg);
			System.out.println("消息进入重试队列...");
			return;
		}
		boolean send=false;
    	for(String key : BoshConnectionManager.conns.keySet()){
    		String targetUid = key.split(KeyConstants.Connection_Resource_Separator)[0];
    		if(targetUid.equals(contactId)){
    			BoshConnection target = BoshConnectionManager.conns.get(key);
    			target.returnText = msg.toString();
    			target.flush();
    			send=true;
    		}
    	}
    	if(!send){
//    		System.out.println("没有找到对方的connection ...");
    		RetryMessagePool.pushMsg(contactId, msg);
			System.out.println("消息进入重试队列...");
    	}
	}
	
}
