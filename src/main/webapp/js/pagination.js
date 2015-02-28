/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2014-06-17 10:55:28
 * @version $Id$
 */

/*
p   当前页码   
pn  总页码
ln  总条数

autoCount      
currentPageNo  
firstOfPage    
lastOfPage     
pageSize       
pageUrl        
totalCount     
totalPageCount
*/
var Page={
  'p':'',
  'pn':'',
  'ln':'',
  'prev_disabled':'',
  'next_disabled':'',
  'pageInfos':'',
  'pageNumInfos':'',
  'minp':'',
  'maxp':'',
  'nump':'',
  'pageHtml':'',
  'btn_css':' btn-primary',
  setPageInfo:function(page){
      this.p=page['currentPageNo'];
      this.pn=page['totalPageCount'];
      this.ln=page['totalCount'];
      $('.foot_page_box').html("");
      this.pageHtml=""
      if(this.p<=1){
        this.prev_disabled=" disabled='disabled'"
        this.next_disabled=""
      }else if(this.p>=this.pn){
        this.next_disabled=" disabled='disabled'"
        this.prev_disabled=""
      }else{
        this.next_disabled=""
        this.prev_disabled=""
      }
    if(this.pn <= 10){
      //总页数小于10
      for(var i = 1;i<=this.pn;i++){  
        if(this.p == i){  
            this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list hand'+this.btn_css+'">'+i+'</button>';
        }else{  
            this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list hand">'+i+'</button>';
        }  
      }
    }else{
      var pshow = 3;
      if(window.screen.width>=1028 && window.screen.width<1366){
        pshow=5;
      }else if(window.screen.width>=1366 && window.screen.width<1600){
        pshow=6;
      }else if(window.screen.width>=1600){
        pshow=8;
      }
      if(this.p<pshow){  
        for(var i = this.p-1;i>0;i--){  
            this.pageHtml='<button type="button" action="page" class="btn btn-default btn_p_list hand">'+i+'</button>'+this.pageHtml; 
        }  
        this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list hand '+this.btn_css+'">'+this.p+'</button>';
        for(var i=1;i<=pshow-2;i++){
          this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list hand">'+ (this.p+i) +'</button>';  
        }
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+1) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+2) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+3) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+4) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+5) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+6) +'</button>';
        this.pageHtml+='<button type="button" action="page" class="btn btn-default hand">...</button>';
      }else if(this.p>=pshow && (this.p<=this.pn-pshow+1)){
        this.pageHtml+='<button type="button" action="page" class="btn btn-default hand">...</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-6) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-5) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-4) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-3) +'</button>';
        for(var i=2-pshow;i<pshow-1;i++){
          if(i==0){
            this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list hand'+this.btn_css+'">'+this.p+'</button>';
          }else{
            this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list hand">'+ (this.p+i) +'</button>';
          } 
        }
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-2) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-1) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list '+this.btn_css+'">'+this.p+'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+1) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+2) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+3) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+4) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+5) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p+6) +'</button>';
        this.pageHtml+='<button type="button" action="page" class="btn btn-default hand">...</button>';
      }else if(this.p>this.pn-pshow){
        //尾页
        this.pageHtml+='<button type="button" action="page" class="btn btn-default hand">...</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-6) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-5) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-4) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-3) +'</button>';
        for(var i=pshow-2;i>0;i--){
          this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list hand">'+ (this.p-i) +'</button>';  
        }
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-2) +'</button>';
        // this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list">'+ (this.p-1) +'</button>';
        this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list hand'+this.btn_css+'">'+this.p+'</button>';
        for(var i = this.p+1;i<=this.pn;i++){
            this.pageHtml+='<button type="button" action="page" class="btn btn-default btn_p_list hand">'+i+'</button>';
        }
      }
    }


      var pageInfoStrs=''+
      '<div class="btn-group page_btn_group">'+
      '  <button type="button" class="btn btn-default" title="总条数">'+this.ln+'</button>'+
      '  <button type="button" class="btn btn-default" title="当前页码/总页码">'+this.p+'/'+this.pn+'</button>'+
      '</div>'+
      '<div class="btn-group">';
      pageInfoStrs=pageInfoStrs+
      '  <button type="button" action="page" class="btn btn-default btn_p_prev_no hand"'+this.prev_disabled+'>首页</button>';
      pageInfoStrs=pageInfoStrs+
      '  <button type="button" action="page" class="btn btn-default btn_p_prev hand"'+this.prev_disabled+'>上一页</button>';
      pageInfoStrs=pageInfoStrs+this.pageHtml;
      pageInfoStrs=pageInfoStrs+
      '  <button type="button" action="page" class="btn btn-default btn_p_next hand"'+this.next_disabled+'>下一页</button>';
      pageInfoStrs=pageInfoStrs+
      '  <button type="button" action="page" class="btn btn-default btn_p_next_no hand"'+this.next_disabled+'>尾页</button>'+
      '</div>';

      $('.foot_page_box').html(pageInfoStrs);
    },
    Init:function(){
      $('form[name=form1]').append('<input type="hidden" class="pageInput" name="currentPageNo" value="">');
      $('form[name=form1]').append('<input type="hidden" name="orderBy" value="">');
      $('form[name=form1]').append('<input type="hidden" name="order" value="">');
      $(document).on('click', '.btn_p_prev_no', function(event) {
        $('.pageInput').val(1);
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      }).on('click', '.btn_p_prev', function(event) {
        if(Page.p-1<=Page.pn){$('.pageInput').val(Page.p-1);}
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      }).on('click', '.btn_p_next', function(event) {
        if(Page.p+1<=Page.pn){$('.pageInput').val(Page.p+1);}
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      }).on('click', '.btn_p_next_no', function(event) {
        $('.pageInput').val(Page.pn);
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      }).on('click', '.btn_p_list', function(event) {
        var ThiVal=$(this).text();
      //  alert($(this).text())
        $('.pageInput').val(ThiVal);
        if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
      });
    },
    setOrderField:function(obj,field){
      var order = $(obj).attr('order');
      if(order=="desc"){
        order = "asc";
      }else{
        order= "desc";
      }
      $(obj).attr('order',order);
      $('input[name=order]').val(order);
      $('input[name=orderBy]').val(field);
      if($('.btn_subnmit').length>0){$('.btn_subnmit').click();}else{$('form[name=form1]').submit();}
    }
}