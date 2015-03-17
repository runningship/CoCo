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
<script type="text/javascript">
$(function(){
	check_user = true;
	setting.check.enable=check_user;
	initUserTree('cocoList');
});

function getSelectUsers(){
	var result = JSON.parse('{}');
	var uids = [];
	var names = [];
	var treeObj = $.fn.zTree.getZTreeObj("cocoList");
	var nodes = treeObj.getCheckedNodes(true);
	for(var i=0;i<nodes.length;i++){
		var node = nodes[i];
		if(node.type!='user'){
			continue;
		}
		uids.push(node.uid);
		names.push(node.name);
		result.uids = uids;
		result.names = names;
	}
	return result;
}
</script>
</head>
<body>
<ul id="cocoList" class="ztree jtree cocoList"></ul>
<div>
<jsp:include page="userTree2.jsp"></jsp:include>
</div>
</body>
</html>