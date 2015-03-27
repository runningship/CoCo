package com.youwei.bosh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.youwei.coco.KeyConstants;

public class RetryMessagePool extends Thread{

	private Map<String ,List<JSONObject>> userMsgs = new HashMap<String , List<JSONObject>>();

	private static final int timeoutSeconds =70; 
	
	private static RetryMessagePool instance = new RetryMessagePool();
	
	public static void pushMsg(String uid , JSONObject msg){
		if(!instance.userMsgs.containsKey(uid)){
			instance.userMsgs.put(uid, new ArrayList<JSONObject>());
		}
		instance.userMsgs.get(uid).add(msg);
	}
	
	public static void startUp(){
		instance.start();
	}
	public static boolean isUserRetryMsgEmpty(String uid){
		if(!instance.userMsgs.containsKey(uid)){
			return true;
		}
		return instance.userMsgs.get(uid).isEmpty();
	}
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(String uid : instance.userMsgs.keySet()){
				List<JSONObject> list = instance.userMsgs.get(uid);
				if(list.isEmpty()){
					continue;
				}
				//每次只能发一条
				JSONObject msg = list.get(0);
				boolean send=false;
		    	for(String key : BoshConnectionManager.conns.keySet()){
		    		String targetUid = key.split(KeyConstants.Connection_Resource_Separator)[0];
		    		if(targetUid.equals(uid)){
		    			BoshConnection target = BoshConnectionManager.conns.get(key);
		    			target.returnText = msg.toString();
		    			target.flush();
		    			System.out.println("发送重试消息["+msg.getString("msg")+"]成功..");
		    			send=true;
		    		}
		    	}
		    	if(send){
		    		list.remove(msg);
		    	}else{
		    		if(!msg.containsKey("retryTimes")){
		    			msg.put("retryTimes", 0);
		    		}
		    		int retryTimes = msg.getInt("retryTimes");
		    		msg.put("retryTimes", retryTimes++);
		    		if(retryTimes>=10){
		    			list.remove(msg);
		    		}
		    	}
		    	
			}
		}
	}
}
