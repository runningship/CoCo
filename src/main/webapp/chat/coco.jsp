<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<script src="js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script type="text/javascript" src="chat/js/messagesBox.js"></script>
<script type="text/javascript" src="chat/js/chat.js"></script>
<script type="text/javascript" src="chat/js/select.js"></script>
<script type="text/javascript" charset="utf-8" src="js/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="js/ueditor1_4_3/ueditor.all.yw.min.js"> </script>
<!--<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.all.js"> </script>-->
<script type="text/javascript" charset="utf-8" src="js/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="chat/js/buildHtml.js"></script>
<script type="text/javascript">

function isGetOutLxr(){
	if($('.cocoWinLxrList').children('li').length>0){
		$('#outListBtn').click();
	}
}

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
    
    
    heartBeat();
});

function showSearchPanel(){
	$('#searchResult ul').empty();
	$('#searchResult ul').append($('#lxrList .a_user').clone());
	$('#searchResult').css('display','');
	selBoxCge('searchResult');
	
}

var searchHandler;
function prepareSearch(){
	if(!searchHandler){
		searchHandler= setTimeout(doSearchContact,300);	
	}else{
		clearTimeout(searchHandler);
		searchHandler= setTimeout(doSearchContact,300);
	}
}

function endChangeSign(){
  var a=$('form[name=form1]').serialize();
  var b = $('#user_sign_input').val();
  YW.ajax({
    type: 'POST',
    url: '/coco/c/im/updateUserSign',
    data:a,
    mysuccess: function(data){
      $('#user_sign_input').css('display','none');
        $('#sign').css('display','');
      if (b!="") {
        $('#sign').html(b);
      }else {
        $('#sign').html("我的个性签名");
      }
    }
  });
}

function editSign(){
  $('.mainabout').attr('style','display:none');
  $('#user_sign_input').attr('style','width:152px;');
  $('#user_sign_input').focus();
}

function doSearchContact(){
	clearTimeout(searchHandler);
	searchHandler=null;
	var st = $('#search_input').val();
	console.log('search text : '+st);
	$('#searchResult ul .search_clone').each(function(index,obj){
		var py = $(obj).attr('py');
		var pyshort = $(obj).attr('pyshort');
		var name = $(obj).attr('name');
		if(py.indexOf(st)>-1 || pyshort.indexOf(st)>-1 || name.indexOf(st)>-1){
			$(obj).css('display' , '');
		}else{
			$(obj).css('display' , 'none');
		}
	});
}

function startCreateGroup(){
	 art.dialog.open("chat/selectUser.jsp",{
		 id:'user_tree',
		 width:300,
		 height:350,
		 title:'选择联系人',
		 ok: function () {
			 var data = this.iframe.contentWindow.getSelectUsers();
			 if(data.uids){
				 createGroupWithUsers(data.groupName,data.uids , data.names , data.avatars);
			 }
		 },
		 cancel:function(){
			 
		 }
	 });
}

function createGroupWithUsers(groupName, uids , names ,avatars){
	var subGroupName = groupName;
	if(!subGroupName){
		subGroupName = names.join();
	}
	if(subGroupName.length>11){
		subGroupName = subGroupName.substring(0,10);
		subGroupName+="...";
	}
	YW.ajax({
	    type: 'POST',
	    url: '/coco/c/im/createGroupWithUsers',
	    data:'uids='+uids.join()+'&groupName='+subGroupName,
	    mysuccess: function(data){
	    	var json = JSON.parse(data);
	    	//add to 
	    	addGroupToPanel(json.groupId , subGroupName , uids.length,avatars);
	    }  
	  });
}

