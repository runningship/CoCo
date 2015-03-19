var coco_ws;
var my_avatar;
var my_name;
var my_uid;
var ue_text_editor;
var chat_conts=[];
var default_avatar='150';
//web集成时要提供的来信息时的回调接口
var web_plugin_message_callback;
var web_plugin_update_title_callback;
var web_plugin_update_user_status_callback;
function openChat(contactId,contactName,avatar , status){
	if(!avatar){
		avatar=default_avatar;
	}
	//打开聊天面板
	//showBox();
	//设置zIndex
	$("#layerBoxDj").css({"z-index":999});
	if(web_plugin_update_title_callback){
		web_plugin_update_title_callback('与 '+contactName+'... 聊天中');
	}
	// 判断chat是否已经存在
	if($('#chat_'+contactId).length>0){
		selectChat(contactId);
		return;
	}
	//判断当前用户是否在线
	var imgFilterClass = "";
	if(status=='0'){
		imgFilterClass = 'user_offline_filter';
	}
	// 添加联系人
	var lxrHtml=	'<li type="msg" avatar="'+avatar+'" cname="'+contactName+'" cid="'+contactId+'" id="chat_'+contactId+'" onclick="selectChat(\''+contactId+'\' ,true)">'
                    +   '<div  class="cocoWinLxrListTx Fleft"><img title="'+contactName+'" class="'+imgFilterClass+' user_avatar_img_'+contactId+'" src="chat/images/avatar/'+avatar+'.jpg" /></div>'
                    +   '<div class="cocoWinLxrListPerInfo Fleft">'
                    +   '   <p class="name" style="display:none">'+contactName+'</p>'
                    +   '</div>'
                    +	'<div class="new_msg_count"></div>'
					+	'<div class="msgClose" onclick="closeChat(\''+contactId+'\')">×</div>'
                    +'</li>';
	
	// 添加聊天内容窗口
	var msgContainer = '<div currentPageNo="1" class="WinInfoListShowMainBox" id="msgContainer_'+contactId+'">'
						+'<a href="#" class="msg_more" onclick="nextPage();">查看更多消息</a>'
						+'</div>';
	$('.WinInfoListShowMainBox').css('display','none');
	$('.cocoWinInfoListShow').prepend(msgContainer);
	
	// 显示最新聊天
	// $('.cocoWinLxrList li').removeClass('now');
	$('.cocoWinLxrList').prepend(lxrHtml);
	$('.qunBox').css('display','none');
	
	
	//selectChat(contactId);
	//在selectchat的地方loadHistory
	//loadHistory(contactId,1);
	// $('.msgContainer').css('display','');
	// 清空当前联系人未读消息提醒
	var jmsgCount = $('#lxr_'+contactId).find('.new_msg_count');
	jmsgCount.text('');
	jmsgCount.removeClass('cocoWinNewsNum');
	$('.user_avatar_img_'+contactId).removeClass('doudong');

	//reCacuUnreadStack(contactId,'chat');
	//设置已读,只在select chat的时候设置已读
	//setSigleChatRead(contactId);
	if(!status){
		getUserStatus(contactId);
	}
}

function getUserStatus(contactId){
	$.ajax({
		type: 'get',
		dataType: 'json',
		url: 'c/im/getUserStatus?contactId='+contactId,
		success:function(data){
			setUserStatus(data);
		}
	});
}
function setSigleChatRead(contactId){
	$.ajax({
		type: 'get',
		dataType: 'json',
		url: 'c/im/setSingleChatRead?contactId='+contactId+'&'+Math.random(),
		success:function(data){
		}
	});
}


function loadHistory(contactId , currentPageNo){
  $.ajax({
    type: 'get',
    dataType: 'json',
    url: 'c/im/getHistory?contactId='+contactId+'&currentPageNo='+currentPageNo+'&'+Math.random(),
    success:function(data){
    	buildHistory(data.history);
    	if(data.history.length<10){
    		$('#msgContainer_'+contactId+' .msg_more').css('display','none');
    	}
    }
  });
}

