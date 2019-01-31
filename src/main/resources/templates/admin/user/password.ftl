<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>表单验证</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/hadmin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/style.css?v=4.1.0" rel="stylesheet">

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>修改用户密码</h5>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal m-t" id="frm" method="post" action="">
                        	<input type="hidden" id="id" name="id" value="${user.id}">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">原密码：</label>
                                <div class="col-sm-8">
                                    <input id="oldPassword" name="oldPassword" class="form-control" type="password" value=""  >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">新密码：</label>
                                <div class="col-sm-8">
                                    <input id="password" name="password" class="form-control" type="password" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">确认密码：</label>
                                <div class="col-sm-8">
                                    <input id="confirmPassword" name="confirmPassword" class="form-control" type="password" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-8 col-sm-offset-3">
                                    <button class="btn btn-primary" type="submit">提交</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <!-- 全局js -->
    <#include "/admin/common/common.ftl">
    <script src="${ctx!}/hadmin/js/plugins/layer/laydate/laydate.js"></script>
    <script type="text/javascript">
    $(document).ready(function () {


	    $("#frm").validate({
    	    rules: {
    	    	oldPassword: {
    	        required: true,
    	        minlength: 6,
    	    	maxlength: 26
    	      },
    	      	password: {
    	        required: true,
    	        minlength: 6,
    	    	maxlength: 26
    	      },
                confirmPassword: {
                    required: true,
                    minlength: 6,
                    maxlength: 26
    	    	}
    	    },
    	    messages: {},
    	    submitHandler:function(form){
    	    	$.ajax({
                    type: "POST",
                    dataType: "json",
   	    		    url: "${ctx!}/admin/user/save_password/${user.id}",
   	    		    data: $(form).serialize(),
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index);
                        });
                    }
   	    		});
            }
    	});
    });
    </script>

</body>

</html>
