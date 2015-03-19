package com.youwei.bosh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bc.sdak.TransactionalServiceHelper;
import org.java_websocket.WebSocket;

import com.youwei.coco.IMChatHandler;
import com.youwei.coco.KeyConstants;
import com.youwei.coco.YjhChatHandler;
import com.youwei.coco.im.IMServer;

import net.sf.ehcache.store.chm.ConcurrentHashMap;
import net.sf.json.JSONObject;

public class BoshConnectionManager extends Thread{

	private IMChatHandler chatHandler = TransactionalServiceHelper.getTransactionalService(YjhChatHandler.class);
	
	public static Map<String , BoshConnection> conns = new ConcurrentHashMap<String , BoshConnection>();
	
	public static Map<String , Long> connectionStatus = new ConcurrentHashMap<String , Long>();
	
	public static void put(String key , BoshConnection conn){
		conns.put(key, conn);
		//update last active time
		connectionStatus.put(key, System.currentTimeMillis());
	}
	
	public static void remove(String key){
		conns.remove(key);
	}
	
	public static BoshConnection get(String key){
		return conns.get(key);
	}

	@Override
	public void run() {
		//监控用户状态
		while(true){
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(String key: connectionStatus.keySet()){
				Long lastActive = connectionStatus.get(key);
				if(System.currentTimeMillis()-lastActive>=BoshConnection.Poll_Interval_In_Seconds*1000){
					//bosh端只有最近联系人
					String offlineUid = key.split(KeyConstants.Connection_Resource_Separator)[0];
					List<Map> chats = chatHandler.getRecentChats("", offlineUid);
					for(Map chat : chats){
						String cid = chat.get("uid").toString();
						notifyUserStatusToBoshClient(cid,offlineUid , KeyConstants.User_Status_Offline);
						notifyUserStatusSocketClient(offlineUid , (String)chat.get("uname") , cid , KeyConstants.User_Status_Offline);
					}
					connectionStatus.remove(key);
				}
			}
		}
	}
	
	public static List<BoshConnection> getBoshConnections(String uid){
		List<BoshConnection> result = new ArrayList<BoshConnection>();
		for(String key : BoshConnectionManager.conns.keySet()){
    		if(key.startsWith(uid)){
    			result.add(BoshConnectionManager.conns.get(key));
    		}
    	}
		return result;
	}

	public static void notifyUserStatusToBoshClient(String cid, String offlineUid ,int status) {
		JSONObject jobj = new JSONObject();
		jobj.put("type", "user_status");
		jobj.put("status", status);
		jobj.put("senderId", offlineUid);
		for(String key : BoshConnectionManager.conns.keySet()){
    		if(key.startsWith(cid)){
    			BoshConnection target = BoshConnectionManager.conns.get(key);
    			target.returnText = jobj.toString();
    			target.flush();
    		}
    	}
	}
	
	public static void notifyUserStatusSocketClient(String fromUid ,String fromUname , String targetUid ,int status) {
		JSONObject jobj = new JSONObject();
		jobj.put("type", "user_status");
		jobj.put("status", status);
		jobj.put("contactId", fromUid);
		jobj.put("contactName", fromUname);
		WebSocket conn = IMServer.getUserSocket(targetUid);
		if(conn!=null){
			conn.send(jobj.toString());
		}
	}
}
