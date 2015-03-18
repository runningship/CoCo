var coco_ws;
var my_avatar;
var my_name;
var my_uid;
var ue_text_editor;
var ws_url;
var unread_stack = [];
var chat_conts=[];
var default_avatar='157';
//web集成时要提供的来信息时的回调接口
var web_plugin_message_callback;
//web或者native,表示在web页面嵌入，还是客户端登录
var resource;
//http://pub.idqqimg.com/lib/qqface/0.gif
function openChat(contactId,contactName,avatar){
	if(!avatar){
		avatar=default_avatar;
	}
	//打开聊天面板
	showBox();
	//设置zIndex
	$("#layerBoxDj").css({"z-index":999});
	$('.chat_title').text(' '+contactName+' 聊天中');
	
	// 判断chat是否已经存在
	if($('#chat_'+contactId).length>0){
		// $('.cocoWinLxrList li').removeClass('now');
		// $('#chat_'+contactId).toggleClass('now');

		// $('.WinInfoListShowMainBox').css('display','none');
		// $('#msgContainer_'+contactId).css('display','');
		selectChat($('#chat_'+contactId));
		return;
	}
	//判断当前用户是否在线
	var xx = $('#user_avatar_'+contactId+' img');
	
	var imgFilterClass = "";
	if(xx.hasClass('user_status_filter_0')){
		imgFilterClass = 'user_status_filter_0';
	}
	// 添加联系人
	var lxrHtml=	'<li type="msg" avatar="'+avatar+'" cname="'+contactName+'" cid="'+contactId+'" id="chat_'+contactId+'" onclick="selectChat(this)">'
                    +   '<div  class="cocoWinLxrListTx Fleft"><img class="'+imgFilterClass+' user_avatar_img_'+contactId+'" src="chat/images/avatar/'+avatar+'.jpg" /></div>'
                    +   '<div class="cocoWinLxrListPerInfo Fleft">'
                    +   '   <p class="name">'+contactName+'</p>'
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
	
	//在select chat中加载历史聊天信息
//	selectChat($('#chat_'+contactId));

//	loadHistory(contactId,1);
	// $('.msgContainer').css('display','');
	// 清空当前联系人未读消息提醒

	//reCacuUnreadStack(contactId,'msg');
	//设置已读
//	setSigleChatRead(contactId);
	addRecentContact(contactId);
}

function openAndSelectChat(contactId,contactName,avatar){
	openChat(contactId , contactName , avatar);
	selectChat($('#chat_'+contactId));
}
function setSigleChatRead(contactId){
	$.ajax({
		type: 'get',
		dataType: 'json',
		url: 'c/im/setSingleChatRead?contactId='+contactId,
		success:function(data){
		}
	});
}

function setGroupChatRead(groupId){
	$.ajax({
		type: 'get',
		dataType: 'json',
		url: 'c/im/setGroupChatRead?groupId='+groupId,
		success:function(data){
		}
	});
}

