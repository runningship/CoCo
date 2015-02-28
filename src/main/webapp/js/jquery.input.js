// JavaScript Document
jQuery.fn.extend({
	input_tips:function(){var m=$(this);if(m.val()!=''){return}var t=m.attr('tips'),p=m.parent(),n=m.is('input');if(typeof t=='undefined'){return}if(p.css('position')&&$.inArray(p.css('position'),['relative','absolute','fixed'])<0){p.css('position','relative')}var pos=m.position();if(false && pos.left==0){return}m.after('<span style="font-size:12px;color:#666;margin:0;padding:0;position:absolute;left:'+(pos.left+10)+'px;top:'+(n?pos.top:pos.top+8)+'px;line-height:'+(n?m.outerHeight(true):16)+'px">'+t+'</span>');var s=m.next('span');var r=function(){m.focus()};m.focus(function(){s.animate({'left':pos.left+30+'px','opacity':0.5},300)}).keydown(function(){s.hide()}).blur(function(){if($(this).val()==''){s.show().animate({'left':pos.left+10+'px','opacity':1},300)};s.click(function(){r()});})}
});
$(function() {
	$('input[type=text],input[type=password],textarea').each(function() {
		$(this).input_tips();
	});
});