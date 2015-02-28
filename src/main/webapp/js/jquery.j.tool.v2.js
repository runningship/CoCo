/**
 * All Javascript
 * @authors YiMiXia.com (7687849@qq.com)
 * @date    2014-08-06 11:02:12
 * @version zjb 5.0
 */
// JavaScript Document
//$(document).bind("contextmenu", function () { return false; });
//$(document).bind("selectstart", function () { return false; });
/*
*/
//屏蔽右键菜单
// document.oncontextmenu = function (event){
// 	if(window.event){
// 		event = window.event;
// 	}try{
// 		var the = event.srcElement;
// 		if (!((the.tagName == "INPUT" && (the.type.toLowerCase() == "text" || the.type.toLowerCase() == "password")) || the.tagName == "TEXTAREA")){
// 			return false;
// 		}
// 		return true;
// 	}catch (e){
// 		return false; 
// 	} 
// }
//屏蔽全选
// document.onselectionchange=function(event){
//   var the = document.getSelection().extentNode;
//   if(the.tagName==undefined){
//     the = the.parentElement;
//   }
//   // console.log(sel.outerHTML);
//   if(the.tagName=='BODY' || the.tagName=='LI' || the.tagName=='EM' || the.tagName=='SELECT' ){
//       return false;
//     }else if(the.classList.contains('not-select')){
//       return false;
//     }else{
//       return true;
//     }
// }
 document.onselectstart = function (event){
 	if(window.event){
 		event = window.event;
 	}

  try{
 		var the = event.srcElement;
    if(the.tagName==undefined){
      the = the.parentElement;
    }
    if(the.tagName=='BODY' || the.tagName=='LI' || the.tagName=='EM' || the.tagName=='SELECT' ){
      return false;
    }else if(the.classList.contains('not-select')){
      return false;
    }else{
      return true;
    }
   //  if((the.tagName == "INPUT" && (the.type.toLowerCase() == "text" || the.type.toLowerCase() == "password")) || (the.tagName == "SPAN" && (the.className == "lxr" || the.className == "tel")) || the.tagName == "TEXTAREA" || the.className == "neirong" || the.className == "telNum" || the.className == "onselect" ){
 		// 	//alert(the.getAttribute("selectstart"))
   //     return true;
 		// }else if(the.tagName == "TD" || the.tagName == "TH"){
   //     return true;
   //  }else{
   //    return false;
   //  }
 	}catch (e){
 		return false; 
 	} 
 }
 //屏蔽全选  结束


 //添加例外
    // var omitformtags = ["input", "table"];//设置可以的元素名称
    // omitformtags = "|"+ omitformtags.join("|") + "|";
    // function disableselect(){//判断是否是可选中的元素，不是的话就return false
    //   if(event.srcElement.tagName==undefined){
    //     return false;
    //   }
    //   if (omitformtags.indexOf("|"+event.srcElement.tagName.toLowerCase()+"|")==-1){
    //     return false;
    //   }
    // }
    // document.onselectstart = disableselect;
 //添加例外  结束
 