function openGroupChat(groupId,groupName){
	//打开聊天面板
	showBox();
	$('.chat_title').text(' '+groupName+' 聊天中');
	// 判断chat是否已经存在
	if($('#group_chat_'+groupId).length>0){
		// $('.cocoWinLxrList li').removeClass('now');
		// $('#group_chat_'+groupId).toggleClass('now');

		// $('.WinInfoListShowMainBox').css('display','none');
		// $('#msgContainer_group_'+groupId).css('display','');
		selectChat($('#group_chat_'+groupId),groupId);
		return;
	}
	// 添加联系人
	var lxrHtml=	'<li type="groupmsg" cname="'+groupName+'" cid="'+groupId+'" id="group_chat_'+groupId+'" onclick="selectChat(this,\''+groupId+'\')">'
                    +   '<div  class="qunTx Fleft">'+$('#group_avatar_'+groupId).html()+'</div>'
                    +   '<div class="cocoWinLxrListPerInfo Fleft">'
                    +   '   <p class="name">'+groupName+'</p>'
                    +   '</div>'
                    +	'<div class="new_msg_count"></div>'
					+	'<div class="msgClose" onclick="closeChat('+groupId+','+groupId+')">×</div>'
                    +'</li>';
	
	// 添加聊天内容窗口
	var msgContainer = '<div currentPageNo="1" class="WinInfoListShowMainBox" style="width:78%" id="msgContainer_group_'+groupId+'">'
						+'<a href="#" class="msg_more" onclick="nextPage();">查看更多消息</a>'
						+'</div>';
	$('.WinInfoListShowMainBox').css('display','none');
	$('.cocoWinInfoListShow').prepend(msgContainer);
	
	// 显示最新聊天
	// $('.cocoWinLxrList li').removeClass('now');
	$('.cocoWinLxrList').prepend(lxrHtml);
	//selectChat($('#group_chat_'+groupId),groupId);

	loadGroupHistory(groupId,1);

	//添加组成员窗口
	$('.qunBox .qunBoxList').append('<ul id="group_members_'+groupId+'"></ul>');

	// 显示群成员窗口
	$('.qunBox').css('display','');
	$('.qunBox ul').css('display','none');
	$('#group_members_'+groupId).css('display','');
	//清空消息提醒
	var jmsgCount = $('#group_'+groupId).find('.new_msg_count');
	jmsgCount.text('');
	jmsgCount.removeClass('cocoQunNewsNum');

	//reCacuUnreadStack(groupId,'group');

	//设置已读
	//setGroupChatRead(groupId);
	//加载组成员
	loadGroupMembers(groupId);
	
	addRecentContact(groupId , 'group');
}

function loadGroupMembers(groupId){
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: 'c/im/getGroupMembers?groupId='+groupId,
	    success:function(data){
	    	//build group members
	    	buildGroupMembers(groupId , data.members);
	    }
	  });
}

function buildGroupMembers(groupId,members){
	var ul = $('#group_members_'+groupId);
	ul.empty();
	for(var i=0;i<members.length;i++){
		var mem = members[i];
		var avatar = default_avatar;
		if(mem.avatar){
			avatar = mem.avatar;
		}
		ul.append('<li><div class="qunTxImg Fleft"><img class="user_avatar_img_'+mem.uid+'" src="chat/images/avatar/'+avatar+'.jpg"></div><div class="qunLxrInfo Fleft"><p class="name">'+mem.uname+'</p></div></li>');
	}
}

function loadGroupHistory(groupId , currentPageNo){
  $.ajax({
    type: 'get',
    dataType: 'json',
    url: 'c/im/getGroupHistory?groupId='+groupId+'&currentPageNo='+currentPageNo,
    success:function(data){
    	if(data.history.length<10){
    		$('#msgContainer_group_'+groupId+' .msg_more').css('display','none');
    	}
    	buildHistory(data.history,groupId);
    }
  });
}

function loadHistory(contactId , currentPageNo){
  $.ajax({
    type: 'get',
    dataType: 'json',
    url: 'c/im/getHistory?contactId='+contactId+'&currentPageNo='+currentPageNo,
    success:function(data){
    	buildHistory(data.history);
    	if(data.history.length<10){
    		$('#msgContainer_'+contactId+' .msg_more').css('display','none');
    	}
    }
  });
}