function buildHistory(history){
	var chat = getCurrentChat();
	var offsetTop = 0;
	for(var i=0;i<history.length;i++){
		var msg = history[i];
		var container;
		var senderName;
		container = $('#msgContainer_'+chat.contactId);
		
		if(msg.senderId==my_uid){
			//我发送的消息
			var html = buildSentMessage(msg.conts,msg.sendtime,'',my_uid);
			container.prepend(html);
		}else{
			var senderAvatar = getAvatarByUid(msg.senderId);
			var html = buildRecvMessage(senderAvatar,msg.conts , msg.sendtime , '',msg.senderId);
			container.prepend(html);
		}
		var top = container.children()[0];
		offsetTop +=$(top).height();
	}
	if(container){
		container.scrollTop(offsetTop);	
	}
	
}

function getAvatarByUid(uid){
	var img = $('#user_avatar_'+uid+' img');
	if(img.size()==0){
		return default_avatar;
	}
	return img.attr('user_avatar_img');
}
function getContactNameByUid(uid){
	var name = $('#lxr_'+uid+' .name');
	return name.text();
}

function getRecentContactNameByUid(uid){
	var name = $('#recent_lxr_'+uid+' .name');
	return name.text();
}

function send(){
	var text = ue_text_editor.getContent();
	if(text==""){
		return;
	}
	var chat = getCurrentChat();
	
	onSendMsg(text,chat);
	chat.msg = text;
	sendToServer(chat);
	ue_text_editor.setContent('',false);
	$('#msg_textarea').focus();
	
	scrollToLatestNews();
}

function sendToServer(chat){
	// chat.type="msg";
	chat.senderAvatar = my_avatar;
	chat.senderName = my_name;
	sendByBosh(chat);
	//coco_ws.send(JSON.stringify(chat));
}
function selectChat(contactId,setRead){
	if(!contactId){
		return;
	}
	//保存当前窗口内容
	var li = $('#chat_'+contactId);
	var contactName = li.find('.name').text();
	if(web_plugin_update_title_callback){
		web_plugin_update_title_callback('与 '+contactName+'... 聊天中');
	}
	var conts = "";
	if(UE.Editor.body){
		conts = ue_text_editor.getContent();
	}
	var oldChat = $('.now');
	if(oldChat.length>0){
		pushChatConts(oldChat.attr('cid'),'chat',conts);
	}

	var  msgContainer = $('#msgContainer_'+$(li).attr('cid'));
	
	$('.cocoWinLxrList li').removeClass('now');
	$(li).toggleClass('now');
	
	$('.WinInfoListShowMainBox').css('display','none');
	
	msgContainer.css('display','');

	$(li).find('.new_msg_count').removeClass('cocoWinNewsNum').text('');
	$('#msg_textarea').focus();
	scrollToLatestNews();
	$('.qunBox').css('display','none');

	//切换消息窗口内容
	var oldConts = getChatConts($(li).attr('cid'), 'chat');
	if(UE.Editor.body){
		ue_text_editor.setContent(oldConts);
	}
	if(!msgContainer.attr('hasLoadHistory')){
		loadHistory(contactId , 1);
		msgContainer.attr('hasLoadHistory',true);
	}
	if(setRead){
		setSigleChatRead(contactId);
	}
	
}

function pushChatConts(senderId , type , conts){
	if(!senderId){
		return;
	}
	for(var i=0;i<chat_conts.length;i++){
		var tmp = chat_conts[i];
		if(tmp.senderId==senderId && tmp.type==type){
			tmp.conts = conts;
			return;
		}
	}
	var json = jQuery.parseJSON('{}');
	json.senderId = senderId;
	json.type = type;
	json.conts = conts;
	chat_conts.push(json);
}

function getChatConts(senderId , type){
	for(var i=0;i<chat_conts.length;i++){
		var tmp = chat_conts[i];
		if(tmp.senderId==senderId && tmp.type==type){
			return tmp.conts;
		}
	}
	return "";
}
function buildSentMessage(text,time , senderName,senderId){
	var senderHtml = "";
//	if(senderName){
//		senderHtml = '<div class="lxrName pright">'+senderName+'</div>';
//	}
//	var avatar_img = $('#chat_'+senderId).find('img');
//	var user_status_class='';
//	if(avatar_img.hasClass('user_offline_filter')){
//		user_status_class = 'user_offline_filter';
//	}
	var sentMsgHtml='<div class="WinInfoListAppend">'
                    +     '<div class="txImgRight"><img class=" user_avatar_img_'+senderId+'" src="chat/images/avatar/'+my_avatar+'.jpg" /></div>'
                    +     '<div class="newsAppend">'
                    +     		senderHtml
                    +          '<div class="newsAppendBox Fright">'
                    +               '<div class="conTxt">'+text+'</div>'
                    +               '<div class="conTime"><i class="clock"></i>'+time+'</div>'
                    +               '<div class="sanjiaoRight"></div>'
                    +          '</div>'
                    +     '</div>'
                    +'</div>';
    var obj =  $(sentMsgHtml);
    obj.find('img').on('dblclick',function(){
    	showBigImg(this);
    });
    return obj;
    
}


