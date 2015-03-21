<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
<link href="/coco/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/coco/chat/style/im_native.css" />
<script type="text/javascript" src="/coco/js/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
        
<script type="text/javascript">
var check_user=false;
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
  check:{
    enable: false
  },
  callback: {
    // onRightClick: OnRightClick
    // onClick: onClick
    onCheck: onCheck
  }
};

function onCheck(event, treeId, treeNode){
  console.log(treeNode.id);
}

function initUserTree(treeId){
   $.ajax({
     type: 'POST',
     url: '/coco/c/getUserTree',
     data:'',
     success: function(data){
         var result=JSON.parse(data);
         $.fn.zTree.init($("#"+treeId), setting, result.result);
         var treeObj = $.fn.zTree.getZTreeObj(treeId); 
         treeObj.expandAll(true);
         //treeObj.expandAll(false); 
         if(check_user==false){
        	 getRecentChats(function(){
        		 isGetOutLxr();
        		 getUnReadChats();
        	 });	 
         }
         
     }
   });
}



function addDiyDom(treeId, treeNode) {
	treeNode.nocheck=false;
  console.log(treeId);
  var aObj = $("#" + treeNode.tId + "_a");
  aObj.css('display','inline');

  aObj.parent().addClass('a_'+treeNode.type);
  if(treeNode.cnum!=null){
    var cnumStr = '<span class="">'+ treeNode.cnum +' </span>';
    aObj.prepend(cnumStr);  
  }
  var checkBox = $('#'+treeNode.tId+'_check');
  if(treeNode.type=='user'){
		if(check_user){
			checkBox.css('position','absolute').css('top' ,'24px');
			//checkBox.addClass('contact_check_box');
		}
	  var li = $("#" + treeNode.tId);
	  li.empty();
	  li.append(checkBox);
	  var sign = treeNode.sign;
	  if (!sign) {
	    sign = "";
	  };
	  var style="";
	  if(check_user){
		  style="margin-left:20px;";
	  }
	  var avatar = treeNode.avatar;
	  if(!avatar){
		  avatar = default_avatar;
	  }
	  var span = '<span class="">'
	 +'<li style="'+style+'"  name="'+treeNode.name+'" title="'+sign+'" class="search_clone " py="'+treeNode.namePy+'" pyShort="'+treeNode.namePyShort+'" id="lxr_'+treeNode.id+'" onclick="openAndSelectChat(\''+treeNode.uid+'\',\''+treeNode.name+'\','+avatar+')">'
	 //+ checkBox[0].outerHTML
	 + '<div id="user_avatar_'+treeNode.id+'" class="cocoTx Fleft">'
     +'<img user_avatar_img="'+avatar+'" src="/coco/chat/images/avatar/'+avatar+'.jpg" class="user_avatar_img_'+treeNode.id+' user_status_filter_'+treeNode.status+'">'
     + '</div>'
     + '<div class="cocoPerInfo Fleft">'
     +    '<span name="'+treeNode.name+'" class="name">'+treeNode.name+'&nbsp;&nbsp;&nbsp;'+sign+'</span>'
     //+     '<p class="txt"></p>'
     
     + '</div>'
     //+ 	'<div class="user_status_'+treeNode.id+'  user_status_'+treeNode.status+' "></div>'
     + '<div class="new_msg_count "></div>'
	 +	'</li>'
	  +'</span>';
	  
	  li.append(span);
  }
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