//主要交互逻辑
//js 模块化
var seckill={
    //封装秒杀相关ajax的url
    URL:{

    },
    //详情页秒杀逻辑
    detail:{
        //详情页初始化
        init:function (params) {
            //手机验证和登录
            var killPhone = $.cookie('killPhone');
            var startTime=params['startTime'];
            var endTime=params['endTime'];
            var seckillId=params['seckillId'];
            if (!seckill.validatePhone(killPhone)){
                //绑定手机号 控制输出
                var killPhoneModal = $("#killPhoneModal");
                killPhoneModal.modal({
                    show:true,//显示弹出层
                    backdrop:"static",//禁止位置关闭
                    keyboard:false//关闭键盘事件
                })
                $("#killPhoneBtn").click(function () {
                    var inputPhone = $("#killPhoneKey").val();
                    if (seckill.validatePhone(inputPhone)){
                        $.cookie("killPhone",inputPhone,{expires:7,path:'/seckill'})
                        window.location.reload();
                    }else {
                        $("#killPhoneMessage").hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                })
            }
        }
    },
    validatePhone:function (phone) {
        if(phone && phone.length==11 && !isNaN(phone)){
            return true;
        }else {
            return false;
        }
    }
}