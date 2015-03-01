<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
<link href="/style/css.css" rel="stylesheet">
<link href="/style/style.css" rel="stylesheet">
<link href="/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/oa/style/im.css" />
<script src="/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
        
<script type="text/javascript">
var setting = {
  view: {
    showIcon: false,
    addDiyDom: addDiyDom,
    showLine:false,
    dblClickExpand: true,
  },
  data: {
    simpleData: {
      enable: true
    }
  },
  async: {
	enable: true,
	url: "/c/getChildren",
	autoParam: ["id", "type"]
  },
  callback: {
    // onRightClick: OnRightClick
    // onClick: onClick
    // onCheck: onCheck
    beforeExpand: beforeExpand
  }
};

function onCheck(event, treeId, treeNode){
  console.log(treeNode.id);
}
function beforeExpand(treeId, treeNode) {
  if(treeNode.zAsync){
    return;
  }
  if (!treeNode.isAjaxing) {
    startTime = new Date();
    treeNode.times = 1;
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    // ajaxGetNodes(treeNode, "refresh");
    zTree.reAsyncChildNodes(treeNode, 'refresh', true);
    return true;
  } else {
    alert("zTree 正在下载数据中，请稍后展开节点。。。");
    return false;
  }
}

function initUserTree(treeId){
	$.fn.zTree.init($("#"+treeId), setting ,null);
  // $.ajax({
  //   type: 'POST',
  //   url: '/c/getChildren?parent=-1',
  //   data:'',
  //   success: function(data){
  //       var result=JSON.parse(data);
  //       $.fn.zTree.init($("#"+treeId), setting, result.result);
  //   }
  // });
}



function addDiyDom(treeId, treeNode) {
  console.log(treeId);
  var aObj = $("#" + treeNode.tId + "_a");
  aObj.css('display','inline');

  aObj.parent().addClass('a_'+treeNode.type);
  if(treeNode.cnum!=null){
    var cnumStr = '<span class="">'+ treeNode.cnum +' </span>';
    aObj.prepend(cnumStr);  
  }
  if(treeNode.type=='user'){
	  var li = $("#" + treeNode.tId);
	  li.empty();
	  var span = '<span class="">'
	 +'<li id="lxr_'+treeNode.id+'" onclick="openChat('+treeNode.uid+',\''+treeNode.name+'\','+treeNode.avatar+')">'
     + '<div id="user_avatar_'+treeNode.id+'" class="cocoTx Fleft">'
     +'<img user_avatar_img="'+treeNode.avatar+'" src="/oa/images/avatar/'+treeNode.avatar+'.jpg" class="user_avatar_img_'+treeNode.id+' user_status_filter_'+treeNode.status+'">'
     + '</div>'
     + '<div class="cocoPerInfo Fleft">'
     +    '<p class="name">'+treeNode.name+'</p>'
     +     '<p class="txt"></p>'
     
     + '</div>'
     //+ 	'<div class="user_status_'+treeNode.id+'  user_status_'+treeNode.status+' "></div>'
     + '<div class="new_msg_count "></div>'
	 +	'</li>'
	  +'</span>';
	  li.append(span);
  }
  // var color="icon_sh";
  // if(treeNode.sh==0 || treeNode.sh==undefined){
  //   color="icon_wsh";
  // }
  // if(treeNode.type!="group"){
  //   var lockStr = '<span id="'+treeNode.tId+'_sh_a" onClick="shenhe(\''+treeNode.tId+'\')" class="icon iconfont '+color+'">&#xe64e;</span>';
  //   aObj.after(lockStr);
  // }
  // aObj.append('<span class="icon iconfont btns runMenu" data-type="runMenu" data-tree="'+treeNode.tId+'">&#xe641;</span>');
}

</script>
<style type="text/css">
  div#rMenu {position:absolute; top:0;width:150px; text-align: left;padding: 2px;}
  div#rMenu ul li{
    margin: 5px 0;
    padding: 0 15px;
    cursor: pointer;
    list-style: none outside none;
  }
  ul.jtree li a{
    position: initial;
  }
</style>
</head>