function buildHistory(history,groupId){
	var chat = getCurrentChat();
	var offsetTop = 0;
	for(var i=0;i<history.length;i++){
		var msg = history[i];
		var container;
		var senderName;
		if(groupId){
			container = $('#msgContainer_group_'+groupId);
			senderName = getContactNameByUid(msg.senderId);
		}else{
			container = $('#msgContainer_'+chat.contactId);
		}
		
		if(msg.senderId==my_uid){
			//我发送的消息
			var html = buildSentMessage(msg.conts,msg.sendtime);
			container.prepend(html);
		}else{
			var senderAvatar = getAvatarByUid(msg.senderId);
			var html = buildRecvMessage(senderAvatar,msg.conts , msg.sendtime , senderName);
			// var html = "";
			// if(groupId){
			// 	buildRecvMessage(msg.senderId,msg.conts , msg.sendtime);
			// }else{
			// 	buildRecvMessage(chat.avatar,msg.conts , msg.sendtime);
			// }
			
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
function getGroupNameByGid(gid){
	var name = $('#group_name_'+gid);
	var tmp = name.text();
	if(tmp){
		return tmp.split('(')[0].trim();
	}else{
		return name.text();	
	}
	
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
	coco_ws.send(JSON.stringify(chat));
}
function selectChat(li,groupId){
	//保存当前窗口内容
	selBoxCge('outList');
	var conts = "";
	if(UE.Editor.body){
		conts = ue_text_editor.getContent();
	}
	var oldChat = $('.now');
	if(oldChat.length>0){
		if(oldChat.attr('type')=='groupmsg'){
			pushChatConts(oldChat.attr('cid') , 'group',conts);
		}else{
			pushChatConts(oldChat.attr('cid'),'msg',conts);
		}	
	}
	

	var msgContainer;
	if(groupId){
		 msgContainer = $('#msgContainer_group_'+groupId);
	}else{
		 msgContainer = $('#msgContainer_'+$(li).attr('cid'));
	}
	$('.cocoWinLxrList li').removeClass('now');
	$(li).toggleClass('now');
	if(unread_stack.length<=0){
		$('.chat_title').text(' '+$(li).find('p').text()+' 聊天中');	
	}
	
	$('.WinInfoListShowMainBox').css('display','none');
	
	msgContainer.css('display','');

	if(groupId){
		$(li).find('.new_msg_count').removeClass('cocoQunNewsNum').text('');
		$('#group_'+$(li).attr('cid')).find('.new_msg_count').removeClass('cocoQunNewsNum').text('');
	}else{
		$(li).find('.new_msg_count').removeClass('cocoWinNewsNum').text('');
		$('#lxr_'+$(li).attr('cid')).find('.new_msg_count').removeClass('cocoWinNewsNum').text('');
	}
	
	$('#msg_textarea').focus();
	scrollToLatestNews();

	//显示组成员窗口
	if(groupId){
		$('.qunBox').css('display','');
		$('.qunBox ul').css('display','none');
		$('#group_members_'+groupId).css('display','');
	}else{
		$('.qunBox').css('display','none');
	}

	//切换消息窗口内容
	var oldConts = "";
	if(groupId){
		oldConts = getChatConts(groupId, 'group');
	}else{
		oldConts = getChatConts($(li).attr('cid'), 'msg');
	}
	if(UE.Editor.body){
		ue_text_editor.setContent(oldConts);
	}
	if(!msgContainer.attr('hasLoadHistory')){
		loadHistory($(li).attr('cid') , 1);
		msgContainer.attr('hasLoadHistory',true);
	}
	if(!groupId){
		$('.user_avatar_img_'+$(li).attr('cid')).removeClass('doudong');
		setSigleChatRead($(li).attr('cid') );
		reCacuUnreadStack($(li).attr('cid'),'msg');
	}else{
		setGroupChatRead(groupId);
		reCacuUnreadStack(groupId,'group');
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
	var json = JSON.parse('{}');
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
function buildSentMessage(text,time , senderName){
	var senderHtml = "";
	if(senderName){
		senderHtml = '<div class="lxrName pright">'+senderName+'</div>';
	}
	var sentMsgHtml='<div class="WinInfoListAppend">'
                    +     '<div class="txImgRight"><img src="chat/images/avatar/'+my_avatar+'.jpg" /></div>'
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
	
	$("#imgBigSee").remove();
	var htmlText = "<div id='imgBigSee' onclick='$(this).remove()' style='display:block; position:absolute; background-color:#ffffff; z-index:9999999999; box-shadow:#666 0px 0px 10px; border-radius:3px; font-family:'宋体'; background-color:#ffffff;'>"+ img.outerHTML +"</div>";
	$("body").append(htmlText);
	layerBoxCenter("imgBigSee");
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

function buildRecvMessage(senderAvatar , msg , time , senderName){
	var senderHtml="";
	if(senderName){
		senderHtml='<span class="lxrName pleft">'+senderName+'</span>';
	}
	var recvMsgHtml='<div class="WinInfoListAppend">'
                    +     '<div class="txImg"><img src="chat/images/avatar/'+senderAvatar+'.jpg" /></div>'
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
	if(chat.type=='groupmsg'){
		$('#msgContainer_group_'+chat.contactId).append(buildSentMessage(text,time));
	}else{
		$('#msgContainer_'+chat.contactId).append(buildSentMessage(text,time));	
	}
    
}

function notifyGroupNews(groupId,msgCount){
	if(msgCount==0){
		return;
	}
	var group_msgCount = $('#group_'+groupId).find('.new_msg_count');
	//同时提醒到recent chat
	var chat_msgCount = $('#group_chat_'+groupId).find('.new_msg_count');
	if(group_msgCount){
		group_msgCount.text(msgCount);
		chat_msgCount.text(msgCount);
	}else{
		var num_msg_count = 0;
		if(group_msgCount.text()!=''){
			num_msg_count = Number.parseInt(jmsgCount.text());
		}
		num_msg_count++;
		group_msgCount.text(num_msg_count);
		chat_msgCount.text(num_msg_count);
	}
	
	group_msgCount.addClass('cocoQunNewsNum');
	chat_msgCount.addClass('cocoQunNewsNum');
	
	//判断是否给小红点
	if($('.sle #qunbox_dot').length==0){
		$('#qunbox_dot').addClass('alertDot');
	}
	// $('#user_avatar_img_'+groupId).addClass('doudong');
	requestWindowAttention(groupId,'group');
}
function notifyNewChat(contactId,msgCount , contactName){
	var lxr_msgCount = $('#lxr_'+contactId).find('.new_msg_count');
	var chat_msgCount = $('#chat_'+contactId).find('.new_msg_count');
	if(msgCount){
		lxr_msgCount.text(msgCount);
		//同时通知到recent chat 中
		chat_msgCount.text(msgCount);
	}else{
		//未读消息数量++
		var num_msg_count = 0;
		if(lxr_msgCount.text()!=''){
			num_msg_count = Number.parseInt(jmsgCount.text());
		}
		num_msg_count++;
		lxr_msgCount.text(num_msg_count);
		chat_msgCount.text(num_msg_count);
	}
	
	lxr_msgCount.addClass('cocoWinNewsNum');
	chat_msgCount.addClass('cocoWinNewsNum');

	$('.user_avatar_img_'+contactId).addClass('doudong');

	requestWindowAttention(contactId,'msg' , contactName);
}

function addRecentContact(senderId,type){
	//msg中有联系人的所有信息
	//添加到数据库
	if(!type){
		type="";
	}
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: 'c/addRecentContact?contactId='+senderId+'&type='+type,
	    success:function(data){
	    }
	  });
	
	//添加界面元素
}

function removeRecentContact(contactId){
	//msg中有联系人的所有信息
	//添加到数据库
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: 'c/removeRecentContact?contactId='+contactId,
	    success:function(data){
	    }
	  });
	
	//添加界面元素
}

function onReceiveMsg(msg){
	
	var data = JSON.parse(msg);
	if(data.type=='user_status'){
		setUserStatus(data);
		return;
	}
	if(data.type=="groupmsg"){
		onReceiveGroupMsg(msg);
		return;
	}
	var currentChat = getCurrentChat();
	//只要不是在和当前人聊天就提醒
	if(currentChat.contactId!=data.senderId){
		requestFoxBarAttention(data.senderId,'msg',data.senderName);
	}
	
	//判断是否在联系人列表和最近联系人列表中
	var sender = getContactNameByUid(data.senderId);
	if(!sender){
		//不在联系列表
		sender = getRecentContactNameByUid(data.senderId);
		if(!sender){
			//不在最近联系人列表中,添加最近联系人
			addRecentContact(data.senderId);
		}
	}
	
	// 判断是否新会话
	var chat = $('#chat_'+data.senderId);
	if(chat.length==0){
		//给消息提醒
		notifyNewChat(data.senderId ,'', data.senderName);
		return;
	}
    $('#msgContainer_'+data.senderId).append(buildRecvMessage(data.senderAvatar,data.msg , data.sendtime));

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

    
    //此处不应该设置已读,所有已读要有select chat触发
    //setSigleChatRead(data.senderId);
}

function onReceiveGroupMsg(msg){
	//判断是否给小红点
	if($('.sle #qunbox_dot').length==0){
		$('#qunbox_dot').addClass('alertDot');
	}
	

	var data = JSON.parse(msg);
	//只要不是在和当前人聊天就提醒
	var currentChat = getCurrentChat();
	if(currentChat.contactId!=data.senderId){
		requestFoxBarAttention(data.contactId,'group');
	}
	// 判断是否新会话
	var chat = $('#group_chat_'+data.contactId);
	if(chat.length==0){
		//给消息提醒
		notifyGroupNews(data.contactId);
		return;
	}
    $('#msgContainer_group_'+data.contactId).append(buildRecvMessage(data.senderAvatar,data.msg , data.sendtime , data.senderName));

    //判断是否当前会话
    var curr = getCurrentChat();
    if(curr.contactId!=data.contactId){
    	//未读消息数量++
    	var jmsgCount = $('#group_chat_'+data.contactId).find('.new_msg_count');
    	var num_msg_count = 0;
    	if(jmsgCount.text()!=''){
    		num_msg_count = Number.parseInt(jmsgCount.text());
    	}
    	num_msg_count++;
		jmsgCount.text(num_msg_count);
		jmsgCount.addClass('cocoWinNewsNum');
    }else{
    	//是当前会话
    	//滚动到最新消息
    	scrollToLatestNews();
    }
    
}

function scrollToLatestNews(){
	var chat = getCurrentChat();
	var msgContainer;
	if(chat.type=='groupmsg'){
		msgContainer = $('#msgContainer_group_'+chat.contactId);
	}else{
		msgContainer = $('#msgContainer_'+chat.contactId);
	}
	msgContainer.scrollTop(9999999);
}
function getCurrentChat(){
	var li = $('.now');
	var chat = JSON.parse('{}');
	chat.contactId=li.attr('cid');
	chat.type=li.attr('type');
	chat.contactName=li.attr('cname');
	//好友的头像
	chat.avatar=li.attr('avatar');
	if(chat.type=='groupmsg'){
		chat.currentPageNo = $('#msgContainer_group_'+chat.contactId).attr('currentPageNo');
	}else{
		chat.currentPageNo = $('#msgContainer_'+chat.contactId).attr('currentPageNo');	
	}
	
	return chat;
}

function nextPage(){
	var chat = getCurrentChat();
	var currentPageNo = Number.parseInt(chat.currentPageNo);
	currentPageNo++;
	
	if(chat.type=='groupmsg'){
		loadGroupHistory(chat.contactId,currentPageNo);
		$('#msgContainer_group_'+chat.contactId).attr('currentPageNo',currentPageNo);
	}else{
		loadHistory(chat.contactId,currentPageNo);
		$('#msgContainer_'+chat.contactId).attr('currentPageNo',currentPageNo);	
	}
	
}

function setUserStatus(json){
	var statusDom = $('.user_status_'+json.contactId);
	if(json.status==0){
		//离线
		statusDom.removeClass('cocoOnline');
		statusDom.addClass('cocoLeave');
		$('.user_avatar_img_'+json.contactId).addClass('user_offline_filter');
	}else if(json.status==1){
		// 在线
		statusDom.removeClass('cocoLeave');
		statusDom.addClass('cocoOnline');
		$('.user_avatar_img_'+json.contactId).removeClass('user_offline_filter');
	}

	console.log(json.contactName+'状态: '+json.status);
	lxrzaixian('cocoList');
}

function msgAreaKeyup(event){
	if(event.keyCode==13 && event.ctrlKey){
		send();
	}
}

function getRecentChats(success){
	$.ajax({
		type: 'get',
		dataType: 'json',
		url: 'c/im/getRecentChats',
		success:function(data){
			if(data.recentChats){
				for(var i=0;i<data.recentChats.length;i++){
					var chat = data.recentChats[i];
					if(chat.type=='group'){
						openGroupChat(chat.uid, chat.name);
					}else{
						openChat(chat.uid,chat.name , chat.avatar);
					}
					
				}
				if(data.recentChats.length>0){
					var last = data.recentChats[data.recentChats.length-1];
					if(last.type='group'){
						selectChat($('#group_chat_'+last.uid) , last.uid );
					}else{
						selectChat($('#chat_'+last.uid));	
					}
				}
			}
			console.log(data);
			if(success){
				success();
			}
		}
	});
}
function getUnReadChats(){
	$.ajax({
		type: 'get',
		dataType: 'json',
		url: 'c/im/getUnReadChats',
		success:function(data){
			buildSingleChatUnreads(data.unReadSingleChats);
			buildGroupChatUnreads(data.unReadGroupChats);
			//lxrzaixian('cocoList');
		}
	});
}

function buildSingleChatUnreads(unReads){
	if(unReads){
		for(var i=0;i<unReads.length;i++){
			notifyNewChat(unReads[i].senderId,unReads[i].total , unReads[i].contactName);
		}	
	}
}
function buildGroupChatUnreads(unReads){
	if(unReads){
		for(var i=0;i<unReads.length;i++){
			notifyGroupNews(unReads[i].groupId,unReads[i].total , unReads[i].groupName);
		}
	}
	
}

function closeChat(contactId,groupId){
	if($('.cocoWinLxrList li').length<=1){
		closeBox(closeAllChat);
		return;
	}
	var next;
	if(groupId){
		next = $('#group_chat_'+groupId).next();
		$('#group_chat_'+groupId).remove();
		$('#msgContainer_group_'+groupId).remove();
		
	}else{
		next = $('#chat_'+contactId).next();
		$('#chat_'+contactId).remove();
		$('#msgContainer_'+contactId).remove();	
		
	}
	
	//如果删除的是当前聊天，重新选择下一个聊天为当前聊天，如果没有其他聊天，关闭聊天面板
	event.cancelBubble=true;
	if(next.length>0){
		if(next.attr('type')=='groupmsg'){
			selectChat(next[0],next.attr('cid'));
		}else{
			selectChat(next[0]);	
		}
	}
	
	//remove recent chat
	removeRecentContact(contactId);
}
function closeAllChat(){
	$('.cocoWinLxrList').empty();
	$('.cocoWinInfoListShow').empty();
	$('.chat_title').text('COCO 聊天');
}

function reCacuUnreadStack(senderId,type){
	var pos;
	for(var i=0;i<unread_stack.length;i++){
		var tmp = unread_stack[i];
		if(tmp.type==type && tmp.senderId==senderId){
			pos = i;
		}
	}
	if(pos!=undefined){
		unread_stack = unread_stack.splice(pos+1,1);
	}
	//检查还有没有新消息
	if(unread_stack.length>0){
		var json2 = unread_stack[unread_stack.length-1];
		$('.cocoNews span').text(json2.senderName+" 的新消息");
	}else{
		$('.cocoNews').removeClass('cocoNewsAlert');
	}
}

function recoverChatPanel(){
	if(unread_stack.length>0){
		var json = unread_stack.pop();
		if(json.type=='msg'){
		 	openChat(json.senderId,json.senderName , getAvatarByUid(json.senderId));
		 	selectChat($('#chat_'+json.senderId));
		}else{
			openGroupChat(json.senderId , json.senderName);
		}
		//检查还有没有新消息
		if(unread_stack.length>0){
			var json2 = unread_stack[unread_stack.length-1];
			$('.cocoNews span').text(json2.senderName+" 的新消息");
		}else{
			$('.cocoNews').removeClass('cocoNewsAlert');
		}
		//$("#layerBoxDj").css({"z-index":artDialog.defaults.zIndex++});
	}else{
		showBox();
//		if($('.cocoWinLxrList li').length>0){
//			if($("#layerBoxDj").css("display")=='none'){
//				showBox();
////				$("#layerBoxDj").css({"z-index":artDialog.defaults.zIndex++});
//			}else{
//				closeBox();
//			}
//		}
	}
	
}

function showCoco(){
    $('.cocoMain').toggleClass('hide');
}

function showFacePanel(){
	$('#facePanel').css('display','');
}

function appendFaceToMsg(face){
	var url = "http://forum.csdn.net/PointForum/ui/scripts/csdn/Plugin/001/face/"+face+".gif";
	$('#msg_textarea').val($('#msg_textarea').val()+url);
	$('#facePanel').css('display','none');
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
	var a = JSON.parse('{}');
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

function selectAvatar(i){
	my_avatar = i;
	$('#avatarId').attr('src','chat/images/avatar/'+i+'.jpg');
    $.ajax({
      type: 'POST',
      url: 'c/im/setAvatar?avatarId='+i,
      success: function(data){
          alert('操作成功');
      }
    });
}

function requestFoxBarAttention(senderId , type , contactName){
	//判断是否重复
	for(var i=0;i<unread_stack.length;i++){
		var tmp = unread_stack[i];
		if(tmp.type==type && tmp.senderId==senderId){
			return;
		}
	}
	var senderName = "";
	if(contactName){
		senderName=contactName;
	}else{
		if('msg'==type){
			senderName = getContactNameByUid(senderId);	
		}else{
			senderName = getGroupNameByGid(senderId);
		}
	}
	
	var json = JSON.parse('{}');
	json.senderId = senderId;
	json.senderName = senderName;
	json.type = type;
	unread_stack.push(json);
	//判断是否在最小化栏闪动
	// if($('.cocoMain').hasClass('hide')){
		$('.cocoNews').addClass('cocoNewsAlert');
		$('.cocoNews span').text(senderName+' 的新消息');
	// }
}
function requestWindowAttention(senderId , type, senderName){
	requestFoxBarAttention(senderId,type , senderName);
	try{
		var win = gui.Window.get();
		if(!win.isFocus){
			win.title="您有新的消息";
			win.requestAttention(true);
		}
	}catch(e){

	}

}
var retry;
function connectWebSocket(){
	retry=null;
    if(web_socket_on){
        return;
    }
    
    coco_ws = new WebSocket(ws_url);
    console.log('正在连接...');
    coco_ws.onopen = function() {
        web_socket_on = true;
        console.log('登录成功');
        $('#avatarId').removeClass('user_offline_filter');
    };
    coco_ws.onclose = function() {
        web_socket_on = false;
        console.log('we are getting offline');
        //掉线重连
        if(!retry){
        	retry = setTimeout(connectWebSocket,10*1000);	
        }
        
        $('#avatarId').addClass('user_offline_filter');
    };
    coco_ws.onmessage = function(e) {
        console.log('收到消息:'+e.data);
        onReceiveMsg(e.data);
    };
    coco_ws.onerror = function(e) {
    	web_socket_on = false;
        console.log('连接失败:10秒后重新连接.'+e);
        //掉线重连
        if(!retry){
        	retry = setTimeout(connectWebSocket,10*1000);	
        }
        $('#avatarId').addClass('user_offline_filter');
    };
}

function heartBeat(){
	if(coco_ws && web_socket_on){
		coco_ws.send("ping");
	}
	setTimeout(heartBeat,300*1000);
}
