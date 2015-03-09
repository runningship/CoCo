package com.youwei.bosh;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class BoshConnection {

	public String uid;
	
	public HttpServletResponse resp;
	
	public String resource;
	public BoshConnection(String res ,String uid){
		this.resource = res;
		this.uid = uid;
	}
	
	public boolean flush = false;
	public boolean finish=false;
	
	private long lifeStart=0;
	
	public String returnText = "";
	
	public Boolean returned=false;
	
	public void flush(){
		flush = true;
//		respond();
	}
	private void respond(){
		synchronized (returned) {
			if(returned==false){
				try {
					resp.setContentType("text/html");
					resp.setCharacterEncoding("utf-8");
					resp.getOutputStream().write(returnText.getBytes("utf-8"));
					BoshConnectionManager.remove(uid+"-"+resource);
//					System.out.println("send:"+returnText);
					returned = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("重复回复...");
			}
		}
	}
	
	public void hold() {
		lifeStart = System.currentTimeMillis();
		while(System.currentTimeMillis()-lifeStart<=30*1000){
			if(flush){
				break;
			}
			if(finish){
				break;
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
//		respond();
	}
}
