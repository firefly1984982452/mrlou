LXS(function(ajax){

    util.tpl.format.rentPriceFunction = function(){
        var obj = arguments[arguments.length - 1];
        if(obj.rent_price){
            return '<p class="money">租金'+obj.rent_price+'</p>'
        }
        return '';
    }
    util.tpl.format.sellPriceFunction = function(){
        var obj = arguments[arguments.length - 1];
        if(obj.sell_price){
            return '<p class="money">售价'+obj.sell_price+'</p>'
        }
        return '';
    }

    window.checkSelectedRegion = function(){
        return $('#go_select_region').attr('vals') ? false : true;
    }
    
    /**
        详情页
    */
    var detail = {
        id : 0,
        init : function(){
            !this.inited && this.bindAction();
            this.getBulidingInfo();
            this.getBulidingMoreInfo();
        },
        bindAction : function(){
            var that = this;
            detail.detailMoreScroller = new IScroll("#detail_more_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            detail.detailScroller = new IScroll("#detail_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            $('#collect_buliding').click(function(){
                if(that.id){
                    var dom = $(this);
                    var collected = $(this).hasClass('collected');
                    if(collected) return;
                    ajax.collectBuliding(that.id,function(){
                        $('#collect_buliding').toggleClass('collected');
                    })
                }
            })
            $('#buliding_share').click(function(){
                ajax.createShare(2,that.id,function(data){
                    // var content = encodeURIComponent(data.content);
                    app.share(data);
                    // location.href = 'http://obj@share@'+content+'@'+data.url+'@'+data.avatar+'@'+data.url+'@'+data.id;
                })
            })
            this.inited = true;
        },
        getBulidingInfo : function(){
            var that = this;
            ajax.getBulidingDetail(this.id,function(data){
                $('#go_map').unbind().click(function(){
                    app.goMap(data.data);
                    // var title = encodeURIComponent(data.data.name);
                    // var address = encodeURIComponent(data.data.address);
                    // location.href = 'http://obj@navmap@' + title + '@' + data.data.lat + '@' + data.data.lon + '@' + address + '@' + data.data.id;
                })
                if(data.data.display_detialinfo == '1'){
                    $('.go_detail').show();
                }
                that.fillBulidingInfo(data.data);
                that.createRooms(data.data.rooms);
            })
        },
        getBulidingMoreInfo : function(){
            var that = this;
            ajax.getBulidingMoreInfo(this.id,function(data){
				var isBlank = true;
				for(var p in data.data){
					isBlank= false;
					break;
				}
                if(!isBlank){
					// $('.go_detail').show();
                    that.fillMoreInfo(data.data);
                }else{
                    $('.go_detail').hide();
                }
            })
        },
        createRooms : function(list){
            var html = '';
            for(var i=0;i<list.length;i++){
                html += util.tpl.apply('room',list[i]);
            }
            $('#rooms').html(html);
            $('.room_detail').click(function(){
                app.href('lxs_room_detail.html',{
                    roomid : $(this).attr('rid')
                })
                // location.href = 'http://obj@href@lxs_room_detail.html@roomid=' + $(this).attr('rid');
            })
            detail.detailScroller.refresh();
            
            //经纪经推荐
            $(".mui-grid-view").children().remove();
            var imgs = new Array();
            var recom_url = "http://appapi.imrlou.com/api/recommonagent";
            var recom_parm = {
                building_id: localStorage.getItem("mine_buid"),
                limit: 6
            }
            localStorage.setItem("building_id",detail.id);
            $.getJSON(recom_url, recom_parm, function(data) {
                var img_len = data.list;
             // alert(img_len.length);
                if(img_len.length!=0) {
                    $("#con_recom").show();
                    if (util.getLocal('userType')=='299') {
                        $('#consultant_space').show();
                    }else {
                        $('#consultant_space_bottom').show();
                    }
                    $("#curid").text(localStorage.getItem("curId"))
                        //获取图片
                  var li_start = '<li class="mui-table-view-cell mui-media mui-col-xs-2"><a id="consul_recom" href="javascript:app.href(\'lxs_consultant_recommand.html\',building_id='+detail.id+')"><img class="mui-media-object" src="';
                    var li_end = '" /></a></li>';                                
                    for(var i = 0; i < img_len.length; i++) {
                        imgs[i] = img_len[i].avatar;
                        $(".mui-grid-view").append(li_start + imgs[i] + li_end);
                    }
                }else {
                    $("#con_recom").hide();
                    if (util.getLocal('userType')=='299') {
                        $('#consultant_space').hide()
                    }else {
                        $('#consultant_space_bottom').hide();
                    }
                }
            })         
            
        },
        fillBulidingInfo : function(data){
            $('#buliding_img').unbind().click(function(){
                app.href('image_view.html','buildid=' + detail.id,'hide');
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
                    detail.detailScroller.refresh();
                    img.onload = null;
                }
                img.src = data.avatar;
            }
            if(data.image_total != '0'){
                $('#buliding_img').show();
                $('#img_total_span').html(data.image_total);
            }else{
                $('#buliding_img').hide();
            }
            $('#name,#buliding_title2').html(data.name);
            $('#address').html(data.address);
            $('#detail .info_item').hide();
            
            $('#detail .info_item').each(function(){
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
            if(data.phones.length){
                $('#phones').show();
                if(data.phones[0]){
                    $('#phone1').html(data.phones[0]);
                    $('#phone1').attr('href','tel:' + data.phones[0]);
                    $('#phone1').attr('data-verify', '1-' + data.id);
                    $('#phone1').unbind().click(function(){
                        var tel = $(this).html();
                        var verify = $(this).attr('data-verify');
                        ajax.telLog(tel,verify,function(){});
                    })
                }
                if(data.phones[1]){
                    $('#phone2').html(data.phones[1]);
                    $('#phone2').attr('href','tel:' + data.phones[1]);
                    $('#phone2').attr('data-verify', '1-' + data.id);
                    $('#phone2').unbind().click(function(){
                        var tel = $(this).html();
                        var verify = $(this).attr('data-verify');
                        ajax.telLog(tel,verify,function(){});
                    })
                }
            }
        },
        fillMoreInfo : function(data){
            var html = '';
            for(var p in data){
                html += util.tpl.apply('more_info',{
                    key : p ,
                    value : data[p]
                })
            }
            $('#more_info').html(html);
        }
    }


    util.tpl.format.isVerified = function(){
        var obj = arguments[arguments.length - 1];
        if(obj.verified == '1'){
            return '<span class="house_active"><i></i></span>';
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
    window.detailFallback = function(pageIn,pageOut,target){
        detail.detailScroller && detail.detailScroller.scrollTo(0,0,1);
        detail.roomDetailScroller && detail.roomDetailScroller.scrollTo(0,0,1);
    }

    //params = 'buildid=31002808'
    // confirm('页面接受到的参数params=' + params);
    // alert('解析后：'+JSON.stringify(search))
    var search;
    function pageInit(){
        search = util.parseURI(params);
        detail.id = search.buildid;
        detail.init();
    }
    if(params){
        pageInit();
    }else{
        window.afterParams = pageInit;
    }
});


