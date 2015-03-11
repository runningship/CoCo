package com.youwei.coco;

import java.io.IOException;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.bc.web.UserOfflineHandler;

public class CocoUserOfflineHandler implements UserOfflineHandler{


	@Override
	public void handle(HttpServletRequest req, ServletResponse response) {
		try {
			response.getWriter().write("<script type='text/javascript'>window.location='/coco/chat/open.html'</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
