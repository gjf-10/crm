layui.use(['table','layer', 'form'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        form = layui.form,
        $ = layui.jquery,
        table = layui.table;

    form.on("submit(saveBtn)", function (obj){
        layer.confirm("您确认修改吗？",{
            btn: ["确认", "取消"]
        }, function (index){
            // 关闭确认框
            layer.close(index);

            $.ajax({
                type: "post",
                url: ctx + "/user/changeInfo",
                data: obj.field,
                dataType: "json",
                success: function (res){
                    if(res.code == 200){
                        layer.msg("修改成功", {icon: 6});
                        parent.location.href = ctx+"/main";
                    }else{
                        layer.msg(res.msg, {icon: 5});
                    }
                }
            })
        })
        return false;
    })
})