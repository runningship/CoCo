package com.codemarvels.boshservlet;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class Connection{

	public HttpServletResponse resp;
	
	public String  uid;
	
	private boolean flush = false;
	private boolean close=false;
	
	private boolean terminate = false;
	private long lifeStart=0;
	
	public String returnText = "";
	
	public Connection(String uid){
		this.uid = uid;
	}
	
	public void flush(){
		flush = true;
		respond();
	}
	private void respond(){
		if(resp!=null){
			try {
				resp.setContentType("text/html");
				resp.setCharacterEncoding("utf-8");
				resp.getOutputStream().write(returnText.getBytes("utf-8"));
				OutMessageManager.conns.remove(uid);
//				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void start() {
		lifeStart = System.currentTimeMillis();
		while(close==false && System.currentTimeMillis()-lifeStart<=30*1000){
			if(flush){
				return;
			}
			if(terminate){
				return;
			}
			if(close){
				return;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		if(flush ){
//			respond();
//			return;
//		}
//		if(terminate){
//			returnText = "terminated";
//			respond();
//			return;
//		}
//		if(close){
//			//timeout
//			returnText = "new_connection_received";
//		}
		//现在自然超时，
		respond();
	}
	
	public void terminate(){
		returnText = "terminated";
		terminate = true;
		respond();
	}
	public void close(){
		close = true;
		respond();
//		OutMessageManager.conns.remove(uid);
	}
}
