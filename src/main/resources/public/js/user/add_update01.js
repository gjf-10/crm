layui.use(['table','layer', 'formSelects'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        form = layui.form,
        $ = layui.jquery,
        table = layui.table;
    var formSelects = layui.formSelects;

    form.on("submit(addOrUpdateUser)", function (data){
        var index = top.layer.msg("数据提交中.....", {
            icon:16,
            time: true,
            shade: 0.8
        });
        var url = ctx + "/user/add";
        if(data.field.id){
            url = ctx + "/user/update";
        }
        $.ajax({
            type: "post",
            url: url,
            data: data.field,
            dataType: "json",
            success: function(res){
                if(res.code == 200){
                    top.layer.msg("操作成功", {icon: 6})
                    top.layer.close(index);
                    layer.closeAll("iframe");
                    parent.location.reload();
                }else{
                    layer.msg(res.msg, {icon: 5});
                    top.layer.close(index);
                }
            }
        })
        return false;
    })

    var userId = $("input[name='id']").val();
    formSelects.config("selectId", {
        type: "post",
        searchUrl: ctx + "/role/queryAllRoles?userId="+userId,
        keyName: "role_name",
        keyVal: "id",
    }, true);

    $("#closeBtn").click(function (){
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })
})