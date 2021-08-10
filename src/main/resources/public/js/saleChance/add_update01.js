layui.use(['form', 'jquery', 'layer', 'jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery;



    form.on("submit(addOrUpdateSaleChance)",function (data){
        var index = layer.msg("数据提交中.....", {
            icon: 16,
            time: false,
            shade: 0.8
        })
            //添加的url
        var url = ctx + "/sale_chance/add";

        var id = $("input[name='id']").val();
        if(id){
            // 修改
            url = ctx + "/sale_chance/change"
        }
        $.ajax({
            type: "post",
            url: url,
            data: data.field,
            success: function (res){
                if(res.code===200){
                    layer.msg("添加成功", {icon: 6});
                    layer.close(index);
                    layer.closeAll("iframe");
                    parent.location.reload();
                } else{
                    layer.msg("数据添加失败....", {icon: 5});
                    layer.close(index);
                }
            }
        })
        return false;
    });

    $("#closeBtn").click(function(){
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })


    $.get(ctx + "/user/assign_man", function(res){
        for (const i in res) {
            var assignMan = $("input[name='man']").val();
            if(assignMan == res[i].id){
                $("#assignMan").append('<option value="'+res[i].id+'" selected>'+res[i].true_name+'</option>')
            }else{
                $("#assignMan").append('<option value="'+res[i].id+'">'+res[i].true_name+'</option>')
            }
        }
        layui.form.render("select");

    })

})