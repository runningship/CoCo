<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script type="text/javascript">
var j_dialogShow=false;

var token = '${token}';
var webchat_dialog;
var iframe;
var message_box=[];
var shan_dong_interval;
var web_dialog_show=false;
var allow_coco_doudong=true;
$(function(){

	webchat_dialog = art.dialog.open('webchat.jsp', {title: '提示' ,width: 861, height: 562,
		init: function () {
	    	iframe = this.iframe.contentWindow;
	    	iframe.web_plugin_message_callback = onMessage;
	    	resizeDialog();
		},
		close:function(){
			webchat_dialog.hide();
			web_dialog_show = false;
			return false;
		}
	});
	webchat_dialog.hide();
	
});

function resizeDialog(){
	if(!j_dialogShow){
		var wW=document.body.clientWidth,
		wH=document.documentElement.clientHeight,
		dW=webchat_dialog.config.width,
		dH=webchat_dialog.config.height,
		sW=(wW-dW)/2,
		sH=(wH-dH)/2;
		sW=sW<0?0:sW;
		sH=sH<0?0:sH;
		webchat_dialog.position(sW,sH);// webchat_dialog.config.left
		j_dialogShow=true;
	}
}

function onMessage(msg){
	//把消息保存在消息盒子中,闪动提醒,点击闪动图标时，才打开消息盒子中的所有信息
	if(!iframe.isContactOpen(msg.senderId)){
		message_box.push(msg);
	}
	notifyNewMessage(msg);
}

function notifyNewMessage(msg){
	//会话没有打开，或者聊天窗口关闭状态下，给闪动提醒
	if(!iframe.isContactOpen(msg.senderId) || web_dialog_show==false){
		if(!shan_dong_interval){
			shan_dong_interval = setInterval(function(){
				if(allow_coco_doudong==false){
					$('#coco').removeClass('hide');	
				}else{
					$('#coco').toggleClass('hide');	
				}
			},300);
		}	
	}
}

function openChat(sellerId , name ,avatar){
	if(!token){
		//go to login
		return;
	}
	clearInterval(shan_dong_interval);
	shan_dong_interval=null;
	if(!iframe.valid){
		//登录coco...
		iframe.auth(token,function(){
			//登录成功
			doOpenChat(sellerId , name ,avatar);	
		} , function(){
			//登录失败
		});
	}else{
		for(var i = 0;i<message_box.length;i++){
			var msg = message_box[i];
			iframe.openContact(msg.senderId,msg.senderName,msg.senderAvatar);
		}
		doOpenChat(sellerId , name ,avatar);
	}
	
}

function doOpenChat(sellerId , name ,avatar){
	webchat_dialog.show();
	webchat_dialog.size(861, 562);
	iframe.openContact(sellerId ,name , avatar);
	$('iframe').css('display','');
	web_dialog_show= true;
}

function updateTitle(title){
	if(webchat_dialog){
		webchat_dialog.title(title);
	}
}
window.onresize = function(){
j_dialogShow=false;resizeDialog();
};
</script>
<style type="text/css">
.hide{display:none}

</style>
</head>
<body>
<a href="#" onclick="openChat('1','合肥供应商','')">合肥供应商</a>
<a href="#" onclick="openChat('2','南京供应商','')">南京供应商</a>
<a href="#" onclick="openChat('3','杭州供应商','')">杭州供应商</a>
<div>
<p>1.判断调用页面session是否存在,不存在去登录</p>
<p>2.chat_window.valid是否为true,不是则调用chat_window.auth()方法.若为true，调用chat_window.openChat()</p>
</div>
<span onclick="openChat();" style="width:16px;height:16px;"><img id="coco"  onmouseover="allow_coco_doudong=false;" onmouseout="allow_coco_doudong=true;"  src='oa/images/ww.jpg' style="width:16px;height:16px;position: absolute;bottom: 10px;right: 20px;cursor:pointer"/></span>
</body>
</html>