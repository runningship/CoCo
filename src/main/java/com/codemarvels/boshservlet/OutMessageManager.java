package com.codemarvels.boshservlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class OutMessageManager extends Thread{

	public static Map<Integer , Connection> conns = new HashMap<Integer , Connection>();
	
	private List<JSONObject> msgs = new ArrayList<JSONObject>();

	private static final int timeoutSeconds = 30; 
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(JSONObject msg : msgs){
				int uid = msg.getInt("uid");
				if(conns.containsKey(uid)){
					Connection conn = conns.get(uid);
					conn.returnText = msg.toString();
					conn.close();
				}else{
					if(!msg.containsKey("lastFailTime")){
						msg.put("lastFailTime", System.currentTimeMillis());
					}
					if(System.currentTimeMillis() - msg.getLong("lastFailTime")>timeoutSeconds*1000){
						//timeout,client offline
					}
				}
			}
		}
	}
	
	
}
