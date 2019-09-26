$(function(){
    $.ajax({
        url:"http://localhost:8084/sso/checkLogin",
        dataType:"JSONP",
        jsonpCallback:"checkLogin",
        success:function (data) {
            if (data){
                data = JSON.parse(data);
                $("#pid").html("欢迎"+data.username+"登录码农网！<a href='http://localhost:8084/sso/logout'>注销</a>");
            }else {
                $("#pid").html("<a href='javascript:void(0)' onclick='toLogin()'>登录</a>/<a href='http://localhost:8084/toRegister'>注册</a>");
            }

        }
    })

})
function toLogin() {
    debugger
    var returnUrl=location.href;
    returnUrl=encodeURIComponent(returnUrl);
    location.href="http://localhost:8084/toLogin?returnUrl="+returnUrl;
}