$(document).keydown(function(event){  
//alert('a'+event.keyCode)
    if ((event.altKey)&&   
      ((event.keyCode==37)||   //屏蔽 Alt+ 方向键 ←   
       (event.keyCode==39)))   //屏蔽 Alt+ 方向键 →   
   {   
       event.returnValue=false;   
       return false;  
   }   
    if(event.keyCode==8){   //
      if(window.event){
        event = window.event;
      }try{
        var the = event.srcElement;
        if (!((the.tagName == "INPUT" && (the.type.toLowerCase() == "text" || the.type.toLowerCase() == "password")) || the.tagName == "TEXTAREA" || the.className == "neirong" || the.className == "telNum" || the.className == "onselect")){
          //alert(the.getAttribute("selectstart"))
          return false;
        }
        return true;
      }catch (e){
        return false; 
      }   
    }  
	
	
	if ((event.keyCode==17)&&(event.keyCode==65))   
   {   
       if(window.event){
        event = window.event;
      }try{
        var the = event.srcElement;
        if ((the.tagName == "INPUT" && (the.type.toLowerCase() == "text" || the.type.toLowerCase() == "password")) || (the.tagName == "SPAN" && (the.className == "lxr" || the.className == "tel")) || the.tagName == "TEXTAREA" || the.className == "neirong" || the.className == "telNum" || the.className == "onselect"){
          return true;
        }
        return false;
      }catch (e){
        return false; 
      }  
   }   
	
	
    if(event.keyCode==9){  
      if(window.event){
        event = window.event;
      }try{
        var the = event.srcElement;
        if (the.tagName.toLowerCase() == "select" || the.tagName.toLowerCase() == "textarea" || the.tagName.toLowerCase() == "input"){
          return true;
        }
        return false;
      }catch (e){
        return false; 
      }  
    }  
    if(event.keyCode=="116"){  
        return false; //屏蔽F5刷新键   
    }  
    if((event.ctrlKey) && (event.keyCode==82)){  
        return false; //屏蔽alt+R   
    }  
}); 

//窗口调用的公共函数
try{
var gui = require('nw.gui');
var win = gui.Window.get();
var shell = gui.Shell;
var winMaxHeight,winMaxWidth;
}catch (e){}
function WinMaxOrRev(a){
  if(a==0){
    winMaxNone='none';
    winRevertNone='inline-block';
  }else{
    winMaxNone='inline-block';
    winRevertNone='none';
  }
  if($('.winBtnMax').length>0){
    $('.winBtnMax').css('display',winMaxNone);
  }
  if($('.winBtnRevert').length>0){
    $('.winBtnRevert').css('display',winRevertNone);
  }
  // ChangeW();
  
}
function WinMin(){/*最小化*/
	win.minimize();
}
function WinMax(){/*最大化*/
//  alert(0)
  // if(process.platform === 'win32' && parseFloat(require('os').release(), 10) > 6.1) {
    win.setMaximumSize(screen.availWidth + 15, screen.availHeight + 15);
  // }
	win.maximize();
  winMaxHeight=win.height;
  winMaxWidth=win.width;
	WinMaxOrRev(0);
}
function WinRevert(){/*恢复*/
//  alert(1)
	win.restore();
  if(win.width<692){
    win.resizeTo(692,win.height);
  }

  WinMaxOrRev(1);
}
function WinMaxRev(){/*最大化*/
//alert(hex.formState)
	if(hex.formState==0){
		WinMax();
	}else if(hex.formState==2){
		WinRevert();
	}
}
function WinClose(force){/*退出*/
//    hex.close();
  win.close(force);	
 /* YW.ajax({
    type: 'POST',
    url: '/zb/c/user/logout',
    data:'',
    success: function(data){
      alert('正在关闭...');
      hex.close();
    },
    error:function(data){
      hex.close();
    }
  });*/
}

//添加边框线条
function WinBoxBorder(){
	var winBorder='<div class="winBoxBorder winBoxBorderT"></div>'+
'<div class="winBoxBorder winBoxBorderR"></div>'+
'<div class="winBoxBorder winBoxBorderB"></div>'+
'<div class="winBoxBorder winBoxBorderL"></div>'
	$('body').append(winBorder)

}
//计算内页侧边标题高度等
function getSideH(){
  var htmls=$('body>.html'),
  sides=htmls.find('.sideCont');
  return sides.css('top');
}
function getHeadH(){
  var htmls=$('body>.html'),
  sides=htmls.find('.header').innerHeight();
  return sides;
}
// 列表标题与内容宽度保持一致
function tableFix(TableH,TableB){
  for(var i=0;i<=TableB.find('td:last').index();i++){
    TableH.find('th').eq(i).width(TableB.find('tr').eq(1).find('td').eq(i).width());
  }
}
function setTableFix(){
	var TableH=$('.TableH'),
	TableB=$('.TableB')
	if(TableH&&TableB){
		tableFix(TableH,TableB);
	}
}
// 
function tableHover(){
    $(document).find('.table-hover').on('click', 'tr', function(event) {
        $(this).parents('table').find('tr').removeClass('cursor');
        $(this).addClass('cursor').addClass('curr');
    });
}

  //改善btn-group的操作感受
    var btn_group_time;
    $(document).on('hide.bs.dropdown','.btn-group', function () {
      return false;
    }).on('show.bs.dropdown','.btn-group', function () {
      $(".btn-group:not(this)").removeClass('open');
    }).on('mousemove','.btn-group', function(event) {
      clearTimeout(btn_group_time);
    }).on('mouseout','.btn-group', function () {
      btn_group_time=setTimeout(function(){
        $(".btn-group").removeClass('open');
      },500);
    });

