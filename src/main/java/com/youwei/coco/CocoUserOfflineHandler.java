package com.youwei.coco;

import java.io.IOException;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.bc.web.UserOfflineHandler;

public class CocoUserOfflineHandler implements UserOfflineHandler{


	@Override
	public void handle(HttpServletRequest req, ServletResponse response) {
		try {
//			response.getWriter().write("<script type='text/javascript'>startLogin()</script>");
			response.getWriter().write("<script type='text/javascript'>window.location='/oa/login.jsp'</script>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
