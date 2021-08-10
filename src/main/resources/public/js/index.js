layui.use(['form', 'jquery', 'layer', 'jquery_cookie'], function () {
        var form = layui.form,
            layer = layui.layer,
            $ = layui.jquery,
            $ = layui.jquery_cookie($);


        form.on("submit(login)", function (data) {
            let dataField = data.field;
            // 判断用户名密码是否为空
            if (dataField.username === "undefined" && dataField.username.trim() === "") {
                alert("用户名不能为空");
                return false;
            }
            if (dataField.password === "undefined" && dataField.password.trim() === "") {
                alert("密码不能为空");
                return false;
            }

            $.ajax({
                type: "get",
                url: ctx + "/user/login",
                data: {
                    "userName": dataField.username,
                    "userPwd": dataField.password
                },
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    if (data.code === 200) {
                        $.cookie("userIdStr", data.result.userIdStr)
                        $.cookie("userName", data.result.userName)
                        $.cookie("trueName", data.result.trueName)
                        if($("#rememberMe").prop("checked")){
                            $.cookie("userIdStr", data.result.userIdStr, {expires:7})
                            $.cookie("userName", data.result.userName,{expires: 7})
                            $.cookie("trueName", data.result.trueName, {expires:7})
                        }
                        layer.msg(data.msg, {
                            icon: 6,
                            time: 2000
                        })
                        window.parent.location.href = ctx + "/main";
                    } else {
                        layer.msg(data.msg, {icon: 5})
                    }

                }
            })
            return false;
        })

    }
);