//页面加载完之后执行的脚本
$(document).ready(function() {
// 增加class为table-hover的表格点击行换色
tableHover();
// 所有A标签添加禁止拖拽
$('a').attr('draggable','false');
  //页面侧边动作
  //aBtnNavFun('.aNavBtn');
  //设置表格标题宽度
  setTableFix();
  //winBtn
    $(document).on('click', '.winBtn', function(event) {
        var Thi=$(this);
        var ThiQ=Thi.data('q');
        if(ThiQ=='menu'){

        }else if(ThiQ=='min'){
            WinMin();
        }else if(ThiQ=='max'){
            WinMax();
        }else if(ThiQ=='rev'){
            WinRevert();
        }else if(ThiQ=='close'){
        	if(Thi.attr('close')=='force'){
        		WinClose(true);
        	}else{
        		quit();
        	}
            
        }
        return false;
    });
}).resize(function(event) {
  /* Act on the event */
  WinMaxOrRev(0);
//  alert(0)
});

function menuTopFuns(){
    menuTopFun();/* 顶部自动选中 */
    $('.menuLi').on('click', 'a', function(event) {
        var Thi=$(this),
        ThiUl=Thi.parents('ul'),
        ThiLi=Thi.parents('li'),
        ThiUrl=Thi.attr('href'),
        ThiType=Thi.data('type');
        if(ThiType=='url'){
            menuSetCurr(ThiLi);
        }else if(ThiType=='dialog'){
            art.dialog.open(ThiUrl,{id:'addHouse',height:460,width:640});
            return false;
        }else{
            return false;
        }
    });
    var menuLi_time;
    $('.menuLi').find('.dropdown-toggle').parents('.btn-group').hover(function() {
        var Thi=$(this);
        menuLi_time=setTimeout(function(){
            Thi.addClass('open');
        },200);
    },function(){
        clearTimeout(menuLi_time);
    });
}

$(document).ready(function() {
});

/**
 * 测试中
 */
function DesktopAlert(str) {
  alert(str)
  // Let's check if the browser supports notifications
  if (!("Notification" in window)) {
    //alert("This browser does not support desktop notification");
  }

  // Let's check if the user is okay to get some notification
  else if (Notification.permission === "granted") {
    // If it's okay let's create a notification
    var notification = new Notification(str);
  }

  // Otherwise, we need to ask the user for permission
  // Note, Chrome does not implement the permission static property
  // So we have to check for NOT 'denied' instead of 'default'
  else if (Notification.permission !== 'denied') {
    Notification.requestPermission(function (permission) {
      // Whatever the user answers, we make sure we store the information
      if (!('permission' in Notification)) {
        Notification.permission = permission;
      }

      // If the user is okay, let's create a notification
      if (permission === "granted") {
        var notification = new Notification(str);
      }
    });
  }

  // At last, if the user already denied any notification, and you 
  // want to be respectful there is no need to bother them any more.
}