function addGroupToPanel(groupId , groupName,userCount ,avatars){
	userCount++;//加自己
	avatars.push(my_avatar);
	var html = '<li id="group_'+groupId+'" onclick="openGroupChat(\''+groupId+' \',\''+groupName+' \')">'
					  +	 '<div id="group_avatar_'+groupId+'" class="qunTx Fleft">';
		for(var i=0;i<avatars.length;i++){
			if(i>=4){
				break;
			}
			html+= '<img src="chat/images/avatar/'+avatars[i]+'.jpg">';
		}
		html =html  +'</div>'
					  +'<div class="cocoQunInfo Fleft">'
					  +'<p id="group_name_'+groupId+'" class="name">'+groupName 
					  +'<span style="position: absolute;right: -20px;top: 10px;">('+userCount+'人)</span>'
					  +'</p>'
					  +'</div>'
					  +'<div class="new_msg_count"></div>'
					  +	'<div class="msgClose" onclick="removeGroup(\''+groupId+'\',1)">×</div>'
					  +'</li>';
	$('#cocoQunList').prepend(html);
	openGroupChat(groupId , groupName);
	selectChat($('#group_chat_'+groupId) , groupId);
}

function removeGroup(groupId , isOwner){
	event.preventDefault();
	event.cancelBubble=true;
	//提醒确认
	var cfmText = "";
	var url="";
	if(isOwner){
		cfmText='确定要删除群组吗？';
		url = '/coco/c/im/removeGroup?groupId='+groupId;
	}else{
		cfmText = '确定要离开群组吗？';
		url = '/coco/c/im/leaveGroup?groupId='+groupId;
	}
	art.dialog.confirm(cfmText, function () {
		YW.ajax({
		    type: 'POST',
		    url: url,
		    mysuccess: function(data){
		    	$('#group_'+groupId).remove();
		    }
		});
		//如果chat被打开则关闭
		if($('#group_chat_'+groupId).length>0){
			closeChat(groupId , groupId);	
		}
  	},function(){},'warning');
	
}

/*document.ready*/
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




</script>
<div class="cocoMain" style="z-index:9999999">
     
    <div class="table w100 h100">
        
         <div class="tr w100">

            <form class="form1" name="form1" role="form" onsubmit="submits();return false;">
              <div class="td cocoMainTit oaTitBgCoco titlebar">
                <div class=""><img src="chat/images/avatar/${me.avatar}.jpg" class="user_offline_filter" id="avatarId" onclick="openNewWin('changeAvatar','695','500','修改头像','oa/avatar.jsp');" />
                    <input name="uid" value="${me.id}" style="display:none;"/>
                    <div title="" class="mainInfo mainName" id="user_name_div">${me.name}</div>
                    <input id="user_name_input" style="display:none;width:152px;" onblur="endChangeName();" />
                    <c:if test="${me.sign==null||me.sign==''}"><div class="mainInfo mainabout nobar" onclick="editSign();">我的个性签名</div></c:if>
                    <div class="mainInfo mainabout nobar" id="sign" onclick="editSign();">${me.sign}</div>
                    <input id="user_sign_input" class=" nobar" style="display:none;width:152px;" value="${me.sign}" name="sign" onblur="endChangeSign();" />
                    <div class="turnLit" style="display:none;" onclick="$('.cocoMain').toggleClass('hide');">-</div>
                </div>
                <!-- <img src="oa/images/coco.png" /> -->
            </div>
            </form>
         </div>
         <div class="tr w100">
              <div class="td cocoMainSelect" id="cocoMainSelectId">
                   <span class="sle" onclick="selBoxCge('lxrList')" id="lxrListBtn"><i class="Bg lxr"></i></span>
                   <span onclick="selBoxCge('qunList')" id="qunListBtn"><i class="Bg qun"></i><em id="qunbox_dot" class=""></em></span>
                   <span onclick="selBoxCge('outList')" id="outListBtn"><i class="Bg ldq"></i></span>
              </div>
         </div>
         
         <div class="tr w100">
              <div class="td cocoMainSearch">
              
                   <input type="text" id="search_input" class="cocoMainSearchBox" placeholder="搜索联系人"  onkeyup="prepareSearch();" onfocus="showSearchPanel()"/>
              
              </div>
         </div>
         
         
         <div class="tr w100">
              <div class="td cocoMainCon">
                    
                    <div id="lxrList" class="cocoMainConBox" style="height:100%; overflow:hidden; overflow-y:auto; z-index:3;">
                    
                    	<ul id="cocoList" class="ztree jtree cocoList"></ul>
                    </div>
                    
                    <div id="searchResult" class="cocoMainConBox" style="height:100%; overflow:hidden; overflow-y:auto; z-index:1;display:none">
                    	<ul class="cocoSearchList">
                    	</ul>
                    </div>
                    
                    <div id="qunList" class="cocoMainConBox" style="height:100%; overflow:hidden; overflow-y:auto; left:-100%;">
                    
                         <ul class="cocoList" id="cocoQunList" >
                            <c:forEach items="${groups}" var="group">
                            	<c:if test="${group.totalUsers>0 }">
                            		<li id="group_${group.gid}" onclick="openGroupChat('${group.gid}','${group.dname }')">
	                                 <div id="group_avatar_${group.gid}" class="qunTx Fleft">
                                        <c:forEach items="${group.users}" var="user">
                                        	<c:if test="${user.avatar eq null}"><img src="chat/images/avatar/157.jpg"></c:if>
											<c:if test="${user.avatar ne null}"><img src="chat/images/avatar/${user.avatar }.jpg"></c:if>
                                        </c:forEach>
                                     </div>
	                                 <div class="cocoQunInfo Fleft">
	                                     <p  id="group_name_${group.gid}" class="name">${group.dname } 
                                         <span style="position: absolute;right: -20px;top: 10px;">(${group.totalUsers}人)</span>
                                         </p>
	                                 </div>
                                     <div class="new_msg_count"></div>
                                     <div class="msgClose" onclick="removeGroup('${group.gid}' ,${group.isOwner })">×</div>
                             		</li>
                            	</c:if>
                            	 
                            </c:forEach>
                            
                         
                         </ul>
                    	<img onclick="startCreateGroup();" src="chat/cocoImages/plus.png" style="width:20px;height:20px;position:absolute;bottom:0px;left:95px;cursor:pointer"/>
                    </div>
                    
                    
                    <div id="outList" class="cocoMainConBox" style="height:100%; overflow:hidden; overflow-y:auto; z-index:1;">
                    
                      <ul class="cocoWinLxrList">
                        
                      </ul>
                    </div>
              </div>
         </div>
         
         <div class="tr w100">
              <div class="td">
                   
                   <div onclick="recoverChatPanel();" class="cocoNews"><span class="name chat_title"></span>
                   <!-- <i class="Bg close"></i> -->
                   </div>
                   
              </div>
         </div>
         
         
    </div>

