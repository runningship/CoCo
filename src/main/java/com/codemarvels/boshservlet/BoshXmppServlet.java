package com.codemarvels.boshservlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bc.sdak.GException;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ServletHelper;

import com.youwei.coco.ThreadSessionHelper;
import com.youwei.coco.user.entity.User;

public class BoshXmppServlet extends HttpServlet{

	private static final long serialVersionUID = -6582356799296606455L;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException
    {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	Integer uid = 0;
    	User u = ThreadSessionHelper.getUser();
    	if(u!=null){
//    		return;
    		uid = u.id;
    	}
    	String type = getStringParam("type" , request);
    	if("ping".equals(type)){
    		
    	}else if("send".equals(type)){
    		
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
}