/**
 * 添加 autoComplete 功能
 * autoComplete($('#input的class或id'))
 * 需在页面script中添加以下段落
  function setSearchValue(index){
      var ThiA=$('#autoCompleteBox').find('a');
      ThiA.removeClass('hover');
      var Vals=ThiA.eq(index).addClass('hover').attr('title');
      $('#search').val(Vals);
  }
 * setSearchValue(当前选中的行)
 */
function autoComplete(id){
    $(document).find('body').prepend('<div id="autoCompleteBox" class="autocomplete"></div>');
    var Thi=id,
    oldVal,ThiMaxLen=0,ThiCurrIndex=-1,
    ThiWidth=Thi.innerWidth(),
    ThiHeight=Thi.innerHeight()+1,
    ThiOptTop=Thi.offset().top+ThiHeight,
    ThiOptLeft=Thi.offset().left,
    autocomplete=$('#autoCompleteBox');
    // autocomplete.width(0);
    // autocomplete.width(ThiWidth).css({'top':ThiOptTop+ThiHeight,'left':ThiOptLeft});
    autocomplete.css({'top':ThiOptTop+ThiHeight,'left':ThiOptLeft});
    autocomplete.on('click','a',function(event) {
       var This=$(this),
       ThisIndex=This.index(),
       ThisArea=This.attr('area'),
       ThisAddress=This.data('address'),
       ThisQuyu=This.data('quyu');
       setSearchValue(ThisIndex,true);
       return false;
    });
    Thi.on('keyup',function(event) {
        var This=$(this),
        ThisVal=This.val(),
        param={search:ThisVal};
        ThiOptTop=Thi.offset().top+ThiHeight+3,
        ThiOptLeft=Thi.offset().left,
        autocomplete.css({'top':ThiOptTop,'left':ThiOptLeft});
        //alert(event.keyCode)
        if(event.keyCode=='13'){
            autocomplete.hide();
            return false;
        }else if(event.keyCode=='27'){
            autocomplete.hide();
            return false;
        }else if(event.keyCode=='38'){
            if(ThiCurrIndex<=0){
                ThiCurrIndex=0;
            }else{
                ThiCurrIndex--;
            }
            if(ThiCurrIndex<$('#autoCompleteBox').find('a').length-13){
              $('#autoCompleteBox').scrollTop($('#autoCompleteBox').scrollTop()-26);
            }
            setSearchValue(ThiCurrIndex);
            //alert(ThiCurrIndex+'|'+ThiMaxLen+'|'+autocomplete.find('a').eq(ThiCurrIndex).attr('title'))
            return false;
        }else if(event.keyCode=='40'){
            if(ThiCurrIndex>=ThiMaxLen-1){
                ThiCurrIndex=ThiMaxLen-1;
            }else{
                ThiCurrIndex++;
            }
            if(ThiCurrIndex>12){
              $('#autoCompleteBox').scrollTop($('#autoCompleteBox').scrollTop()+26);
            }
            setSearchValue(ThiCurrIndex);
            //alert(ThiCurrIndex+'|'+ThiMaxLen+'|'+autocomplete.find('a').eq(ThiCurrIndex).attr('title'))
            return false;
        }
        if(oldVal!=ThisVal){
            oldVal=ThisVal;
            YW.ajax({
                type: 'POST',
                url: '/c/areas/prompt',
                data:param,
                success: function(data){
                    var d=JSON.parse(data);
                    autocomplete.html('');
                    $.each(d['houses'], function(index, val) {
                        autocomplete.prepend('<a href="#" area="'+val.area+'" title="'+val.address+'" data-address="'+val.address+'" data-quyu="'+val.quyu+'" ><i>'+val.quyu+'</i><b>'+val.area+'</b>'+val.address+'</a>');
                    });
                    ThiMaxLen=d['houses'].length;
                    ThiCurrIndex=-1;
                    if(ThiMaxLen>0){
                        autocomplete.show()
                    }else{
                        autocomplete.hide()
                    }
                },complete:function(){
                    
                },beforeSend:function(){}
            });
        }
    }).on('focusin',function(event) {
        if(autocomplete.html()){autocomplete.show();}
    }).on('focusout',function(event) {
        ThiCurrIndex=0;
        var autocompleteTime=setTimeout(function(){
            autocomplete.hide();
            clearTimeout(autocompleteTime);
        },200);
    });
}

