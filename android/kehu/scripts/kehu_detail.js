KEHU(function(ajax){
    
    var totalWidth = document.documentElement.clientWidth - 20;
    
    util.tpl.format.threadTitleSplit = function(){
        var obj = arguments[arguments.length - 1];
        var title = obj.title , title = title.htmlEncode() , len = obj.title.length;
        var div = document.createElement('div');
        div.className = 'width_compute_helper';
        div.innerHTML = title;
        $('body').append(div);
        var width = div.clientWidth;
        if(width < totalWidth * 2 - 30) {
            div.parentNode.removeChild(div);
            return title;
        }
        var i = 0;
        while(width > totalWidth * 2 - 30){
            div.innerHTML = title.substr(0,len + i);
            width = div.clientWidth;
            i --;
        }
        div.parentNode.removeChild(div);
        return title.substr(0 , len + i) + (i == 0 ? '' : '……');
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
            $('.add_note')[0].ontouchstart = function(e){
                e.preventDefault();
            }
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
        },
        getCustomerInfo : function(){
            var that = this;
            ajax.getCustomerInfo({id:detail.id},function(data){
                that.fillCustomerInfo(data.data);
                editer.obj = data.data;
                detail.logScroller = new IScroll("#log_scroller",{
                    hScrollbar: false,
                    vScrollbar: false,
                    hideScrollbar: true
                });
                this.inited = true;
                //that.createLogs(data.data.rooms);
            })
        },
        fillCustomerInfo : function(data){
            $('#detail').find('.progess_options input').eq(parseInt(data.process) - 1).attr('checked','checked');
            var htmls = '';
            for(var i=0;i<data.work_log.length;i++){
                htmls += util.tpl.apply('work_log_list',data.work_log[i]);
            }
            $('#detail').find('#workLog').html(htmls);
            $('#page_title').html(data.name);
            detail.logScroller && detail.logScroller.refresh();
        }
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
            $('#edit .go_back').each(function(){
                this.ontouchstart = function(e){
                    e.preventDefault();
                }
            })
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
            $('#edit .submit').click(function(){
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
                    _data.week_no = '星期'+['日','一','二','三','四','五','六'][date.toDate().getDay()]
                    var html = util.tpl.apply('work_log_list',_data);
                    if(that.edit == true){
                        var dom = $('#note_'+that.id);
                        $(dom).after(html);
                        dom.remove();
                    }else{
                        p.append(html);
                    }
                    $('.note').unbind().click(function(){
//                        if(that.id){
                            logs.target_id=detail.id;
                            logs.id=$(this).attr('data-id');
                            logs.edit=true;
                            logs.data_time = $(this).attr('data-time');
                            logs.data_content = $(this).attr('data-content');
                            logs.data_notify = $(this).attr('data-notify');
                            logs.init();
//                        }
                    })
                    if(detail.logScroller){
                        setTimeout(function(){
                            detail.logScroller.refresh();
                        },500);
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
            $('#editClent .go_back').each(function(){
                this.ontouchstart = function(e){
                    e.preventDefault();
                }
            })
            $('#editClent').on('click','.del_btn',function(){
                var params = {};
                params.id = that.obj.id;
                confirm('是否确定删除此客户',function(){
                    console.log(1);
                    ajax.customerdelete(params,function(data){
                        page.init();
                        Mobilebone.transition($('#index')[0],$('#editClent')[0],true);
                    })
                })
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
            $('#editClent').on('input','#phone_number',function(e){
                var val = this.value;
                var reg = /[^\d-]/g;
                this.value = val.replace(reg,'');
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
                        detail.kehuDetailScroller && detail.kehuDetailScroller.refresh();
                    }
                })
                detail.kehuDetailScroller && detail.kehuDetailScroller.refresh();
            },500);
            detail.kehuDetailScroller = new IScroll("#kehu_detail_scroller",{
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

            if(!params.intro || params.intro && (params.intro.length>100)){
                if ( params.intro ) {
                    alert('客户简介应在100字之内');
                    return false;
                }
            }
            if(!params.phone_number || params.phone_number && (params.phone_number.length < 8 || params.phone_number.length > 12)){
                if ( params.phone_number ) {
                    alert('联系电话应在8-12字之间');
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
    
    function pageInit(){
        var search = util.parseURI(params);
        if(search.back == 'index'){
            $('#detail .go_back').attr('href','javascript:app.href("kehu_index.html");');
        }else{
            $('#detail .go_back').attr('href','javascript:app.goBack();');
        }
        detail.id = search.goid;
        detail.init();
    }

    //params = 'goid=43'
    if(params){
        pageInit();
    }else{
        window.afterParams = pageInit;
    }
    document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
})
