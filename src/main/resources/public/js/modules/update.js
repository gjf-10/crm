layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    form.on("submit(updateModule)",function(obj){
        var index = layer.open("数据提交中.....", {
            type: 16,
            time: false,
            shade: 0.8
        })
        $.ajax({
            type: "post",
            url: ctx + "/modules/update",
            data: obj.field,
            dataType: "json",
            success: function(res){
                if(res.code==200){
                    layer.msg("修改成功", {icon: 6})
                    top.layer.close(index);
                    layer.closeAll("iframe");
                    parent.location.reload();
                }else{
                    layer.msg(res.msg, {icon: 5})
                }
            }
        })
        return false;
    })
    $("#closeBtn").click(function(){
        var index = parent.layer.getFrameIndex(window.name)
        parent.layer.close(index);
    })
})