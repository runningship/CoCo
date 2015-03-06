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
var token = '${token}';
</script>
<script type="text/javascript" src="/oa/seller.js"></script>
<style type="text/css">
.hide{display:none}
</style>
</head>
<body>
<div>
<p>1.判断调用页面session是否存在,不存在去登录</p>
<p>2.chat_window.valid是否为true,不是则调用chat_window.auth()方法.若为true，调用chat_window.openChat()</p>
</div>
<span onclick="openChat();"><img id="coco"  onmouseover="allow_coco_doudong=false;" onmouseout="allow_coco_doudong=true;" src='oa/images/ww.jpg' style="width:16px;height:16px;position: absolute;bottom: 10px;right: 20px;cursor:pointer"/></span>
</body>
</html>