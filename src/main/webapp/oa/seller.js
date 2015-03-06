
$(function(){
	webchat_dialog = art.dialog.open('webchat.jsp', {title: '' ,width: 661, height: 562,
		left:'auto',
		init: function () {
	    	iframe = this.iframe.contentWindow;
	    	iframe.web_plugin_message_callback = onMessage;
	    	//自动登录coco
    		if(token){
    			iframe.auth(token,function(){
    				//成功
    				$('#coco').removeClass('user_offline_filter');
    			});	
    		}
	    	resizeDialog();
		},
		close:function(){
			webchat_dialog.hide();
			web_dialog_show=false;
			return false;
		}
	});
	webchat_dialog.hide();
	
});

function getWebUnReadChats(){
	$.ajax({
		type: 'get',
		dataType: 'json',
		url: '/c/im/getWebUnReadChats',
		success:function(data){
			//和收到新消息一样处理
			//iframe.openChats(data.unReadChats);
			unReadChats = data.unReadChats;
			shandong();
		}
	});
}
function onMessage(msg){
	//把消息保存在消息盒子中,闪动提醒,点击闪动图标时，才打开消息盒子中的所有信息
	if(!iframe.isContactOpen(msg.senderId)){
		message_box.push(msg);
	}
	notifyNewMessage(msg);
}

function notifyNewMessage(msg){
	//会话没有打开，或者聊天窗口关闭状态下，给闪动提醒
	if(!iframe.isContactOpen(msg.senderId) || web_dialog_show==false){
		shandong();
	}
}
function shandong(){
	if(!shan_dong_interval){
		shan_dong_interval = setInterval(function(){
			if(allow_coco_doudong==false){
				$('#coco').removeClass('hide');	
			}else{
				$('#coco').toggleClass('hide');	
			}
			
		},300);
	}
}
function openChat(){
	if(!token){
		//go to login
		return;
	}
	clearInterval(shan_dong_interval);
	shan_dong_interval=null;
	if(!iframe.valid){
		//登录coco...
		iframe.auth(token,function(){
			//登录成功
			doOpenChat();	
		} , function(){
			//登录失败
		});
	}else{
		for(var i = 0;i<message_box.length;i++){
			var msg = message_box[i];
			iframe.openContact(msg.senderId,msg.senderName,msg.senderAvatar);
		}
		
		doOpenChat();
	}
	
}

function doOpenChat(){
	webchat_dialog.show();
	webchat_dialog.size(861, 562);
	$('iframe').css('display','');
	web_dialog_show= true;
	iframe.selectFirstChatIfNoOneSelected();
}

function updateTitle(title){
	if(webchat_dialog){
		webchat_dialog.title(title);	
	}
}