</div>

<!-- <div style=" position:absolute; bottom:1px; left:165px;cursor:pointer;width:30px; height:30px; overflow:hidden; z-index:99999992;"> -->
<!--      <span><img onclick=" $('.cocoMain').toggleClass('hide');" src="style/images/litFox.png" width="32" /></span> -->
<!-- </div> -->

<div style="position:absolute; bottom:0; left:0px; width:201px; height:36px; z-index:99999991;" onselectstart="return false;">
     <div onclick="recoverChatPanel();" class="cocoNews " style="text-align:center; margin-top:0;"><span class="name chat_title">CoCo 聊天</span></div>
</div>



           <iframe class="mask"></iframe>  
           <div class="mask"></div>  
<div class="cocoWin" id="layerBoxDj" style=" display:none;">
<!-- 聊天窗口，可多人 -->
     <div class="cocoWintit" id="cocoWintit" style="-webkit-user-select:none;" >
	     <span class="chat_title"></span><i class="closeBg none" onclick="closeBox()" title="最小化"></i>
	     <i class="closeBg closeX" onclick="closeBox(closeAllChat)" title="关闭"></i>
     </div>
     
     <div class="cocoWinContent">
     
          <div class="cocoWinContentLxr" style="-webkit-user-select:none; display:none;">
               <!-- 左侧聊天人列表 -->
               <ul class="cocoWinLxrList">
                                    
               </ul>
               
          </div>
          <!--  width:510px; float:left; --> 
          <div style="height:100%;-webkit-user-select:text;">
          	  <!-- 聊天记录区 -->
              <div class="cocoWinInfoListShow" style="height:411px;">
              
                   
                   
              
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
           
           
           <div class="qunBox" style="display:none">
               
               <div class="qunBoxTit"><span>群组成员</span></div>
                <div class="qunBoxList">

                   <ul>
                   
                   </ul>

                </div>

          
          </div>

     </div>

</div>