function layerBoxCenter(id){//漂浮层居中
		  
	      var $mainId =$("#"+id);
		  var w = $mainId.width();
		  var h = $mainId.height();
		  var winW = $(window).width();
		  var winH = $(window).height();
		  var L = (winW - w)/2;
		  var T = (winH - h)/2;

		  T = T<=50?50:T;
		  T = T>=winH-200?winH-200:T;
					
		  L = L<=10?10:L;
		  L = L>=winW-300?winW-300:L;
		  
		  $mainId.css("left",L);
		  $mainId.css("top",T);
		  
	  }




function showBigImg(img){
	if($(img).parent().hasClass('txImg') || $(img).parent().hasClass('txImgRight')){
		$("#imgBigSee").remove();
		var htmlText = "<div id='imgBigSee' onclick='$(this).remove()' style='display:block; position:absolute; background-color:#ffffff; z-index:9999999999; box-shadow:#666 0px 0px 10px; border-radius:3px; font-family:'宋体'; background-color:#ffffff;'>"+ img.outerHTML +"</div>";
		$("body").append(htmlText);
		layerBoxCenter("imgBigSee");
	}else{
		window.open("http://localhost:8088/."+$(img).attr('src'), "_blank");
	}
}

function showBigAvatar(img,event){
	var parent = $('#changeAvatar');
	var copyImg = img.cloneNode();
	copyImg.style.width='100%';

	$("#avatarBigSee").remove();
	var htmlText = "<div id='avatarBigSee' onclick='$(this).remove()' style='display:block; position:absolute; background-color:#ffffff; z-index:9999999999; box-shadow:#666 0px 0px 10px; border-radius:3px; font-family:'宋体'; background-color:#ffffff;'>"+ copyImg.outerHTML +"</div>";
	$("body").append(htmlText);
	layerBoxCenter("avatarBigSee");
	$("#avatarBigSee").css("left",parent.position().left + event.clientX);
    $("#avatarBigSee").css("top",parent.position().top + event.clientY);
}

function buildRecvMessage(senderAvatar , msg , time , senderName , senderId){
	var senderHtml="";
//	if(senderName){
//		senderHtml='<div class="lxrName pleft">'+senderName+'</div>';
//	}
	if(!senderAvatar){
		senderAvatar = default_avatar;
	}
	var avatar_img = $('#chat_'+senderId).find('img');
	var user_status_class='';
	if(avatar_img.hasClass('user_offline_filter')){
		user_status_class = 'user_offline_filter';
	}
	var recvMsgHtml='<div class="WinInfoListAppend">'
                    +     '<div class="txImg"><img class="'+user_status_class+' user_avatar_img_'+senderId+'" src="chat/images/avatar/'+senderAvatar+'.jpg" /></div>'
                    +     '<div class="newsAppend">'
                    +         	senderHtml
                    +          '<div class="newsAppendBox Fleft">'
                    +               '<div class="conTxt">'+msg+'</div>'
                    +               '<div class="conTime"><i class="clock"></i>'+time+'</div>'
                    +               '<div class="sanjiaoLeft"></div>'
                    +          '</div>'
                    +     '</div>'
                    +'</div>';
    var obj= $(recvMsgHtml);
    obj.find('img').on('dblclick',function(){
    	showBigImg(this);
    });
    return obj;
}
function onSendMsg(text,chat){
	var now = new Date();
	var time = now.getMonth()+1+'-'+now.getDate()+' ' + now.getHours()+':'+now.getMinutes() + ':'+now.getSeconds();
	$('#msgContainer_'+chat.contactId).append(buildSentMessage(text,time,'',my_uid));	
}

function notifyNewChat(contactId,msgCount){
	var jmsgCount = $('#lxr_'+contactId).find('.new_msg_count');
	if(msgCount){
		jmsgCount.text(msgCount);
	}else{
		//未读消息数量++
		var num_msg_count = 0;
		if(jmsgCount.text()!=''){
			num_msg_count = Number.parseInt(jmsgCount.text());
		}
		num_msg_count++;
		jmsgCount.text(num_msg_count);
	}
	
	jmsgCount.addClass('cocoWinNewsNum');

//	$('.user_avatar_img_'+contactId).addClass('doudong');

}

