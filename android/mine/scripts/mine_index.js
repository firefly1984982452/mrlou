MINE(function(ajax){
    var tplMap = {
        '2' : 'buliding',
        '3' : 'room',
        '4' : 'thread',
//      '7' : 'mineThread'
    }
    var subjectMap = {
        '1' : '房源合作',
        '2' : '客户合作',
        '3' : '其他主题'
    }
    var page = {
        pageIndex : 1,
        pageSize : 20,
        type : 2,
        init : function(){
            !this.inited && this.globalBind();
            this.getCollection();
        },
        globalBind : function(){
            var that = this;
            $('.nav').click(function(){
                if($(this).hasClass('cur')) return;
                $('.nav').removeClass('cur');
                $(this).addClass('cur');
                that.pageIndex = 1;
                that.type = $(this).attr('type');
                that.getCollection();
            });
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
                
                _page.getCollection();
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
                _page.getCollection();
                setTimeout(function () {
                    myScroll.refresh();
                    ajaxing = false;
                    pullUpEl.style.visibility = 'hidden';
                }, 1000);
            }

            setTimeout(function(){
                myScroll = that.scroller = new IScroll('#collectionListArea', {
                    mouseWheel: true,
                    click: true,
                    useTransition: false,
                    hScrollbar: false,
                    vScrollbar: false,
                    hideScrollbar: true,
                    topOffset: pullDownOffset,
                    startY:-pullDownOffset
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
                            this.maxScrollY = this.maxScrollY;
                        } else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
                            pullUpEl.className = '';
                            pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉获取更多...';
                            this.maxScrollY = pullUpOffset;
                        }
                        $('body').focus();
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
                });
                document.addEventListener('touchend', function (e) {  $('.searchInput').blur(); }, false);
            },300);
            var scrollArea = $('#collectionListArea')[0];
            scrollArea.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
            /*$('#data_list').on('click','.del_btn',function(){
                var id = $(this).attr('cid');
                var map = {
                    2 : 2,
                    3 : 3,
                    4 : 4
//                  7 : 4
                }
                var strMap = {
                    '2' : '确定要删除此收藏的楼盘？',
                    '3' : '确定要删除此收藏的房源？',
                    '4' : '确定要删除此收藏的经纪人？',
                }
                var dom = $(this);
                confirm(strMap[that.type],function(){
                    ajax.uncollect(map[that.type],id,function(){
                        dom.parent().remove();
                    })
                });
            });*/
            $('#data_list').on('click','.collect_thread_status',function(){
                var tid = $(this).attr('tid');
                var dom = $(this);
                confirm('确定要将此帖子设置为失效？',function(){
                    ajax.changeThreadStatus(2,tid,function(){
                        dom.remove();
                    })
                })
            })
            this.inited = true;
        },
        getCollection : function(){
            var that = this;
            ajax.getCollection(that.type,that.pageIndex,that.pageSize,function(data){
                var noContentMap = {
                    '2' : '您还没有收藏楼盘，赶快去收藏吧！',
                    '3' : '您还没有收藏房源，赶快去收藏吧！',
                    '4' : '您还没有收藏经纪人，赶快去收藏吧！'
//                  '7' : '您还没有发布过易楼帮帖子'
                }
                if(that.pageIndex == 1){
                    if(data.list.length == 0){
                        $('#data_list').html('<p class="no_content">'+noContentMap[that.type]+'</p>');
                    }else{
                        $('#data_list').html('');
                    }
                }

                if(data.list.length == 0){
                    $('#pullUp').hide();
                    that.pageIndex = Math.max(that.pageIndex - 1 , 1);
                    
                }else{
                    $('#pullUp').show();
                }
                that.createHtml(data.list);
            })
        },
        //创建收藏数据列表
        createHtml : function(list){
        	$(".nav").val()=$(".nav").val()+list.length;
            var that =  this;
            var html = '';
            for(i=0;i<list.length;i++){
                html += util.tpl.apply(tplMap[that.type],list[i]);
            }
            $('#data_list').append(html);
            that.scroller && that.scroller.refresh();
            //image_more的点击事件
            $("#image_more").on("click",function(){
            	localStorage.setItem("mine_buid",$("#mine_cid").attr("cid"));
            });
        }
    }
    util.tpl.format.isVerified = function(){
        var obj = arguments[arguments.length - 1];
        if(obj.data.verified == '1'){
            return '<span class="room_active"><i></i></span>';
        }
        return '';
    }
    util.tpl.format.priceFormat = function(){
        var obj = arguments[arguments.length - 1];
        if(obj.data.transaction_type == '1'){
            return '租金报价：¥' + obj.data.rent_price;
        }else{
            return '出售报价：¥' + obj.data.sell_price;
        }
    }
    util.tpl.format.subjectFormat = function(){
        var obj = arguments[arguments.length - 1];
        var data = obj.data || obj;
        return subjectMap[data.subject];
    }
    util.tpl.format.timeFormat = function(){
        var obj = arguments[arguments.length - 1];
        // var  time = obj.create_time || obj.expire_time || '';
        return obj.status == '1' ? obj.create_time : obj.expire_time;
    }
    
    util.tpl.format.statusFormat = function(){
        var obj = arguments[arguments.length - 1];
        return ['','发布','失效'][obj.status];
    }
    
    util.tpl.format.btnFormat = function(){
        var obj = arguments[arguments.length - 1];
        return obj.status == '2' ? '' : '<a href="javascript:;" class="collect_thread_status" tid="'+obj.id+'">失效</a>';
    }
    
    util.tpl.format.blidingPriceFormat = function(id,type,obj){
        if(type == 1){
            return obj.data.sell_price ? '售价' + obj.data.sell_price : '';
        }else{
            return obj.data.rent_price ? '租金' + obj.data.rent_price : '';
        }
    }
    page.init();
    
})