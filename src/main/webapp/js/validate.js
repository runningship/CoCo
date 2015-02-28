
function checkmji(input){
  if((event.keyCode>=48 && event.keyCode<=57) || (event.keyCode>=96 && event.keyCode<=105) || event.keyCode==110 || event.keyCode==190){
    var val = $(input).val();
    if(event.keyCode==110 || event.keyCode==190){
      //检查小数点
      val = val.substring(0,val.length-1);
      if(val.indexOf('.')>-1){
        //已经有小数点了
        // event.preventDefault();
        input.value=input.value.substring(0,input.value.length-1);
      }else{
        if(val=="" || val==null || val==undefined){
          //小数点在第一位
          // event.preventDefault();
          input.value=input.value.substring(0,input.value.length-1);
        }
      }
    }else{
      var arr = val.split(".");
      if(arr.length>1){
        if(arr[1].length>2){
          //最大两位小数
          // event.preventDefault();
          input.value=input.value.substring(0,input.value.length-1);
        }
      }else{
        if(arr[0].length>4){
          //最大四位整数
          // event.preventDefault();
          input.value=input.value.substring(0,input.value.length-1);
        }
      }
    }
  }else{
    if(isCtrlKey(event.keyCode)==false){
      // event.preventDefault();
      input.value=input.value.substring(0,input.value.length-1);
    }
  }
}

function checkzjia(input){
  if((event.keyCode>=48 && event.keyCode<=57) || (event.keyCode>=96 && event.keyCode<=105)){
    var val = $(input).val();
    if(val.length>4){
      //最大四位整数
      // event.preventDefault();
      input.value=input.value.substring(0,input.value.length-1);
    }
  }else{
    if(isCtrlKey(event.keyCode)==false){
      // event.preventDefault();
      input.value=input.value.substring(0,input.value.length-1);
    }
  }
}

function checkdjia(input){
  if((event.keyCode>=48 && event.keyCode<=57) || (event.keyCode>=96 && event.keyCode<=105)){
    var val = $(input).val();
    if(val.length>5){
      //最大四位整数
      // event.preventDefault();
      input.value=input.value.substring(0,input.value.length-1);
    }
  }else{
    if(isCtrlKey(event.keyCode)==false){
      // event.preventDefault();
      input.value=input.value.substring(0,input.value.length-1);
    }
  }
}

function checklceng(input){
  if((event.keyCode>=48 && event.keyCode<=57) || (event.keyCode>=96 && event.keyCode<=105)){
    var val = $(input).val();
    if(val.length>2){
      //最大四位整数
      // event.preventDefault();
      input.value=input.value.substring(0,input.value.length-1);
    }
  }else{
    if(isCtrlKey(event.keyCode)==false){
      // event.preventDefault();
      input.value=input.value.substring(0,input.value.length-1);
    }
  }
}

function checkyear(input){
  if((event.keyCode>=48 && event.keyCode<=57) || (event.keyCode>=96 && event.keyCode<=105)){
    var val = $(input).val();
    if(val.length>4){
      //最大四位整数
      // event.preventDefault();
      input.value=input.value.substring(0,input.value.length-1);
    }
  }else{
    if(isCtrlKey(event.keyCode)==false){
      // event.preventDefault();
      input.value=input.value.substring(0,input.value.length-1);
    }
  }
}

function jianCe(a){
  var count = 0;
  $('input[name="'+a.name+'"]').each(function(i,obj){
  if(obj.checked){
    count++;
    }
  });
  if (count>5) {
    a.checked=false;
    alert('最多只能选择五项');
  };
}

function isCtrlKey(keyCode) {
    // 8        - 退格
    // 9        - Tab
    // 13       - 回车
    // 16~18    - Shift, Ctrl, Alt
    // 37~40    - 左上右下
    // 35~36    - End Home
    // 46       - Del
    // 112~123  - F1-F12
    switch (keyCode) {
        case 8: case 9: case 13:
        case 16: case 17: case 18:
        case 37: case 38: case 39: case 40:
        case 35: case 36: case 46:
            return true;
        default:
            if (keyCode >= 112 && keyCode <= 123) {
                return true;
            }
            return false;
    }
}