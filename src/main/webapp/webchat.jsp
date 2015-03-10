<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html >
<head>
<meta charset="utf-8">
<meta http-equiv="pragram" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<title>coco</title>
<meta name="description" content="">
<meta name="keywords" content="">
<!-- <link href="/bootstrap/css/bootstrap.css" rel="stylesheet"> -->
<!-- <script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script> -->
<!-- <link href="/style/style.css" rel="stylesheet"> -->
<link rel="stylesheet" type="text/css" href="oa/style/cocoWindow.css" />
<!-- <link rel="stylesheet" type="text/css" href="/oa/style/cocoWinLayer.css" /> -->
<link rel="stylesheet" type="text/css" href="oa/style/cssOa.css" />
<link rel="stylesheet" type="text/css" href="oa/style/im.css" />
<script src="js/jquery.js" type="text/javascript"></script>
<!-- <script type="text/javascript" src="/oa/js/messagesBox.js"></script> -->
<script type="text/javascript" src="oa/js/json2.js"></script>
<script type="text/javascript" src="oa/js/web_chat.js"></script>
<script type="text/javascript" src="oa/js/bosh_connection.js"></script>
<script type="text/javascript" charset="utf-8" src="js/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="js/ueditor1_4_3/ueditor.all.yw.min.js"> </script>
<!-- <script type="text/javascript" charset="utf-8" src="js/ueditor1_4_3/ueditor.all.js"> </script> -->
<script type="text/javascript" charset="utf-8" src="js/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript">
var valid = false;
my_uid = '${myUid}';
var resource = '${resource}';
var callByArtDialog=false;
web_plugin_update_title_callback = window.top.updateTitle;
web_plugin_update_user_status_callback = window.top.updateContactStatus;
$(function(){
	if(my_uid){
		valid=true;
	}
	ue_text_editor = UE.getEditor('editor', {
        toolbars: [
            ['simpleupload','emotion','forecolor']
        ],
        theme:'default',
        autoHeightEnabled: false
    });
    ue_text_editor.addListener( 'ready', function( editor ) {
    	//$(ue_text_editor.document).bind('keyup','ctrl+enter', function(){ send();});
        //ue_text_editor.document.onkeyup=function(e){
        //  msgAreaKeyup(e);
        //};
        
        UE.dom.domUtils.on(ue_text_editor.body,"keyup",function(e){
        	msgAreaKeyup(e);
        });
        ue_text_editor.document.onpaste=function(e){
          // onPasteHandler(ue,e);
          console.log(e);
        };
    });

    //$(document).bind('keyup','ctrl+enter', function(){ send();}); 
});


function selectFirstChatIfNoOneSelected(){
	var current = $('.now');
	if(current.length==0){
		var lis = $('.cocoWinLxrList li');
		if(lis.length>0){
			selectChat($(lis[0]).attr('cid'),true);
		}
	}
}
function isContactOpen(contactId){
	if($('#chat_'+contactId).size()>0){
		return true;
	}else{
		return false;
	}
}
function addWebRecentContact(contactId){
	//判断用户是否已经在最近历史会话中
	if(isContactOpen(contactId)){
		return;
	}
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: 'c/addRecentContact?contactId='+contactId,
	    success:function(data){
	    }
	  });
}

//买家点击卖家时
//卖家收到买家信息时
function openContact(contactId, name , avatar,status){
	if(!contactId){
		return;
	}
	addWebRecentContact(contactId);
	openChat(contactId , name , avatar);
}
function openChats(contacts){
	for(var i=0;i<contacts.length;i++){
		var contact = contacts[i];
		openChat(contact.uid , contact.name , contact.avatar,contact.status);
	}
}
function select_chat(contactId){
	selectChat(contactId);
}
function auth(token , success, error){
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: 'c/auth?token='+token+'&'+Math.random(),
	    success:function(data){
	    	var user = data.me;
	    	if(user.id){
	    		my_uid=user.id;
		    	my_avatar=user.avatar;
		    	my_name = user.name;
		    	valid = true;
		    	startBosh();
		    	
		    	
		    	//历史会话
		    	//
		    	if(data.contacts){
		    		openChats(data.contacts);
		    	}
		    	if(data.unReadChats.length>0){
		    		for(var i=0;i<data.unReadChats.length;i++){
		    			var chat = data.unReadChats[i];
		    			if(!isContactOpen(chat.senderId)){
		    				openContact(chat.senderId,chat.senderName,chat.senderAvatar , chat.status);
		    			}
		    			//设置未读消息数量
		    			var jmsgCount = $('#chat_'+chat.senderId).find('.new_msg_count');
		    			jmsgCount.text(chat.total);
		    			jmsgCount.addClass('cocoWinNewsNum');
		    		}
		    		//闪动提醒
		    		window.top.shandong();
		    		//通知调用页面
			    	if(success){
			    		success();
			    	}
		    	}
	    	}else{
	    		if(error){
	    			
	    		}
	    	}
	    }
	  });
}




