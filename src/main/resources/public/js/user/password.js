layui.use(['form', 'jquery', 'layer', 'jquery_cookie'], function () {
        var form = layui.form,
            layer = layui.layer,
            $ = layui.jquery,
            $ = layui.jquery_cookie($);


        form.on("submit(saveBtn)", function (data) {
            let dataField = data.field;
            let flag = isEmptyCheck(dataField.old_password, dataField.new_password, dataField.again_password)
            if (!flag){
                layer.msg("输入信息不全")
                return ;
            }
            if(dataField.new_password !== dataField.again_password){
                layer.msg("新密码和确认密码不一致");
                return ;
            }

            $.ajax({
                type: "put",
                url: ctx+"/user/updatePassword",
                data: {
                    "oldPwd": dataField.old_password,
                    "newPwd": dataField.new_password,
                    "againPwd": dataField.again_password
                },
                dataType: "json",
                success: function(data){
                    console.log(data);
                    if(data.code === 200){
                        // 修改成功，删除cookie记录,返回登录页面
                        $.removeCookie("userIdStr")
                        $.removeCookie("userName")
                        $.removeCookie("trueName")
                        layer.msg(data.msg, function (){
                            alert("3秒后跳转到登录页面")
                            window.parent.location.href = ctx + "/index";
                        })
                    }else{
                        layer.msg(data.msg);
                    }
                }
            })
            return false;
        })

        // 输入框非空检查
        function isEmptyCheck(pwd1, pwd2, pwd3){
            if(pwd1 === "undefined" || pwd1.trim() ===""){
                return false;
            }
            if(pwd2 === "undefined" || pwd2.trim() ===""){
                return false;
            }
            if(pwd3 === "undefined" || pwd3.trim() ===""){
                return false;
            }
            return true;
        }

    }
);