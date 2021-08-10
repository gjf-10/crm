layui.use(['table', 'treetable','layer'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var treeTable = layui.treetable;
    var layer = layui.layer;
    treeTable.render({
        treeColIndex: 1,
        treeSpid: -1,
        treeIdName: 'id',
        treePidName: 'parentId',
        elem: '#munu-table',
        url: ctx + '/modules/list',
        toolbar: "#toolbarDemo",
        treeDefaultClose: true,
        page: true,
        cols: [[
            {type: 'numbers'},
            {field: 'moduleName', minWidth: 100, title: '菜单名称'},
            {field: 'optValue', title: '权限码'},
            {field: 'url', title: '菜单url'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updateDate', title: '更新时间'},
            {
                field: 'grade', width: 80, align: 'center', templet: function(d) {
                    if (d.grade == 0) {
                        return '<span class="layui-badge layui-bg-blue">⽬录</span>';
                    }
                    if (d.grade == 1) {
                        return '<span class="layui-badge-rim">菜单</span>';
                    }
                    if (d.grade == 2) {
                        return '<span class="layui-badge layui-bg-gray">按钮</span>';
                    }
                },
                title: '类型'
            },
            {templet: '#auth-state', width: 180, align: 'center', title: '操作'}
        ]],
        done: function () {
            layer.closeAll('loading');
        }
    });
    table.on("toolbar(munu-table)", function(obj){
        switch(obj.event){
            case "expand":
                treeTable.expandAll('#munu-table');
                break;
            case "fold":
                treeTable.foldAll('#munu-table');
                break;
            case "add":
                layer.msg("添加目录")
                openAddPage(0, -1);
                break;
        };
    });

    table.on("tool(munu-table)", function (obj){
        var data = obj.data;
        if(obj.event === 'add'){
            layer.msg("添加子项")
            if(data.grade == 2){
                layer.msg("暂不支持四级菜单添加操作！")
                return ;
            }
        alert(data.grade+1)
            openAddPage(data.grade+1, data.id)
        }else if(obj.event === 'edit'){
            layer.msg("修改")
            openUpdatePage(data.id);
        }else if(obj.event === 'del'){
            layer.msg("删除")
            layer.confirm("确定要删除该条数据吗？",{
                btn: ["确定", "删除"]
            },function (index){
                layer.close(index);
            })
        }
    })
    function openAddPage(grade, parentId){
        var title = "<h2>菜单模块 -- 添加</h2>";
        var url = ctx + "/modules/toAddPage?grade="+grade+"&parentId="+parentId;
        layui.layer.open({
            type: 2,
            title: title,
            content: url,
            area: ["700px", "450px"],
            maxmin: true,
        })
    }
    function openUpdatePage(id){
        var title = "<h2>菜单模块 -- 修改</h2>"
        var url = ctx + "/modules/toUpdatePage?id="+id;
        layer.open({
            type: 2,
            title: title,
            content: url,
            area: ["700px", "450px"],
            maxmin: true
        })
    }
});