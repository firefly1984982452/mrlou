YLB(function(ajax){
    var search,threadId;
    var page = {
        init : function(){
            var that = this;
            !that.inited && that.bindAction();
            that.getThreadDetail();
        },
        bindAction : function(){
            var that = this;
            var scroller = that.scroller = new IScroll('#thread_detail_scroller', {
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            var width = $('#reply_content').width() + 20;

            /*膜拜大神*/
            var oldH = window.innerHeight;
            /*
            $('#reply_content').focus(function(){
                window.mrlou.hidebottom();
                setTimeout(function(){
                    var st = $(document).scrollTop();
                    var newH = window.innerHeight;
                    $('.reply_area').css({
                        position:'absolute',
                        top : newH + st - $('.reply_area').height()
                    })
                },500);
            })
            $('#reply_content').blur(function(){
                window.mrlou.showbottom();
                $('.reply_area').css({
                    position:'fixed',
                    top : 'auto',
                    bottom : 0
                });
            })
            */

            $('#reply_content').bind('input',function(){
                var val = this.value;
                var reg = /\n/g;
                var match = val.match(reg) , enterNum = 0;
                if(match) enterNum = match.length;
                var div = document.createElement('div');
                div.innerHTML = val.replace(reg,'');
                div.className = 'width_compute_helper';
                $('body').append(div);
                var _w = div.clientWidth;
                var lines = Math.ceil(_w/width);
                var total = Math.min(3,enterNum + lines) - 0.5;
                div.parentNode.removeChild(div);
                var totalHeight = total * 26;
                $('.reply_txt_con,#reply_content').css('height',Math.max(26,totalHeight));
            })
            var posting = false;
            $('#post_reply').click(function(){
                if(posting) return;
                var content = $.trim($('#reply_content').val());
                if(content.length > 100 || content.length < 1){
                    alert('回复内容必须在1~100个字符之间');
                    return;
                }
                posting = true;
                ajax.postReply(threadId,content,function(data){
                    posting = false;
                    that.createReply({
                        replys : [data.data]
                    },true);
                    $('.reply_txt_con,#reply_content').css('height',26);
                    $('#reply_content').val('');
                },function(data){
                    alert(data.msg);
                    posting = false;
                })
            });
            $('#collect_thread').click(function(){
                var dom = $(this);
                var collected = $(this).hasClass('collected');
                if(collected) return;
                ajax.collectThread(threadId,function(){
                    $('#collect_thread').toggleClass('collected');
                })
            })
            that.inited = true;
        },
        getThreadDetail : function(){
            var that = this;
            ajax.getThreadDetail(threadId,function(data){
                var _data = data.data;
                that.createThreadDetail(_data);
                that.createReply(_data);
            })
        },
        createThreadDetail : function(data){
            var that = this;
            var user = data.user;
            if(data.is_collected == 1){
                $('#collect_thread').addClass('collected');
            }else{
                $('#collect_thread').removeClass('collected');
            }
            
            $('#reply_count').html(data.replys.length);
            $('#poster_avatar').attr('src',user.avatar);
            
            $('#poster_name').html(user.cn_username || user.en_username);
            $('#poster_phone').html(user.phone);
            if(user.phone){
                $('#poster_phone').attr('href','tel:' + user.phone);
            }
            var tpl = user.type == '102' ? 'type1' : 'type2';
            $('#poster_info').html(util.tpl.apply(tpl, user));
            $('#poster_phone,#user_phone').unbind().click(function(){
                var tel = $(this).html();
                var verify = $(this).attr('data-verify');
                ajax.telLog(tel,verify,function(){});
            });
            // $('#poster_desc').html(user.intro);
            // $('#poster_company').html(user.company);
            // $('#poster_work_age').html("从业" + user.work_age + "年");
            // $('#poster_goodat').html(user.regions.join('、'));
            
            var postTime = data.create_time.toDate();
            $('#post_time').html(postTime.format('hh:mm'));
            $('#post_date').html(postTime.format('YYYY-MM-DD'));
            $('#thread_type').html(util.tpl.format.subjectConvert(data.subject));
            $('#thread_title').html(data.title.htmlEncode());
            //$('#thread_content').html(data.content.htmlEncode());
            $('#thread_content').html(data.content);
            
            if(data.avatar){
                $('#thread_img_con').show();
                $('#thread_img').attr('src',data.avatar);
                var img = new Image();
                img.onload = function(){
                    that.scroller && that.scroller.refresh();
                    img.onload = null;
                }
                img.src = data.avatar;
            }
            that.scroller && that.scroller.refresh();
        },
        createReply : function(data,append){
            var that = this;
            var list = data.replys , len = list.length;
            var html = '';
            for(var i=0;i<len;i++){
                html += util.tpl.apply('reply_item',list[i]);
            }
            if(append){
                $('#reply_list').append(html);
            }else{
                $('#reply_list').html(html);
            }
            setTimeout(function(){
                that.scroller.refresh();
            })
        }
    }

    var subjectStr = {
        '1' : '我有房源',
        '2' : '我有客户',
        '3' : '其他主题'
    }
    util.tpl.format.subjectConvert = function(subject){
        return subjectStr[subject || '1'];
    }
    var today = (new Date()).format('YYYY-MM-DD');
    util.tpl.format.replyTimeFn = function(){
        var obj = arguments[arguments.length - 1];
        var replyTime = obj.reply_time.toDate();
        var replyDay = replyTime.format('YYYY-MM-DD');
        return replyDay == today ? replyTime.format('hh:mm') : replyTime.format('YYYY-MM-DD hh:mm');
    }
    util.tpl.format.posterNameFn = function(){
        var obj = arguments[arguments.length - 1];
        return obj.user_cn_username || obj.en_username;
    }
    util.tpl.format.companyAssert = function(){
        var obj = arguments[arguments.length - 1];
        return obj.company ? '<span class="company_name" style="margin-right:8px">' + obj.company + '</span>' : '';
    }
    var typeMap = {
        '1' : '开发商',
        '2' : '代理商',
        '3' : '运营商',
        '102' : '经纪人',
        '201' : '小业主',
        '299' : '其他'
    }
    util.tpl.format.posterTypeFn = function(){
        var obj = arguments[arguments.length - 1];
        return typeMap[obj.user_type];
    }
    var thread_detail_scroller = document.getElementById('thread_detail_scroller');
    thread_detail_scroller.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
    
    function pageInit(){
        search = util.parseURI(params);
        if(search.back == 0 || search.back === undefined){
            // 易楼帮首页点击帖子后，回退为back
            // 其他地方跳转进帖子详情后，回退为back
            $('.go_back').attr('href','javascript:app.goBack();');
        }
        if(search.back == 'index'){
            // 发帖后跳转到帖子详情后，回退到易楼帮首页
            $('.go_back').attr('href','javascript:app.href("ylb_index");');
        }
        threadId = search.goid || search.id;
        page.init();
    }

    util.tpl.format.regionsFormat = function(){
        var obj = arguments[arguments.length - 1];
        return obj.regions.join('、');
    }
    util.tpl.format.typeFormat = function(){
        var obj = arguments[arguments.length - 1];
        return typeMap[obj.type];
    }
    util.tpl.format.posterNameFormat = function(){
        var obj = arguments[arguments.length - 1];
        return obj.cn_username + obj.en_username;
    }
    //params = 'id=70'
    if(params){
        pageInit();
    }else{
        window.afterParams = pageInit;
    }

});
function getRpleyName(name) {
    var current_str = $('#reply_content').val();
    if (!current_str.endWith('@'+name)) {
        $('#reply_content').val($('#reply_content').val() + '@'+name);
    }
}
String.prototype.endWith=function(endStr){
    var d=this.length-endStr.length;
    return (d>=0&&this.lastIndexOf(endStr)==d)
}

// /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)