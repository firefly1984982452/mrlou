REG(function(ajax){
    function openLoading(){
        if(document.getElementById('loading_icon')){
            return;
        }
        var loadingMask = document.createElement('div');
        var loadingIcon = document.createElement('div');
        loadingMask.className = 'loading_mask';
        loadingMask.id = 'loading_mask';
        loadingIcon.className = 'loading_icon';
        loadingIcon.id = 'loading_icon';
        $('body').append(loadingMask);
        $('body').append(loadingIcon);
    }
    function closeLoading(){
        if(document.getElementById('loading_mask')){
            $('#loading_mask').remove();
            $('#loading_icon').remove();
        }
    }
    var page = {
        isinit:false,
        index:1,
        data:{},
        harems:[],
        count:false,
        init:function(opt){
            var that = this;
            that.globalBind();
        },
        globalBind:function(){
            var that = this;
            $(document).on('click','.clear_ipt',function(){
                $('#phone')[0].value = '';
            }).on('focus','#phone',function(){
                $('.clear_ipt').show();
            }).on('blur','#phone',function(){
                if( $('#phone')[0].value == ''){
                    $('.clear_ipt').hide();
                }
            })
            $(document).on('click','.send_code_btn',function(){
                if(that.count != false) return false;
                var tel = $('#phone').val();
                var telReg = !!tel.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[03678]|18[0-9]|14[57])[0-9]{8}$/);
                if(telReg == false){
                    alert('请填写正确的手机号码');
                    return false;
                }
                var data = {
                    phone_number : tel
                }
                ajax.getSendsms(data,function(data) {
                        if(data.errorid == 0){
                            $('.send_code_btn').addClass('.wait');
                            that.count = 60;
                            that.countDown();
                        }
                    }
                )
            }).on('click','.submit_reg',function(){
                if($('#phone').val() == '' || $('#phone').val().trim() == ''){
                    alert('手机号不能为空');
                    return false;
                }
                if($('#phone').val().length!=11){
                	alert('手机号有错误');
                	return false;
                }
                if($('#code').val() == '' || $('#code').val().trim() == ''){
                    alert('验证码不能为空');
                    return false;
                }
                var data = {
                    action : 'login',
                    phone_number : $('#phone').val(),
                    sms_code : $('#code').val()
                }
                openLoading();
                ajax.getUserByMobilePhone(data,function(data) {
                        closeLoading();
                        if(data.errorid == 0){
                            //window.location.href="http://obj@loginbytoken@"+data.token;
                            if(data.action == 'regist'){
                                app.loginByToken(data.token);
                                // appDebug('检测到用户注册定义aftergetuserinfo',function(){
                                    // app.loginByToken(data.token);
                                // })
                                window.afterGetUserInfo = function(){
                                    app.href("register_step2.html");
                                    // appDebug('用户信息获取成功',function(){
                                        // app.href("register_step2.html");
                                    // })
                                }
                                return;
                            }else{
                                window.afterGetUserInfo = function(){
                                    app.closeLogin();
                                    // app.href("lxs_index.html");
                                }
                            }
                            app.loginByToken(data.token);
                            
                            // appDebug('ajax 获取token成功，将token发送给APP，token='+data.token,function(){
                                // window.location.href="http://obj@loginbytoken@"+data.token; // 将token发送给APP
                                // 定义一个在loginbyTOken成功之后的回调方法，每个页面自定义，可实现不同逻辑
                                // window.afterLoginByToken = function(){
                                    // window.location.href="http://obj@href@lxs_index.html"; // 页面跳转到楼先生首页
                                // }
                            // })
                        }
                    },function(data){
                        alert(data.msg);
                        closeLoading();
                    }
                )
            })

        },
        countDown:function(){
            var that = this;
            setTimeout(function(){
                $('.send_code_btn').html(''+that.count+'秒');
                if(that.count != 0){
                    that.count--;
                    that.countDown();
                }else{
                    $('.send_code_btn').removeClass('wait').html('发送手机验证码');
                    that.count = false;
                }
            },1000)
        }
    }

    page.init();

    Array.prototype.del=function(n) {
        if(n<0) {
            return this;
        }else{
            return this.slice(0,n).concat(this.slice(n+1,this.length));
        }
    }
    var protocolScroller;
    window.protocolInit = function(){
        if(!protocolScroller){
            protocolScroller = new IScroll("#contact_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
        }
    }
    var regScroller;
    window.regscrollInit = function(){
        if(!regscrollInit){
            regscrollInit = new IScroll("#user_reg_form",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
        }
    }
})
function UserInfoCallBack(session_id,user_id,type_id){
    var reg = /^\((.+)\)$/g;
    session_id = session_id.replace(reg,function(v1,v2){
        return v2;
    })
    user_id = user_id.replace(reg,function(v1,v2){
        return v2;
    })
    type_id = type_id.replace(reg,function(v1,v2){
        return v2;
    })
    if(session_id == 'null' || session_id == '(null)'){
        // appDebug('没有收到session_id，重新打开登陆',function(){
            // location.href = 'http://obj@openlogin';
        // })
        // location.href = 'http://obj@openlogin';
        app.openLogin();
        return;
    }

    /*
    appDebug('收到session_id，login.js  点击确定关闭登陆页登陆,data:'+[session_id,user_id,type_id].join('，'),function(){
        util.userData.sessionId = session_id;
        util.userData.userId = user_id;
        util.userData.typeId = type_id;
        window.afterLoginByToken && window.afterLoginByToken();
    })
    */
    util.setLocal('sessionId',session_id);
    util.setLocal('userId',user_id);
    util.setLocal('userType',type_id);
    // appDebug([session_id,user_id,type_id].join(','),function(){
        // window.afterGetUserInfo && window.afterGetUserInfo();
    // });
    util.userData.sessionId = session_id;
    util.userData.userId = user_id;
    util.userData.typeId = type_id;
    window.afterGetUserInfo && window.afterGetUserInfo();
}