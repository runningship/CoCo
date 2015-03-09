package com.codemarvels.boshservlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class OutMessageManager extends Thread{

	public static Map<String , Connection> conns = new HashMap<String , Connection>();
	
	private List<JSONObject> msgs = new ArrayList<JSONObject>();

	private static final int timeoutSeconds = 10; 
	
	private static OutMessageManager instance = new OutMessageManager();
	
	public static OutMessageManager getInstance(){
		return instance;
	}
	public static void pushMsg(JSONObject msg){
		synchronized (instance.msgs) {
			getInstance().msgs.add(msg);
		}
	}
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (msgs) {
				for(int i=msgs.size()-1;i>=0;i--){
					JSONObject msg = msgs.get(i);
					String contactId = msg.getString("contactId");
					if(conns.containsKey(contactId)){
						Connection conn = conns.get(contactId);
						conn.returnText = msg.toString();
						msgs.remove(msg);
						conn.flush();
					}else{
						//如果connnection不存在，则需要等待一会,60秒如果一直
						if(!msg.containsKey("lastFailTime")){
							msg.put("lastFailTime", System.currentTimeMillis());
						}
						if(System.currentTimeMillis() - msg.getLong("lastFailTime")>timeoutSeconds*1000){
							//timeout,client offline
							System.out.println("离线消息:"+msg.toString());
							msgs.remove(msg);
							//发送offline status notify
						}
					}
				}
			}
			
		}
	}
	
	
}
