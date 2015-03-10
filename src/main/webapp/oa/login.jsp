<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="pragram" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>有机会 - 叮铛</title>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<link href="../style/css.css" rel="stylesheet">
<link href="../bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="../style/style.css" rel="stylesheet">
<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script type="text/javascript">
try{
var gui = require('nw.gui');
var win = gui.Window.get();
var shell = gui.Shell;
}catch (e){}

function openurl(url){
    shell.openExternal(url);
}
function login(){
    var dlbtn=$('.btn_submit');
    dlbtn.text('登陆中...');
	$.ajax({
	    type: 'get',
	    dataType: 'json',
	    url: '../c/login?name='+$('#idName').val()+'&pwd='+$('#idPassword').val()+'&type='+$('#idType').val(),
	    success:function(data){
            dlbtn.text('成功...');
	    	window.top.location='../home.jsp';
	    },
	    error:function(data){
	    	var json = JSON.parse(data.responseText);
	    	dlbtn.text(json.msg);
	    	console.log(data);
	    }
    });
}
</script>
<style>
 input,button,select,textarea{outline:none}
body{ }
    .form_login li{ margin-bottom: 10px;}

    .form_login{ display:inline-block; margin: 30px 0px 0px; width: 100%; }
    .form_login .labU,.form_login .labP{ border: 1px solid #E6E6E6; width: 100%; height: 40px; overflow: hidden; position: relative; background: #FFF no-repeat 5px center; font-weight:normal;}
    .form_login .labU{ background-image: url('images/icon_user.png');}
    .form_login .labP{ background-image: url('images/icon_password.png');}
    .form_login .labU .inputbox,.form_login .labP .inputbox{ position: absolute; top: 0; right:0; bottom: 0; left: 40px;}
    .form_login .labU .input,.form_login .labP .input{width: 100%;height: 100%;border: 0; padding-left: 5px;font-weight: normal;}
    .btn { display: inline-block; background: #EEE; color: #000; height: 40px; line-height: 30px; width: 49%;}
    .btn_submit{ background:#FF6600; color: #FFF; }
    .btn_reg{ background:#28A9D8; color: #FFF; }



/*客户端的样式*/

    .form_login li{ margin-bottom: 0px;}

    .form_login{ display:inline-block; margin: 30px 0px 0px; width: 100%; }
    .form_login .labU,.form_login .labP{ border:0; border-bottom: 1px solid #CCC; width: 100%; height: 40px; overflow: hidden; position: relative; background: #FFF no-repeat 5px center; line-height: 40px; padding-left: 10px;}
    .form_login .labU,
    .form_login .labP{ background:none;display: block;}
    .form_login .labU .inputbox,.form_login .labP .inputbox{ position: absolute; top: 0; right:0; bottom: 0; left: 50px;}
    .form_login .labU .input,.form_login .labP .input{width: 100%;height: 100%;border: 0; padding-left: 5px;font-weight: normal; background: none; height: 40px;line-height: 1.5em;}
    .form_login .selectbox{ border:0; margin-top: 10px;}
    .form_login .selectbox .select{width: 100%;height: 30px; padding: 0 7px; border-width:0; background: none; font-family: 'microsoft yahei';}
    .form_login .selectbox .select:hover{background: rgba(255,255,255,0.7);}
    .form_login .selectbox .select:focus{ border-width:1px; background: #FFF;}
    .btn { display: inline-block; background: #EEE; color: #000; height: 40px; line-height: 40px; width: 100%; margin-top: 10px; text-align: center; border-radius: 2px;}
    .btn:hover{ color: #FFF;}
    .btn_submit{ background:#FF6600; color: #FFF; }
    .btn_submit:hover{ background:#FF4400; }
    .btn_reg{ background:#28A9D8; color: #FFF; }
    .btn_link{ background:none; color: #436895; margin-top: 10px; }
    .btn_link:hover{ color: #09F;}

    .logoBox{ margin: 20px auto 0; width: 80px; display: block;}
    .logoBox img{ width: 100%; height: auto;}

</style>
</head>
<body style="overflow: hidden">
<div class="html ">
    
    <div class="bodyer">
        <div class="mainer bd ">
                    <span class="logoBox">
                        <img src="images/dd.png" alt="" class="">
                    </span>
            <ul class="form_login">
                <li>
                    <label class="labU">
                        帐号
                        <div class="inputbox"><input type="text" class="input" id="idName" placeholder="帐号"></div>
                    </label>
                </li>
                <li>
                    <label class="labP">
                        密码
                        <div class="inputbox"><input type="text" class="input" id="idPassword" placeholder="密码"></div>
                    </label>
                </li>
                <li style="display:;">
                    <div class="selectbox">
                    <select name="idType" id="idType" class="select">
                        <option value="buyer">买家</option>
                        <option value="seller">卖家</option>
                    </select></div>
                </li>
                <li>
                    <a href="#" class="btn btn_submit" onclick="login();">登 陆</a>
                    <a href="javascript:openurl('http://365ji.com/register.action')" target="_blank" class="btn btn_reg btn_link">注 册</a>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>