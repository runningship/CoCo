package com.codemarvels.boshservlet;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class Connection{

	public HttpServletResponse resp;
	
	public Integer  uid;
	
	private boolean close=false;
	
	private long lifeStart=0;
	
	public String returnText = "next_round";
	
	public Connection(Integer uid){
		this.uid = uid;
	}
	
	private void respond(){
		if(resp!=null){
			try {
				resp.getOutputStream().write(returnText.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void start() {
		lifeStart = System.currentTimeMillis();
		while(close==false && System.currentTimeMillis()-lifeStart<=30*1000){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(close){
			//timeout
			returnText = "new_connection_received";
		}
		respond();
	}
	
	public void close(){
		close = true;
		OutMessageManager.conns.remove(uid);
	}
}
