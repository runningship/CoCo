<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>有机会 - 叮铛</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link rel="stylesheet" href="chat/font/iconfont.css">
<script src="js/jquery.js" type="text/javascript"></script>
<link href="" rel="stylesheet">
<style>
html,body{ padding: 0; margin: 0;}
body{ border: 1px solid #DDD; overflow: hidden; background: #F5F5F5; font-family:'microsoft yahei' ; }
.body { padding: 0px 0px 0;}
.body .titbar{-webkit-app-region: drag;}
.body .titbar .titools{-webkit-app-region: no-drag; float: right;}
.body .titbar .titools a{ display: inline-block; height: 16px; line-height: 16px; width: 16px; text-align: center; color: #000; font-family: 'Helvetica Neue',Helvetica,Arial,'Hiragino Sans GB','WenQuanYi Micro Hei','Microsoft YaHei',sans-serif,'microsoft yahei'; text-decoration: none;opacity: 0.5; font-weight: bold;}
.body .titbar .titools a.btn_close:hover{opacity: 1;}

.body .titbar h2{ font-size: 14px; font-weight: normal; padding-left: 10px; height: 30px; line-height: 30px; margin: 0; font-weight: bold; color: #555;}
.body .mainer{}
</style>
<script type="text/javascript">
try{
var gui = require('nw.gui');
var win = gui.Window.get();
var shell = gui.Shell;
var winMaxHeight,winMaxWidth;
win.resizeTo(280,400);
}catch (e){}
 

function WinClose(){
  win.close(); 
}
function WinMin(){
  win.minimize(); 
}

$(document).on('click', '.btn', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    if(ThiType=='winclose'){
        WinClose();
    }else if(ThiType=='winMin'){
        WinMin();
    }else if(ThiType=='winFZ'){
        alert($('.mainer').html())
        $('.mainer').addClass('flipped');
    }
    event.preventDefault();
});
function toLogin(){
    iFrameSrc('http://${host}:8088/coco/chat/login.jsp?l=b');
}
function iFrameSrc(url){
    $('#iframe').attr('src',url);
}
function domsize(){
    $('html').height($(document).height());
    $('body').height($(document).height()-2);
    var bh=$('body').height(),
    th=$('.titbar').height();
    $('.mainer').height(bh-th-0);
}
$(document).ready(function() {
    toLogin();
    domsize();
    //$('#iframe').remove();
});
$(window).resize(function(event) {
    domsize();
});

var ssv=1;
$(document).on('click', '.s', function(event) {
    if(ssv==1){
        $('.mainer').addClass('flipped');
        ssv=0
    }else{
        $('.mainer').removeClass('flipped');
        ssv=1
    }
});
</script>
<style>
.body .mainer{ position: relative;
            -moz-perspective: 800px;
            -webkit-perspective: 800px;
            perspective: 800px;}

.body .mainer .erroring{
opacity:0;
/*rotating the recover password form by default*/
-moz-transform:rotateY(180deg);
-webkit-transform:rotateY(180deg);
transform:rotateY(180deg);}
.body .mainer.flipped .erroring{
    background: #09F;
    opacity: 1;
    -moz-transform: rotateY(0deg);
    -webkit-transform: rotateY(0deg);
    transform: rotateY(0deg);
}
.body .mainer.flipped .iframe{
    background: #F00;
    opacity: 0;
    -moz-transform: rotateY(-180deg);
    -webkit-transform: rotateY(-180deg);
    transform: rotateY(-180deg);
}


.d3d{ position: absolute; top: 0; left: 0; right: 0; bottom: 0; width: 100%; height: 100%;
-moz-transform-style: preserve-3d;
-webkit-transform-style: preserve-3d;
transform-style: preserve-3d;
-moz-backface-visibility: hidden;
-webkit-backface-visibility: hidden;
backface-visibility: hidden;
-moz-transition: 0.5s;
-webkit-transition: 0.5s;
transition: 0.5s;
-moz-animation: pulse 2s infinite;
-webkit-animation: pulse 2s infinite;
}

</style>
</head>
<body>
<div class="body">
    <div class="titbar">
        <div class="titools ">
            <a href="#" class="btn btn_fz s" data-type="winFZs" style=" display:none;"><i class="icon iconfont">s</i></a>
            <a href="#" class="btn btn_close" data-type="winMin" style=" display:;"><i class="icon iconfont">&#xe602;</i></a>
            <a href="#" class="btn btn_close" data-type="winclose"><i class="icon iconfont">&#xe600;</i></a>
        </div>
        <h2 class="">有机会 - 叮铛</h2>
    </div>
    <div class="mainer">
        <iframe id="iframe" class="iframe d3d" name="iframe" src width="100%" height="100%" scrolling="no" marginheight="0" frameborder="0"></iframe>
        <div id="erroring" class="d3d erroring">
            11111111
        </div>
    </div>
    <div id="opener" class="opener">
        
    </div>
</div>
    
</body>
</html>