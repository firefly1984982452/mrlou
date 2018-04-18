LXS(function(ajax){
    var search = util.getSearch();
    var roomDetail = {
        id : 0,
        init : function(){
            !this.inited && this.bindAction();
            this.getRoomInfo();
            if(!app.isMobile()){
                $('#room_detail_scroller').addClass('pcview');
                $('html').addClass('share_page');
            }else{
                $('#room_detail_scroller').addClass('mobile_view');
            }
        },
        bindAction : function(){
            var that = this;
            if(app.isMobile()){
                if(window.iScroll){
                    roomDetail.roomDetailScroller = new iScroll("room_detail_scroller",{
                        hScrollbar: false,
                        vScrollbar: false,
                        hideScrollbar: true
                    });
                }
            }
            this.inited = true;
        },
        getRoomInfo : function(){
            var that = this;
            ajax.getShareRoomInfo2(this.id,function(data){
                document.title = data.building_name;
                that.fillRoomInfo(data);

                var image_html = '';
                for(var i=0;i<data.image_list.length; i++){
                    image_html += '<div class="swiper-slide"><img style="width:100%;" src="' + data.image_list[i] + '" /></div>';
                }
                $('#images').html(image_html);

                var swiper = new Swiper('.swiper-container', {
                    paginationClickable: true,
                    spaceBetween: 5,
                    spaceBetween: 30,
                });

                roomDetail.roomDetailScroller && roomDetail.roomDetailScroller.refresh();
            })
        },
        fillRoomInfo : function(data){
            if(data.is_collected == 1){
                $('#collect_room').addClass('collected');
            }else{
                $('#collect_room').removeClass('collected');
            }
            $('#room_detail_atavar').attr('src',data.avatar);
            if(data.avatar){
                var img = new Image();
                img.onload = function(){
                    roomDetail.roomDetailScroller && roomDetail.roomDetailScroller.refresh();
                    img.onload = null;
                }
                img.src = data.avatar;
            }
            if(data.image_total != '0'){
                $('#room_imgs').show();
                $('#room_total_span').html(data.image_total);
            }
            $('#room_buliding_name').html(data.building_name);
            var roomStatusStr = ['','带看','意向','成交','失败'];
            var statusClass = ['','lead_look','wanted','dealed','disabled'];
            $('#room_status').html(roomStatusStr[data.status]);
            
            $('#unit_no').html(data.unit_no);
            $('#building_address').html(data.building_address);
            
            $('.info_item').hide();
            var deliveryStr = {
                '1' : '毛坯交付',
                '2' : '标准交付',
                '3' : '现状装修隔断',
                '4' : '现状装修家具',
                '5' : '拎包办公',
                '6' : '协商交付条件'
            }
            $('.info_item').each(function(){
                var prop = $(this).attr('prop_val');
                if(prop == 'delivery_status'){
                    if(data[prop]){
                        $(this).show();
                        $('#room_'+prop+'_span').html(deliveryStr[data[prop]]);
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
                '299' : '其他',
                '104' : '企业服务商'
            }
            if(data.user.phone){
                $('#user_phone_con').show();
                $('#user_phone').html(data.user.phone);
                $('#user_phone').attr('href','tel:' + data.user.phone);
            }
            $('#user_avatar').attr('src',data.user.avatar);
            $('#user_company').html(data.user.company);
            $('#user_type').html(typeMap[data.user.type]);
            $('#user_name').html(data.user.cn_username || data.user.en_username);
            roomDetail.roomDetailScroller && roomDetail.roomDetailScroller.refresh();
        }
    }
    roomDetail.id = search.id;
    if(roomDetail.id){
        roomDetail.init();
    }
});
