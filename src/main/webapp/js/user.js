/*
两级级联用户下拉框选择
*/
var groupUserByDeptData;
$(document).ready(function() {
    //cid应该在调用页面中定义好
    //var cid=$${cid};
    YW.ajax({
        url:'/c/user/groupUserByDept?cid='+cid+'&dataScope='+dataScope,
        timeout:3000,
        async:false,
        dataType:'json',
        success:function (data, textStatus) {
            if(data.result.length>0){
                groupUserByDeptData=data;
                buildDepts();
            }
        }
    })
});
function buildDepts(){
    var dept_select=$(".dept_select");
    if(groupUserByDeptData.result.length>1){
        if(!dept_select.attr('NoAll')){
            dept_select.append('<option value="">所有</option>');
        }
        
    }
    $.each(groupUserByDeptData.result, function(index, item) {
        dept_select.append('<option value="'+item.did+'">'+item.dname+'</option>');
    });
    buildUsers(dept_select.val());
    dept_select.bind('change',function(){
        buildUsers(dept_select.val());
    });
}
function buildUsers(did){
    var users;
    for(var i=0;i<groupUserByDeptData.result.length;i++){
        if(groupUserByDeptData.result[i]['did']==did){
            users = groupUserByDeptData.result[i]['users'];
            break;
        }
    }
    var user_select=$(".user_select");
    user_select.empty();
    if(users!=undefined){
        if(users.length>1){
            if(!user_select.attr('NoAll')){
                user_select.append('<option value="">所有</option>');
            }
            
        }
        for(var i=0;i<users.length;i++){
            var user = users[i];
            user_select.append('<option value="'+user.uid+'">'+user.uname+'</option>');
        }   
    }
}