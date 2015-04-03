package com.youwei.bosh;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.youwei.coco.KeyConstants;

public class BoshConnection {

	public String uid;
	
	//链接持续的时间,单位秒,设置一个较长的时间，避免频繁换链接
	public static final int Poll_Interval_In_Seconds = 1800;
	public HttpServletRequest req;
	public HttpServletResponse resp;
	
	public String resource;
	public BoshConnection(String res ,String uid){
		this.resource = res;
		this.uid = uid;
	}
	
	public boolean flush = false;
	public boolean finish=false;
	
	private long lifeStart=0;
	
	private String returnText = "";
	
	public Boolean returned=false;
	
	public void flush(){
		flush = true;
//		respond();
	}
	private void respond(){
		synchronized (returned) {
			if(returned==false){
				try {
					//先remove,避免connection已经response,但其他的线程仍然有几率操作，造成消息丢失
					BoshConnectionManager.remove(uid+KeyConstants.Connection_Resource_Separator+resource);
					resp.setContentType("text/html");
					resp.setCharacterEncoding("utf-8");
					resp.getOutputStream().write(returnText.getBytes("utf-8"));
					resp.getOutputStream().flush();
					
//					System.out.println("send:"+returnText);
					returned = true;
				} catch (IOException e) {
					RetryMessagePool.pushMsg(uid, JSONObject.fromObject(returnText));
					e.printStackTrace();
				}
			}else{
				System.out.println("重复回复..."+returnText);
			}
		}
	}
	
	public void hold() {
		lifeStart = System.currentTimeMillis();
		while(System.currentTimeMillis()-lifeStart<=Poll_Interval_In_Seconds*1000){
			if(flush){
				break;
			}
			if(finish){
				return;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//现在自然超时，next round
		respond();
	}
	
	public void finish(){
		finish = true;
		returnText = "finished";
		respond();
	}
	
	public void setReturnText(String text){
		this.returnText = text;
	}
}