function onReceiveMsg(msg){
	
	var data = jQuery.parseJSON(msg);
	if(data.type=='user_status'){
		setUserStatus(data);
		return;
	}
	
	if(web_plugin_message_callback){
		web_plugin_message_callback(data);
	}
	
	// 判断是否新会话
	var chat = $('#chat_'+data.senderId);
	if(chat.length==0){
		//给消息提醒
		notifyNewChat(data.senderId);
		return;
	}
    $('#msgContainer_'+data.senderId).append(buildRecvMessage(data.senderAvatar,data.msg , data.sendtime,'' ,data.senderId));

    //判断是否当前会话
    var curr = getCurrentChat();
    if(curr.contactId!=data.senderId){
    	//未读消息数量++
    	var jmsgCount = $('#chat_'+data.senderId).find('.new_msg_count');
    	var num_msg_count = 0;
    	if(jmsgCount.text()!=''){
    		num_msg_count = Number.parseInt(jmsgCount.text());
    	}
    	num_msg_count++;
		jmsgCount.text(num_msg_count);
		jmsgCount.addClass('cocoWinNewsNum');
    }else{
    	//滚动到最新消息
    	scrollToLatestNews();
    }

    //设置已读,TODO,注意，此处可能要调整
    setSigleChatRead(data.senderId);
    
    data.status=1;
    setUserStatus(data);
    //
}

function scrollToLatestNews(){
	var chat = getCurrentChat();
	var msgContainer;
	msgContainer = $('#msgContainer_'+chat.contactId);
	msgContainer.scrollTop(9999999);
}
function getCurrentChat(){
	var li = $('.now');
	var chat = jQuery.parseJSON('{}');
	chat.contactId=li.attr('cid');
	chat.type=li.attr('type');
	chat.contactName=li.attr('cname');
	//好友的头像
	chat.avatar=li.attr('avatar');
	chat.currentPageNo = $('#msgContainer_'+chat.contactId).attr('currentPageNo');
	
	return chat;
}

function nextPage(){
	var chat = getCurrentChat();
	var currentPageNo = Number.parseInt(chat.currentPageNo);
	currentPageNo++;
	
	loadHistory(chat.contactId,currentPageNo);
	$('#msgContainer_'+chat.contactId).attr('currentPageNo',currentPageNo);
	
}

function setUserStatus(json){
	if(json.status==0){
		//离线
		$('.user_avatar_img_'+json.senderId).addClass('user_offline_filter');
	}else if(json.status==1){
		// 在线
		$('.user_avatar_img_'+json.senderId).removeClass('user_offline_filter');
	}
	try{
		console.log(json.contactName+'状态: '+json.status);
	}catch(e){
		
	}
	//lxrzaixian('cocoList');
}

function msgAreaKeyup(event){
	if(event.keyCode==13 && event.ctrlKey){
		send();
	}
}




function closeChat(contactId){
	if($('.cocoWinLxrList li').length<=0){
		//closeBox(closeAllChat);
		return;
	}
	var next;
	next = $('#chat_'+contactId).next();
	$('#chat_'+contactId).remove();
	$('#msgContainer_'+contactId).remove();	
	//如果删除的是当前聊天，重新选择下一个聊天为当前聊天，如果没有其他聊天，关闭聊天面板
	event.cancelBubble=true;
	if(next.length>0){
		selectChat(next.attr('cid'));	
	}else{
		window.top.webchat_dialog.hide();
	}
	removeWebRecentContact(contactId);
}

function removeWebRecentContact(contactId){
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: 'c/removeRecentContact?contactId='+contactId,
	    success:function(data){
	    }
	  });
}

function startChangeName(){
	$('#user_name_div').css('display','none');
	$('#user_name_input').css('display','');
	$('#user_name_input').val($('#user_name_div').text());
}

function endChangeName(){
	$('#user_name_div').css('display','');
	$('#user_name_input').css('display','none');
	$('#user_name_div').text($('#user_name_input').val());
	var a = jQuery.parseJSON('{}');
	a.name=$('#user_name_input').val();
	$.ajax({
		type: 'get',
		dataType: 'json',
		data:a,
		url: 'c/im/setUserName',
		success:function(data){

		}
	});
}

