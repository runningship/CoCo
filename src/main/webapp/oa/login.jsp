<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="pragram" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>中介宝 5.0</title>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<link href="/style/css.css" rel="stylesheet">
<link href="/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="/style/style.css" rel="stylesheet">
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/buildHtml.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script type="text/javascript">
function login(){
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: '/c/login?name='+$('#idName').val()+'&pwd='+$('#idPassword').val()+'&type='+$('#idType').val(),
	    success:function(data){
	    	window.location='../home.jsp';
	    },
	    error:function(data){
	    	console.log(data);
	    }
	  });
}
</script>
<style>
    .form_login{ display:inline-block; margin: 30px 30px; }
    .form_login .labU,.form_login .labP{ border: 1px solid #E6E6E6; width: 250px; height: 40px; overflow: hidden; position: relative; background: no-repeat 5px center;}
    .form_login .labU{ background-image: url('images/icon_user.png');}
    .form_login .labP{ background-image: url('images/icon_password.png');}
    .form_login .labU .inputbox,.form_login .labP .inputbox{ position: absolute; top: 0; right:0; bottom: 0; left: 40px;}
    .form_login .labU .input,.form_login .labP .input{width: 100%;height: 100%;border: 0; padding-left: 5px;font-weight: normal;}
    .btn { display: inline-block; background: #EEE; color: #000; height: 40px; line-height: 30px; width: 49%;}
    .btn_submit{ background:#FF6600; color: #FFF; }
    .btn_reg{ background:#28A9D8; color: #FFF; }
</style>
</head>
<body style="overflow: hidden">
<div class="html ">
    
    <div class="bodyer">
        <div class="mainer bd ">
            <ul class="form_login">
                <li>
                    <label class="labU">
                        <div class="inputbox"><input type="text" class="input" id="idName" placeholder="帐号"></div>
                    </label>
                </li>
                <li>
                    <label class="labP">
                        <div class="inputbox"><input type="text" class="input" id="idPassword" placeholder="密码"></div>
                    </label>
                </li>
                <li>
                    <label class="labP">
                        <div class="inputbox">
                        <select name="idType" id="idType" class="input">
                            <option value="buyer">买家</option>
                            <option value="seller">卖家</option>
                        </select></div>
                    </label>
                </li>
                <li>
                    <a href="#" class="btn btn_submit" onclick="login();">登 陆</a>
                    <a href="http://365ji.com/register.action" target="_blank" class="btn btn_reg">注 册</a>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>