KEHU(function(ajax){
    var page = {
        pageIndex : 1,
        pageSize : 20,
        init : function(){
            var that = this;
            !that.inited && that.bindAction();
            that.getCustomerList();
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
                that.getCustomerList();
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
                that.getCustomerList();
                setTimeout(function () {
                    ajaxing = false;
                    myScroll.refresh();
                    pullUpEl.style.visibility = 'hidden';
                }, 500);
            }

            setTimeout(function(){
                myScroll = that.myScroll = new IScroll('#dataListArea', {
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

            // $(document).on('click','.kehu_item',function(){
            // detail.id = $(this).attr('data-id');
            // detail.init();
            // })
        },
        getCustomerList : function(){
            var that = this;
            ajax.getCustomerList(that.pageIndex,that.pageSize,function(data){
                if(that.pageIndex == 1){
                    $('#kehu_list').html('');
                }
                if(!data.list.length) {
                    if(that.pageIndex == 1){
                        $('#no_content').show();
                        $('#dataListArea').hide();
                    }
                    that.pageIndex --;
                    that.myScroll && that.myScroll.refresh();
                    return;
                } else {
                    $('#no_content').hide();
                    $('#dataListArea').show();
                }
                if(data.list.length < page.pageSize) {
                    $('#pullUp').css('visibility','hidden');
                }
                that.createCustomer(data.list);
            })
        },
        createCustomer : function(list){
            var that = this , html = '';
            for(var i=0;i<list.length;i++){
                html += util.tpl.apply('kehu_item',list[i]);
            }
            $('#kehu_list').append(html);
            that.myScroll && that.myScroll.refresh();
        }
    }

    page.init();
    var totalWidth = document.documentElement.clientWidth + 20;

    util.tpl.format.threadTitleSplit = function(){
        var obj = arguments[arguments.length - 1];
        var title = obj.intro , intro = title.htmlEncode() , len = obj.intro.length;
        var div = document.createElement('div');
        div.className = 'width_compute_helper';
        div.innerHTML = intro;
        $('body').append(div);
        var width = div.clientWidth;
        if(width < totalWidth * 2) {
            div.parentNode.removeChild(div);
            return intro;
        }
        var i = 0;
        while(width > totalWidth * 2 ){
            div.innerHTML = intro.substr(0,len + i);
            width = div.clientWidth;
            i --;
        }
        div.parentNode.removeChild(div);
        return intro.substr(0 , len + i) + (i == 0 ? '' : '…');
    }
    var statusClassMap = ['looking','looking','want','dealed','disabled'];
    util.tpl.format.statusClassFormat = function(p){
        return statusClassMap[p];
    }

    var detail = {
        id : 0,
        inited:false,
        init : function(){
            !this.inited && this.bindAction();
            this.getCustomerInfo();
        },
        bindAction : function(){
            var that = this;
            // $('.top_banner a').each(function(){
            // this.ontouchstart = function(e){
            // e.preventDefault();
            // }
            // })
            // $('.add_note')[0].ontouchstart = function(e){
            // e.preventDefault();
            // }
            $('.add_note').click(function(){
                if(that.id){
                    logs.target_id=that.id;
                    logs.edit=false;
                    setTimeout(function(){
                        logs.init();
                    },10)
                }
            })
            $('#detail').on('click','.note',function(){
                if(that.id){
                    logs.target_id=that.id;
                    logs.id=$(this).attr('data-id');
                    logs.edit=true;
                    logs.data_time = $(this).attr('data-time');
                    logs.data_content = $(this).attr('data-content');
                    logs.data_notify = $(this).attr('data-notify');
                    logs.init();
                }
            })
            $('#detail .right_menu').click(function(){
                if(editer.obj){
                    editer.id=that.id;
                    editer.init();
                }
            })
            $('input[name=process]').change(function(){
                var val = $('input[name=process]:checked').val();
                var params = {
                    process : val,
                    id : that.id
                }
                ajax.modifyCustomer(params,function(){})
            })
            $('#log_scroller')[0].ontouchstart = function(e){
                e.preventDefault();
            }
            this.inited = true;
        },
        getCustomerInfo : function(){
            var that = this;
            ajax.getCustomerInfo({id:detail.id},function(data){
                that.fillCustomerInfo(data.data);
                editer.obj = data.data;
                //that.createLogs(data.data.rooms);
                detail.logScroller = new IScroll("#log_scroller",{
                    hScrollbar: false,
                    vScrollbar: false,
                    hideScrollbar: true
                });
            })
        },
        fillCustomerInfo : function(data){
            $('#detail').find('.progess_options input').eq(parseInt(data.process) - 1).attr('checked','checked');
            var htmls = '';
            for(var i=0;i<data.work_log.length;i++){
                htmls += util.tpl.apply('work_log_list',data.work_log[i]);
            }
            $('#detail').find('#workLog').html(htmls);
        }
    }
    if(params){
        // var search = util.parseURI(params);
        // detail.id = search.goid;
        // detail.init();
        // Mobilebone.transition($('#detail')[0],$('#index')[0],false);
    }


    var logs = {
        target_id:0,
        id:0,
        edit:false,
        init : function(){
            !this.inited && this.bindAction();
            if(this.edit == true){
                $('#note_date').val(this.data_time);
                $('#note_content').val(this.data_content);
                $('#notice_day').val(this.data_notify);
                $('#edit .title').html('工作日志编辑');
                $('#delete_log').show();
            }else{
                var today = (new Date()).format('YYYY-MM-DD');
                $('#note_date').val(today);
                $('#edit .title').html('工作日志添加');
                $('#delete_log').hide();
                $('#note_content').val('');
                $('#notice_day').val('');
            }
        },
        bindAction : function(){
            var that = this;
            $('#delete_log').click(function(){
                confirm('确定要删除本工作日志？',function(){
                    ajax.removeNote(that.id,function(){
                        $('#note_'+that.id).remove();
                        Mobilebone.transition($('#detail')[0],$('#edit')[0],true);
                        detail.logScroller && detail.logScroller.refresh();
                    })
                })
            });
            $('#note_date').date();
            $('#notice_day').date({theme:"datetime"});
            $('#edit').on('click','.submit',function(){
                var date = $('#edit').find('#note_date').val();
                var content = $('#edit').find('#note_content').val();
                var time = $('#edit').find('#notice_day').val();
                if(content.length<5 || content.length>100){
                    alert("日志长度要求在5到100字以内");
                    return false;
                }
                var _data= {
                    target_id : that.target_id,
                    event_date : date,
                    content : content,
                    notify_time : time,
                    target_type:5
                }
                if(that.edit == true){
                    _data.id = that.id;
                }
                ajax.addWorklogcreate(_data,function(data){
                    var p = $('#detail').find('#workLog');
                    _data.id=data.id;
                    _data.week_no = '星期'+['日','六','五','四','三','二','一'][(new Date(time)).getDay()];
                    var html = util.tpl.apply('work_log_list',_data);
                    if(that.edit == true){
                        var dom = $('#note_'+that.id);
                        $(dom).after(html);
                        dom.remove();
                    }else{
                        p.append(html);
                        detail.logScroller && detail.logScroller.refresh();
                    }
                    Mobilebone.transition($('#detail')[0],$('#edit')[0],true);
                })
            })
            this.inited = true;
        }
    }



    var editer = {
        id : 0,
        type:'',
        selecting:'',
        init : function(){
            !this.inited && this.bindAction();
            this.getCustomerInfo();
        },
        bindAction : function(){
            var that = this;
            $('#editClent').on('click','.del_btn',function(){
                var params = {};
                params.id = that.obj.id;
                confirm('是否确定删除此客户',function(){
                    ajax.customerdelete(params,function(data){
                        page.init();
                        Mobilebone.transition($('#index')[0],$('#editClent')[0],true);
                    })
                });

            })
            $('#editClent').on('click','input[name=trade]',function(){
                $('#editClent #room_price_unit').html(['元/m²/天','元/m²/天','元/m²'][$(this).val()]);
            })
            $('#editClent').on('click','.submit_btn,#save',function(event){
                var params = that.getSearchFormData();
                if(!params){
                    event.stopPropagation();
                    return false;
                }
                params.id = that.obj.id;
                ajax.modifyCustomer(params,function(data){
                    detail.init();
                    Mobilebone.transition($('#detail')[0],$('#editClent')[0],true);
                })
                event.stopPropagation();
                return false;

            })
            $('#editClent').on('keydown','#pre_square_meter,#pre_price_1',function(e){
                var c = e.keyCode;
                var val = this.value;
                if(c == 8)  return;
                if(!(c == 190 || c >=48 && c<=57)){
                    e.preventDefault();
                    return;
                }else{
                    var arr = val.split('.');
                    if(arr[1] !== undefined && arr[1].length >=2){
                        e.preventDefault();
                    }
                }
                if(c == 190 && val.indexOf('.') != -1){
                    e.preventDefault();
                }
            })
            this.inited = true;

        },
        getCustomerInfo : function(){
            var that = this;
            var form = util.tpl.apply('editClent',that.obj);
            $('#editClent').find('.form').html(form);
            $('#editClent').find('#first_access_time').date();
            $('#editClent').find('#expire_time').date();
            setTimeout(function(){
                $('#intro').reszieTextarea({
                    callback : function(){
                        editer.kehuDetailScroller && editer.kehuDetailScroller.refresh();
                    }
                })
            },500);
            editer.kehuDetailScroller = new IScroll("#kehu_detail_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
        },
        getSearchFormData : function(){
            var params = {};
            $('#editClent .form').find('input[type=text]').each(function(){
                params[$(this).attr('name')] = $(this).val();
            })
            params.transaction_type = $('input[name=trade]:checked').val();
            params.intro = $('#editClent').find('#intro').val();
            params.pre_price_unit_1 = $('#editClent').find('#room_price_unit').html();
            for(var p in params){
                if(params[p] == '' || !params[p]){
                    delete params[p];
                }
            }
            var isBlankObj = true;
            for(var p in params){
                isBlankObj = false;
            }
            if(isBlankObj) return false;

            if(params.intro && (params.intro.length<10 || params.intro.length>200)){
                alert('客户简介应在10-200字之间');
                return false;
            }
            if(params.employee_total){
                if(isNaN(Number(params.employee_total))){
                    alert("员工数量只能为整数！");
                    return false;
                }
            }
            if(params.pre_square_meter){
                if(isNaN(Number(params.pre_square_meter))){
                    alert("意向面积只能为整数！");
                    return false;
                }
            }
            if(params.pre_price_1){
                var reg = new RegExp("^[0-9]+(.[0-9]{2})?$", "g");
                if(!reg.test(params.pre_price_1)){
                    alert("价格预算只能为数字，最多只能有两位小数！");
                    return false;
                }
            }
            return params;
        }
    }

    util.tpl.format.transaction_type = function(a,b){
        if(a == b){
            return 'checked';
        }
    }

    util.tpl.format.pre_price = function(a){
        return ['元/m²/天','元/m²/天','元/m²'][a];
    }
})
