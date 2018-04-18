LXS(function(ajax){
    var detail = {
        id : 0,
        init : function(){
            !this.inited && this.bindAction();
            this.getBulidingInfo();
            // this.getBulidingMoreInfo();
            if(!app.isMobile()){
                $('#detail_scroller').addClass('pcview');
                $('html').addClass('share_page');
            }else{
                $('#detail_scroller').addClass('mobile_view');
            }
        },
        bindAction : function(){
            var that = this;
            if(app.isMobile()){
                if(window.iScroll){
                    detail.detailScroller = new iScroll("detail_scroller",{
                        hScrollbar: false,
                        vScrollbar: false,
                        hideScrollbar: true
                    });
                }
            }
            this.inited = true;
        },
        getBulidingInfo : function(){
            var that = this;
            ajax.getShareBuildingDetail2(this.id,function(data){
                document.title = data.name;

                that.fillBulidingInfo(data);
                that.createRooms(data.rooms);

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
                var info = data.info2;
                var html = '';
                for(var p in info){
                    html += util.tpl.apply('more_info',{
                        key : p ,
                        value : info[p]
                    })
                }
                if (html != '') {
                    $('#more_info').show();
                    $('#more_info').html(html);
                }

                var image_html = '';
                for(var i=0;i<data.images.length; i++){
                    image_html += '<div class="swiper-slide"><img style="width:100%;" src="' + data.images[i] + '" /></div>';
                }
                $('#images').html(image_html);
                detail.detailScroller && detail.detailScroller.refresh();

                var swiper = new Swiper('.swiper-container', {
                    paginationClickable: true,
                    spaceBetween: 5,
                    spaceBetween: 30,
                });
            })
        },
        getBulidingMoreInfo : function(){
            var that = this;
            ajax.getBulidingMoreInfo(this.id,function(data){
                that.fillMoreInfo(data);
            })
        },
        createRooms : function(list){
            var html = '';
            for(var i=0;i<list.length;i++){
                html += util.tpl.apply('room',list[i]);
            }
            $('#rooms').html(html);
            detail.detailScroller && detail.detailScroller.refresh();
        },
        fillBulidingInfo : function(data){
            $('#buliding_img').unbind().click(function(){
                imgView.init(data.data.image_list);
            })
            if(data.is_collected == 1){
                $('#collect_buliding').addClass('collected');
            }else{
                $('#collect_buliding').removeClass('collected');
            }
            $('#avatar').attr('src',data.avatar);
            if(data.avatar){
                var img = new Image();
                img.onload = function(){
                    detail.detailScroller && detail.detailScroller.refresh();
                    img.onload = null;
                }
                img.src = data.avatar;
            }
            if(data.image_total != '0'){
                $('#image_total').show();
                $('#image_total_span').html(data.image_total);
            }
            $('#name,#buliding_title2').html(data.name);
            $('#address').html(data.address);
            $('.info_item').hide();
            
            $('.info_item').each(function(){
                var prop = this.id;
                if(prop == 'tags'){
                    if(data.tags.length){
                        $(this).show();
                        var html = '';
                        for(var i=0;i<data.tags.length;i++){
                            html += '<i>'+data.tags[i]+'</i>'
                        }
                        $('#tags_span').html(html);
                    }
                }else{
                    if(data[prop]){
                        $(this).show();
                        $('#'+prop+'_span').html(data[prop]);
                    }
                }
            })
            $('#phones').show();
            if(data.phones.length){
                if(data.phones[0]){
                    $('#phone1').html(data.phones[0]);
                    $('#phone1').attr('href','tel:' + data.phones[0]);
                }
                if(data.phones[1]){
                    $('#phone2').html(data.phones[1]);
                    $('#phone2').attr('href','tel:' + data.phones[1]);
                }
            }
            detail.detailScroller && detail.detailScroller.refresh();
        },
        fillMoreInfo : function(data){
            var info = data.data;
            var html = '';
            for(var p in info){
                html += util.tpl.apply('more_info',{
                    key : p ,
                    value : info[p]
                })
                $('#more_info').style.display="";
            }
            $('#more_info').html(html);
        }
    }


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
    var search = util.getSearch();
    if(search.id){
        detail.id = search.id;
        detail.init();
    }
});


