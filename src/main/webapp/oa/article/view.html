<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link rel="stylesheet" type="text/css" href="/oa/style/cssOa.css" />
<!--<link rel="stylesheet" type="text/css" href="style/cocoWindow.css" />-->
<link rel="stylesheet" type="text/css" href="/oa/style/cocoWinLayer.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/pagination.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.all.yw.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
<!--<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/layerBox.js"></script>-->
</head>

<body>

<script type="text/javascript">
var id;
var ue;
function getContent(){
  YW.ajax({
    type: 'get',
    url: '/c/oa/notice/get?id='+id,
    data:'',
    mysuccess: function(result){
      var json = JSON.parse(result);
      $('#id').val(json['notice']['id']);
      $('#senderName').html(json['notice']['senderName']);
      $('#addtime').html(json['notice']['addtime']);
      $('#title').html(json['notice']['title']);
      $('#reads').html(json['notice']['reads']);
      $('#replys').html(json['notice']['replys']);
      $('#conts').html(json['notice']['conts']);
      $('#zancount').html(json['notice']['zans']);
      $('#senderAvatar').attr('src','/oa/images/avatar/'+json['notice']['senderAvatar']+'.jpg');
      if (json['nr']['zan']==1) {
        $('#zan').addClass('zanSel');
      }else{
        $('#zan').addClass('zan');
      }
    }
  })
}

function getReplay(){
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'get',
    url: '/c/oa/notice/reply/list?noticeId='+id,
    data:a,
    mysuccess: function(result){
      var json = JSON.parse(result);
      buildHtmlWithJsonArray("id_reply_list",json['page']['data']);
      Page.setPageInfo(json['page']);
    }
  })
}

function save(){
  if (ue.getContent()=='') {
    alert('内容不能为空');
    $('#editor').focus();
    return;
  };
  var a=$('form[name=form1]').serialize();
  YW.ajax({
      type: 'POST',
      url: '/c/oa/notice/reply/add?noticeId='+id,
      data:a,
      mysuccess: function(data){
        ue.setContent('',false);
        getReplay();
        $('.ggLayerMain').scrollTop(9999);
        alert('回复成功');
      }
  });
}

function selectZan(obj){
  $(obj).toggleClass('zan');
  $(obj).toggleClass('zanSel');
  YW.ajax({
    type: 'POST',
    url: '/c/oa/toggleZan?id='+id,
    dataType:'json',
    mysuccess: function(data){
      var b = $('#zancount');
      var c =Number.parseInt(b.text()) + data.zan;
      b.text(c);
    }
  });
}

$(function(){
  id = getParam('id');
  Page.Init();
  getContent();
  getReplay();
  ue = UE.getEditor('editor',{
        toolbars: [['emotion',]],
  });
})
</script>

<!--添加网址窗口-->


   
   <div class="ggLayerMain">
      <form class="form-horizontal form1" onsubmit="getReplay();return false;" role="form" name="form1">
        <div class="textMoeInfoMain">
            <div class="layerInfoBox">
                                            
                
                <div class="layerInfoBoxTit">
                     
                     <div class="layerInfoBoxImg Fleft"><img id="senderAvatar" src="/oa/images/avatar/6.jpg" style="width:50px" /></div>
                     
                     <div class="Fleft">
                          <p><span class="yh" name="senderName" id="senderName"></span><span class="time" name="addtime" id="addtime"></span></p>
                          <p><a href="#" class="tit" name="title" id="title"><span></span></a></p>
                     </div>
                     
                     <div class="layerInfoCaozuo">
                          <i class="Bg" id="zan" onclick="selectZan(this);" style=" width:34px; line-height:15px; text-indent:17px;"><em style="color:#999999;" id="zancount"></em></i>
                     </div>
                     
                </div>
                
                <div class="layerInfoBoxContent">
                
                     <span class="marginRight55 marginLeft20"><span>阅读：<i name="reads" id="reads"></i></span> &nbsp;&nbsp;&nbsp; <span>留言：<i name="replys" id="replys"></i></span><span class="Fright"><a href="/v/oa/article/list.html" target="_self" class="back">返回文章列表</a></span></span><!-- onclick="window.location='/oa/article/list.html'"-->
                
                </div>
                <div class="layerInfoBoxConInfo">
                
                     <p id="conts" name="conts"></p>
                
                </div>
                
                
                <div class="layerInfoHfList">
                
                     <div class="layerInfoHfCon id_reply_list" style="display:none;">
                          <div class="layerInfoBoxLine"><img src="/oa/images/avatar/$[replyAvatar].jpg" /></div>
                          <div class="tit"><span class="titText Fleft">$[replyUname]</span>       <span class="Fright titTime">$[replytime]</span></div>
                          <div class="con">
                             <span class="conMain">$[conts]</span>
                          </div>
                     </div>
                     
                </div>
            
                
<form name="form1" method="post" class="definewidth m20">
            <div class="layerAddHf">
          <span id="editor" type="text/plain" name="conts" style="height:100px;width:99%"></span>
                     <button class="layerSendBtnMessage" onclick="save();return false">发送</button>
                </div>
</form>
            </div>
        </div>
        <div>
             <button class="" type="submit" onclick="getReplay();return false;" value="enter" style="display:none;">搜索</button>
        </div>
        <div class="ggLayerMainPage">
        
              <div style="display:table-row; height:33px;">
                                
                   <div class="divPage"  style=" width:100%; float:left;">
                   
                        <div class="pageMain footer">
                          <div class="maxHW mainCont ymx_page foot_page_box"></div>
                        </div>
                   
                   </div>              
                                
              </div>
        
        </div>
   </form>
   </div>
   

<!--添加网址窗口-->


</body>
</html>
