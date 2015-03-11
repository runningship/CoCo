var connection = null;
var terminated=false;
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
	    	  console.log('掉线了');
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
}

function startConnection(){
	
}
function nextRound(){
	var data = jQuery.parseJSON('{}');
	data.type="ping";
	sendByBosh(data);
}
