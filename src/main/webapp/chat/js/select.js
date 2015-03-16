

function selBoxCge(id){

	/*$("#"+id).siblings();*/
	$("#"+id).siblings().animate({
		   left:'-240px'
		},"fast").css({"z-index":"0","right":"-240px"}).siblings("#"+id).animate({
		   left:'0px'
		},"fast").css("z-index","1");

	if(id=='qunList'){
		$('#qunbox_dot').removeClass('alertDot');
	}
	
	$('#cocoMainSelectId span').removeClass('sle');
	$('#'+id+'Btn').addClass('sle');
}
	 
/*	 $("button").click(function(){
  $("div").animate({
    left:'250px',
    opacity:'0.5',
    height:'150px',
    width:'150px'
  });
}); 
 */
//$(function(){
//	
//	  $("#cocoMainSelectId").children("span").click(function(){
//		 $(this).siblings().removeClass("sle");
//		 $(this).addClass("sle");
//	  });
//	
//});  

