YLB(function(ajax){
    var page = {
        pageIndex : 1,
        pageSize : 20,
        init : function(){
            var that = this;
            !that.inited && that.bindAction();
            that.getThreadList();
        },
        bindAction : function(){
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
                that.pageIndex = 1;
                that.getThreadList();
                setTimeout(function () {
                    ajaxing = false;
                    myScroll.refresh();
                    pullDownEl.style.visibility = 'hidden';
                }, 500);
            }

            function pullUpAction () {
                if(page.more == false){
                    return false;
                }
                that.pageIndex ++;
                that.getThreadList();
                setTimeout(function () {
                    ajaxing = false;
                    myScroll.refresh();
                    pullUpEl.style.visibility = 'hidden';
                }, 500);
            }

            setTimeout(function(){
                myScroll = that.myScroll = new IScroll('#threadListArea', {
                    mouseWheel: true,
                    click: true,
                    useTransition: false,
                    hScrollbar: false,
                    vScrollbar: false,
                    hideScrollbar: true,
                    topOffset: pullDownOffset,
                    startY:-pullDownOffset
                });
		myScroll.on('scrollStart', function () {
		});
                myScroll.on('refresh', function () {
                        if (pullDownEl.className.match('loading')) {
                            pullDownEl.className = '';
                            pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉更新数据...';
                        } else if (pullUpEl.className.match('loading')) {
                            pullUpEl.className = '';
                            pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉获取更多...';
                        }
                });
                myScroll.on('scrollMove', function () {
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
                            this.maxScrollY = this.maxScrollY + 40;
                        } else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
                            pullUpEl.className = '';
                            pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉获取更多...';
                            this.maxScrollY = pullUpOffset;
                        }
                });
                myScroll.on('scrollEnd', function () {
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
                })
            },300)
            document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
            that.inited = true;
        },
        getThreadList : function(){
            var that = this;
            ajax.getThreadList(that.pageIndex,that.pageSize,function(data){
                if(that.pageIndex == 1){
                    $('#thread_list').html('');
                }
                if(!data.list.length) {
                    that.pageIndex --;
                    that.myScroll && that.myScroll.refresh();
                    return;
                }
                if(data.list.length < page.pageSize) {
                    $('#pullUp').css('visibility','hidden');
                }
                that.createThread(data.list);
            })
        },
        createThread : function(list){
            var that = this , html = '';
            for(var i=0;i<list.length;i++){
                html += util.tpl.apply('thread_item',list[i]);
            }
            $('#thread_list').append(html);
            that.myScroll && that.myScroll.refresh();
        }
    }
    
    page.init();
    var totalWidth = document.documentElement.clientWidth + 20;
    
    util.tpl.format.threadTitleSplit = function(){
        var obj = arguments[arguments.length - 1];
        var title = obj.title , title = title.htmlEncode() , len = obj.title.length;
        var div = document.createElement('div');
        div.className = 'width_compute_helper';
        div.innerHTML = title;
        $('body').append(div);
        var width = div.clientWidth;
        if(width < totalWidth * 2) {
            div.parentNode.removeChild(div);
            return title;
        }
        var i = 0;
        while(width > totalWidth * 2){
            div.innerHTML = title.substr(0,len + i);
            width = div.clientWidth;
            i --;
        }
        div.parentNode.removeChild(div);
        return title.substr(0 , len + i) + (i == 0 ? '' : '…');
    }
    var subjectStr = {
        '1' : '我有房源',
        '2' : '我有客户',
        '3' : '其他主题'
    }
    util.tpl.format.subjectConvert = function(subject){
        return subjectStr[subject || '1'];
    }
});

