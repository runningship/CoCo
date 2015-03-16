<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
<link href="js/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="chat/style/im.css" />
<script type="text/javascript" src="js/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
        
<script type="text/javascript">
//一次性加载
var setting = {
  view: {
    showIcon: false,
    addDiyDom: addDiyDom,
    showLine:false,
  },
  data: {
    simpleData: {
      enable: true
    }
  },
  callback: {
    // onRightClick: OnRightClick
    // onClick: onClick
    // onCheck: onCheck
  }
};

function onCheck(event, treeId, treeNode){
  console.log(treeNode.id);
}

function initUserTree(treeId){
   $.ajax({
     type: 'POST',
     url: 'c/getUserTree',
     data:'',
     success: function(data){
         var result=JSON.parse(data);
         $.fn.zTree.init($("#"+treeId), setting, result.result);
         var treeObj = $.fn.zTree.getZTreeObj(treeId); 
         treeObj.expandAll(true);
         //treeObj.expandAll(false); 
         getRecentChats(isGetOutLxr());
     }
   });
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
    var sign = treeNode.sign;
    if (!sign) {
      sign = "";
    };
	  var span = '<span class="">'
	 +'<li name="'+treeNode.name+'" title="'+sign+'" class="search_clone" py="'+treeNode.namePy+'" pyShort="'+treeNode.namePyShort+'" id="lxr_'+treeNode.id+'" onclick="openAndSelectChat(\''+treeNode.uid+'\',\''+treeNode.name+'\','+treeNode.avatar+')">'
     + '<div id="user_avatar_'+treeNode.id+'" class="cocoTx Fleft">'
     +'<img user_avatar_img="'+treeNode.avatar+'" src="chat/images/avatar/'+treeNode.avatar+'.jpg" class="user_avatar_img_'+treeNode.id+' user_status_filter_'+treeNode.status+'">'
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