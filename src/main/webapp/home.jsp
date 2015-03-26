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
<title>有机会 - 叮铛</title>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<!-- <link href="/style/css.css" rel="stylesheet"> -->
<!-- <link href="bootstrap/css/bootstrap.css" rel="stylesheet"> -->
<link href="style/style.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="chat/style/cocoWindow.css" />
<link rel="stylesheet" type="text/css" href="chat/style/cocoWinLayer.css" />
<link rel="stylesheet" type="text/css" href="chat/style/cssOa.css" />

<script src="js/jquery.js" type="text/javascript"></script>
<!-- <script src="bootstrap/js/bootstrap.js" type="text/javascript"></script> -->
<script type="text/javascript">
try{
var gui = require('nw.gui');
var win = gui.Window.get();
var shell = gui.Shell;
var winMaxHeight,winMaxWidth;
}catch (e){}

try{
	if(win.width<850){
	win.resizeTo(850,win.height);
	}
	if(win.height<850){
	win.resizeTo(win.width,590);
	}
	var xs=screen.width/2-win.width/2,
	ys=screen.height/2-win.height/2;
	win.moveTo(xs,ys);
}catch (e){}

function quit(){
	win.close();
}
function logout(){
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: 'c/logout',
	    success:function(data){
	    	
	    }
	  });
	window.location = 'open.jsp';
}
function WinClose(){
	//show choose dialog
	  art.dialog({
	    title: false,
	    width: 250,
	    content: $('#quit_confirm').html(),
	    padding:1
	  });
	//win.close(); 
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
	if(win.width<850){
	win.resizeTo(852,win.height);
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


$(document).on('click', '.btn', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    if(ThiType=='winclose'){
        WinClose();
    }else if(ThiType=='winmax'){
        WinMaxRev();
    }else if(ThiType=='winmin'){
        WinMin();
    }
    event.preventDefault();
});



var web_socket_on=false;

$(function(){
	getMyProfile();
});
	
function getMyProfile(){
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: 'c/myProfile',
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

function cocoWin_resize(){
	if($('.cocoWin').length>0){
		var BH=$('body').height(),
		coTH=$('.cocoWintit').height(),
		coM=$('.cocoWinContent'),
		coMi=$('.cocoWinInfoListShow'),
		coMs=$('.WinInfoSend'),
		coMqb=$('.qunBox');
		coM.height(BH-coTH-1);
		coMi.height(coM.height()-coMs.height())
		coMqb.height(BH-coTH);
		//修改qunbox高度
	}
}
$(document).ready(function() {
	//var win = gui.Window.get();
	try{
		win.window.onblur=function(){
	        win.isFocus=false;
	    };
	    win.window.onfocus=function(){
	        win.title="有机会 - 叮铛";
	        win.isFocus=true;
	    };
	}catch(e){
		
	}
	cocoWin_resize();
	
});
$(window).resize(function(event) {
	cocoWin_resize();
	/* Act on the event */
});
</script>
<style>
body{ overflow: hidden;}

.titlebar{-webkit-app-region:drag;}
.nobar{-webkit-app-region:no-drag;z-index: 9999}
/* Win窗口边框，需配合js使用，too.js里 */
.winBoxBorders{ position:absolute; border-width:0;border-style:solid;border-color:#555;top:0; right:0; bottom:0; left: 0; z-index: 9999999;}
.winBoxBorders.winBoxBorderT{ border-top-width:1px; bottom: auto;}
.winBoxBorders.winBoxBorderR{ border-right-width:1px; left: auto;}
.winBoxBorders.winBoxBorderB{ border-bottom-width:1px; top: auto;}
.winBoxBorders.winBoxBorderL{ border-left-width:1px; right: auto;}/**/

.body{}
.body .winTools{ position: absolute; top: 1px; right: 1px; z-index: 2147483000;}
.body .winTools a{}
.body {-webkit-app-region: drag;}
.body .winTools{-webkit-app-region: no-drag; float: right;}
.body .winTools a{ display: inline-block; height: 30px; line-height: 30px; width: 30px; text-align: center; color: #000; font-family: 'microsoft yahei'; text-decoration: none;opacity: 0.5;}
.body .winTools a.btn_close:hover{opacity: 1;}
.body .winTools a.close_action{background-image: url('chat/images/close.png');background-size: 100%;  width: 20px;  background-repeat: no-repeat;  height: 16px;}
.body .winTools a.min_action{background-image: url('chat/images/min.png');background-size: 100%;  width: 20px;  background-repeat: no-repeat;  height: 16px;}





 body .edui-default .edui-editor{ border: 0;-webkit-border-radius: 0px; 
-moz-border-radius: 0px;
 border-radius: 0px;}

.bodys{ height: 490px; overflow: hidden;}
.rightBox{ display: block; float: right; position: relative; top: 0;width: 200px; border-left: 1px solid #EEE; background: #FFF;height: 100%;}
/* .qunBox{ display: block; float: right; position: relative; top: 0;width: 200px; border-left: 1px solid #EEE; background: #FFF;box-shadow:none; display:none;} */

.qunList{ border-bottom: 1px solid #EEE;}
.qunList dt{ text-align: center; background: url('chat/cocoImages/titBg.png') repeat-x; height: 30px; line-height: 30px; font-size: 12px;}
.qunList dd.conts{ padding: 5px;}

.oaTitBgCoco {background-color: #08B3B6;}
.cocoMainCon{background-color: #f7f7f7;}
.cocoMainConBox{background: none;width: 100%; }
.cocoWin{ position: relative; left: 0;padding-left: 216px;}
.cocoWinContent{ height: 100%;}
.cocoWinContentLxr{ height: 100%;}
.WinInfoSend { float: none;position: relative;height: 113px;overflow: hidden;}
.WinInfoListAppend {}
.WinInfoListAppend .newsAppend { margin-left: 0; margin-right: 0; display: block; width: 100%; }
.WinInfoListAppend .newsAppendBox{ margin-left: 70px;}
.WinInfoListAppend .newsAppendBox.Fright{ margin-left: 0; margin-right: 70px;}
.WinInfoSendWrite{width: auto;float: none;margin-right: 0px;height: auto; margin-left: 0px; /**/}
.WinInfoSendBtn{position: absolute;right: 0;bottom: 3pt;height: 30px; z-index: 22222;}
.WinInfoSendBtnMessage{height: 100%;}
.WinInfoListShowMainBox{ float: none; margin: 0; height: 100%;}

body .edui-default .edui-editor-toolbarbox {
position: relative;
zoom: 1;
-webkit-box-shadow: 0 0px 0px rgba(204, 204, 204, 0.6);
-moz-box-shadow: 0 0px 0px rgba(204, 204, 204, 0.6);
box-shadow: 0 0px 0px rgba(204, 204, 204, 0.6);
border-top-left-radius: 0px;
border-top-right-radius: 0px; */
}
body .edui-default .edui-editor-toolbarboxouter {
border-bottom: 0px solid #d4d4d4;
background: #FFF;
background-repeat: repeat-x;
border: 0px solid #d4d4d4;
border-radius: 0px 0px 0 0;
filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffffff', endColorstr='#ffffffff', GradientType=0);
-moz-box-shadow: 0 0px 0px rgba(0, 0, 0, 0.065);
box-shadow: 0 0px 0px rgba(0, 0, 0, 0.065);
}
body #edui1_iframeholder{ height: 50px;}
.quit_confirm span {color:white; margin-left:50px;}
.quit_confirm p{color:#888;font-size:14px;margin-left:50px;}
.quit_confirm {background-color:rgb(47,53,53);display: inline-block;width: 100%;padding:10px;cursor:pointer}
.quit_confirm .quit{margin-right:10px; background-image: url('chat/images/quit.png');background-repeat: no-repeat; width:100%;height:45px;}
.quit_confirm .quit:hover{background-image: url('chat/images/quit_hover.png');background-repeat: no-repeat; }
.quit_confirm .logout{margin-right:10px; background-image: url('chat/images/user.png');background-repeat: no-repeat; width:100%;height:45px;}
.quit_confirm .logout:hover{background-image: url('chat/images/user_hover.png');background-repeat: no-repeat; }
.aui_content{width:100%;}
</style>
</head>
<body>
<div class="body titlebar">
	<div class="winTools">
		<a href="#" class="btn btn_close min_action" data-type="winmin"></a>
<!-- 		<a href="#" class="btn btn_close" data-type="winmax">□</a> -->
		<a href="#" class="btn btn_close close_action" data-type="winclose"  ></a>
	</div>

	<div>
	    <jsp:include page="chat/coco.jsp"></jsp:include>
	</div>
	    
	<div>
	<jsp:include page="chat/userTree2.jsp"></jsp:include>
	</div>
</div>

<div class="winBoxBorders winBoxBorderT"></div>
<div class="winBoxBorders winBoxBorderR"></div>
<div class="winBoxBorders winBoxBorderB"></div>
<div class="winBoxBorders winBoxBorderL"></div>
<div id="quit_confirm" style="display:none">
<div class="quit_confirm" style="border-bottom:1px solid black" onclick="quit();">
	<div class="quit"><span>关闭叮当</span>
	<p>关闭后你将不能收到新的信息</p>
	</div>
</div>
<div class="quit_confirm" onclick="logout();">
	<div class="logout"><span>重新登录</span>
	<p>注销或切换到其他登录账号</p>
	</div>
</div>
</div>
</body>
</html>