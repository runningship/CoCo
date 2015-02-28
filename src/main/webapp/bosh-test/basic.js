var BOSH_SERVICE = 'http://localhost:8081/bosh/'
var connection = null;

function log(msg) 
{
    $('#log').append('<div></div>').append(document.createTextNode(msg));
}

function rawInput(data)
{
    log('RECV: ' + data);
}

function rawOutput(data)
{
    log('SENT: ' + data);
}

function send(data)
{
	
	$.ajax({
	      type: 'post',
	      url: '/bosh',
	      data: data,
	      success: function(data){
	          console.log(data);
	          if('new_connection_received'==data){
	        	  return;
	          }
	          setTimeout(function(){
	    		  nextRound();
    		  },0);
	      }
    });
}

function nextRound(){
	send('type=ping');
	
}
$(document).ready(function () {

    $('#connect').bind('click', function () {
		send('type=msg&xx='+$('#jid').val()+'&bb=22');
    });
});