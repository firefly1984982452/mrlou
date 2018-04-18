LXS(function(ajax) {
	util.tpl.format.isVerified = function() {
		var obj = arguments[arguments.length - 1];
		if(obj.verified == '1') {
			return '<span class="house_active"><i></i><span>认证</span></span>';
		}
		return '';
	}
	var classArr = ['', 'looking', 'want', 'dealed', 'disabled'];
	util.tpl.format.statusClass = function(status) {
		status = parseInt(status);
		return classArr[status];
	}
	var typeClassArr = ['', 'rent', 'sale'];
	util.tpl.format.typeClass = function(type) {
		type = parseInt(type);
		return typeClassArr[type];
	}

	util.tpl.format.priceAssert = function() {
		var obj = arguments[arguments.length - 1];
		if(obj.transaction_type == '1') {
			return '<p class="house_price">租金报价：<span>¥' + obj.rent_price + '</span></p>';
		} else {
			return '<p class="house_price">出售报价：<span>¥' + obj.sell_price + '</span></p>';
		}
	}

	/**
	    详情页
	*/
	var roomDetail = {
		id: 0,
		init: function() {
			!this.inited && this.bindAction();
			this.getRoomInfo();
		},
		bindAction: function() {
			var that = this;
			roomDetail.roomDetailScroller = new iScroll("room_detail_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			$('#collect_room').click(function() {
				if(that.id) {
					var dom = $(this);
					var collected = $(this).hasClass('collected');
					if(collected) return;
					ajax.collectRoom(that.id, function() {
						$('#collect_room').toggleClass('collected');
					})
				}
			})
			$('#room_share').click(function() {
				ajax.createShare(3, that.id, function(data) {
					app.share(data);
					// var content = encodeURIComponent(data.content);
					// location.href = 'http://obj@share@'+content+'@'+data.url+'@'+data.avatar+'@'+data.url+'@'+data.id;
				});
			})

			//纠错
			/*var href_con = "javascript:app.href('lxs_error_correction.html')";
			$(".error_correction").on("click", function() {
				var parent = $(this).parent().parent().parent().attr("id");
				if(parent == "detail") {
					//楼盘
					var name = $("#name").text();
					localStorage.setItem("lp_name", name);
					localStorage.setItem("err_type", 1);
					$(this).attr("href", href_con);
				}

				if(parent == "room_detail") {
					//房源
					var room_buliding_name = $("#room_buliding_name").text();
					var room_square_meter_span = $("#room_square_meter_span").text(); //面积
					localStorage.setItem("fy_name", room_buliding_name);
					localStorage.setItem("unit", $("#unit_no").text());
					localStorage.setItem("err_type", 2);
					$(this).attr("href", href_con);
				}
			})*/

			this.inited = true;
		},
		getRoomInfo: function() {
			var that = this;
			ajax.getRoomInfo(this.id, function(data) {
				that.fillRoomInfo(data.data);
				$('#room_imgs').unbind().click(function() {
				localStorage.setItem('imageType','room');
				localStorage.setItem('buildImageId',roomDetail.id);
					app.href('image_view.html','roomid=' + roomDetail.id,'hide');
				})
		
				$('.go_buidMap').unbind().click(function(){
                	app.goBuidMap(data.data);
                })
				
			})
		},
		fillRoomInfo: function(data) {
			if(data.is_collected == 1) {
				$('#collect_room').addClass('collected');
			} else {
				$('#collect_room').removeClass('collected');
			}
			$('#room_detail_atavar').attr('src', data.avatar);
			$('#collection_num').html(data.sta_collect_total);
			if(data.avatar) {
				var img = new Image();
				img.onload = function() {
					roomDetail.roomDetailScroller.refresh();
					img.onload = null;
				}
				img.src = data.avatar;
			}

			if(data.image_total != '0') {
				$('#room_imgs').show();
				$('#room_total_span').html(data.image_total);
			} else {
				$('#room_imgs').hide();
			}

			$('#room_buliding_name').html(data.building_name);

			$('#building_address').html(data.address);
			$('#room_building_type_span').html(data.type);

			if(data.build_date.length == 0) {
				$('#room_building_date_span').parent().hide();
			} else {
				$('#room_building_date_span').html(data.build_date);
				$('#room_building_date_span').parent().show();
			}

			if(data.metro.length == 0) {
				$('#room_building_metro_span').parent().hide();
			} else {
				$('#room_building_metro_span').html(data.metro);
				$('#room_building_metro_span').parent().show();
			}

			if(data.tag_list.length == 0) {
				$('#room_building_tags_span').parent().hide();
			} else {
				$('#room_building_tags_span').html(data.tag_list);
				$('#room_building_tags_span').parent().show();
			}
			
			$("#go_build").attr('bid',data.building_id);
			
			//进入楼盘
			$("#go_build").click(function(){
				var u = navigator.userAgent;
				var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
				var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
				var bid = $(this).attr('bid');
				if(isAndroid) {
					var msg = window.mrlou.androidIs("5",bid);
				} else if(isIos) {
					//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
					building(bid);
				}
			})

			var roomStatusStr = ['', '带看', '意向', '成交', '失败'];
			var statusClass = ['', 'lead_look', 'wanted', 'dealed', 'disabled'];
			$('#room_status').html(roomStatusStr[data.status]);

			$('#unit_no').html(data.unit_no);

			$('#room_detail .info_item').hide();
			var deliveryStr = {
				'1': '毛坯交付',
				'2': '标准交付',
				'3': '现状装修隔断',
				'4': '现状装修家具',
				'5': '拎包办公',
				'6': '协商交付条件'
			}
			if(localStorage.getItem('isV') == '0') {
				$("#fy_info").css({
					"width": 0,
					"height": 0,
					"opacity": 0,
					"display": "none"
				});
			} else {
				$("#fy_info").css({
					"width": "auto",
					"height": "auto",
					"opacity": 1,
					"display": "block"
				});
			}
			$('#room_detail .info_item').each(function() {
				var prop = $(this).attr('prop_val');
				if(prop == 'delivery_status') {
					if(data[prop]) {
						$(this).show();
						$('#room_' + prop + '_span').html(deliveryStr[data[prop]]);
					} else {
						$(this).hide();
						$('#room_' + prop + '_span').html('');
					}
				} else {
					if(data[prop]) {
						$(this).show();
						$('#room_' + prop + '_span').html(data[prop]);
					}
				}
			})
			var typeMap = {
				'1': '开发商',
				'2': '代理商',
				'3': '运营商',
				'102': '经纪人',
				'201': '小业主',
				'299': '其他',
				'104' : '企业服务商'
			}
			if(data.user.phone) {
				$('#user_phone_con').show();
				$('#user_phone').html(data.user.phone);
				$('#user_phone').attr('href', 'tel:' + data.user.phone);
			}

			$('#user_avatar').attr('src', data.user.avatar);
			$('#user_avatar').attr('uid', data.user.id);
			$('#user_company').html(data.user.company);
			$('#user_type').html(typeMap[data.user.type]);
            $('#user_name').html(data.user.cn_username + data.user.en_username);

			//加V和公司红底
            if (data.user.v==1) {
				$('#oval_pd').show();
                if (data.user.type=='102') {
					$("#work_age").html("从业"+data.user.work_age+"年");
                    
                    //跳转到公司品牌页
                    $('#user_company').click(function(){
	    				var u = navigator.userAgent;
						var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
						var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
						var company_name = $(this).text();
						if(isAndroid) {
							window.mrlou.androidIs("2",company_name);
						} else if(isIos) {
							//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
//							broker("2",company_name);
						}
	    			})
                    
                    //跳转到个人品牌页
                    $("#user_avatar").click(function(){
						var u = navigator.userAgent;
						var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
						var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
						var id = $(this).attr('uid');
						if(isAndroid) {
							window.mrlou.androidIs("1",id);
						} else if(isIos) {
							//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
//							broker("1",id);
						}    			
	    			})
                }else{
                	$("#work_age").html('');
                }
            }else if(data.user.v==0){
                	$("#work_age").html('');
                $('#oval_pd').hide();
                $('#user_type').html(typeMap[data.user.type]);
            }else{
                	$("#work_age").html('');
                alert("加V出错了");
            }
            
            if (data.user.company_status==1) {
                $('#user_type').removeAttr('style');
                // 红底
                if (data.user.v==1) {
                    $('#user_company').css({'padding':'4px','border-radius':'2px','background':'#bd081c','color':'#fff',"opacity":1,"display":"inline",'margin-left': '5px'});
                }else{
                    $('#user_company').css({'background':'#fff','color':'#9a9a9a','margin-left': '5px'});
                }
            }else if (data.user.company_status==0) {
                $('#user_company').removeAttr('style');
                // 不改变
                 $(".house_info").find('#user_company').css({'background':'#fff','color':'#9a9a9a','margin-left': '10px'});
            }else {
                alert("公司出错了");
            }


			roomDetail.roomDetailScroller.refresh();
		}
	}
	window.detailFallback = function(pageIn, pageOut, target) {
		roomDetail.roomDetailScroller && roomDetail.roomDetailScroller.scrollTo(0, 0, 1);
	}

	// confirm('页面接收到的参数'+params);
	// alert('解析后：'+JSON.stringify(search))
	var search;

	function pageInit() {
		search = util.parseURI(params);
		roomDetail.id = search.roomid;
		roomDetail.init();
	}
	if(params) {
		pageInit();
	} else {
		window.afterParams = pageInit;
	}
});