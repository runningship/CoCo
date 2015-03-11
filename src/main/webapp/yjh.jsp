<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="js/jquery.js" type="text/javascript"></script>
<script src="js/dialog/jquery.artDialog.source.js?skin=twitter" type="text/javascript"></script>
<script src="js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
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
	if(!token){
		$('#coco').addClass('user_offline_filter');
	}
});

function updateContactStatus(uid,status){
	
}
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

window.onresize = function(){
j_dialogShow=false;
//resizeDialog();
};
</script>
<script type="text/javascript" src="chat/${userType }.js"></script>
<style type="text/css">
.hide{display:none}
.user_offline_filter{-webkit-filter: grayscale(100%);}

.aui_nw,.aui_w,.aui_sw,.aui_ne,.aui_e,.aui_se{ width: 0;}
.aui_sw,.aui_s,.aui_se{ height: 0;}
.aui_title{text-align: center;}
</style>
</head>
<body>
<a href="#" onclick="openChat('1','合肥供应商','')">合肥供应商</a>
<a href="#" onclick="openChat('2','南京供应商','')">南京供应商</a>
<a href="#" onclick="openChat('3','杭州供应商','')">杭州供应商</a>

<span onclick="openChat();"><img id="coco"  onmouseover="allow_coco_doudong=false;" onmouseout="allow_coco_doudong=true;" src='chat/images/ww.jpg' style="width:16px;height:16px;position: absolute;bottom: 10px;right: 20px;cursor:pointer"/></span>
</body>
</html>