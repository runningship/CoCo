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

<link rel="stylesheet" type="text/css" href="/oa/style/cocoWindow.css" />
<link rel="stylesheet" type="text/css" href="/oa/style/cocoWinLayer.css" />
<link rel="stylesheet" type="text/css" href="/oa/style/cssOa.css" />
<link rel="stylesheet" type="text/css" href="/oa/style/im.css" />
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/buildHtml.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script type="text/javascript">
var web_socket_on=false;

$(function(){
	getMyProfile();
	
	});
	
function loginSuccess(data){
	// LayerRemoveBox('login');
	// $('.cocoMain').toggleClass('hide');
	// connectWebSocket();
	// initUserTree('cocoList');
	//刷新
	window.location.reload();
}

function getMyProfile(){
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: '/c/myProfile',
	    success:function(data){
	    	data = data.me;
	    	if(data.id){
	    		my_uid=data.id;
		    	my_avatar=data.avatar;
		    	my_name = data.name;
		    	ws_url = 'ws://${domainName}:9099?uid='+data.id;
		    	connectWebSocket();
		    	initUserTree('cocoList');
	    	}else{
	    		 $('.cocoMain').toggleClass('hide');
	    		 startLogin();
	    	}
	    }
	  });
}
function startLogin(){
	openNewWin('login' , '500','300','登录','oa/login.jsp');
}
</script>

</head>
<body>
 <div>
        <jsp:include page="oa/coco.jsp"></jsp:include>
    </div>
    
<div>
<jsp:include page="oa/userTree2.jsp"></jsp:include>
</div>

</body>
</html>