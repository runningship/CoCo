package com.codemarvels.boshservlet;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class Connection{

	public HttpServletResponse resp;
	
	public String  uid;
	
	private boolean flush = false;
	private boolean close=false;
	
	private long lifeStart=0;
	
	public String returnText = "next_round";
	
	public Connection(String uid){
		this.uid = uid;
	}
	
	public void flush(){
		flush = true;
	}
	private void respond(){
		if(resp!=null){
			try {
				resp.setContentType("text/html");
				resp.setCharacterEncoding("utf-8");
				resp.getOutputStream().write(returnText.getBytes("utf-8"));
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void start() {
		lifeStart = System.currentTimeMillis();
		while(close==false && System.currentTimeMillis()-lifeStart<=30*1000){
			if(flush){
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(flush){
			respond();
		}
		if(close){
			//timeout
			returnText = "new_connection_received";
		}
		
	}
	
	public void close(){
		close = true;
		OutMessageManager.conns.remove(uid);
	}
}
