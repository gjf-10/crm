layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    var  tableIns = table.render({
        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '⻆色名', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '⻆色备注', minWidth:100,
                align:'center'},
            {field: 'createDate', title: '创建时间',
                align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间',
                align:'center',minWidth:150},
            {title: '操作', minWidth:150,
                templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });

    $("#search").click(function(){
        table.reload("roleListTable", {
            page:{
                curr: 1
            },
            where:{
                roleName: $("input[name='roleName']").val()
            }
        })
    })

    table.on("toolbar(roles)", function (obj){
        var checkStatus = table.checkStatus(obj.config.id);
        if(obj.event === "add"){
            layer.msg("添加角色")
            openAddOrUpdateRolePage()
        }else if(obj.event === "grant"){
            layer.msg("授权")
            openGrantPage(checkStatus.data)
        }
    })

    table.on("tool(roles)", function (obj){
        if(obj.event === "edit"){
            layer.msg("修改"+obj.data.id)
            openAddOrUpdateRolePage(obj.data.id)
        }else if(obj.event === "del"){
            layer.msg("删除")
            alert(obj.data.id)
            deleteById(obj.data.id)
        }
    })

    function deleteById(roleId){
        layer.confirm("您确定要删除该条数据吗？", {
            btn: ["确定", "取消"]
        }, function (index){
            layer.close(index);
            $.ajax({
                type: "post",
                url: ctx + "/role/del",
                data: {"roleId": roleId},
                success: function(res){
                    if(res.code==200){
                        layer.msg("删除成功", {icon: 6})
                        parent.location.reload();
                    }else{
                        layer.msg(res.msg, {icon: 5})
                    }
                }
            })
        })

    }

    function openAddOrUpdateRolePage(roleId){
        let title = "<h2>角色管理模块 -- 添加 </h2>"
        let url = ctx + "/role/add_update"
        if(roleId){
            title = "<h2>角色管理模块 -- 修改 </h2>"
            url = url + "?roleId=" + roleId
        }

        layui.layer.open({
            type: 2,
            title: title,
            content: url,
            area: ["600px","280px"],
            maxmin: true
        })
    }
    function openGrantPage(data){
        if(data.length == 0){
            layer.msg("请选择被授权的角色")
            return ;
        }
        if(data.length > 1){
            layer.msg("暂未提供多个角色同时授权的功能")
            return ;
        }

        layer.open({
            type: 2,
            title: "<h2>角色授权</h2>",
            content: ctx + "/role/toGrantPage?roleId="+data[0].id,
            area: ["600", "280"],
            maxmin: true
        })
    }
})