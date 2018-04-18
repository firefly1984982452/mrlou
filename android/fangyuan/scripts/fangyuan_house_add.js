FY(function(ajax){

	
    var uploading = '';
    var cache = {
        region : {},
        plate : {},
        status : {},
        metro : {},
        type : {},
        data:{},
        user:{}
    }
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
        return ['认证','认证','未认证'][p];
    }
    util.tpl.format.transaction_type = function(p){
        var obj = arguments[arguments.length - 1];
        if(p == 1){
            return '出租价格：￥'+obj.rent_price+obj.rent_price_unit;
        }else{
            return '出售价格：￥'+obj.sell_price+obj.sell_price_unit;
        }
    }
    util.tpl.format.edit_transaction_type = function(a,b){
        if(a == b){
            return 'checked';
        }
    }
    util.tpl.format.unit_no = function(a){
        return ['','房源单元','房源楼层','房源栋号','中心品牌','空间品牌'][a];
    }
    util.tpl.format.unit_placeholder = function(a){
        return ['','示例：1201室或A栋（1号楼）12A室','示例： 12层或A栋(1号楼)12层','示例：整栋或A栋（1号楼）整栋','示例：2号楼15层雷格斯商务中心','示例：D栋2-4层SOHO3Q'][a];
    }
    

    var adder = {
        id : 0,
        type:'',
        selecting:'',
        init : function(){
            !this.inited && this.bindAction();
            this.getRoominfo();
            this.getUserInfo();
            this.getAllSelectOptions();
            if (type == 1) {
                $('#head_to').attr('placeholder', '示例：朝南');
            } else if (type == 2 || type == 3) {
                $('#head_to').attr('placeholder', '选填（示例：全方向）');
            } else if (type == 4 || type == 5) {
                $('#head_to').attr('placeholder', '选填（示例：朝南）');
            }
        },
        bindAction : function(){
            var that = this;
            $('#select .top_banner a,#summary .top_banner a,#image .top_banner a').each(function(){
                this.ontouchstart = function(e){
                    e.preventDefault();
                }
            })
            $('#addHouse').on('click','input[name=trade]',function(){
                if(that.type < 4){
                    $('#addHouse #room_price_unit').html(['元/m²/天','元/m²/天','元/m²'][$(this).val()]);
                }
            })
            $('#add .next_step').click(function(event){
                var params = that.getSearchFormData();
                if(!params){
                    event.stopPropagation();
                    return false;
                }
                params.category = that.type;
                if (that.id) {
                    params.id = that.id;
                    ajax.addRoomedit(params,function(data){
                        that.id = data.id;
                        Mobilebone.transition($('#summary')[0],$('#add')[0],false);
                    })
                } else {
                    ajax.addRoomcreate(params,function(data){
                        that.id = data.id;
                        Mobilebone.transition($('#summary')[0],$('#add')[0],false);
                    })
                }
                event.stopPropagation();
                return false;

            })
            

            $('#summary .next_step').click(function(event){

                /*
                if($('#summary_area').val() == ''){
                    event.stopPropagation();
                    alert('请填写描述')
                    return false;
                }
                */

                var params={
                	var info_ = $('#summary_area').val();
                	if(info_==null){
                		intro:"该房源暂无简介。";
                	}else{
                		info:info_;
                	}
                }
                params.category = that.type;
                params.id = that.id;
                ajax.addRoomedit(params,function(data){
                    Mobilebone.transition($('#image')[0],$('#summary')[0],false);
                })
                event.stopPropagation();
                return false;

            })
            $('#image .next_step').click(function(e){
                var imgId = $('#thread_img').attr('imgId');
                var params={};
                params.category = that.type;
                params.id = that.id;
                params.avatar = $('#room_cover .img_del').attr('imgId');
                params.images = [];
                $('#other_imgs .img_del').each(function(){
                    params.images.push(this.getAttribute('imgId'));
                })
                params.images = params.images.join(',');
                ajax.addRoomedit(params,function(data){
                	if(data.msg.equals("success")){
                		alert('房源添加成功',function(){
	                        app.href('lxs_index.html',{
	                            room_id : data.id
	                        })
	                        // location.href = 'http://obj@href@fangyuan_index.html@goid='+that.id;
	                    });
                	}else{
                		alert('房源添加失败，系统无此楼盘，请联系客服更行更改',function(){
	                        app.href('lxs_index.html');
	                        // location.href = 'http://obj@href@fangyuan_index.html@goid='+that.id;
	                    })
                	}
                    
                })
                event.stopPropagation();
                return false;

            })
            this.inited = true;
            adder.selectScroller = new IScroll("#select_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            adder.addScroller = new IScroll("#add_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            $('#select .go_back,#select .submit').click(function(){
                // $('.page').hide();
                // $(this).parents('.page').prev().show();
                if(this.className == 'submit'){
                    that.fillSelected();
                }
                that.selecting = '';
            })
            $('#image .img_del').click(function(e){
                $(this).parent().remove();
                if($('#room_cover .img_span').size() == 0){
                    $('#add_cover').show();
                }
                if($('#other_imgs .img_span').size() < 10){
                    $('#add_image').show();
                }
            })
            var bulidingFilterInited = false;
            $('#addHouse').on('focus','#building_name',function(){
                if(bulidingFilterInited) return;
                bulidingFilterInited = true;
                var filter = that.bulidingFilter = new Filter(document.getElementById('building_name'),{
                    top : $('#building_name').offset().top + 8,
                    left : $('#building_name').offset().left - 1,
                    // minWidth : $('#formName').width() + ($.browser.mozilla || $.browser.msie ? 5 : 4) ,
                    width : $('#building_name')[0].clientWidth - 1,
                    className : 'search_buliding_option2',
                    height : 300,
                    ajax : true,
                    ajaxFn : function(fn){
                        var keyword = $('#building_name').val();
                        if(keyword == '') {
                            filter.destroy();
                            return;
                        }
                        ajax.searchBulidingByKeyword(keyword,function(data){
                            if($('#addHouse').is(":hidden")){
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
                            var name = document.createElement('p');
                            name.innerHTML = result[i].name;
                            name.className = '_name';
                            var address = document.createElement('p');
                            address.innerHTML = result[i].address;
                            address.className = '_address'
                            a.appendChild(name);
                            a.appendChild(address);
                            a.className = 'search_buliding_option2';
                            con.appendChild(a);
                        }
                    },
                    selectFn : function(obj,ipt,fo){
                        ipt.value = obj.name;
                        $(ipt).attr('bid',obj.id);
                        //goSearchBuliding();
                        filter.destroy();
                        return false;
                    },
                    noResult : function(){
                        if($('#addHouse').is(":hidden")){
                            filter.destroy();
                            return;
                        }
                        hasResult = false;
                        filter.dom.con.innerHTML = '<p style="padding:10px 0;text-align:center;font-size:14px;color:#444;">没有搜索到结果</p>';
                    }
                })
            })
            var unitFilterInited = false;
            $('#addHouse').on('focus','#unit_no',function(){
                if(that.type >= 4){
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
                                if($('#addHouse').is(":hidden")){
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
                            if($('#addHouse').is(":hidden")){
                                filter.destroy();
                                return;
                            }
                            filter.dom.con.innerHTML = '<p style="padding:10px 0;text-align:center;font-size:14px;color:#444;">没有搜索到结果</p>';
                        }
                    })
                }
            })
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
        getRoominfo : function(){
            var that = this;
            var form='';
            if(that.type<4){
                form = util.tpl.apply('editOne',{type:that.type});
            }else{
                form = util.tpl.apply('editTwo',{type:that.type});
            }
            $('#addHouse').html(form);
            $('#isCompleted').change(function(){
                if(this.checked){
                    $('#delivery_date').val('现房').attr('disable',true);
                }else{
                    $('#delivery_date').val('').attr('disable',false);
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
            
            $('#addHouse').find('#delivery_date').date();
            $('#addHouse').find('#go_select_head').click(function(){
                $('#select_title').html('选择房间位置');
                that.selecting = this.getAttribute('ste');
                that.createOtherSelect();
            })
            $('#addHouse').find('#go_select_status').click(function(){
                $('#select_title').html('选择交付状态');
                that.selecting = this.getAttribute('ste');
                that.createOtherSelect();
            })
            $('#contact_phone').bind('input',function(){
                var val = this.value;
                var reg = /[^\d-]/g;
                this.value = val.replace(reg,'');
            })
            adder.addScroller && adder.addScroller.refresh();
        },
        getUserInfo : function(){
            ajax.getUserInfo(function(data){
                cache.user = data.user;
                $('#contact_phone').val(data.user.phone_number);
            })
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
            $('#image').on('click','.add_img',function(e){
                uploading = $(this).attr('imgt');
            })
            setTimeout(function(){
                adder.selectScroller.scrollTo(0,0,10);
                adder.selectScroller.refresh();
            },10);
        },
        getSearchFormData : function(){
            var params = {};
            params.transaction_type = $('#add').find('input[name=trade]:checked').val();
            params.delivery_date = $('#add').find('#delivery_date').val();
            params.building_name = $('#add').find('#building_name').val();
            params.building_id = $('#add').find('#building_name').attr('bid');
            params.intro = $('#add').find('#intro').val();
            params.unit_no = $('#add').find('#unit_no').val();
            params.position = $('#go_select_head').attr('vals');
            params.room_price = $('#add').find('#room_price').val();
            params.square_meter = $('#add').find('#square_meter').val();
            params.chair_total = $('#add').find('#chair_total').val();
            params.head_to = $('#add').find('#head_to').val();
            params.contact_phone = $('#add').find('#contact_phone').val();
            params.min_rent_period = $('#add').find('#min_rent_period').val();
            params.delivery_status = $('#go_select_status').attr('vals');
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
            if(!params.building_name){
                alert('楼盘名称必填');
                return false;
            }
            if(!params.unit_no){
                alert('楼盘单元必填');
                return false;
            }
            if(!params.room_price){
                alert('房源报价必填');
                return false;
            }
            if(!params.delivery_status){
                alert('交付状态必填');
                return false;
            }
            if(!params.contact_phone || params.contact_phone && (params.contact_phone.length < 8 || params.contact_phone.length > 12)){
                alert('联系电话应在8-12字之间');
                return false;
            }
            // var reg = new RegExp("^[0-9]+(.[0-9]{2})?$", "g");
            // if(!reg.test(params.square_meter)){
                // alert("房源面积只能为数字，最多只能有两位小数！");
                // return false;
            // }
            // if(!reg.test(params.room_price)){
                // alert("房源报价只能为数字，最多只能有两位小数！");
                // return false;
            // }
            var r = /^\+?[1-9][0-9]*$/; //正整数

            if(!!params.chair_total && !r.test(params.chair_total)){
                alert("座位只能为整数！");
                return false;
            }
            if (type == 1) {
                if(!params.head_to){
                    alert("请输入房源朝向");
                    return false;
                }
            }
            if (type == 4 || type == 5) {
                if(!params.min_rent_period){
                    alert("最短租约必填");
                    return false;
                }
            }
            return params;
        },
        fillBulidingInfo : function(data){
            var html = util.tpl.apply('room_detail',data);
            $('#detail').find('#room_detail').html(html);
            $('#detail').find('.progess_options input').eq(data.status).attr('checked','checked');
            var htmls = '';
            for(var i=0;i<data.work_log.length;i++){
                htmls += util.tpl.apply('work_log_list',data.work_log[i]);
            }
            $('#detail').find('#workLog').html(htmls);
        },
        fillSelected : function(){
            var that = this;
            var tar = $('#go_select_'+that.selecting);
            var _cache = cache[that.selecting]
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
        }
    }
    var search , type;
    function pageInit(){
        search = util.parseURI(params);
        type = search.type;
        adder.type = type;
        adder.init();
    }
    //params = 'type=1'
    if(params){
        pageInit();
    }else{
        window.afterParams = pageInit;
    }
    var uploadCallBack = function(id,url){
        if(uploading == 'cover'){
            setCover(id,url);
        }else{
            addImages(id,url);
        }
    }
    function setCover(id,url){
        $('#add_cover').hide();
        var html = util.tpl.apply('img',{
            imgType : 'cover',
            avatar : url,
            id : id
        });
        var newIn = $(html);
        $('#add_cover').before(newIn);
        $('#add_cover').hide();
        $('#image .img_del').unbind().click(function(e){
            $(this).parent().remove();
            if($('#room_cover .img_span').size() == 0){
                $('#add_cover').show();
            }
            if($('#other_imgs .img_span').size() < 10){
                $('#add_image').show();
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
        $('#add_image').before(newIn);
        if($('#other_imgs .img_span').size() >= 10){
            $('#add_image').hide();
        }
        $('#image .img_del').unbind().click(function(e){
            $(this).parent().remove();
            if($('#room_cover .img_span').size() == 0){
                $('#add_cover').show();
            }
            if($('#other_imgs .img_span').size() < 10){
                $('#add_image').show();
            }
        })
    }
    window.addImages = addImages;
    window.setCover = setCover;
    window.uploadCallBack = uploadCallBack;
})

