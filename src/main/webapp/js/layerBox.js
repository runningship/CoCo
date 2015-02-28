      function layerShowBox(){
		  
		  var w = $("#layerBoxDj").width();
		  var h = $("#layerBoxDj").height();
		  var winW = $(window).width();
		  var winH = $(window).height();
		  var L = (winW - w)/2;
		  var T = (winH - h)/2;
		  
		  if(L<=0) L=0;
		  if(T<=0) T=0;
		  
		  $("#layerBoxDj").css("left",L);
		  $("#layerBoxDj").css("top",T);
		  
	  }
	  
	  function showBox(){
		 
		 $("#layerBoxDj").css("display","block");
		 $("#layerBoxBgShadow").css("display","block");
		 layerShowBox();
		   
	  }
	  
	  function closeBox(){
		 
		 $("#layerBoxDj").css("display","none");
		 $("#layerBoxBgShadow").css("display","none");
		   
	  }
	  
	  window.onresize = function(){
		  layerShowBox();
	  }
