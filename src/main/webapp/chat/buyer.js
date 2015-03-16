
var web_chat_inited;
var dialog_width=561;
var dialog_height=362;
$(function(){

 //init();
	if(token){
		init();
	}
	
	
});
function init(callback){
	if(web_chat_inited){
		if(callback){
			callback();
		}
		return;
	}
	web_chat_inited = true;
	webchat_dialog = art.dialog.open('webchat.jsp', {title: '' ,width: dialog_width, height: dialog_height,
		init: function () {
	    	iframe = this.iframe.contentWindow;
	    	iframe.web_plugin_message_callback = onMessage;
	    	if(token){
	    		iframe.auth(token,function(){
	    			if(callback){
	    	    		callback();
	    	    	}
	    		});
	    	}
	    	//resizeDialog();
	    	
		},
		close:function(){
			webchat_dialog.hide();
			web_dialog_show = false;
			return false;
		}
	});
	webchat_dialog.hide();
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
function openChat(contactId , name ,avatar){
	if(!token){
		//go to login
		return;
	}
	clearInterval(shan_dong_interval);
	shan_dong_interval=null;
	init(function(){
		clearInterval(shan_dong_interval);
		shan_dong_interval=null;
		if(!iframe.valid){
			//登录coco...
			iframe.auth(token,function(){
				//登录成功
				$('#coco').removeClass('user_offline_filter');
				doOpenChat(contactId , name ,avatar);	
			} , function(){
				//登录失败
			});
		}else{
			for(var i = 0;i<message_box.length;i++){
				var msg = message_box[i];
				iframe.openContact(msg.senderId,msg.senderName,msg.senderAvatar);
			}
			doOpenChat(contactId , name ,avatar);
		}
	});
	
	
	
}

function doOpenChat(contactId , name ,avatar){
	var top = (window.screen.height-dialog_height)/2+$(document).scrollTop()/2;
	$('.aui_state_focus').css('top',top+'px');
	//webchat_dialog.position(left,top);
	webchat_dialog.show();
	webchat_dialog.size(dialog_width,dialog_height);
	$('iframe').css('display','');
	iframe.openContact(contactId ,name , avatar);
	//select chat
	if(contactId){
		iframe.select_chat(contactId);
	}else{
		iframe.selectFirstChatIfNoOneSelected();
	}
	web_dialog_show= true;
	resizeDialog();
}

function updateTitle(title){
	if(webchat_dialog){
		webchat_dialog.title(title);
	}
}

function cocoFail(){
	$('#coco').addClass('user_offline_filter');
}