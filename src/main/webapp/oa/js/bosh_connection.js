var connection = null;
var terminated=false;
//send('type=msg&xx='+$('#jid').val()+'&bb=22');
function sendByBosh(data)
{
	data.myUid=my_uid;
	data.resource = resource;
	console.log('send:'+JSON.stringify(data));
	data = 'json='+encodeURIComponent(JSON.stringify(data));
	$.ajax({
	      type: 'post',
	      url: 'bosh',
	      data: data,
	      success: function(data){
	    	  console.log('data='+data);
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
	      error:function(){
	    	  
	      }
    });
}

function startBosh(){
	var data = jQuery.parseJSON('{}');
	data.type="open";
	sendByBosh(data);
}

function startConnection(){
	
}
function nextRound(){
	var data = jQuery.parseJSON('{}');
	data.type="ping";
	sendByBosh(data);
}
