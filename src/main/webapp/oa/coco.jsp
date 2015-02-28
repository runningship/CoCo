<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="/oa/js/messagesBox.js"></script>
<script type="text/javascript" src="/oa/js/chat.js"></script>
<script type="text/javascript" src="/oa/js/select.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.all.yw.min.js"> </script>
<!--<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.all.js"> </script>-->
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript">

$(function(){
    connectWebSocket();
    
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

    getUnReadChats();
    heartBeat();
});

</script>
<div class="cocoMain" style="z-index:9999999">
     
    <div class="table w100 h100">
        
         <div class="tr w100">

              <div class="td cocoMainTit oaTitBgCoco">
                <div class=""><img src="/oa/images/avatar/${me.avatar}.jpg" class="user_offline_filter" id="avatarId" onclick="openNewWin('changeAvatar','695','500','修改头像','oa/avatar.jsp');" />
                    <div title="" class="mainInfo mainName" id="user_name_div">${me.uname}</div>
                    <input id="user_name_input" style="display:none;margin-top:5px;" onblur="endChangeName();" />
                    <div class="mainInfo mainabout">${dname}</div>
                    <div class="turnLit" onclick="$('.cocoMain').toggleClass('hide');">-</div>
                </div>
                <!-- <img src="/oa/images/coco.png" /> -->
            </div>
         </div>
         <div class="tr w100">
              <div class="td cocoMainSelect" id="cocoMainSelectId">
                   <span class="sle" onclick="selBoxCge('lxrList')"><i class="Bg lxr"></i></span>
                   <span onclick="selBoxCge('qunList')"><i class="Bg qun"></i><em id="qunbox_dot" class=""></em></span>
                   <span><i class="Bg ldq"></i></span>
              </div>
         </div>
         
         <div class="tr w100">
              <div class="td cocoMainSearch">
              
                   <input type="text" class="cocoMainSearchBox" placeholder="搜索联系人" />
              
              </div>
         </div>
         
         
         <div class="tr w100">
              <div class="td cocoMainCon">
                    
                    <div id="lxrList" class="cocoMainConBox" style="height:100%; overflow:hidden; overflow-y:auto; z-index:1;">
                    
                    	<ul id="cocoList" class="ztree jtree cocoList"></ul>
                    </div>
                    
                    <div id="qunList" class="cocoMainConBox" style="height:100%; overflow:hidden; overflow-y:auto; left:-100%;">
                    
                         <ul class="cocoList" id="cocoQunList" >
                            <c:forEach items="${depts}" var="dept">
                            	<c:if test="${dept.totalUsers>0 }">
                            		<li id="group_${dept.did}" onclick="openGroupChat(${dept.did},'${dept.dname }')">
	                                 <div id="group_avatar_${dept.did}" class="qunTx Fleft">
                                        <c:forEach items="${dept.users}" var="user">
                                            <img src="/oa/images/avatar/${user.avatar}.jpg">
                                        </c:forEach>
                                     </div>
	                                 <div class="cocoQunInfo Fleft">
	                                     <p id="group_name_${dept.did}" class="name">${dept.dname } 
                                         <span>(${dept.type},${dept.totalUsers}人)</span>
                                         </p>
	                                 </div>
                                     <div class="new_msg_count"></div>
                             		</li>
                            	</c:if>
                            	 
                            </c:forEach>
                            
                         
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

<div style=" position:absolute; bottom:1px; right:15px;cursor:pointer;width:30px; height:30px; overflow:hidden; z-index:99999992;">
     <span><img onclick=" $('.cocoMain').toggleClass('hide');" src="/style/images/litFox.png" width="32" /></span>
</div>
<div style="position:absolute; bottom:0; right:10px; width:201px; height:36px; z-index:99999991;" onselectstart="return false;">
     <div onclick="recoverChatPanel();" class="cocoNews " style="text-align:center; margin-top:0;"><span class="name chat_title">CoCo 聊天</span></div>
</div>



           <iframe class="mask"></iframe>  
           <div class="mask"></div>  
<div class="cocoWin" id="layerBoxDj" style=" display:none;">

     <div class="cocoWintit" id="cocoWintit" style="-webkit-user-select:none;"><span class="chat_title Fleft"></span><i class="closeBg none" onclick="closeBox()" title="最小化"></i><i class="closeBg closeX" onclick="closeBox(closeAllChat)" title="关闭"></i></div>
     
     <div class="cocoWinContent">
     
          <div class="cocoWinContentLxr" style="-webkit-user-select:none;">
               
               <ul class="cocoWinLxrList">
                                    
               </ul>
               
          </div>
          
          <div style=" width:510px; float:left; height:100%;-webkit-user-select:text;">
              <div class="cocoWinInfoListShow" style="height:411px;">
              
                   
                   
              
              </div>
                <!-- <div id="facePanel" style="display: none;position:absolute;bottom:125px;border:1px solid #eee">
                    <c:forEach var="i"  begin="1"  end="60"  step="1">
                        <img src="http://forum.csdn.net/PointForum/ui/scripts/csdn/Plugin/001/face/${i}.gif" onclick="appendFaceToMsg(${i});" />
                    </c:forEach>
                </div> -->
              <div class="WinInfoSend">
                    
                    <!-- <div style="height:22px;"><img src="oa/images/face.gif"/ style="cursor:pointer;margin-left:5px;" onclick="showFacePanel()"></div> -->
                    <div class="WinInfoSendWrite">
                         <!-- <textarea id="msg_textarea" onkeyup="msgAreaKeyup();" class="fontStyleNewsWrite"></textarea> -->
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



