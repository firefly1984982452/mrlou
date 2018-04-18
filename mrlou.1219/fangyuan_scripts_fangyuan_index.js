FY(function(ajax){
    var cache = {
        region : {},
        plate : {},
        status : {},
        metro : {},
        type : {},
        data:{}
    }
    var regionCache = {};
    var page = {
        isinit:false,
        index:1,
        pageSize : 20,
        data:{},
        harems:[],
        init:function(opt){
            var that = this;
            that.pagenations();
            that.globalBind();
            $('#pullUp').hide();
        },
        globalBind:function(){
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
                _page.index = 1;
                _page.pagenations();
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
                _page.index += 1;
                _page.pagenations(index);
                setTimeout(function () {

                    ajaxing = false;
                    myScroll.refresh();
                    pullUpEl.style.visibility = 'hidden';
                }, 500);
            }

            setTimeout(function(){
                myScroll = that.scoller = new iScroll('activeListArea', {
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
            // var scrollArea = $('#activeListArea')[0];
            // scrollArea.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

            // $(document).on('click','.room_item',function(){
                // detail.id = $(this).attr('data-id');
                // detail.init();
                // setTimeout(function(){
                    // detail.selectScroller.scrollTo && detail.selectScroller.scrollTo(0,0,10);
                    // detail.selectScroller.scrollTo && detail.selectScroller.refresh();
                // },10);
            // })
        },
        pagenations:function(){
            var that = this;
            if(that.index == 1 || that.index == 0){
                $('#thelist').html('');
            }
            var data = {
                page : that.index,
                per_page : that.pageSize
            }
            ajax.getPageBulids(data,function(data) {
                    if(that.index == 1) {
                        $("#thelist").empty();
                    }
                    if (data.list.length > 0) {
                        var list = data.list;
                        var datalist = [];
                        for(var i=0;i<list.length;i++){
                            var temp = util.tpl.apply('room_item',list[i]);
                            datalist.push(temp);
                        }
                        $('#thelist').append(datalist.join(' '));
                        if(data.list.length==20){
                            $('#pullUp').show();
                        }
                        that.scoller && that.scoller.refresh();
                    }else if(that.index == 1 && data.list.length == 0){
                        $('#index').find('#no_content').show();
                        $('#index').find('#normal_list').hide();
                    }
                }
            )
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

    util.tpl.format.statusChange = function(p){
        return ['looking','looking','want','dealed','disabled'][p];
    }
    util.tpl.format.verified = function(p){
		if(p == '2') return  '';
        return '<span class="room_active"><i></i>认证</span>';
    }
    util.tpl.format.transaction_type = function(p){
        var obj = arguments[arguments.length - 1];
        if(obj.status == 3 || obj.status == 4){
            return obj.last_activity;
        }
        if(p == 1){
            return '出租价格:'+obj.rent_price ;
        }else{
            return '出售价格:'+obj.sell_price ;
        }
    }
    util.tpl.format.edit_transaction_type = function(a,b){
        if(a == b){
            return 'checked';
        }
    }

    util.tpl.format.delivery_status = function(a){
        return ['毛坯交付','毛坯交付','标准交付','现状装修隔断','现状装修家具','拎包办公','协商交付条件'][a];
    }

    var detail = {
        id : 0,
        inited:false,
        selectScroller:{},
        init : function(){
            !this.inited && this.bindAction();
            this.getRoominfo();
            this.logScroller = new iScroll("log_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
        },
        bindAction : function(){
            var that = this;
            $('#detail').on('click','.add_note',function(){
                if(that.id){
                    logs.roomid=that.id;
                    logs.edit=false;
                    setTimeout(function(){
                        logs.init();
                    },10)

                }
            }).on('click','.note',function(){
                if(that.id){
                    logs.roomid=that.id;
                    logs.id=$(this).attr('data-id');
                    logs.edit=true;
                    logs.data_time = $(this).attr('data-time');
                    logs.data_content = $(this).attr('data-content');
                    logs.data_notify = $(this).attr('data-notify');
                    logs.init();
                }
            })
            $('#detail').on('click','.right_menu',function(){
                if(editer.obj){
                    editer.id=that.id;
                    editer.init();
                }
            })
            $('input[name=status]').change(function(){
                var val = $('input[name=status]:checked').val();
                var params = {
                    status : val,
                    id : that.id
                }
                ajax.addRoomedit(params,function(){})
            })
            this.inited = true;
        },
        getRoominfo : function(){
            var that = this;
            ajax.getRoominfo({id:detail.id},function(data){
                that.fillBulidingInfo(data.data);
                editer.obj = data.data;
                //that.createLogs(data.data.rooms);
            })
        },
        fillBulidingInfo : function(data){
            var html = util.tpl.apply('room_detail',data);
            $('#detail').find('#room_detail').html(html);
            $('#detail').find('.progess_options input').eq(parseInt(data.status) - 1).attr('checked','checked');
            var htmls = '';
            for(var i=0;i<data.work_log.length;i++){
                htmls += util.tpl.apply('work_log_list',data.work_log[i]);
            }
            $('#detail').find('#workLog').html(htmls);
            if(detail.logScroller){
                setTimeout(function(){
                    detail.logScroller.refresh();
                },500);
            }
        }
    }
    //if(params){
    //    var search = util.parseURI(params);
    //    detail.id = search.goid;
    //    detail.init();
    //    Mobilebone.transition($('#detail')[0],$('#index')[0],false);
    //}
    var logs = {
        roomid:0,
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
                $('#delete_log').show();
            }
        },
        bindAction : function(){
            var that = this;
            $('#note_date').date();
            $('#notice_day').date({theme:"datetime"});
            $('#delete_log').click(function(){
                confirm('确定要删除本工作日志？',function(){
                    ajax.removeNote(that.id,function(){
                        $('#note_'+that.id).remove();
                        Mobilebone.transition($('#detail')[0],$('#edit')[0],true);
                        detail.logScroller && detail.logScroller.refresh();
                    })
                })
            });
            $('#edit').on('click','.submit',function(){
                var date = $('#edit').find('#note_date').val();
                var content = $('#edit').find('#note_content').val();
                var time = $('#edit').find('#notice_day').val();
                if(content.length<5 || content.length>100){
                    alert("日志长度要求在5到100字以内");
                    return false;
                }
                var _data= {
                    target_id : that.roomid,
                    event_date : date,
                    content : content,
                    notify_time : time,
                    target_type:3
                }
                if(that.edit == true){
                    _data.id = that.id;
                }
                ajax.addWorklogcreate(_data,function(data){
                    var html = util.tpl.apply('work_log_list',_data);
                    var p = $('#detail').find('#workLog');
                    _data.id=data.id;
                    _data.week_no = '星期'+['日','六','五','四','三','二','一'][(new Date(time)).getDay()]
                    if(that.edit == true){
                        var dom = $('#note_'+that.id);
                        $(dom).after(html);
                        dom.remove();
                    }else{
                        p.append(html);
                    }
                    if(detail.logScroller){
                        setTimeout(function(){
                            detail.logScroller.refresh();
                        },500);
                    }
                    Mobilebone.transition($('#detail')[0],$('#edit')[0],true);
                    // $('#edit .go_back').click()
                })
            })
            this.inited = true;
        }
    }

    var uploading = '';
    var editer = {
        id : 0,
        type:'',
        selecting:'',
        init : function(){
            !this.inited && this.bindAction();
            this.getAllSelectOptions();
            this.getRoominfo();
        },
        bindAction : function(){
            var that = this;
            editer.selectScroller = new iScroll("select_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            editer.editScroller = new iScroll("edit_room_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });

            $('#editHouse').on('click','.common_submit',function(){

            })
            $('#editHouse').on('click','input[name=trade]',function(){
                $('#addHouse #room_price_unit').html(['元/m²','元/m²','元/m²/天'][$(this).val()]);
            })
            $('#editHouse').on('click','.common_submit',function(event){
                var params = that.getSearchFormData();
                if(!params){
                    event.stopPropagation();
                    return false;
                }
                params.category = that.obj.category;
                params.id = that.obj.id;
                ajax.addRoomedit(params,function(data){
                    detail.init();
                    Mobilebone.transition($('#detail')[0],$('#editHouse')[0],true);
                })
                event.stopPropagation();
                return false;

            })
            $('#editHouse').on('click','.add_img',function(e){
                uploading = $(this).attr('imgt');
            })
            $('#editHouse').on('click','.img_del',function(e){
                $(this).parent().remove();
                if($('#room_cover .img_span').size() == 0){
                    $('#add_cover_edit').show();
                }
                if($('#other_imgs .img_span').size() < 10){
                    $('#add_image_edit').show();
                }
            })
            this.inited = true;
            $('#select .go_back,#select .submit').click(function(){
                // $('.page').hide();
                // $(this).parents('.page').prev().show();
                if(this.className == 'submit'){
                    that.fillSelected();
                }
                that.selecting = '';
            })
        },
        getRoominfo : function(){
            var that = this;
            var title = util.tpl.apply('editerTitle',that.obj);
            $('#editHouse').find('#editHouse_title').html(title);
            var form='';
            //if(that.obj.category<4){
            //
            //}else{
            //    form = util.tpl.apply('editTwo',that.obj);
            //}
            form = util.tpl.apply('editThree',that.obj);
            $('#editHouse').find('#editHouse_form').html(form);
            $('#editHouse').find('#delivery_date').date();
            $('#editHouse').find('#go_select_status').unbind().click(function(){
                $('#select_title').html('选择交付状态');
                that.selecting = this.getAttribute('ste');
                that.createOtherSelect();
            })
            if(that.obj.avatar){
                $('#add_cover_edit').hide();
            }
            if(that.obj.images.length >= 10){
                $('#add_image_edit').hide();
            }
            if(editer.editScroller){
                setTimeout(function(){
                    editer.editScroller.refresh();
                },500);
            }
        },
        getAllSelectOptions : function(){
            ajax.getAllSelectOption(function(data){
                cache.metro = data.metro;
                cache.status = data.status;
                cache.type = data.type;
                for(var i=0;i<data.region_plate.length;i++){
                    var regId = data.region_plate[i].id;
                    var regName = data.region_plate[i].name;
                    var child = data.region_plate[i].child;
                    cache.region[regId] = regName;
                    regionCache[regId] = data.region_plate[i];
                    for(var j=0;j<child.length;j++){
                        cache.plate[child[j].id] = child[j].name;
                    }
                }
            })
        },
        getSearchFormData : function(){
            var params = {};
            params.transaction_type = $('#editHouse').find('input[name=trade]:checked').val();
            params.delivery_date = $('#editHouse').find('#delivery_date').val();
            params.intro = $('#editHouse').find('#intro').val();
            params.unit_no = $('#editHouse').find('#unit_no').val();
            params.position = $('#editHouse').find('#position').val();
            params.room_price = $('#editHouse').find('#room_price').val();
            params.square_meter = $('#editHouse').find('#square_meter').val();
            params.chair_total = $('#editHouse').find('#chair_total').val();
            params.head_to = $('#editHouse').find('#head_to').val();
            params.delivery_status = $('#go_select_status').attr('vals');
            params.avatar = $('#room_cover .img_del').attr('imgId');
            params.contact_phone = $('#editHouse').find('#contact_phone').val();
            params.images = [];
            $('#other_imgs .img_del').each(function(){
                params.images.push(this.getAttribute('imgId'));
            })
            params.images = params.images.join(',');
            for(var p in params){
                if(p != 'avatar' && (params[p] == '' || !params[p])){
                    delete params[p];
                }
            }
            
            var isBlankObj = true;
            for(var p in params){
                isBlankObj = false;
            }
            if(isBlankObj) return false;
            if(!params.room_price){
                alert('房源价格必填');
                return false;
            }
            if(params.intro.length > 200){
                alert('房源简介应在200字之内');
                return false;
            }
            return params;
        },
        fillSelected : function(){
            var that = this;
            var tar = $('#go_select_'+that.selecting);
            var _cache = cache[that.selecting];
            var name = [], ids = [];
            $('input[name='+that.selecting+']').each(function(){
                if(this.checked){
                    name.push(_cache[this.value]);
                    ids.push(this.value);
                }
            })
            if(name.length){
                tar.find('p').html(name.join(';'));
                tar.attr('vals',ids.join(','));
            }else{
                tar.find('p').html('全部');
            }
        },
        createOtherSelect : function(){
            var html = '<div class="areas">';
            var data = cache[this.selecting];
            var list = [];
            for(var i in data){
                if(data.hasOwnProperty(i)){
                    list.push({
                        id : i,
                        name : data[i]
                    })
                }
            }
            var selected = $('#go_select_' + this.selecting).attr('vals') || '';
            selected = selected.split(',');
            var temp = {};
            for(var i=0;i<selected.length;i++){
                temp[selected[i]] = 1;
            }
            for(var i=0;i<list.length;i++){
                list[i].keyName = this.selecting;
                html += util.tpl.apply('select_option',list[i]);
            }
            html += '</div>';
            $('#options').html(html);
            $('input[name='+this.selecting+']').each(function(){
                if(temp[this.value]){
                    this.checked = true;
                }
            })
            setTimeout(function(){
                if(editer.selectScroller){
                    editer.selectScroller.scrollTo(0,0,10);
                    editer.selectScroller.refresh();
                }
            },10);
        }
    }
    
    util.tpl.format.roomImages = function(){
        var obj = arguments[arguments.length -1];
        if(!obj.images.length){
            return '';
        }else{
            var html = '';
            for(var i=0;i<obj.images.length;i++){
                html += util.tpl.apply('img',{
                   imgType : 'image',
                   avatar : obj.images[i].url,
                   id : obj.images[i].id
                });
            }
            return html;
        }
    }
    util.tpl.format.roomAvatar = function(){
        var obj = arguments[arguments.length -1];
        // obj.avatar = '/lxs/images/test.png'
        if(!obj.avatar){
            return '';
        }else{
            return util.tpl.apply('img',{
               imgType : 'cover',
               avatar : obj.avatar,
               id : obj.avatar_id
            });
        }
    }
    
    var uploadCallBack = function(id,url){
        if(uploading == 'cover'){
            setCover(id,url);
        }else{
            addImages(id,url);
        }
    }
    function setCover(id,url){
        $('#add_cover_edit').hide();
        var html = util.tpl.apply('img',{
           imgType : 'cover',
           avatar : url,
           id : id
        });
        var newIn = $(html);
        $('#add_cover_edit').before(newIn);
        $('#add_cover_edit').hide();
    }
    function addImages(id,url){
        var html = util.tpl.apply('img',{
           imgType : 'cover',
           avatar : url,
           id : id
        });
        var newIn = $(html);
        $('#add_image_edit').before(newIn);
        if($('#other_imgs .img_span').size() >= 10){
            $('#add_image_edit').hide();
        }
        if(editer.editScroller){
            setTimeout(function(){
                editer.editScroller.refresh();
            },500);
        }
    }
    window.addImages = addImages;
    window.setCover = setCover;
    window.uploadCallBack = uploadCallBack;
})