/**
 * 房源重复提示
 * 仅在出售添加和修改里用
 */
var getHouseTooStr;
function getHouseToo(callback){
    var area=$('#area'),
    dhao=$('#dhao'),
    fhao=$('#fhao'),
    seeGX=$('#seeGX'),
    areav=area.val(),
    dhaov=dhao.val(),
    fhaov=fhao.val();
    var seeGXv;
    if(seeGX[0]==null || seeGX[0]==undefined){
      seegxv = "0";
    }else{
      seeGX[0].checked?"1":"0";  
    }
    if(getHouseTooStr==areav+dhaov+fhaov){
        return false;
    }else{
      getHouseTooStr=areav+dhaov+fhaov;
      //alert(areav+dhaov+fhaov)
    }
    if(!areav || !dhaov || !fhaov){
        return false;
    }else{
        api.title(apiTitle);
    }
    var param={
        area:areav,
        dhao:dhaov,
        fhao:fhaov,
        seeGX:seeGXv
    }
    YW.ajax({
        type: 'POST',
        url: '/c/house/exist',
        data:param,
        mysuccess: function(data){
            var jsons=JSON.parse(data);
            if(jsons['exist']==1){
                if(jsons['hid']==id){
                    api.title(apiTitle);
                }else{
                    api.title(apiTitle + '　<b style="color:#F00;">房源重复：'+ jsons['hid'] +'</b>');
                }
            }else{
                api.title(apiTitle + '　<b style="color:#090;">无重复</b>');
                callback;
            }
        }
    });
}

/**
 * 列表循环完成后，替换相应class，一般用在审核未审核的样式上
 */
/*function replaceClass(a,c){
a.addClass(c);
}
function replaceClassAll(d1,a1,a2,c1,c2){
  if(!d1!a1||!a2||c2){return false;}
  var Thi=d1;
  if(d1.hasClass(a1)){
    d1.addClass(c1);
  }else if(d1.hasClass(a2)){
    d1.addClass(c2)
  }
}*/


/**
 * 后台侧边搜索功能
 */
function getFunSettingSideEarch(){
  function getSearch(val){
    //alert($('.jtree').html())
    var input = val;  //记录输入的商家名
    var tds = $('.jtree>li>a');  //取商家名那一列
    if(tds.length<1){
        //blockAlert(0)
        //art.dialog.tips('搜不到任何数据...','warning')
        return false;
    }else{
      for(var i = 0; i < tds.length; i++){
        var td = $(tds[i]).eq(0);
    //  blockAlert( input +'|'+ td.html() +'|'+ td.parent().html() +'|'+ td.html().indexOf(input))
    //    alert(td.attr('rev').indexOf(input))
        if(td.text().indexOf(input) >= 0){ //如果商家名这列总某行内容不包含输入的商家名
          td.parent().show();
        }else{
          td.parent().hide();  //隐藏这行
        }
      }
    }
  }
  $(document).on('keyup','.input_search', function(event) {
      //blockAlert(event.keyCode)
      if(event.keyCode==27){
          $('[data-toggle=popover]').popover('hide');return false;
      }
      var Thi=$(this),
      ThiVal=Thi.val();
      if(event.keyCode==13){
          getSearch(ThiVal);
      }
      return false;
  });
}




//获取url里需要的值
function getParam(name){
var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i"),
decode;
decode=(reg.test(location.search))? encodeURIComponent(decodeURIComponent(RegExp.$2.replace(/\+/g, " "))) : '';
decode=decodeURIComponent(decode);
return decode;
}

