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
	auth('${token}');
});

function auth(token){
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: '/c/auth?token='+token,
	    success:function(data){
	    	var user = data.me;
	    	if(user.id){
	    		my_uid=user.id;
		    	my_avatar=user.avatar;
		    	my_name = user.name;
		    	ws_url = 'ws://${domainName}:9099?uid='+user.id+'&type='+user.type+'&uname='+user.name;
		    	connectWebSocket();
	    	}else{
	    		 //startLogin();
	    	}
	    }
	  });
}
function startLogin(){
	openNewWin('login' , '310','270','登录','oa/login.jsp');
}
</script>

</head>
<body>
 <div> 

<script type="text/javascript" src="/oa/js/messagesBox.js"></script>
<script type="text/javascript" src="/oa/js/chat.js"></script>
<script type="text/javascript" src="/oa/js/select.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.all.yw.min.js"> </script>
<!--<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.all.js"> </script>-->
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript">

$(function(){
    ue_text_editor = UE.getEditor('editor', {
        toolbars: [
            ['simpleupload','emotion','spechars','forecolor']
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

    //getUnReadChats();
    heartBeat();
});

</script>




           <iframe class="mask"></iframe>  
           <div class="mask"></div>  
<div class="cocoWin" id="layerBoxDj" style="">
<!-- 聊天窗口，可多人 -->
     <div class="cocoWintit" id="cocoWintit" style="-webkit-user-select:none;" >
	     <span class="chat_title Fleft"></span><i class="closeBg none" onclick="closeBox()" title="最小化"></i>
	     <i class="closeBg closeX" onclick="closeBox(closeAllChat)" title="关闭"></i>
     </div>
     
     <div class="cocoWinContent">
     
          <div class="cocoWinContentLxr" style="-webkit-user-select:none;">
               <!-- 左侧聊天人列表 -->
               <ul class="cocoWinLxrList">
                                    
               </ul>
               
          </div>
          
          <div style=" width:510px; float:left; height:100%;-webkit-user-select:text;">
          	  <!-- 聊天记录区 -->
              <div class="cocoWinInfoListShow" style="height:411px;">
              
                   
                   
              
              </div>
              <!-- 消息发送区 -->
              <div class="WinInfoSend">
                    
                    <div class="WinInfoSendWrite">
                         <span id="editor" type="text/plain" name="conts" style="height:84px;width:100%"></span>
                    </div>
                    
                    <div class="WinInfoSendBtn">
                    
                         <button class="WinInfoSendBtnAddPhoto Fleft" title=""></button>
                         <button title="ctrl+enter 直接发送" class="WinInfoSendBtnMessage Fleft" onclick="send();">发送</button>
                    
                    </div>
               
               </div>
           </div>
           
           
           <div class="qunBox" style="display:none">
               
               <div class="qunBoxTit"><span>群组成员</span></div>
                <div class="qunBoxList">

                   <ul>
                   
                   </ul>

                </div>

          
          </div>

     </div>

</div>




</div>
    

</body>
</html>