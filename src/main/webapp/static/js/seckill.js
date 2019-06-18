var seckill = {
    //封装秒杀相关的ajax 的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    validatePhone: function (phone) {
        if(phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    handleSeckillKill: function (seckillId, node) {
        //处理秒杀逻辑

        node.html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');

        $.post(seckill.URL.exposer(seckillId), {}, function (rst) {
            if (rst && rst['success']) {
                var exposer = rst['data'];

                if (exposer['exposed']) {
                    //开始秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    console.log("秒杀地址md5：" + md5);

                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("秒杀地址md5：" + killUrl);

                    //绑定一次事件
                    $('#killBtn').one('click', function () {
                        //禁用按钮
                        $(this).addClass('disabled');
                        //执行秒杀
                        console.log(6);
                        $.post(killUrl, {}, function (r) {
                            console.log(r['state']);

                            if (r) {
                                var killResult = r['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                console.log(killResult['stateInfo']);
                                //显示秒杀结果
                                console.log(7);
                                node.html('<span class="label label-success"> ' + stateInfo + "</span>");

                            }
                        });
                        console.log(4);
                        node.show();

                    });

                } else {
                    //可能用户终端与服务器有差别，所以会有时间差
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countdown(seckillId, now, start, end)
                }
            }
            else {
                console.log('result: ' + rst);
            }

        });


    },
    //计时函数
    countdown: function (seckillId, nowTime, startTime, endTime) {
        console.log('当前时间：'+nowTime);
        console.log('开始时间：'+startTime);
        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            //使用jquery 计时函数
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时 : %D天 %H时 %M分 %S秒');
                seckillBox.text(format);
            }).on('finish.countdown', function () {
                seckill.handleSeckillKill(seckillId, seckillBox);
            });
        } else {
            //执行秒杀
            console.log("执行秒杀中 countdown");
            seckill.handleSeckillKill(seckillId, seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail: {
        init: function (params) {
            //手机验证相关操作,计时交互
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //绑定手机号
                var killPhonModal = $('#killPhoneModal');
                killPhonModal.modal({
                    show: true,
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false //关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage')
                            .hide()
                            .html('<label class="label label-danger">手机号错误</label>')
                            .show(300);
                    }
                });
            }
            //已经登录
            //计时交互
            $.get(seckill.URL.now(), {}, function (rst) {
                if (rst) {
                    var nowTime = rst['data'];
                    //时间判断
                    console.log(rst['data']);
                    console.log("进入倒计时");
                    seckill.countdown(seckillId, nowTime, startTime, endTime);

                } else {
                    console.log('获取时间函数并返回失败');
                    console.log('result: ' + rst['data']);
                }
            });
        }
    }
}