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
	    url: '/c/login?name='+$('#idName').val()+'&pwd='+$('#idPassword').val(),
	    success:function(data){
	    	window.parent.loginSuccess();
	    },
	    error:function(data){
	    	console.log(data);
	    }
	  });
}
</script>
</head>
<body style="overflow: hidden">
<div class="html ">
    
    <div class="bodyer">
        <div class="mainer bd ">
            <ul>
                <li>
                    <div class="row loginBox" >
                        <div class="col-xs-2"></div>
                        <div class="col-xs-8">
                            <form class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label for="idName" class="col-xs-2 control-label">账号:</label>
                                    <div class="col-xs-8 form_menu_box" id="idNameBox">
                                        <input type="text" class="form-control" id="idName" tabindex="10" placeholder="">
                                        <div class="form_menu_list"></div>
                                    </div>
                                    <a class="col-xs-2 btn btn-link" data-type="wangjizhanghao">忘记帐号？</a>
                                </div>
                                <div class="form-group">
                                    <label for="idPassword" class="col-xs-2 control-label">密码:</label>
                                    <div class="col-xs-8">
                                        <input type="password" class="form-control" id="idPassword" tabindex="11" placeholder="">
                                    </div>
                                    <a class="col-xs-2 btn btn-link" data-type="wangjimima">忘记密码？</a>
                                </div>
                                <div class="form-group"></div>
                                <div class="form-group">
                                    <label class="col-xs-2 control-label"></label>
                                    <div class="col-xs-8">
                                        <button onclick="login();" type="button" class="btn btn-primary "  tabindex="12"><span class="ladda-label">登　录</span></button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="col-xs-2"></div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>