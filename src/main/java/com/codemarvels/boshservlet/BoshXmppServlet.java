package com.codemarvels.boshservlet;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.bc.sdak.GException;
import org.bc.sdak.SimpDaoTool;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ServletHelper;
import org.bc.web.ThreadSession;

import com.youwei.coco.KeyConstants;
import com.youwei.coco.ThreadSessionHelper;
import com.youwei.coco.im.entity.Message;
import com.youwei.coco.user.entity.User;
import com.youwei.coco.util.DataHelper;

public class BoshXmppServlet extends HttpServlet{

	private static final long serialVersionUID = -6582356799296606455L;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException
    {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		String message = getStringParam("json" , request);
		JSONObject data = JSONObject.fromObject(message);
    	String type = data.getString("type");
    	String uid = data.getString("myUid");
//    	if("ping".equals(type)){
//    		
//    	}else
		if("msg".equals(type)){
			data.put("senderId", uid);
			sendMsg(data);
    		return;
    	}else if("status".equals(type)){
    		return;
    	}
    	
    	Connection conn = new Connection(uid);
    	conn.resp = response;
    	Connection oldConn = OutMessageManager.conns.get(uid);
    	if(oldConn!=null){
    		//replace old one
//    		oldConn.respond("New_Connection_Received");
    		oldConn.close();
    	}
    	OutMessageManager.conns.put(uid, conn);
    	conn.start();
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
    	String contactId = data.getString("contactId");
    	data.put("sendtime", DataHelper.sdf4.format(new Date()));
    	Connection conn = OutMessageManager.conns.get(contactId);
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
    	if(conn!=null){
    		conn.returnText = data.toString();
    		conn.flush();
    	}
    }
}
