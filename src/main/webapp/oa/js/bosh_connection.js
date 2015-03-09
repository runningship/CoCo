var connection = null;

//send('type=msg&xx='+$('#jid').val()+'&bb=22');
function sendByBosh(data)
{
	data.myUid=my_uid;
	data = 'json='+encodeURIComponent(JSON.stringify(data));
	$.ajax({
	      type: 'post',
	      url: 'bosh',
	      data: data,
	      success: function(data){
	          if('new_connection_received'==data){
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
	          
	      }
    });
}

function startBosh(){
	nextRound();
}

function startConnection(){
	
}
function nextRound(){
	var data = jQuery.parseJSON('{}');
	data.type="ping";
	sendByBosh(data);
}
