package com.youwei.coco;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.GException;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ThreadSession;

import com.youwei.coco.user.entity.User;

public class ThreadSessionHelper {

	public static User getUser(){
    	HttpSession session = ThreadSession.getHttpSession();
    	if(session==null){
    		return null;
    	}
    	User u =  (User)session.getAttribute("user");
    	if(u==null){
    		throw new GException(PlatformExceptionType.UserOfflineException , "");
    	}
    	return u;
    }
    public static String getIp(){
    	HttpSession session = ThreadSession.getHttpSession();
    	return (String)session.getAttribute("ip");
    }
    
}
