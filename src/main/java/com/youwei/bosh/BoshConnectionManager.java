package com.youwei.bosh;

import java.util.List;
import java.util.Map;

import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.coco.IMChatHandler;
import com.youwei.coco.KeyConstants;
import com.youwei.coco.YjhChatHandler;

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
		while(true){
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(String key: connectionStatus.keySet()){
				Long lastActive = connectionStatus.get(key);
				if(System.currentTimeMillis()-lastActive>=60*1000){
					//最近联系人
					String offlineUid = key.split("-")[0];
					List<Map> chats = chatHandler.getRecentChats("", offlineUid);
					for(Map chat : chats){
						String cid = (String)chat.get("uid");
						notifyUserOffline(cid,offlineUid);
					}
					connectionStatus.remove(key);
				}
			}
		}
	}

	private void notifyUserOffline(String cid, String offlineUid) {
		JSONObject jobj = new JSONObject();
		jobj.put("type", "user_status");
		jobj.put("status", KeyConstants.User_Status_Offline);
		jobj.put("senderId", offlineUid);
		for(String key : BoshConnectionManager.conns.keySet()){
    		if(key.startsWith(cid)){
    			BoshConnection target = BoshConnectionManager.conns.get(key);
    			target.returnText = jobj.toString();
    			target.flush();
    		}
    	}
	}
	
	
}
