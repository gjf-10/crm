layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.render();
    form.on("submit(addModule)", function(obj){
        var index = top.layer.msg("数据提交中.....", {
            icon:16,
            time: true,
            shade: 0.8
        });
        $.ajax({
            type: "post",
            url: ctx + "/modules/add",
            data: obj.field,
            dataType: "json",
            success: function(res){
                if(res.code==200){
                    layer.msg("添加成功", {icon: 6})
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