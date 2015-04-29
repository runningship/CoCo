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
<link href="../style/style.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="style/cocoWindow.css" />
<link rel="stylesheet" type="text/css" href="style/cocoWinLayer.css" />
<link rel="stylesheet" type="text/css" href="style/cssOa.css" />
<script src="/coco/js/jquery.js" type="text/javascript"></script>
<script src="../js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="../js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>

<script type="text/javascript" charset="utf-8" src="../js/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="../js/ueditor1_4_3/ueditor.all.yw.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="../js/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>

<script type="text/javascript" src="../js/buildHtml.js"></script>
<script type="text/javascript" src="js/chat.js"></script>
<script type="text/javascript">
$(function(){
	 ue_text_editor = UE.getEditor('editor', {
	        toolbars: [
	            ['forecolor']
	        ],
	        autoHeightEnabled: false
	    });
	    ue_text_editor.addListener( 'ready', function( editor ) {
	        ue_text_editor.document.onkeyup=function(e){
	          msgAreaKeyup(e);
	        };
	        ue_text_editor.document.onpaste=function(e){
	          // onPasteHandler(ue,e);
	          console.log(e);
	        };
	    });
	    
	check_user = true;
	setting.check.enable=check_user;
	initUserTree('cocoList');
});

function getSelectUsers(){
	var result = JSON.parse('{}');
	result.groupName=$('#groupName').val();
	var uids = [];
	var names = [];
	var avatars = [];
	var treeObj = $.fn.zTree.getZTreeObj("cocoList");
	var nodes = treeObj.getCheckedNodes(true);
	for(var i=0;i<nodes.length;i++){
		var node = nodes[i];
		if(node.type!='user'){
			continue;
		}
		uids.push(node.uid);
		names.push(node.name);
		if(node.avatar){
			avatars.push(node.avatar);	
		}else{
			avatars.push(default_avatar);			
		}
	}
	result.uids = uids;
	result.names = names;
	result.avatars = avatars;
	return result;
}

function qunfa(my_uid,my_name,my_avatar){
	var text = ue_text_editor.getContent();
	if(text==""){
		infoAlert('内容不能为空');
		return;
	}
	$('#msg').val(text);
	var data = getSelectUsers();
	$('#senderId').val(my_uid);
	$('#senderName').val(my_name);
	$('#senderAvatar').val(my_avatar);
	if(data.uids && data.uids.length){
		$('#contactIds').val(data.uids.join());
		var a=$('form[name=form1]').serialize();
		YW.ajax({
		    type: 'POST',
		    url: '/coco/c/im/pushMsgToUserList',
		    data:a,
		    mysuccess: function(data){
		    	alert('消息发送成功');
		    	setTimeout(function(){art.dialog.close();},2000);
		    	
		    },
		    error:function(data){
		    	alert(data);
		    }
		  });
	 }else{
		 infoAlert('请先选择接受人');
	 }
}
</script>
</head>
<body>
<form name="form1">
<input type="hidden" id="senderId" name="senderId"/>
<input type="hidden"  id="senderName" name="senderName"/>
<input type="hidden"  id="senderAvatar" name="senderAvatar"/>
<input type="hidden"  id="contactIds" name="contactIds"/>
<input type="hidden"  id="msg" name="msg"/>
</form>
<span id="editor" type="text/plain" style="height:154px;width:100%"></span>
<ul id="cocoList" class="ztree jtree cocoList"></ul>
<div>
<jsp:include page="userTree2.jsp"></jsp:include>
</div>
</body>
</html>