(function () {
  var ie = !!(window.attachEvent && !window.opera);
  var wk = /webkit\/(\d+)/i.test(navigator.userAgent) && (RegExp.$1 < 525);
  var fn = [];
  var run = function () { for (var i = 0; i < fn.length; i++) fn[i](); };
  var d = document;
  d.ready = function (f) {
    if (!ie && !wk && d.addEventListener)
      return d.addEventListener('DOMContentLoaded', f, false);
    if (fn.push(f) > 1) return;
    if (ie)
      (function () {
        try { d.documentElement.doScroll('left'); run(); }
        catch (err) { setTimeout(arguments.callee, 0); }
      })();
    else if (wk)
      var t = setInterval(function () {
        if (/^(loaded|complete)$/.test(d.readyState))
          clearInterval(t), run();
      }, 0);
  };
})();
function rewin(){
    var bH=$('.bodys').height(),
    uH,
    sH=$('.WinInfoSend').height();
    $('.cocoWinInfoListShow').height(bH-sH-3);
    $('.cocoWinContentLxr').height(bH);
    $('#edui1_iframeholder').height(60);
}
document.ready(function(){
  //$('#edui1_iframeholder').height('510px');
  //alert(0)
});
//alert(1)
$(document).ready(function() {
    rewin();
});
$(window).resize(function() {
    rewin();
});
</script>
<style>
    .title{display:none;background: #ff6600; padding:10px; color: #FFF;} 
    .title .logobox{ border-right: 1px solid #000; padding-right: 20px; margin: 20px;}
	.aui_title{text-align:center}

 body .edui-default .edui-editor{ border: 0;-webkit-border-radius: 0px; 
-moz-border-radius: 0px;
 border-radius: 0px;}

.bodys{ height: 490px; overflow: hidden; height: 100%;}
.rightBox{ display: none; float: right; position: relative; top: 0;width: 200px; border-left: 1px solid #EEE; background: #FFF;height: 100%;}
.qunBox{ display: block; float: right; position: relative; top: 0;width: 200px; border-left: 1px solid #EEE; background: #FFF;box-shadow:none; display:none;}

.qunList{ border-bottom: 1px solid #EEE;}
.qunList dt{ text-align: center; background: url('oa/cocoImages/titBg.png') repeat-x; height: 30px; line-height: 30px; font-size: 12px;}
.qunList dd.conts{ padding: 5px;}

.cocoWin{ position: relative; left: 0;}
.cocoWinContent{ height: 100%;}
.cocoWinContentLxr{ height: 100%;width: 60px;}
.cocoWinLxrListTx{ width: 38px; height: 38px;}
.cocoWinLxrListTx img{ width: 100%; height: 100%;}
.cocoWinLxrList li{padding: 5px 0 10px; height: auto;}
.WinInfoSend { float: none;position: relative; background: none; border: 0;}
.WinInfoListAppend {}
.WinInfoListAppend .newsAppend { margin-left: 0; margin-right: 0; display: block; width: 100%; }
.WinInfoListAppend .newsAppendBox{ margin-left: 70px;}
.WinInfoListAppend .newsAppendBox.Fright{ margin-left: 0; margin-right: 70px;padding: 4px 8px;}
.WinInfoListAppend .newsAppendBox .conTime{ margin-top: 2px;}
.WinInfoSendWrite{width: auto;float: none;margin-right: 0px;}
.WinInfoSendBtn{position: absolute;right: 0;bottom: 0;height: 30px; z-index: 22222;}
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
</style>
</head>
<body>
<div class="title">
    <a href="#" class="logobox"><img src="style/images/logo_white.png" alt=""></a>
    某某店
</div>
<div class="bodys"> 




           <iframe class="mask"></iframe>  
           <div class="mask"></div>  
<div class="cocoWin" id="layerBoxDj" style="position:initial">
<!-- 聊天窗口，可多人 -->
<!--      <div class="cocoWintit" id="cocoWintit" style="-webkit-user-select:none;" > -->
<!-- 	     <span class="chat_title Fleft"></span><i class="closeBg none" onclick="closeBox()" title="最小化"></i> -->
<!-- 	     <i class="closeBg closeX" onclick="closeBox(closeAllChat)" title="关闭"></i> -->
<!--      </div> -->
     
     <div class="cocoWinContent">
           
           
           <div class="rightBox " style="display:">

                <dl class="qunList shangpinBox">
                    <dt class="">商品详情</dt>
                    <dd class="conts">
                        <jsp:include page="product.jsp"></jsp:include>
                    </dd>
                </dl>

          </div>
     
          <div class="cocoWinContentLxr" style="-webkit-user-select:none;">
               <!-- 左侧聊天人列表 -->
               <ul class="cocoWinLxrList">
                                    
               </ul>
          </div>
          <!--  margin-left:150px; -->
          <div style=" margin-right:0px; height:100%;-webkit-user-select:text;">
          	  <!-- 聊天记录区 -->
              <div class="cocoWinInfoListShow" style="height:377px;">
              
              </div>
              <!-- 消息发送区 -->
              <div class="WinInfoSend">
                    
                    <div class="WinInfoSendWrite">
                         <span id="editor" type="text/plain" name="conts" style="height:84px;width:100%"></span>
                    </div>
                    
                    <div class="WinInfoSendBtn">
                         <button title="ctrl+enter 直接发送" class="WinInfoSendBtnMessage Fleft" onclick="send();">发送</button>
                    </div>
               
               </div>
           </div>

     </div>

</div>




</div>

</body>
</html>