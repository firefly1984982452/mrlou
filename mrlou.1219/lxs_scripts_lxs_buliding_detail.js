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
            detail.detailMoreScroller = new iScroll("detail_more_scroller",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            detail.detailScroller = new iScroll("detail_scroller",{
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

            //纠错
            var href_con = "javascript:app.href('lxs_error_correction.html')";
            $(".error_correction").on("click", function() {
                var parent = $(this).parent().parent().parent().attr("id");
                if(parent == "detail") {
                    //楼盘
                    var name = $("#name").text();
                    localStorage.setItem("lp_name", name);
                    localStorage.setItem("err_type",1);
                    $(this).attr("href",href_con);
                }
        
                if(parent == "room_detail") {
                    //房源
                    var room_buliding_name = $("#room_buliding_name").text();
                    var room_square_meter_span = $("#room_square_meter_span").text(); //面积
                    localStorage.setItem("fy_name", room_buliding_name);
                    localStorage.setItem("unit", $("#unit_no").text());
                    localStorage.setItem("err_type",2);
                    $(this).attr("href",href_con);
                }
            })


            $('#buliding_share').click(function(){
                ajax.createShare(2,that.id,function(data){
                    // var content = encodeURIComponent(data.content);
                    app.share(data);
                    // location.href = 'https://obj@share@'+content+'@'+data.url+'@'+data.avatar+'@'+data.url+'@'+data.id;
                })
            })
            this.inited = true;
        },
        getBulidingInfo : function(){
            var that = this;
            ajax.getBulidingDetail(this.id,function(data){
                $('#go_map').unbind().click(function(){
                    app.goMap(data.data);
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
                // location.href = 'https://obj@href@lxs_room_detail.html@roomid=' + $(this).attr('rid');
            })
            detail.detailScroller.refresh();

        },
        fillBulidingInfo : function(data){
            $('#buliding_img').unbind().click(function(){
				localStorage.setItem('imageType','build');
				localStorage.setItem('buildImageId',detail.id);
            	setTimeout(function(){
                	app.href('image_view.html','buildid=' + detail.id,'hide');            		
            	},100);
            })
			$("#mui-grid-view").children().remove();
			$("#mui-grid-view1").children().remove();
			var recom_url = "https://appapi.imrlou.com/api/recommonagent";
			var recomSer_url = "https://appapi.imrlou.com/api/recommonservice";
			var recom_parm = {
				building_id: detail.id,
				limit: 6
			}
			var recomServer_parm = {
				building_id: detail.id,
				limit: 3
			}
			localStorage.setItem("building_id", detail.id);
			var recom = '';
			//经纪人推荐列表
			$.getJSON(recom_url, recom_parm, function(data) {
				var img_len = data.list;
				// alert(img_len.length);
				$("#total").text(img_len.length);
				if(img_len.length != 0) {
					$("#con_recom").show();
					if(util.getLocal('userType') == '299' || util.getLocal('userType')=='104') {
						$('#consultant_space').show();
					} else {
						$('#consultant_space_bottom').show();
					}
					$("#curid").text(localStorage.getItem("curId"))
						//获取图片
					for(var i = 0; i < img_len.length; i++) {
						$("#mui-grid-view").append('<li class="mui-table-view-cell mui-media mui-col-xs-2"><a id="consul_recom" ><img class="mui-media-object" src="'+img_len[i].avatar+'" /></a></li>');
					}
				

				} else {
					$("#con_recom").hide();
					if(util.getLocal('userType') == '299' || util.getLocal('userType')=='104') {
						$('#consultant_space').hide()
					} else {
						$('#consultant_space_bottom').hide();
					}
				}
			})

			//增值服务商推荐列表
			$.getJSON(recomSer_url, recomServer_parm, function(data) {
				var img_len = data.list;
				$("#total1").text(img_len.length);
				if(img_len.length != 0) {
					$("#con_recom1").show();
					$('#consultant_space_bottom1').show();
					$("#curid").text(localStorage.getItem("curId"))
						//获取图片
					var li_start = '<li><img src="';
					var li_end_noLength = '" /><div class="info"><span class="noLength">';
					var li_end_leng = '" /><div class="info"><span class="length">';
					var li_end_ = '</span></div></li>';
					recom = 'recomServer';
					localStorage.setItem('recom', recom);
					for(var i = 0; i < img_len.length; i++) {
						if (img_len[i].user_company.length>=5) {
							$("#mui-grid-view1").append(li_start + img_len[i].avatar + li_end_noLength + img_len[i].user_company + li_end_);										
						} else{
							$("#mui-grid-view1").append(li_start + img_len[i].avatar + li_end_leng + img_len[i].user_company + li_end_);									
						}
					}

					$('#go_server').click(function() {
							var u = navigator.userAgent;
							var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
							var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
							if(isAndroid) {
								window.mrlou.androidIs("7", "");
							} else if(isIos) {
							}
						})

				} else {
					$("#con_recom1").hide();
					$('#consultant_space_bottom1').hide();
				}
			})
					
			
			//经纪页面
			$("#mui-grid-view").on('click',function(){
				console.log(1)
				app.href('lxs_consultant_recommand.html')
			})
			//服务商页面
			$("#mui-grid-view1").on('click',function(){
				console.log(2)
				app.href('lxs_server_recommand.html')
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
            
            				
				var isv = localStorage.getItem('isV');				
				console.log('========='+isv+typeof(isv))
				if(isv=='0') {
					$("#callPhone").show();
					$('#phone1').html(data.call_phone);
					$('#phone1').attr('href', 'tel:' + data.call_phone);
				}else{
					$("#callPhone").hide();
				}
				
				
				$("#callPhone").on('click',function(){
					//进入易楼精英会
					var u = navigator.userAgent;
					var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
					var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
					if(isAndroid) {
						window.mrlou.androidIs("4", "");
					} else if(isIos) {
						//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
						broker("4","");
					}
									
				})
            
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
            // return '<span class="house_active"><i></i><span>认证</span></span>';
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
    window.detail = detail;
});

