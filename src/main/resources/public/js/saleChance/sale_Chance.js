layui.use(['table', 'layer', 'form', 'table'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    var tableIns = table.render({
        elem: '#saleChanceList', // 表格绑定的ID
        url: ctx + '/sale_chance/list', // 访问数据的地址
        cellMinWidth: 95,
        page: true, // 开启分页
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "saleChanceListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'chanceSource', title: '机会来源', align: "center"},
            {field: 'customerName', title: '客户名称', align: 'center'},
            {field: 'cgjl', title: '成功几率', align: 'center'},
            {field: 'overview', title: '概要', align: 'center'},
            {field: 'linkMan', title: '联系人', align: 'center'},
            {field: 'linkPhone', title: '联系电话', align: 'center'},
            {field: 'description', title: '描述', align: 'center'},
            {field: 'createMan', title: '创建人', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center'},
            {field: 'uname', title: '指派人', align: 'center'},
            {field: 'assignTime', title: '分配时间', align: 'center'},
            {
                field: 'state', title: '分配状态', align: 'center', templet: function (d) {
                    return formatterState(d.state);
                }
            },
            {
                field: 'devResult', title: '开发状态', align: 'center', templet: function (d) {
                    return formatterDevResult(d.devResult);
                }
            },
            {
                title: '操作', templet: '#saleChanceListBar', fixed: "right", align: "center",
                minWidth: 150
            }
        ]]
    });

    /**
     * 格式化分配状态
     * 0 - 未分配
     * 1 - 已分配
     * 其他 - 未知
     * @param state
     * @returns {string}
     */
    function formatterState(state) {
        if (state == 0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if (state == 1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态
     * 0 - 未开发
     * 1 - 开发中
     * 2 - 开发成功
     * 3 - 开发失败
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value) {
        if (value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if (value == 1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if (value == 2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if (value == 3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    $("#search").click(function (){
        table.reload('saleChanceListTable', {
            where: { //设定异步数据接口的额外参数，任意设
                customerName: $("input[name='customerName']").val(),
                createMan: $("input[name='createMan']").val(),
                state: $("#state").val()
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        }); //只重载数据
    })

    table.on('toolbar(saleChances)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case 'add':
                layer.msg('添加');
                openAddOrUpdateDialog();
                break;
            case 'del':
                deleteSaleChance(checkStatus.data);
                break;
        };
    });

    table.on('tool(saleChances)', function (obj) {
        var data = obj.data;
        if(obj.event === "edit"){
            layer.msg("编辑")
            openAddOrUpdateDialog(data.id);
        }else if(obj.event === "del"){

            layer.confirm("确认要删除这条数据吗？", {
                btn: ["确认", "取消"]
            }, function (index){
                // 关闭确认框
                layer.close(index);
                $.ajax({
                    type: "post",
                    url: ctx + "/sale_chance/del",
                    data: {"ids": data.id},
                    dataType: "json",
                    success: function (res){
                        if(res.code === 200){
                            layer.msg("删除成功...", {icon: 6});
                            tableIns.reload();
                        }else{
                            layer.msg(res.msg, {icon: 5});
                        }
                    }
                })
            })
        }
    })

    function openAddOrUpdateDialog(saleChanceId){
        // 如果没有id说明是添加
        // 1. url
        let url = ctx + "/sale_chance/add_update_page"
        let title = "<h2> 营销机会管理 - 机会添加 </h2> >"
        if(saleChanceId){
            title = "<h2> 营销机会管理 - 机会修改 </h2>"
            url = url + "?id=" + saleChanceId;
        }
        // 打开一个弹出框
        layui.layer.open({
            title:title,
            type:2,
            content: url,
            area:["500px","620px"],
            maxmin:true
        });

    }

    /*
        删除
     */

    function deleteSaleChance(data){
        if(data.length === 0){
            layer.msg("选择要删除的数据")
            return ;
        }
        layer.confirm("您确定要删除选中的数据吗？",{
            btn:["确认", "取消"]
        }, function (index){
            layer.close(index);
            var ids = "ids=";
            for (let i = 0; i < data.length; i++) {
                if(i < data.length - 1){
                    ids = ids + data[i].id + "&ids=";
                }else{
                    ids = ids + data[i].id;
                }
            }
            alert(ids)
            $.ajax({
                type: "post",
                url: ctx + "/sale_chance/del",
                data: ids,
                success: function (res){
                    if(res.code === 200){
                        layer.msg("删除成功", {icon: 6});
                        tableIns.reload();
                    }else{
                        layer.msg(res.msg, {icon: 5})
                    }
                }
            })

        })
    }
});