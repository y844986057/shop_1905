function add(id){
    debugger
    var cart=new Object();
    var num=$("#count_"+id).val();
    cart.gid=id;
    cart.num=num;
    // $.post("http://localhost:8085/cart/addCart2",cart,function (data) {
    //     location.reload();
    // })
    $.ajax({
        url:"http://localhost:8085/cart/addCart2",
        data:cart,
        dataType:"JSONP",
        jsonpCallback:"addCart",
        success:function (data) {
            location.reload();
        }
    })

}
function sub(id){
    debugger
    var num=$("#count_"+id).val();
    if (num>1){
        // $.post("http://localhost:8085/cart/subCart/"+id,"",function (data) {
        //     location.reload();
        // })
        $.ajax({
            url:"http://localhost:8085/cart/subCart/"+id,
            dataType:"JSONP",
            jsonpCallback:"subCart",
            success:function (data) {
                location.reload();
            }
        })
    }
}