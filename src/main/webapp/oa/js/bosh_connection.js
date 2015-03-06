var connection = null;

//send('type=msg&xx='+$('#jid').val()+'&bb=22');
function sendByBosh(data)
{
	data.myUid=my_uid;
	console.log('send:'+JSON.stringify(data));
	data = 'json='+JSON.stringify(data);
	$.ajax({
	      type: 'post',
	      url: 'bosh',
	      data: data,
	      success: function(data){
	          console.log(data);
	          if('new_connection_received'==data){
	        	  return;
	          }
	          if('next_round'==data || ''==data){
	        	  setTimeout(function(){
		    		  nextRound();
	    		  },0);
	        	  return;
	          }
	          onReceiveMsg(data);
	      }
    });
}

function startBosh(){
	nextRound();
}

function startConnection(){
	
}
function nextRound(){
	var data = JSON.parse('{}');
	data.type="ping";
	sendByBosh(data);
}
