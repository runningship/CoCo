package com.youwei.bosh;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.GException;
import org.bc.sdak.SimpDaoTool;
import org.bc.web.PlatformExceptionType;
import org.java_websocket.WebSocket;

import com.youwei.coco.im.IMServer;
import com.youwei.coco.im.entity.Message;
import com.youwei.coco.util.DataHelper;

public class BoshServlet extends HttpServlet{

	private static final long serialVersionUID = -6582356799296606455L;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException
    {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		String message = getStringParam("json" , request);
//		System.out.println("收到:"+message);
		JSONObject data = JSONObject.fromObject(message);
    	String type = data.getString("type");
    	String uid = data.getString("myUid");
    	String res = data.getString("resource");
    	if(StringUtils.isEmpty(res)){
    		res = UUID.randomUUID().toString();
    	}
    	BoshConnection oldConn = BoshConnectionManager.get(uid+"-"+res);
    	if(oldConn!=null){
    		//客户端主动发来新请求,要结束掉老的请求
    		oldConn.finish();
    	}else{
    		//有可能页面刷新
    	}
    	if("msg".equals(type)){
    		//有消息发消息
			data.put("senderId", uid);
			sendMsg(data);
    	}
    	BoshConnection newConn = new BoshConnection(res,uid);
    	newConn.resp = response;
    	try {
    		//等待oldConn.finish();完成
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	BoshConnectionManager.put(uid+"-"+res, newConn);
    	newConn.hold();
    }

    @Override
    public void init() throws ServletException
    {
    }
    
    private String getStringParam(String param ,HttpServletRequest request){
    	String[] arr = request.getParameterValues(param);
    	if(arr==null || arr.length==0){
    		return "";
    	}
    	if(arr.length==1){
    		return arr[0];
    	}
    	throw new GException(PlatformExceptionType.ParameterTypeError,"want a string , but a string array.");
    }
    
    private void sendMsg( JSONObject data){
    	Message dbMsg = new Message();
		dbMsg.sendtime = new Date();
		dbMsg.conts = data.getString("msg");
		dbMsg.senderId = data.getString("senderId");
		dbMsg.receiverId = data.getString("contactId");
		dbMsg.hasRead=0;
		try{
			SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(dbMsg);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
    	String contactId = data.getString("contactId");
    	data.put("sendtime", DataHelper.sdf4.format(new Date()));
    	if(IMServer.isUserOnline(contactId)){
    		WebSocket userSocket = IMServer.getUserSocket(contactId);
    		userSocket.send(data.toString());
    	}
//    	MessagePool.pushMsg(data);
    	boolean send=false;
    	for(String key : BoshConnectionManager.conns.keySet()){
    		if(key.startsWith(contactId)){
    			BoshConnection target = BoshConnectionManager.conns.get(key);
    			target.returnText = data.toString();
    			target.flush();
    			send=true;
    		}
    	}
    	if(!send){
    		System.out.println("没有找到对方的connection ...");
    	}
//		OutMessageManager.pushMsg(data);
    }
}
