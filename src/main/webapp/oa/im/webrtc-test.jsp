<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/js/jquery.js"></script>
<script src="http://cdn.peerjs.com/0.3/peer.min.js"></script>
<script type="text/javascript">
var peer;
$(function(){
    peer = new Peer({key: 'phmngu2vbntxogvi'});
    peer.on('open', function(id) {
        $('#peerId').text(id);
    });
});

function connectTo(peerId){
    if(!peer){
        return;
    }
    var conn = peer.connect(peerId);
    conn.on('open', function() {
        $('#msg').text("成功连接到 "+peerId);
      // Receive messages
      conn.on('data', function(data) {
        console.log('Received', data);
      });

      // Send messages
      // conn.send('Hello!');
    });
}

function sendFile(filepath){

}
</script>

</head>
<body>
<div>我的id是 <span id="peerId"></span></div>
<input /> <button>连接</button>
<input type="file"/><button>发送</button>
<div id="msg"></div>
</body>
</html>