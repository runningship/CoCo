
var retry;
var web_socket_on=false;
var ws_url;
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