<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
var saveSiteUrl="";
var siteAreaId = "share";
function showSites(id){
	siteAreaId = id;
	if(id=='personal'){
		$('#'+id).css('display','');
		$('#share').css('display','none');
		saveSiteUrl="/c/oa/site/addMy";
		$('.weblineAdd').css('display','');
		$('.editStart').css('display','');
	}else{
		//公告的
		$('#'+id).css('display','');
		$('#personal').css('display','none');
		saveSiteUrl="/c/oa/site/addShare";
		if(${!addPublicSite}){
			$('.weblineAdd').css('display','none');
			$('.editStart').css('display','none');	
		}
		
	}
	$('.weblineTit span').toggleClass('sel');
}

function addSite(){
	if ($('#site_title').val()==null||$('#site_title').val()==undefined||$('#site_title').val().trim()=='') {
		alert('名称不能为空');
		$('#site_title').focus();
		return;
	};
	if ($('#site_url').val()==null||$('#site_url').val()==undefined||$('#site_url').val().trim()=='') {
		alert('网址不能为空');
		$('#site_url').focus();
		return;
	};
	var a=$('form[name=siteForm]').serialize();
	YW.ajax({
	    type: 'POST',
	    url: saveSiteUrl,
	    data:a,
	    mysuccess: function(data){
	    	alert('保存成功');
	    	var len = $('#'+siteAreaId).children().length;
	    	len++;
	    	var html = '<a href="'+$('#site_url').val()+'" onclick="openSite(this);" class="a green marginLeft15 color_'+len%4+'">'+$('#site_title').val()+'</a>';
	    	$('#'+siteAreaId).append(html);
	    	$('form input').val('');
	    }
	});
}

function openSite(a){
	if(a.href){
		event.preventDefault();
		var href=$(a).attr('href');
		if(href.indexOf('http')<=0){
			href="http://"+href;
		}
		try{
			window.top.gui.Shell.openExternal(href);
		}catch(e){

		}
		
	}
}
function editSite(a){
	if (a==0) {
		$('.site_del').css('display', '');
		$('.editSite').css('display', '');
		$('.over').css('display', 'none');
	}else {
		$('.site_del').css('display', 'none');
		$('.over').css('display', '');
		$('.editSite').css('display', 'none');
	}
}

function delSite(site,id){
	event.preventDefault();
	event.cancelBubble=true;
	YW.ajax({
	    type: 'POST',
	    url: '/c/oa/site/delete?id='+id,
	    mysuccess: function(data){
			$(site).parent().remove();
	    	alert('删除成功');
	    }
	});
}

</script>
<div class="webline">
    <div class="weblineTit"><span onclick="showSites('share')" class="sel">公共导航网址</span><span onclick="showSites('personal')">我的个人网址</span>
    <a href="#" onclick="editSite(0);" class="marginRight15 over editStart" <c:if test="${!addPublicSite}">style="display:none"</c:if> >编辑</a>
    <a href="#" onclick="editSite(1);" class="marginRight15 editSite" style="display:none">完成</a>
    </div>
    <div class="webLineCon" id="share">
    	<c:forEach items="${shareList}"  var="site" varStatus="status">
    		<a href="${site.url }" onclick="openSite(this)" class="a grey marginLeft15 color_${status.count%4}">${site.title } <i class="site_del" style="display:none" onclick="delSite(this,${site.id});" >×</i></a>
    	</c:forEach>
    </div>
    <div class="webLineCon" id="personal" style="display:none">
         <c:forEach items="${personalList}"  var="site" varStatus="status">
    		<a href="${site.url }" onclick="openSite(this)" class="a grey marginLeft15 color_${status.count%4}">${site.title } <i class="site_del" style="display:none" onclick="delSite(this,${site.id});" >×</i></a>
    	</c:forEach>
    </div>
    <div class="weblineAdd" <c:if test="${!addPublicSite}">style="display:none"</c:if> >
    	<form name="siteForm" onsubmit="return false;">
         <span class="txt" style="width:45px;">名称：</span><input type="text" id="site_title" class="inputBox" name="title" style=" width:28%;" />
         <span class="txt" style="width:45px;">网址：</span><input type="text" id="site_url" class="inputBox" name="url" style=" width:28%;" />
         <button class="btn" onclick="addSite()"></button>
    	</form>
    </div>
</div>