//相同class点击填充背景，导航选中状态
function aBtnNavFun(a){
  if($(document).on!=undefined){
    $(document).on('click', a, function(event) {
        $(a).removeClass('acurr');
        $(this).addClass('acurr');
    });
  }
}

function menuSetCurr(This){
    This.parent().find('li').removeClass('curr');
    This.addClass('curr')
}
function menuTopFun(){
    var acts=getParam('act'),
    menuBox=$('.menuLi'),
    menuBLi=menuBox.children('li');
    if(acts){
        menuBLi.removeClass('curr');
        menuBox.children('.act_'+acts).addClass('curr')
    }else{
        menuBLi.eq(0).addClass('curr')
    }
    menuBLi.addClass('nobar');
    menuBLi.attr('draggable' , 'false');
}

// 获取归属地信息
function getTelFrom(a,t,callback){
$.ajax({
dataType:'script',
scriptCharset:'gb2312',////////
url:'http://php.tech.sina.com.cn/iframe/download/download_srv.php?action=bst_ms&code='+a,
success:function(e){
if(I_SUCCESS==1){
    //extAlert(a,ms_data[1]+ms_data[2]);
    callback(ms_data[1]+ms_data[2])
}else {
    callback('查询失败');
}
}
})
}
	



function copyToClipBoard(text,callback){ 
var clipBoardContent=text; 
window.clipboardData.setData("Text",clipBoardContent); 
callback('复制成功');
} 








function btnGenjin(a,b){
	art.dialog.open("/house/house_genjin.asp?hid="+a+"&chuzu="+b,{
		title:'跟进 [编号'+a+']',
		height:160,
		width:330,
		button:[{
			name:'跟进规则',
			callback:function(){
				art.dialog({
					content: '<div class="genjinguize" style="font-size:16px; line-height:1.3em;">规则：<br />'+
'1、显示在售房源的，五家公司操作修改成已售或停售，则此房源状态自动改变；<br />'+
'2、显示已售或停售，两家公司再次提交已售或停售，则此房源自动删除；<br />'+
'3、显示已售或停售，两家公司操作修改成其他状态，则此房源状态自动改变；<br />'+
'4、以上所说“公司”非同一公司的分公司、门店；<br />'+
'5、请合理跟进。中介宝将自动屏蔽不良用语，人工删除或修改不良跟进；<br />'+
'6、请勿随跟进填写暗语或自我判定的代码，否则也将人工删除；<br />'+
'7、好房源，来自日积月累，来自点点滴滴，来自我们共同的努力。<div>',
					title:'中国好房源，来自好经纪！——中介宝'
				});
			return false;
			}
		}],okVal:'提交',
		ok: function () {
			var iframe = this.iframe.contentWindow,
			iframe_btn = iframe.document.getElementById('submits');
			$(iframe_btn).click();
			return false;
		},cancel: true
	})
}




