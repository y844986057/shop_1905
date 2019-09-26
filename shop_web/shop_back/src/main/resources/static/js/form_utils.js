

function fromToObject(form){
	// 1.序列化表单对象
	var array = form.serializeArray();

	// 2.创建一个对象
	var param = new Object();

	// 3.遍历数组，把数据中的数据放入到对象中
	for (var i = 0; i < array.length; i++) {
		var name = array[i].name; // 表单中的属性名称
		var value = array[i].value; // 对象的属性值

		param[name] = value;
	}
	return param;
}

function submitFrom(url,form){
	
	var param = fromToObject(form);
	
	// 4.把对象通过异步的方式传递服务端
	$.post(url, param, function(
			data) {
		if (data.state =='SUCCESS') {
			layer.msg(data.message, {
				icon : 1, // 提示图标
				time : 1000 // 1s后自动关闭
			}, function() {

				// 获取框的索引
				var index = parent.layer
						.getFrameIndex(window.name);
				
				// 关闭框
				parent.layer.close(index);

			})
		} else {
			layer.msg(data.message, {
				icon : 2,
				time : 1000
			})
		}
	}, "JSON");
}


function format(json){
    var ret = [], o = {};
 
    function add(arr, data){
        var obj = {
            "id": data.id,
            "pId": data.pId,
            "name":data.name,
            "open":true,
            "children": [],
            "checked":data.checked 
        };
        o[data.id] = obj;
        arr.push(obj);
    }
 
    json.forEach(x => {
        if(o[x.pId]){
            add(o[x.pId].children, x);
        }else{
            add(ret, x);
        }
    });
    return ret;
}