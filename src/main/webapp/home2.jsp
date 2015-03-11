<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html >
<head>
<meta charset="utf-8">
<meta http-equiv="pragram" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>中介宝</title>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<!-- <link href="/style/css.css" rel="stylesheet"> -->
<link href="/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="/style/style.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="/chat/style/cocoWindow.css" />
<link rel="stylesheet" type="text/css" href="/chat/style/cocoWinLayer.css" />
<link rel="stylesheet" type="text/css" href="/chat/style/cssOa.css" />
<link rel="stylesheet" type="text/css" href="/chat/style/im.css" />
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/buildHtml.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script type="text/javascript">
resource='native';
try{
var gui = require('nw.gui');
var win = gui.Window.get();
var shell = gui.Shell;
var winMaxHeight,winMaxWidth;
}catch (e){}
function WinClose(){
	win.close(); 
}
function WinMin(){
	win.minimize();
}
function WinMax(){
	win.setMaximumSize(screen.availWidth + 15, screen.availHeight + 15);
	win.maximize();
	winMaxHeight=win.height;
	winMaxWidth=win.width;
	WinMaxOrRev(0);
}
function WinRevert(){
	win.restore();
	if(win.width<692){
	win.resizeTo(692,win.height);
	}
	WinMaxOrRev(1);
}
function WinMaxRev(){
	if(hex.formState==0){
		WinMax();
	}else if(hex.formState==2){
		WinRevert();
	}
}




var web_socket_on=false;

$(function(){
	getMyProfile();
});
	
function getMyProfile(){
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: '/c/myProfile',
	    success:function(data){
	    	var user = data.me;
	    	if(user.id){
	    		my_uid=user.id;
		    	my_avatar=user.avatar;
		    	my_name = user.name;
		    	ws_url = 'ws://${domainName}:9099?uid='+user.id+'&type='+user.type+'&uname='+user.name;
		    	connectWebSocket();
		    	initUserTree('cocoList');
	    	}else{
	    		 //$('.cocoMain').toggleClass('hide');
	    		 //startLogin();
	    		 window.location='chat/login.jsp';
	    	}
	    }
	  });
}

function startLogin(){
	openNewWin('login' , '310','270','登录','chat/login.jsp');
}
</script>

</head>
<body>
 <div>
        <jsp:include page="chat/coco.jsp"></jsp:include>
    </div>
    
<div>
<jsp:include page="chat/userTree2.jsp"></jsp:include>
</div>

</body>
</html>