package com.youwei.bosh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bc.sdak.TransactionalServiceHelper;
import org.java_websocket.WebSocket;

import com.youwei.coco.IMChatHandler;
import com.youwei.coco.YjhChatHandler;
import com.youwei.coco.im.IMServer;

import net.sf.json.JSONObject;

public class MessagePool extends Thread{

	private IMChatHandler chatHandler = TransactionalServiceHelper.getTransactionalService(YjhChatHandler.class);
	private List<JSONObject> msgs = new ArrayList<JSONObject>();

	private static final int timeoutSeconds =70; 
	
	private static MessagePool instance = new MessagePool();
	
	public static MessagePool getInstance(){
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
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(BoshConnection bc : BoshConnectionManager.conns.values()){
				System.out.println("id="+bc.uid+"->"+bc.resource+";returned="+bc.returned+";finished="+bc.finish+";flush="+bc.flush);
			}
			synchronized (msgs) {
				for(int i=msgs.size()-1;i>=0;i--){
					JSONObject msg = msgs.get(i);
					String contactId = msg.getString("contactId");
					boolean send = false;
					for(String key : BoshConnectionManager.conns.keySet()){
			    		if(key.startsWith(contactId)){
			    			BoshConnection target = BoshConnectionManager.conns.get(key);
			    			target.returnText = msg.toString();
			    			target.flush();
			    			send=true;
			    		}
			    	}
					if(send==false){
						//如果connnection不存在，则需要等待一会,60秒如果一直
						if(!msg.containsKey("lastFailTime")){
							msg.put("lastFailTime", System.currentTimeMillis());
						}
						if(System.currentTimeMillis() - msg.getLong("lastFailTime")>timeoutSeconds*1000){
							//timeout,client offline
							System.out.println("离线消息:"+msg.toString());
							msgs.remove(msg);
							if(!IMServer.isUserOnline(contactId)){
								//发送offline status notify
								//最近联系人
								List<Map> chats = chatHandler.getRecentChats("", contactId);
								for(Map chat : chats){
									String cid = (String)chat.get("uid");
									
								}
							}
						}
					}else{
						msgs.remove(msg);
					}
					
				}
			}
			
		}
	}
}
