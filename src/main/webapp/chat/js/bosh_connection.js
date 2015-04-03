var connection = null;
var terminated=false;
var lastActiveTime;
//send('type=msg&xx='+$('#jid').val()+'&bb=22');
function sendByBosh(data)
{
	data.myUid=my_uid;
	data.resource = resource;
	try{
		console.log('send:'+JSON.stringify(data));
	}catch(e){
		
	}
	
	data = 'json='+encodeURIComponent(JSON.stringify(data));
	$.ajax({
	      type: 'post',
	      url: 'bosh',
	      data: data,
	      success: function(data){
	    	  try{
	    		  console.log('data='+data);
	    	  }catch(e){
	    		  
	    	  }
	          if('finished'==data || 'finishedfinished'==data){
	        	  //老的请求没有结束，又有了新的请求
	        	  return;
	          }
	          if('next_round'==data || ''==data){
	        	  setTimeout(function(){
		    		  nextRound();
	    		  },0);
	        	  return;
	          }else{
	        	  onReceiveMsg(data);
		          setTimeout(function(){
		    		  nextRound();
	    		  },0);  
	          }
	          
	      },
	      error:function(data){
	    	  setTimeout(function(){
	    		  nextRound();
    		  },30*1000);
	      }
    });
}

function startBosh(){
	var data = jQuery.parseJSON('{}');
	data.type="open";
	sendByBosh(data);
	//heartBeat();
}

function nextRound(){
	var data = jQuery.parseJSON('{}');
	data.type="ping";
	sendByBosh(data);
}

function heartBeat(){
	setTimeout(function(){
		var now = new Date().getTime();
		if(now-lastActiveTime>=65*1000){
			//断网
			window.top.cocoFail();
			console.log('heart beat')
			nextRound();
		}
		heartBeat();
	},70*1000);
}
