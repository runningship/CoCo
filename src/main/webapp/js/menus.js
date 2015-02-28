var queryHistory=[];
var currentQueryIndex=-1;
function Menu() {
  var gui = require('nw.gui');
  var menu = new gui.Menu();

  var refresh = new gui.MenuItem({
    label: "刷 新",
    type: 'normal',
    click: function() {
      window.location.href=window.location.pathname+window.location.search;
    }
  });
  // refresh.style.width="50px;";
  var back = new gui.MenuItem({
    label: "后 退",
    click: function() {
      if(currentQueryIndex>0){
        currentQueryIndex--;

        var inputs = $('form input');
         //先清空
        inputs.each(function(i,input){
          if(input.type=='input'){
            input.value="";
          }else{
            input.checked=false;
          }
        });
        $(queryHistory[currentQueryIndex]).each(function(index,obj){
          if(obj.value==''){
            if(obj.name!='ztai' && obj.name!='she'){
              return;  
            }
          }

          inputs.each(function(i,input){
            if(obj.name!=input.name){
              return;
            }
            if(input.type=='radio' || input.type=='checkbox'){
              if(input.value==obj.value){
                input.checked=true;
              }
              // else{
              //   input.checked=false;
              // }
            }else{
              input.value = obj.value;
            }
          });
          // 查询
          doSearchAndSelectFirst();
        });
      }
    }
  });
  var forward = new gui.MenuItem({
    label: "前 进",
    click: function() {
      window.history.forward();
    }
  });
  menu.append(back);
  menu.append(forward);
  menu.append(refresh);
  return menu;
}
var menu = new Menu();
$(document).on("contextmenu", function(e) {
  e.preventDefault();
  menu.popup(e.originalEvent.x+30, e.originalEvent.y);
  // menu.popup(e.clientX, e.clientY);
  // alert(e);
});

function pushQueryToHistory(query){
  queryHistory.push(query);
  currentQueryIndex = queryHistory.length-1;
}