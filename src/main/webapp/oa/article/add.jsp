<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="UTF-8">
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/ueditor.all.yw.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="/js/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="/js/dialog/jquery.artDialog.source.js?skin=default"></script>
<script type="text/javascript" src="/js/dialog/plugins/iframeTools.source.js"></script>
<script type="text/javascript">
var ue;
$(function(){
	ue = UE.getEditor('editor',{
        toolbars: [
            ['forecolor', 'simpleupload','emotion','spechars', 'attachment', '|', 'fontfamily', 'fontsize', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'formatmatch', 'pasteplain', '|', 'backcolor', 'insertorderedlist', 'insertunorderedlist', '|','justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', 'indent', 'rowspacingtop', 'rowspacingbottom', 'lineheight',
            ]
        ],
  });
    // var ue = UE.getEditor('editor', {});
    ue.setHeight('100%');
	initUserTree('receiverTree');
});

function save(){
    var tit=$('#title').val();
    if (tit==null||tit==undefined||tit.trim()=='') {
       alert('请填写标题'); 
       return;
    };
    if (ue.getContent()=='') {
        alert('内容不能为空');
        $('#editor').focus();
        return;
    };
    var a=$('form[name=form1]').serialize();
    YW.ajax({
        type: 'POST',
        url: '/c/oa/notice/save',
        data:a,
        mysuccess: function(data){
            window.parent.doSearch();
            window.parent.LayerRemoveBox("addArt");
            alert('发布成功');
        }
    });
}
</script>

<style type="text/css">

.tableleft{ width:100px;}
.addSureBtn{ padding:6px 25px; background-color:#f55252; border-radius:2px; color:#ffffff; border:none; cursor:pointer; margin-top:12px;}

.addSureBtn:hover{ background-color:#de2a2a;}

</style>
</head>
<body>
<form name="form1" method="post" class="definewidth m20">
<input name="isPublic"type="hidden" value="1">
<table class="table table-bordered table-hover m10" style="width:100%; height:100%; font-family:'宋体'; font-size:13px; padding-top:15px;">
    <tr>
        <td height="30" align="right" valign="middle" class="tableleft"><span style="margin-right:10px;">标题:</span></td>
      <td valign="middle"><input type="text" name="title" id="title" style="border:1px solid #d4d4d4; padding:4px 3px; border-radius:3px;" /></td>
    </tr>
    <tr>
        <td align="right" valign="middle" class="tableleft"><span style="margin-right:10px;">正文:</span></td>
        <td valign="middle">
        	<span id="editor" type="text/plain" name="conts" style="height:400px;width:95%"></span>
        </td>
    </tr>
    
    <tr>
        <td align="right" valign="middle" class="tableleft" height="50"></td>
        <td valign="middle">
          <button class="addSureBtn" type="button" onclick="save();return false;">保存</button>
        </td>
    </tr>
</table>
</form>
<jsp:include page="../userTree.jsp"></jsp:include>
</body>
</html>