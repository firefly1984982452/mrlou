FY(function(ajax){
    var cache = {
        region : {},
        plate : {},
        status : {},
        metro : {},
        type : {},
        data:{}
    }
    var formTplMap = {
        '1' : 'form1',
        '2' : 'form1',
        '3' : 'form1',
        '102' : 'form2',
        '201' : 'form2'
    }
    var typeStrMap = {
        '1' : '开发商',
        '2' : '代理商',
        '3' : '运营商',
        '102' : '经纪人',
        '201' : '小业主',
        '299' : '其他'
    }
    var USER;
    var regionCache = {};
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
            return '出租价格:￥'+obj.rent_price + obj.rent_price_unit;
        }else{
            return '出售价格:￥'+obj.sell_price+obj.sell_price_unit;
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

    util.tpl.format.unit_no = function(a){
        return ['','房源单元','房源楼层','房源栋号','中心品牌','空间品牌'][a];
    }
    util.tpl.format.unit_placeholder = function(a){
        return ['','示例：1201室或A栋（1号楼）12A室','示例： 12层或A栋(1号楼)12层','示例：整栋或A栋（1号楼）整栋','示例：2号楼15层雷格斯商务中心','示例：D栋2-4层SOHO3Q'][a];
    }


//    var search = util.getSearch();
    var detail = {
        id : 0,
        inited:false,
        selectScroller:{},
        init : function(){
            !this.inited && this.bindAction();
            this.getUserInfo();
            this.getRoominfo();
            this.logScroller = new IScroll("#log_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
        },
        bindAction : function(){
            var that = this;
            $('#edit .go_back , #select .go_back,#editHouse .go_back').each(function(){
                this.ontouchstart = function(e){
                    e.preventDefault();
                }
            })
            $('.add_note').click(function(){
                if(that.id){
                    logs.roomid=that.id;
                    logs.edit=false;
                    setTimeout(function(){
                        logs.init();
                    },10)
                }
            })
            $('.right_menu').click(function(){
                if(editer.obj){
                    editer.id=that.id;
                    // alert('点击编辑 id为'+editer.id);
                    editer.init();
                }
            })
            // $('#detail').on('click','.right_menu',function(){

            // })
            $('input[name=status]').change(function(){
                var val = $('input[name=status]:checked').val();
                var params = {
                    status : val,
                    id : that.id
                }
                ajax.addRoomedit(params,function(){})
            })
            
            var unitFilterInited = false;
            $('#editHouse').on('focus','#unit_no',function(){
               if(unitFilterInited) return;
               unitFilterInited = true;
                var filter = that.unitFilter = new Filter(document.getElementById('unit_no'),{
                    top : $('#unit_no').offset().top + 8,
                    left : $('#unit_no').offset().left - 1,
                    // minWidth : $('#formName').width() + ($.browser.mozilla || $.browser.msie ? 5 : 4) ,
                    width : $('#unit_no')[0].clientWidth - 1,
                    className : 'search_buliding_option2',
                    height : 300,
                    ajax : true,
                    ajaxFn : function(fn){
                        var keyword = $('#unit_no').val();
                        if(keyword == '') {
                            filter.destroy();
                            return;
                        }
                        ajax.searchUnitByKeyword(keyword,function(data){
                            if($('#editHouse').is(":hidden")){
                                filter.destroy();
                                return;
                            }
                            fn(data.list);
                        })
                    },
                    showFilted : function(result,con,fo){
                        var len = result.length;
                        for(var i=0;i<len;i++){
                            var a = document.createElement('a');
                            a.innerHTML = result[i];
                            a.className = 'search_buliding_option2';
                            con.appendChild(a);
                        }
                    },
                    selectFn : function(obj,ipt,fo){
                        ipt.value = obj;
                        filter.destroy();
                        return false;
                    },
                    noResult : function(){
                        if($('#editHouse').is(":hidden")){
                            filter.destroy();
                            return;
                        }
                        filter.dom.con.innerHTML = '<p style="padding:10px 0;text-align:center;font-size:14px;color:#444;">没有搜索到结果</p>';
                    }
                })
            })
            this.inited = true;
        },
        getRoominfo : function(){
            var that = this;
            ajax.getRoominfo({id:detail.id},function(data){
                that.createLogList(data.data);
                editer.obj = data.data;
            })
        },
        createLogList : function(data){
            var that = this;
            var html = util.tpl.apply('room_detail',data);
            $('#detail').find('#room_detail').html(html);
            $('#detail').find('.progess_options input').eq(parseInt(data.status) - 1).attr('checked','checked');
            var htmls = '';
            for(var i=0;i<data.work_log.length;i++){
                htmls += util.tpl.apply('work_log_list',data.work_log[i]);
            }
            $('#detail').find('#workLog').html(htmls);
            $('.note').click(function(){
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
            if(detail.logScroller){
                setTimeout(function(){
                    detail.logScroller.refresh();
                },500);
            }
        },
        getUserInfo : function(){
            var that = this;
            ajax.getUserInfo(function(data){
                USER = data.user;
            })
        }
    }

    function pageInit(){
        var search = util.parseURI(params);
        if(search.back == 'index'){
            $('#detail .go_back').attr('href','javascript:app.href("fangyuan_index.html");');
        }else{
            $('#detail .go_back').attr('href','javascript:app.goBack()');
        }
        // alert('detail.init ID为'+search.goid);
        detail.id = search.goid;
        //detail.id = 20;
        detail.init();
    }

    //params = 'goid=53'
    if(params){
        // alert('页面load 有参数 开始init');
        pageInit();
    }else{
        // alert('页面load 没参数 回调init');
        window.afterParams = pageInit;
    }

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
                $('#delete_log').hide();
                $('#note_content').val('');
                $('#notice_day').val('');
            }
        },
        bindAction : function(){
            var that = this;
            // $('#editHouse .top_banner a').each(function(){
                // this.ontouchstart = function(e){
                    // e.preventDefault();
                // }
            // })
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
            $('#edit .submit').click(function(){
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
                    var p = $('#detail').find('#workLog');
                    _data.id=data.id;
                    _data.week_no = '星期'+['日','一','二','三','四','五','六'][date.toDate().getDay()]
                    console.log(_data)
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
                            logs.roomid=detail.id;
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
            editer.type = editer.obj.category;
            !this.inited && this.bindAction();
            this.getAllSelectOptions();
            this.getRoominfo();
        },
        bindAction : function(){
            var that = this;
            // $('#editHouse .top_banner a').each(function(){
                // this.ontouchstart = function(e){
                    // e.preventDefault();
                // }
            // })
            editer.selectScroller = new IScroll("#select_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            editer.editScroller = new IScroll("#edit_room_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            $('#editHouse').on('click','input[name=trade]',function(e){
                return false;
                if(that.type < 4){
//                    $('#editHouse #room_price_unit').html(['元/m²/天','元/m²/天','元/m²'][$(this).val()]);
                }
            })
            $('#editHouse .common_submit').click(function(event){
                var params = that.getSearchFormData();
                if(!params){
                    event.stopPropagation();
                    return false;
                }
                params.category = that.obj.category;
                params.id = that.obj.id;
                ajax.addRoomedit(params,function(data){
                    detail.init();
                    if(detail.logScroller){
                        setTimeout(function(){
                            detail.logScroller.refresh();
                        },200);
                    }
                    Mobilebone.transition($('#detail')[0],$('#editHouse')[0],true);
                })
                event.stopPropagation();
                return false;

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
            // alert('编辑初始化 数据为'+that.obj);
            if(that.obj.category<4){
                form = util.tpl.apply('editOne',that.obj);
            }else{
                form = util.tpl.apply('editTwo',that.obj);
            }
            //form = util.tpl.apply('editThree',that.obj);
            $('#editHouse').find('#editHouse_form').html(form);
            editer.editScroller.scrollTo(0,0,10);
            // editer.editScroller.refresh();
            $('#isCompleted').change(function(){
                if(this.checked){
                    $('#delivery_date').val('现房').attr('disable',true);
                }else{
                    $('#delivery_date').val(that.obj.delivery_date).attr('disable',false);
                }
            })
            $('#square_meter,#room_price').keydown(function(e){
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
            
            $('#editHouse').find('#delivery_date').date();
            $('#editHouse').find('#go_select_head').click(function(){
                $('#select_title').html('选择房间位置');
                that.selecting = this.getAttribute('ste');
                that.createOtherSelect();
            })
            $('#editHouse').find('#go_select_status').click(function(){
                $('#select_title').html('选择交付状态');
                that.selecting = this.getAttribute('ste');
                that.createOtherSelect();
            })
            setTimeout(function(){
                $('#intro').reszieTextarea({
                    callback : function(){
                        editer.editScroller.scrollTo(0,0,10);
                        editer.editScroller && editer.editScroller.refresh();
                    }
                })
            },500);

            $('#editHouse .img_del').click(function(e){
                $(this).parent().remove();
                if($('#room_cover .img_span').size() == 0){
                    $('#add_cover_edit').show();
                }
                if($('#other_imgs .img_span').size() < 10){
                    $('#add_image_edit').show();
                }
            })
            $('#editHouse .add_img').click(function(e){
                uploading = $(this).attr('imgt');
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
                },100);
            }
            $('#contact_phone').bind('input',function(){
                var val = this.value;
                var reg = /[^\d-]/g;
                this.value = val.replace(reg,'');
            })
            $('#editHouse #room_price_unit').html(['元/m²/天','元/m²/天','元/m²'][$('input[name=trade]:checked').val()]);

        },
        getAllSelectOptions : function(){
            ajax.getAllSelectOption(function(data){
                cache.metro = data.metro;
                cache.status = data.status;
                cache.type = data.type;
                cache.head = {
                    '外侧间':'外侧间',
                    '内侧间':'内侧间',
                    '公共位':'公共位'
                };
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
            params.position = $('#go_select_head').attr('vals');
            params.room_price = $('#editHouse').find('#room_price').val();
            params.square_meter = $('#editHouse').find('#square_meter').val();
            params.chair_total = $('#editHouse').find('#chair_total').val();
            params.head_to = $('#editHouse').find('#head_to').val();
            params.delivery_status = $('#go_select_status').attr('vals');
            params.avatar = $('#room_cover .img_del').attr('imgId');
            params.contact_phone = $('#editHouse').find('#contact_phone').val();
            params.min_rent_period = $('#editHouse').find('#min_rent_period').val();
            params.images = [];
            $('#other_imgs .img_del').each(function(){
                params.images.push(this.getAttribute('imgId'));
            })
            params.images = params.images.join(',');
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
            if(!params.room_price){
                alert('房源价格必填');
                return false;
            }
            var reg = new RegExp("^[0-9]+(.[0-9]{2})?$", "g");
            /*
            if(!reg.test(params.square_meter)){
                alert("房源面积只能为数字，最多只能有两位小数！");
                return false;
            }
            if(!reg.test(params.room_price)){
                alert("房源报价只能为数字，最多只能有两位小数！");
                return false;
            }
            */
            if(!params.contact_phone || params.contact_phone && (params.contact_phone.length < 8 || params.contact_phone.length > 12)){
                alert('联系电话应在8-12字之间');
                return false;
            }
            var r = /^\+?[1-9][0-9]*$/; //正整数
            if(!!params.chair_total && !r.test(params.chair_total)){
                alert("座位只能为整数！");
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
                tar.find('p').html('请选择');
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
    util.tpl.format.isCompletedRoom = function(){
        var obj = arguments[arguments.length - 1];
        return obj.delivery_date == '现房' ? ' checked="checked"' : '';
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
        $('#editHouse .img_del').unbind().click(function(e){
            $(this).parent().remove();
            if($('#room_cover .img_span').size() == 0){
                $('#add_cover_edit').show();
            }
            if($('#other_imgs .img_span').size() < 10){
                $('#add_image_edit').show();
            }
        })
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
        $('#editHouse .img_del').unbind().click(function(e){
            $(this).parent().remove();
            if($('#room_cover .img_span').size() == 0){
                $('#add_cover_edit').show();
            }
            if($('#other_imgs .img_span').size() < 10){
                $('#add_image_edit').show();
            }
        })
    }
    window.addImages = addImages;
    window.setCover = setCover;
    window.uploadCallBack = uploadCallBack;
    
    window.editFallback = function(){
        detail.unitFilter && detail.unitFilter.destroy();
    }
})
