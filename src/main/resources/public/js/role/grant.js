
var zTreeObj = null;

$(function (){
    var roleId = $("#roleId").val();
    $.ajax({
        type: "post",
        url: ctx + "/modules/ztreeData?roleId="+roleId,
        dataType: "json",
        success: function (res){
            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    },
                    key:{
                        name: "moduleName",
                        checked: "selected"
                    }
                },
                check: {
                    enable: true,

                    chkboxType: { "Y": "ps", "N": "ps" }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }

            };
            var zNodes = res;
            zTreeObj=$.fn.zTree.init($("#test1"), setting, zNodes);
        }
    })
})
function zTreeOnCheck(event, treeId, treeNode) {
    var nodes = zTreeObj.getCheckedNodes(true);
    var mids = "mids="
    for(let i = 0; i< nodes.length; i++){
        if(i < nodes.length - 1){
            mids = mids + nodes[i].id + "&mids=";
        }else{
            mids = mids + nodes[i].id;
        }
    }
    var roleId = $("#roleId").val();
    $.ajax({
        type: "post",
        url: ctx + "/role/grantRole?roleId="+roleId +"&"+ mids,
        success: function(res){
            if(res.code == 200){
                alert("授权成功");
            }else{
                alert(res.msg);
            }
        }
    })

};