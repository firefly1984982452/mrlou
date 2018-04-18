LXS(function(ajax){
    util.tpl.format.isVerified = function(){
        var obj = arguments[arguments.length - 1];
        if(obj.verified == '1'){
            return '<span class="house_active"><i></i><span>认证</span></span>';
        }
        return '';
    }
    var classArr = ['','looking','want','dealed','disabled'];
    util.tpl.format.statusClass = function(status){
        status = parseInt(status);
        return classArr[status];
    }
    var typeClassArr = ['','rent','sale'];
    util.tpl.format.typeClass = function(type){
        type = parseInt(type);
        return typeClassArr[type];
    }
    
    util.tpl.format.priceAssert = function(){
        var obj = arguments[arguments.length - 1];
        if(obj.transaction_type == '1'){
            return '<p class="house_price">租金报价：<span>¥' + obj.rent_price + '</span></p>';
        }else{
            return '<p class="house_price">出售报价：<span>¥' + obj.sell_price + '</span></p>';
        }
    }
    
    /**
        详情页
    */
    var roomDetail = {
        id : 0,
        init : function(){
            !this.inited && this.bindAction();
            this.getRoomInfo();
        },
        bindAction : function(){
            var that = this;
            roomDetail.roomDetailScroller = new IScroll("#room_detail_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            $('#collect_room').click(function(){
                if(that.id){
                    var dom = $(this);
                    var collected = $(this).hasClass('collected');
                    if(collected) return;
                    ajax.collectRoom(that.id,function(){
                        $('#collect_room').toggleClass('collected');
                    })
                }
            })
            $('#room_share').click(function(){
                ajax.createShare(3,that.id,function(data){
                    app.share(data);
                    // var content = encodeURIComponent(data.content);
                    // location.href = 'http://obj@share@'+content+'@'+data.url+'@'+data.avatar+'@'+data.url+'@'+data.id;
                });
            })

            this.inited = true;
        },
        getRoomInfo : function(){
            var that = this;
            ajax.getRoomInfo(this.id,function(data){
                that.fillRoomInfo(data.data);
                $('#room_imgs').unbind().click(function(){
                    app.href('image_view.html','buildid=' + detail.id,'hide');
                })
            })
        },
        fillRoomInfo : function(data){
            if(data.is_collected == 1){
                $('#collect_room').addClass('collected');
            }else{
                $('#collect_room').removeClass('collected');
            }
            $('#room_detail_atavar').attr('src',data.avatar);
            $('#collection_num').html(data.sta_collect_total);
            if(data.avatar){
                var img = new Image();
                img.onload = function(){
                    roomDetail.roomDetailScroller.refresh();
                    img.onload = null;
                }
                img.src = data.avatar;
            }
            
            if(data.image_total != '0'){
                $('#room_imgs').show();
                $('#room_total_span').html(data.image_total);
            }else{
                $('#room_imgs').hide();
            }

            $('#room_buliding_name').html(data.building_name);
            var roomStatusStr = ['','带看','意向','成交','失败'];
            var statusClass = ['','lead_look','wanted','dealed','disabled'];
            $('#room_status').html(roomStatusStr[data.status]);
            
            $('#unit_no').html(data.unit_no);
            
            $('#room_detail .info_item').hide();
            var deliveryStr = {
                '1' : '毛坯交付',
                '2' : '标准交付',
                '3' : '现状装修隔断',
                '4' : '现状装修家具',
                '5' : '拎包办公',
                '6' : '协商交付条件'
            }
            $('#room_detail .info_item').each(function(){
                var prop = $(this).attr('prop_val');
                if(prop == 'delivery_status'){
                    if(data[prop]){
                        $(this).show();
                        $('#room_'+prop+'_span').html(deliveryStr[data[prop]]);
                    }else{
                        $(this).hide();
                        $('#room_'+prop+'_span').html('');
                    }
                }else{
                    if(data[prop]){
                        $(this).show();
                        $('#room_'+prop+'_span').html(data[prop]);
                    }
                }
            })
            var typeMap = {
                '1' : '开发商',
                '2' : '代理商',
                '3' : '运营商',
                '102' : '经纪人',
                '201' : '小业主',
                '299' : '其他'
            }
            if(data.user.phone){
                $('#user_phone_con').show();
                $('#user_phone').html(data.user.phone);
                $('#user_phone').attr('href','tel:' + data.user.phone);
            }
            
            
            $('#user_avatar').attr('src',data.user.avatar);
            $('#user_company').html(data.user.company);
            $('#user_type').html(typeMap[data.user.type]);
            $('#user_name').html(data.user.cn_username + data.user.en_username);
            roomDetail.roomDetailScroller.refresh();
        }
    }    
    window.detailFallback = function(pageIn,pageOut,target){
        roomDetail.roomDetailScroller && roomDetail.roomDetailScroller.scrollTo(0,0,1);
    }

    // confirm('页面接收到的参数'+params);
    // alert('解析后：'+JSON.stringify(search))
    var search;
    function pageInit(){
        search = util.parseURI(params);
        roomDetail.id = search.roomid;
        roomDetail.init();
    }
    if(params){
        pageInit();
    }else{
        window.afterParams = pageInit;
    }
});


