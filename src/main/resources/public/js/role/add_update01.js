layui.use(['form', 'jquery', 'layer', 'jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery;

    form.on("submit(addOrUpdateRole)", function(obj){
        var index = top.layer.msg("数据提交中....", {
            icon:16,
            time: true,
            shade: 0.8
        })
        var url = ctx + "/role/add";
        if(obj.field.id){
            url = ctx + "/role/update"
        }
        $.ajax({
            type: "post",
            url: url,
            data: obj.field,
            success: function(res){
                if(res.code == 200){
                    layer.msg("操作成功", {icon: 6})
                    top.layer.close(index);
                    parent.location.reload();
                }else{
                    layer.msg(res.msg, {icon: 5});
                }
            }
        })
        return false;
    })
    $("#search").click(function (){
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })
})