XIAOXI(function(ajax){
    var page = {
        type : 0,
        pageIndex : 1,
        pageSize : 20,
        init : function(){
            !this.inited && this.globalBind();
            this.getEventList();
        },
        globalBind : function(){
            var that = this;
            var myScroll,
                pullDownEl, pullDownOffset,
                pullUpEl, pullUpOffset,
                ajaxing = false,
                _page = this;

            pullDownEl = document.getElementById('pullDown');
            pullDownOffset = pullDownEl.offsetHeight;
            pullUpEl = document.getElementById('pullUp');
            pullUpOffset = pullUpEl.offsetHeight;

            function pullDownAction () {
                _page.pageIndex = 1;
                
                _page.getEventList();
                setTimeout(function () {
                    myScroll.refresh();
                    ajaxing = false;
                    pullDownEl.style.visibility = 'hidden';
                }, 1000);
            }

            function pullUpAction () {
                if(page.more == false){
                    return false;
                }
                _page.pageIndex += 1;
                _page.getEventList();
                setTimeout(function () {
                    myScroll.refresh();
                    ajaxing = false;
                    pullUpEl.style.visibility = 'hidden';
                }, 1000);
            }

            setTimeout(function(){
                myScroll = that.scroller = new iScroll('dataListArea', {
                    mouseWheel: true,
                    click: true,
                    useTransition: false,
                    hScrollbar: false,
                    vScrollbar: false,
                    hideScrollbar: true,
                    topOffset: pullDownOffset,
                    onRefresh: function () {
                        if (pullDownEl.className.match('loading')) {
                            pullDownEl.className = '';
                            pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉更新数据...';
                        } else if (pullUpEl.className.match('loading')) {
                            pullUpEl.className = '';
                            pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉获取更多...';
                        }
                    },
                    onScrollMove: function () {
                        if(ajaxing == true){
                            return false;
                        }
                        if(this.y > this.absStartY){
                            pullDownEl.style.visibility = 'visible';
                            pullUpEl.style.visibility = 'hidden';
                        }else{
                            pullUpEl.style.visibility = 'visible';
                            pullDownEl.style.visibility = 'hidden';
                        }
                        if (this.y > 5 && !pullDownEl.className.match('flip')) {
                            pullDownEl.className = 'flip';
                            pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开确认更新...';
                            this.minScrollY = 0;
                        } else if (this.y < 5 && pullDownEl.className.match('flip')) {
                            pullDownEl.className = '';
                            pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉更新数据...';
                            this.minScrollY = -pullDownOffset;
                        } else if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
                            pullUpEl.className = 'flip';
                            pullUpEl.querySelector('.pullUpLabel').innerHTML = '松开确认更新...';
                            this.maxScrollY = this.maxScrollY;
                        } else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
                            pullUpEl.className = '';
                            pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉获取更多...';
                            this.maxScrollY = pullUpOffset;
                        }
                        $('body').focus();
                    },
                    onScrollEnd: function () {
                        if (pullDownEl.className.match('flip')) {
                            pullDownEl.className = 'loading';
                            pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';
                            ajaxing = true;
                            pullDownAction();
                        } else if (pullUpEl.className.match('flip')) {
                            pullUpEl.className = 'loading';
                            pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';
                            ajaxing = true;
                            pullUpAction();
                        }
                    },
                    onTouchEnd:function(){
                        $('.searchInput').blur();
                    }
                })
            },300);
            var scrollArea = $('#dataListArea')[0];
            scrollArea.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
            that.inited = true;
        },
        getEventList : function(){
            var that = this;
            ajax.eventList(that.type,that.pageIndex,that.pageSize,function(data){
                if(that.pageIndex == 1){
                    $('#msg_list').html('');
                }
                if(data.list.length == 0){
                    $('#pullUp').hide();
                    that.pageIndex = Math.max(that.pageIndex - 1 , 1);
                }else{
                    $('#pullUp').show();
                }
                that.c
                that.createEventList(data.list);
            })
        },
        createEventList : function(list){
            var that = this;
            var len = list.length , html = '';
            for(var i=0;i<len;i++){
                html += util.tpl.apply('type' + that.type,list[i]);
            }
            $('#msg_list').append(html);
            that.scroller && that.scroller.refresh();
        }
    }
    var titleMap = {
        '3' : '房源工作日志提醒',
        '4' : '我的帖子评论提醒',
        '5' : '客户工作日志提醒',
        '8' : '楼先生'
    }
    // params = 'type=5'
    var search;
    function pageInit(){
        search = util.parseURI(params);
        $('#title').html(titleMap[search.type]);
        page.type = search.type;
        page.init();
    }
    if(params){
        pageInit();
    }else{
        window.afterParams = pageInit;
    }
    var today = (new Date()).format('YYYY-MM-DD');
    util.tpl.format.noticeTime = function(){
        var obj = arguments[arguments.length - 1];
        var notifyTime = obj.notify_time;
        var eventDay = notifyTime.toDate('YYYY-MM-DD');
        return eventDay == today ? notifyTime.toDate('hh-mm') : notifyTime.toDate('YYYY-MM-DD hh:mm');
    }
    util.tpl.format.splitName = function(str){
        var arr = str.split(' ');
        arr.pop();
        return arr.join(' ');
    }
    util.tpl.format.splitType = function(str){
        var arr = str.split(' ');
        return arr.pop();
    }
    var totalWidth = document.documentElement.clientWidth - 20;
    util.tpl.format.contentSplit = function(content){
        // content ='vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过vvvv吹吹风风风光光过'
        var len = content.length;
        var obj = arguments[arguments.length - 1];
        var div = document.createElement('div');
        div.className = 'width_compute_helper';
        div.innerHTML = content;
        div.style.fontSize = '13px';
        $('body').append(div);
        var width = div.clientWidth;
        if(width < totalWidth * 2) {
            div.parentNode.removeChild(div);
            return content;
        }
        var i = 0;
        while(width > totalWidth * 2){
            div.innerHTML = content.substr(0,len + i);
            width = div.clientWidth;
            i --;
        }
        div.parentNode.removeChild(div);
        return content.substr(0 , len + i) + (i == 0 ? '' : '…');
    }
});