/*-=-=-=-=-=-=-=-=-=[ 功能 ]=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
//判断字符串长度
function lens(s) {
var l = 0;
var a = s.split("");
for (var i=0;i<a.length;i++) {
if (a[i].charCodeAt(0)<299) {
l++;
} else {
l+=2;
}
}
return l;
}
//拆分字符串判断另个字符串是否存在
function splitIsStr(a,b,c){
var str=a;
if(b==''){b=','};
strs=str.split(b); //字符分割      
for (i=0;i<strs.length ;i++ ){    
if(strs[i]==c){
return 1;
break;
}else{
return 0;
}
} 
}

/*-=-=-=-=-=-=-=-=-=[ 全选的多选框 开始 ]=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
function selectAll(){
	if(this.checked==true){ 
		checkAll('id'); 
	} else { 
		clearAll('id'); 
	}
}
function checkAll(name){
	var el = document.getElementsByTagName('input');
	var len = el.length;
	for(var i=0; i<len; i++){
		if((el[i].type=="checkbox") && (el[i].name==name)){
			el[i].checked = true;
		}
	}
}
function clearAll(name){
	var el = document.getElementsByTagName('input');
	var len = el.length;
	for(var i=0; i<len; i++){
		if((el[i].type=="checkbox") && (el[i].name==name)){
			el[i].checked = false;
		}
	}
}
//全选的多选框 结束


/*-=-=-=-=-=-=-=-=-=[ cookies ]=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
//写入cookies
function SetCookie(name,value){
	var Days = 30;
	var exp = new Date(); 
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
//读取cookies
function GetCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg)) return unescape(arr[2]);
	else return undefined;
}
//删除cookies
function DelCookie(name){
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval=GetCookie(name);
	if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
//使用示例
//SetCookie("a","yimixia.com");
//alert(GetCookie("a"));
// $(function(){
// //屏蔽JS报错
// function killErrors() {return true;}
// window.onerror = killErrors;	
// })









function getEnumTexts(category,code){
  var arr = category;
  for(var i=0;i<arr.length;i++){
    if(arr[i]['code']==code){
      return arr[i]['name'];
    }
  }
}
/* 封装 */
function jajax(url,param,asyncs){
    YW.ajax({
        type: 'POST',
        async:false,
        url: '/c/dept/listDept',
        data:param,
        success: function(data){
            return JSON.parse(data);
        }
    });
}


function quit(){
  win.focus();
  art.dialog.confirm('退出系统？', function () {
      $.ajax({
        type: 'get',
        url: '/c/user/logout',
        success: function(data){
          WinClose(true);
        },
        error:function(){
          WinClose(true);
        }
      });
  },function(){},'warning');
}

function relogin(){
  art.dialog.confirm('退出并重新登录？', function () {
      $.ajax({
            type: 'get',
            url: '/c/user/logout',
            success: function(data){
                gui.App.clearCache();
                window.location="/login/login.html";
            }
      });
  },function(){},'warning');
}

function check(s){
    var isPhone = new RegExp(/^(0[0-9]{2,3}-?)?[0-9]{7,8}$/);
    var isMob = new RegExp(/^(1[34578][0-9]{9})$/);
    var is400 = /^([0-9]{10})-[0-9]{5,6}$/;
    if(s=="" || s==null || s==undefined){
        return true;
    }
    if(isMob.test(s) || isPhone.test(s) || is400.test(s)){
        return true;
    }else{
        return false;
    }
}
function checkTel(){
    var reg = new RegExp(/^[0-9-/]+$/);
    var inputBox=$("#tel");
    var value=inputBox.val().trim();
    if(value.length == 0){ return true; }
    if(!reg.test(value)){
        infoAlert("输入错误！只能输入‘数字’或‘-’或‘/’");
        inputBox.focus();
        return false;
    }

    var str = value.split("/");
    for(var i = 0; i < str.length; i ++){
        if(check(str[i]) == false){
            infoAlert("号码"+str[i]+"填写错误?请检查电话号码的长度,如果想输入多个号码请用/隔开");
            inputBox.focus();
            return false;
        }
    }
    return true;
}

function checkDateyear(input){
   //检查建筑年代
  // var year = $('#dateyear').val();
  var year = input.val();
  var reg = new RegExp(/^[0-9/]+$/);
  if(year.length>0){
    if(year.length!=4 || !reg.test(year)){
      art.dialog.tips("年代必须为4位数字");
      input.focus();
      // event.preventDefault();
      return false;
    }else{
      var now = new Date();
      if(year>now.getFullYear() || year<1900){
        art.dialog.tips("年代因该在1900到"+now.getFullYear()+"之间");
        input.focus();
        return false;
      }
    }
  }
  return true;
}
function openAddHouse(url){
  art.dialog.open(url,{id:'house_add',width:642,height:500});
}

function initTopMenu(){
  $(".MenuBox").hover(function(){
    $(this).children("div.topMenuChid").hide();
    $(this).children("div.topMenuChid").show();
  },function(){
       $(this).children("div.topMenuChid").hide(); 
  });
}