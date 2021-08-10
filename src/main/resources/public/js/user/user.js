layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * ⽤户列表展示
     */
    var tableIns = table.render({
        elem: '#userList',
        url: ctx + '/user/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [5, 10, 15, 20],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "userListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "id", title: '编号', fixed: "true", width: 80},
            {field: 'userName', title: '⽤户名', minWidth: 50, align: "center"},
            {field: 'email', title: '⽤户邮箱', minWidth: 100, align: 'center'},
            {field: 'phone', title: '⽤户电话', minWidth: 100, align: 'center'},
            {field: 'trueName', title: '真实姓名', align: 'center'},
            {
                field: 'createDate', title: '创建时间',
                align: 'center', minWidth: 150
            },
            {
                field: 'updateDate', title: '更新时间',
                align: 'center', minWidth: 150
            },
            {
                title: '操作', minWidth: 150,
                templet: '#userListBar', fixed: "right", align: "center"
            }
        ]]
    });

    $("#search").click(function (){
        table.reload("userListTable",{
            where:{
                userName: $("input[name='userName']").val(),
                email: $("input[name='email']").val(),
                phone: $("input[name='phone']").val()
            },
            page:{
                curr: 1
            }
        })
    })

    table.on("toolbar(users)", function (obj){
        var checkStatus = table.checkStatus(obj.config.id);
        if(obj.event === "add"){
            layer.msg("添加");
            openAddOrUpdateUserPage();
        }else if(obj.event === "del"){
            layer.msg("删除");
            deleteUsers(checkStatus.data);
        }
    })
    table.on("tool(users)", function (obj){
        var data = obj.data;
        if(obj.event === "edit"){
            layer.msg("修改")
            openAddOrUpdateUserPage(data.id);
        }else if(obj.event === "del"){
            layer.msg("删除");
            layer.confirm("确定要删除该条数据吗？",{
                btn:["确认", "取消"]
            }, function (index){
                layer.close(index);
                $.ajax({
                    type: "post",
                    url: ctx + "/user/del",
                    data: {"ids": data.id},
                    dataType: "json",
                    success: function(res){
                        if(res.code == 200){
                            layer.msg("删除成功",{icon: 6})
                            tableIns.reload();
                        }else{
                            layer.msg("删除失败");
                        }
                    }
                })
            })
        }
    })

    function openAddOrUpdateUserPage(userId) {
        var title = "<h2> 用户模块 -- 添加</h2>";
        var url = ctx + "/user/add_update";
        if(userId){
            title = "<h2> 用户模块 -- 修改</h2>"
            url = url + "?userId="+userId;
        }
        layer.open({
            title: title,
            type: 2,
            content: url,
            area: ["500px", "620px"],
            maxmin: true
        })
    }

    function deleteUsers(data){
        if(data.length === 0){
            layer.msg("选择要删除的数据")
            return ;
        }
        var ids = "ids=";
        for (let i = 0; i < data.length; i++) {
            if(i < data.length-1){
                ids = ids + data[i].id + "&ids=";
            }else{
                ids = ids + data[i].id;
            }
        }
        layer.confirm("您确定要删除选中的数据吗？", {
            btn: ["确定", "取消"]
        }, function (index){
            // 关闭确认框
            layer.close(index);
            $.ajax({
                type: "post",
                url: ctx + "/user/del",
                data: ids,
                dataType: "json",
                success: function (res){
                    if(res.code == 200){
                        layer.msg("删除成功", {icon: 6});
                        tableIns.reload();
                    }else{
                        layer.msg("删除失败", {icon: 5})
                    }
                }
            })
        })
    }
})