LXS(function(ajax) {
	var leftPart = $('#thelist #left_part');
	var brandLeftPart = $('#thelistbrand #left_part');
	var rightPart = $('#thelist #right_part');
	var brandRightPart = $('#thelistbrand #right_part');
	var cache = {
		region: {},
		plate: {},
		status: {},
		metro: {},
		type: {}
	}
	var regionList = [];
	var regionCache = {};
	var totalHeight = {
		left: 0,
		right: 0
	}
	var current;
	var top = true;
	var loadedBuildingId = [];
	var page = {
		pageIndex: 1,
		pageSize: 20,
		searching: false,
		selecting: '',
		regionList: [],
		plateList: [],
		init: function() {
			var that = this;
			leftPart.html('');
			rightPart.html('');
			brandLeftPart.html('');
			brandRightPart.html('');
			!that.inited && this.bindAction();
			this.searchBulids();
			this.getAllSelectOptions();
			this.newPage();
		},
		bindAction: function() {
			var that = this;
			page.roomDetailScroller = new iScroll("room_detail_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			page.selectScroller = new iScroll("select_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			page.detailMoreScroller = new iScroll("detail_more_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			page.searchScroller = new iScroll("search_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			page.detailScroller = new iScroll("detail_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			page.detailScroller1 = new iScroll("test_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			//test
			that.inited = true;
			var myScroll,
				pullDownEl, pullDownOffset,
				pullUpEl, pullUpOffset,
				ajaxing = false,
				_page = this;

			pullDownEl = document.getElementById('pullDown');
			pullDownOffset = pullDownEl.offsetHeight;
			pullUpEl = document.getElementById('pullUp');
			pullUpOffset = pullUpEl.offsetHeight;
			function pullDownAction() {
				that.pageIndex = 1;
				loadedBuildingId.length = 0;
				that.searchBulids();
				setTimeout(function() {
					ajaxing = false;
					myScroll.refresh();
					pullDownEl.style.visibility = 'hidden';
				}, 500);
			}

			function pullUpAction() {
				if(page.more == false) {
					return false;
				}
				that.pageIndex++;
				that.searchBulids();
				setTimeout(function() {
					ajaxing = false;
					myScroll.refresh();
					pullUpEl.style.visibility = 'hidden';
				}, 500);
			}
			setTimeout(function() {
					myScroll = that.myScroll = new iScroll('bulidingListArea', {
						mouseWheel: true,
						click: true,
						useTransition: false,
						hScrollbar: false,
						vScrollbar: false,
						hideScrollbar: true,
						topOffset: pullDownOffset,
						onRefresh: function() {
							if(pullDownEl.className.match('loading')) {
								pullDownEl.className = '';
								pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉更新数据...';
							} else if(pullUpEl.className.match('loading')) {
								pullUpEl.className = '';
								pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉获取更多...';
							}
						},
						onScrollMove: function() {
							if(ajaxing == true) {
								return false;
							}
							if(this.y > this.absStartY) {
								pullDownEl.style.visibility = 'visible';
								pullUpEl.style.visibility = 'hidden';
							} else {
								pullUpEl.style.visibility = 'visible';
								pullDownEl.style.visibility = 'hidden';
							}
							if(this.y > 5 && !pullDownEl.className.match('flip')) {
								pullDownEl.className = 'flip';
								pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开确认更新...';
								this.minScrollY = 0;
							} else if(this.y < 5 && pullDownEl.className.match('flip')) {
								pullDownEl.className = '';
								pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉更新数据...';
								this.minScrollY = -pullDownOffset;
							} else if(this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
								pullUpEl.className = 'flip';
								pullUpEl.querySelector('.pullUpLabel').innerHTML = '松开确认更新...';
								this.maxScrollY = this.maxScrollY;
							} else if(this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
								pullUpEl.className = '';
								pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉获取更多...';
								this.maxScrollY = pullUpOffset;
							}
							// $('body').focus();
							// that.firstPageLoaded && app.hideTabBar();
							// location.href = 'https://obj@hidetabbar';
						},
						onScrollEnd: function() {
							if(pullDownEl.className.match('flip')) {
								pullDownEl.className = 'loading';
								pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';
								ajaxing = true;
								pullDownAction();
							} else if(pullUpEl.className.match('flip')) {
								pullUpEl.className = 'loading';
								pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';
								ajaxing = true;
								pullUpAction();
							}
							// app.showTabBar();
							// location.href = 'https://obj@showtabbar';
						}
					})
				}, 300)
				// document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

			function refreshSearchScroller(t) {
				setTimeout(function() {
					page.searchScroller.refresh();
				}, 1000);
			}
			$('#index_search_btn').click(function(e) {
				$('#keyword').val('');
				//$('#search_go_back').attr('href', '#index');
				refreshSearchScroller();
				// e.preventDefault();
				// e.stopPropagation();
			})
			$('#bulidingdetail_search_btn').click(function() {
				$('#keyword').val('');
				//$('#search_go_back').attr('href', '#detail');
				refreshSearchScroller();
			})

			//首页上面的展开收起
			$(".topContro").click(function() {
				if(top) {
					$("#top_lb_index").hide(400);
					$("#topControImage").attr('src', 'lxs_images_down.png');
					top = false;
				} else {
					$("#top_lb_index").show(300);
					$("#topControImage").attr('src', 'lxs_images_up.png');
					top = true;
				}
			})

			$("#mrlou_title_img").on('click', function() {
				window.location.reload();
			});

			//品牌推广页
			$(".pd-lb").on('click', function() {
				localStorage.setItem('pp_id',$(this).attr('imgId'));
				var brandInfoUrl = 'https://appapi.imrlou.com/api/getbrandbyid';
				var param = {
					id: localStorage.getItem('pp_id')
				}
				$.getJSON(brandInfoUrl, param, function(data) {
					$("#mrlouBrandTitle").text(data.info.name);
					$("#brandIntro").text(data.info.note);

					var cb = page.createBulids;
					cb(data.list, true);
				});
			})
			var href_con = "javascript:app.href('lxs_error_correction.html')";
			$(".error_correction").on("click", function() {
				var parent = $(this).parent().parent().parent();
				if(parent.attr("id") == "detail") {
					//楼盘
					var name = $("#name").text();
					localStorage.setItem("lp_name", name);
					localStorage.setItem("err_type", 1);
					localStorage.setItem("err_id",parent.attr('bid'));
					$(this).attr("href", href_con);
				}

				if(parent.attr("id") == "room_detail") {
					//房源
					var room_buliding_name = $("#room_buliding_name").text();
					var room_square_meter_span = $("#room_square_meter_span").text(); //面积
					localStorage.setItem("fy_name", room_buliding_name);
					localStorage.setItem("unit", $("#unit_no").text());
					localStorage.setItem("err_type", 2);
					localStorage.setItem("err_id",parent.attr('rid'));
					$(this).attr("href", href_con);
				}
			})
			$('#roomdetail_search_btn').click(function() {
				$('#keyword').val('');
				$('#search_go_back').attr('href', '#room_detail');
				refreshSearchScroller();
			})
			$('#search_go_back').click(function() {
				if($('#search_go_back').attr('href') == '#index') {
					that.pageIndex = 1;
					loadedBuildingId.length = 0;
					that.searching = false;
					that.searchBulids();
					//在这里，是在搜索时点击返回按钮后的操作
					$("#top_lb_index").show();
					$("#topControImage").attr('src', 'lxs_images_up.png');
					top = true;
				}
			})
			$('.go_detail').click(function() {
				setTimeout(function() {
					page.detailMoreScroller.refresh();
				}, 500);
			})
			$('.select_condition').click(function(e) {
				if(this.id == 'go_select_region') {
					that.selecting = 'region';
					$('#select_title').html('区域选择');
					that.createRegion();
				} else if(this.id == 'go_select_plate') {
					if(!$('#go_select_region').attr('vals')) {
						alert('您还未选择区域');
						return;
					}
					that.selecting = 'plate';
					$('#select_title').html('板块选择');
					that.createPlate();
				} else {
					that.selecting = this.getAttribute('ste');
					$('#select_title').html(this.getAttribute('st') + '选择');
					that.createOtherSelect();
				}
			})
			$('#select .go_back,#select .submit').click(function() {
				// $('.page').hide();
				// $(this).parents('.page').prev().show();
				if(this.className == 'submit') {
					that.fillSelected();
				}
				that.selecting = '';
			})
			$('.reset').click(function() {
				that.resetSearchForm();
			})
			
			/*$(".condition_ipt").on('focus',function(){				
				$('.search_operate').hide();
			})
			
			$(".condition_ipt").on('blur',function(){				
				$('.search_operate').show();
			})*/
			
			$('#search_buliding').click(function() {
				that.pageIndex = 1;
				loadedBuildingId.length = 0;
				ajaxing = false;
				that.searching = true;
				leftPart.html('');
				rightPart.html('');
				brandLeftPart.html('');
				brandRightPart.html('');
				that.searchBulids();
				setTimeout(function() {
						page.myScroll.refresh();
					})
					//这里的操作是搜索后显示的，应把首页轮播隐藏
				$("#top_lb_index").hide();
				$("#topControImage").attr('src', 'lxs_images_down.png');
				top = false;
			})
			$('input[name=trade]').change(function() {
				var trade = $('input[name=trade]:checked').val();
				if(trade == '2') {
					$('#price_unit').html('元/m²');
					$('#prop4,#prop5').addClass('disabled');
					$('#prop1,#prop2,#prop3').removeClass('disabled');
					$('#prop4_ipt,#prop5_ipt').attr('checked', false);
					$("#price_unit_day").val(3);
					$("#price_unit_day").next().next().text('元/m²');
					$("#price_unit_moon").val(4);
					$("#price_unit_moon").next().next().text('万元');
				} else {
					$("#price_unit_day").val(1);
					$("#price_unit_day").next().next().text('元/m²/天');
					$("#price_unit_moon").val(2);
					$("#price_unit_moon").next().next().text('元/月');
					$('#prop4,#prop5').removeClass('disabled');
					var select1 = false,
						select2 = false;
					$('input[name=prop]').each(function() {
						if(this.checked) {
							if(this.value == 1 || this.value == 2 || this.value == 3) {
								select1 = true;
								select2 = false;
							} else {
								select1 = false;
								select2 = true;
							}
						}
					})
					if(select1) {
						$('#prop1,#prop2,#prop3').removeClass('disabled');
						$('#prop4,#prop5').addClass('disabled');
					}
					if(select2) {
						$('#prop1,#prop2,#prop3').addClass('disabled');
						$('#prop4,#prop5').removeClass('disabled');
					}
					if($('#prop_all').attr('checked')) {
						select1 = true;
						select2 = false;
					}
					if(select1) $('#price_unit').html('元/m²/天');
					if(select2) $('#price_unit').html('元/月');
				}
				if($('input[name=prop]:checked').size() == 0) {
					$('#prop_all').attr('checked', 'checked');
				}
			})
			$('#prop_all').change(function() {
				var trade = $('input[name=trade]:checked').val();
				var select = [];
				$('input[name=prop]').each(function() {
					this.checked && select.push(this.value);
				})
				if(this.checked) {
					if(select.length) {
						$('input[name=prop]').attr('checked', false);
						if(trade == 2) {
							$('#prop1,#prop2,#prop3').removeClass('disabled');
						} else {
							$('#price_unit').html('元/m²/天');
							$('#prop1,#prop2,#prop3,#prop4,#prop5').removeClass('disabled');
						}
					}
				} else {
					if(!select.length) {
						this.checked = true;
						return false;
					}
				}
			})
			$('input[name=prop]').change(function() {
				if($(this).parent().hasClass('disabled')) {
					this.checked = false;
					return false;
				}
				var trade = $('input[name=trade]:checked').val();
				if(trade == '2') {
					if(this.checked) {
						if(this.value == 4 || this.value == 5) {
							this.checked = false;
							return false;
						}
					} else {
						var select = [];
						$('input[name=prop]').each(function() {
							this.checked && select.push(this.value);
						})
						if(!select.length) {
							$('#prop_all').attr('checked', 'checked');
						}
					}
				} else {
					var select1 = false,
						select2 = false;
					$('input[name=prop]').each(function() {
						if(this.checked) {
							if(this.value == 1 || this.value == 2 || this.value == 3) {
								select1 = true;
								select2 = false;
							} else {
								select1 = false;
								select2 = true;
							}
						}
					})
					if(this.checked) {
						if(this.value == 1 || this.value == 2 || this.value == 3) {
							$('#prop4,#prop5').addClass('disabled');
							$('#prop4_ipt,#prop5_ipt').attr('checked', false);
							$('#price_unit').html('元/m²/天');
						} else {
							$('#price_unit').html('元/月');
							$('#prop1,#prop2,#prop3').addClass('disabled');
							$('#prop1_ipt,#prop2_ipt,#prop3_ipt').attr('checked', false);
						}
					} else {
						var select = [];
						$('input[name=prop]').each(function() {
							this.checked && select.push(this.value);
						})
						if(!select.length) {
							$('#prop_all').attr('checked', 'checked');
							$('#prop1,#prop2,#prop3,#prop4,#prop5').removeClass('disabled');
						}
					}
				}
				if(this.checked) {
					$('#prop_all').attr('checked', false);
				}
			})

			$('#keyword').blur(function() {
				$('#search .content').show();
				$('.search_operate').show();
			})
			$('#keyword').keydown(function(e) {
				if(!hasResult && e.keyCode == 13) {
					goSearchBuliding();
				}
			})
			var filterInited = false;
			var hasResult = true;
			$('#keyword').focus(function() {
				//新增楼盘按钮不可点
				$('.search_operate').hide();
				$('#search .content').hide();
				hasResult = true;
				if(filterInited) return;
				var filter = that.filterInited = new Filter(document.getElementById('keyword'), {
					top: $('.search_warper').offset().top + 8,
					left: $('.search_warper').offset().left - 1,
					// minWidth : $('#formName').width() + ($.browser.mozilla || $.browser.msie ? 5 : 4) ,
					width: $('.search_warper')[0].clientWidth - 1,
					className: 'search_buliding_option',
					height: 300,
					ajax: true,
					ajaxFn: function(fn) {
						var keyword = $('#keyword').val();
						if(keyword == '') {
							filter.destroy();
							return;
						}
						ajax.searchBulidingByKeyword(keyword, function(data) {
							if($('#search').is(":hidden")) {
								filter.destroy();
								return;
							}
							fn(data.list);
						})
					},
					showFilted: function(result, con, fo) {
						var len = result.length;
						for(var i = 0; i < len; i++) {
							var a = document.createElement('a');
							var name = document.createElement('p');
							name.innerHTML = result[i].name;
							name.className = '_name';
							var address = document.createElement('p');
							address.innerHTML = result[i].address;
							address.className = '_address'
							a.appendChild(name);
							a.appendChild(address);
							a.className = 'search_buliding_option';
							con.appendChild(a);

							//首页的隐藏与显示

							$("#top_lb_index").hide(400);
							$("#topControImage").attr('src', 'lxs_images_down.png');
							top = false;
						}
					},
					selectFn: function(obj, ipt, fo) {
						ipt.value = obj.name;
						goSearchBuliding();
						filter.destroy();
						return false;
					},
					noResult: function() {
						if($('#search').is(":hidden")) {
							filter.destroy();
							return;
						}
						hasResult = false;
						filter.dom.con.innerHTML = '<p style="padding:10px 0;text-align:center;font-size:14px;color:#444;">系统无此楼盘，请新增该楼盘</p>';
					}
				})
			})

			function goSearchBuliding() {
				Mobilebone.transition($('#index')[0], $('#search')[0], true);
				filterInited && filterInited.destroy();
				document.getElementById('keyword').blur();
				that.pageIndex = 1;
				loadedBuildingId.length = 0;
				that.searching = true;
				ajaxing = false;
				leftPart.html('');
				rightPart.html('');
				brandLeftPart.html('');
				brandRightPart.html('');
				that.searchBulids();
				setTimeout(function() {
					page.myScroll.refresh();
				})
			}
			$('#acreage,#budget').keydown(function(e) {
				var c = e.keyCode;
				var val = this.value;
				if(c == 8) return;
				if(!(c == 190 || c >= 48 && c <= 57)) {
					e.preventDefault();
					return;
				} else {
					var arr = val.split('.');
					if(arr[1] !== undefined && arr[1].length >= 2) {
						e.preventDefault();
					}
				}
				if(c == 190 && val.indexOf('.') != -1) {
					e.preventDefault();
				}
			})
			$('#select_all').change(function() {
				var checkboxs = $('#options input');
				var selected = [];
				checkboxs.each(function() {
					if(this.checked) {
						selected.push(this.value);
					}
				})
				if(selected.length) {
					checkboxs.each(function() {
						this.checked = false;
					})
				} else {
					this.checked = true;
					return false;
				}
			})
		},
		queryBulidList: function() {
			var that = this;
			ajax.getPageBulids({
				page: that.pageIndex,
				per_page: that.pageSize
			}, function(data) {
				if(that.pageIndex == 1) {
					leftPart.html('');
					rightPart.html('');
					brandLeftPart.html('');
					brandRightPart.html('');
				}
				that.createBulids(data.list);
			})
		},
		resetSearchForm: function() {
			$('input[name=trade]').each(function() {
				if(this.value == 2) {
					this.checked = false;
				} else {
					this.checked = true;
				}
			})
			$('input[name=prop]').each(function() {
				this.checked = false;
			})
			$('#acreage').val('');
			$('#budget').val('');
			$('.select_condition p').html('全部');
			$('.select_condition').attr('vals', '');
			$('#keyword').val('');
		},
		getSearchFormData: function() {

			var params = {};
			params.trs_type = $('input[name=trade]:checked').val();
			var props = [];
			if(!$('#prop_all').attr('checked')) {
				$('input[name=prop]:checked').each(function() {
					this.checked && props.push(this.value);
				})
				params.category = props.join(',')
			}
			params.meter = $("#acreage").val() + ',' + $("#acreage_").val();
			params.price = $("#budget").val() + ',' + $("#budget_").val();
			params.unit = $('input[name=budget]:checked').val();
			params.regions = $('#go_select_region').attr('vals');
			params.plate = $('#go_select_plate').attr('vals');
			params.metro = $('#go_select_metro').attr('vals');
			params.type = $('#go_select_type').attr('vals');
			params.status = $('#go_select_status').attr('vals');
			params.key = $('#keyword').val();
			for(var p in params) {
				if(params[p] == '' || !params[p]) {
					delete params[p];
				}
			}
			var isBlankObj = true;
			for(var p in params) {
				isBlankObj = false;
			}
			if(isBlankObj) return false;
			return params;
		},
		searchBulids: function() {
			var that = this;
			var params = this.getSearchFormData();
			params.page = that.pageIndex;
			params.per_page = that.pageSize;
			params.session_id = util.getLocal('sessionId');
			if(!that.searching) {
				that.queryBulidList();
			} else {
				ajax.bulidSearch(params, function(data) {
					if(that.pageIndex == 1) {
						leftPart.html('');
						rightPart.html('');
						brandLeftPart.html('');
						brandRightPart.html('');
					}
					that.createBulids(data.list);
				},function(){
					alert("error"+JSON.stringify(arguments[0]));
				})
			}
		},
		createBulids: function(list, flag) {//在哪里diao yong de 
			if(flag) { //防止重复加载，需要加载很多，删除。
				brandLeftPart.html('');
				brandRightPart.html('');
			}

			function isIos() {
				u = navigator.userAgent;
				if(u.indexOf('Android') > -1 || u.indexOf('Adr') > -1) {
					return false;
				} else if(!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
					return true;
				} else {
					return null;
				}
			}
			
			var userInfo = {
				isV : null,
				fyCount : null,
				fyInfo : null
			};
			
			setTimeout(function(){
				if (userInfo.isV==null) {
					getUserToLocal();
				}
			},1000)
			
			
			function getUserToLocal(){
				var infoUrl = 'https://appapi.imrlou.com/api/users';
				var infoParam = {
					session_id : util.getLocal('sessionId')
				}
				$.getJSON(infoUrl, infoParam, function(data) {
					console.log('==--=='+data.user.v)
					localStorage.setItem('isV',data.user.v);
					localStorage.setItem('fyInfo',data.user.info);
					userInfo.isV = data.user.v;
					userInfo.fyCount = data.user.count;
					userInfo.fyInfo = data.user.info;
				});
			}
			
			//发布房源之前的操作
			$("#goFangyuan").on('click', function() {
				var count = userInfo.fyCount;
				var info = userInfo.fyInfo;
				if(count == 0 || count == '0') {
					alert(info)
					return false;
				} else {
					if(isIos()) {
						app.href('fangyuan_select_house_type.html');
					} else {
						location.href = 'fangyuan_select_house_type.html';
					}
				}
			});
			
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

			var that = this;
			if(that.pageIndex == 1 || flag) {
				totalHeight.left = 0;
				totalHeight.right = 0;
			}
			var target, temp;
			for(var i = 0; i < list.length; i++) {
				//thelistbrand
				loadedBuildingId.push(list[i].id);
				if(flag) {
					target = totalHeight.left <= totalHeight.right ? brandLeftPart : brandRightPart;
				} else {
					target = totalHeight.left <= totalHeight.right ? leftPart : rightPart;
				}
				temp = $(util.tpl.apply('bulid_item', list[i]));
				target.append(temp);
				if(totalHeight.left <= totalHeight.right) {
					totalHeight.left += temp.height();
				} else {
					totalHeight.right += temp.height();
				}
				$(temp).find('#image_more').click(function() {
					var hash = this.getAttribute("href");
					if(hash=="#detail"){
						setTimeout(function(){
							page.detailScroller.refresh();
						})
					}
					var isBrand = $(this).parent().parent().parent().parent().attr('id');
					if (isBrand == "thelistbrand") {
						detail.id = $(this).attr('bid');
						detail.pp_id = localStorage.getItem('pp_id');
						detail.initBrand();
						page.detailScroller.refresh();
					}else{
						detail.id = $(this).attr('bid');
						detail.init();
						page.detailScroller.refresh();
					}

					if($(this).parent().attr("class") == "item_img") {
						$(".mui-grid-view").children().remove();
						$("#mui-grid-view1").children().remove();
						var imgs = [];
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
								var li_start = '<li class="mui-table-view-cell mui-media mui-col-xs-2"><a id="consul_recom" ><img class="mui-media-object" src="';
								var li_end = '" /></a></li>';
								for(var i = 0; i < img_len.length; i++) {
									imgs[i] = img_len[i].avatar;
									$("#mui-grid-view").append(li_start + imgs[i] + li_end);
								}

								//进入易楼精英会
								/* if (util.getLocal('userType')=='102') {
								 	$('#go_jinyin').show();*/
								$('#go_jinyin').click(function() {
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
									/*}else{
										$('#go_jinyin').hide();
									}*/

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
								var comName = [];
								recom = 'recomServer';
								localStorage.setItem('recom', recom);
								for(var i = 0; i < img_len.length; i++) {
									imgs[i] = img_len[i].avatar;
									comName[i] = img_len[i].user_company;
									if (comName[i].length>=5) {
										$("#mui-grid-view1").append(li_start + imgs[i] + li_end_noLength + comName[i] + li_end_);										
									} else{
										$("#mui-grid-view1").append(li_start + imgs[i] + li_end_leng + comName[i] + li_end_);										
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
					}
				})

			}
			page.detailScroller1.refresh();
			that.myScroll && that.myScroll.refresh();
			that.firstPageLoaded = true;
		},

		newPage: function() {
			var brandUrl = 'https://appapi.imrlou.com/api/brandlist';
			$.getJSON(brandUrl, '', function(data) {
				for(var i = 0; i < data.list.length; i++) {
					$(".pd-lb:eq(" + i + ") img").attr('src', data.list[i].avatar);
					$(".pd-lb:eq(" + i + ")").attr('imgId', data.list[i].id);
				}
			});
			$(".index-box li").on("click", function() {
				var index = $(this).index();
				var ms = -1;
				var params = {
						"session_id": util.getLocal('sessionId'),
						"trs_type":"1",
						"meter":"0,0",
						"price":",",
						"unit":"1",
						"page":1,
						"per_page":20
					}
					/*console.log('eval: '+JSON.stringify(params))*/
				switch(index) {
					case 0:
						{
							params.trs_type = "1";
							$("#top_lb_index").hide(400);
							$("#topControImage").attr('src', 'lxs_images_down.png');
							top = false;
						};
						break;
					case 1:
						{
							params.trs_type = "2";
							$("#top_lb_index").hide(400);
							$("#topControImage").attr('src', 'lxs_images_down.png');
							top = false;
						};
						break;
					case 2:
						{
							ms = 1;
						};
						break;
					case 3:
						{
							params.type = "6";
							$("#top_lb_index").hide(400);
							$("#topControImage").attr('src', 'lxs_images_down.png');
							top = false;
						};
						break;
					case 4:
						{
							params.type = "8";
							$("#top_lb_index").hide(400);
							$("#topControImage").attr('src', 'lxs_images_down.png');
							top = false;
						};
						break;
					case 5:
						{
							ms = 2;
						};
						break;

					case 6:
						{
							ms = 3;
						};
						break;
					case 7:
						{
							ms = 4;
						};
						break;
					case 8:
						{
							ms = 5;
						};
						break;
					case 9:
						{
							ms = 6;
						};
						break;
					default:
						break;
				}

				if(ms > 0) {
					msgServers(ms);
				} else {
					search(params);
				}
			})

			function search(params) {
				var searchUrl = "https://appapi.imrlou.com/api/buildingsearch";
				$.post(searchUrl,params,function(responseText){//这里不是getJSON 或者$.ajax({dataType:"json"}
					$('#thelist #left_part').html("");
					$('#thelistbrand #left_part').html("");
					$('#thelist #right_part').html("");
					$('#thelistbrand #right_part').html("");
					setTimeout(function() {
						var data = null;
						try{
							data = JSON.parse(responseText);//将文本转换成json对象
						}catch(e){
							//报网络错误
							
							return;
						}
						//随便说戏下
							var unicode = escape("你好");//中文转成unicode编码，一般用url（get）传参。中文乱码问题
							console.log(unescape(unicode)==='你好');//true 中文转成unicode解码
						page.createBulids(data.list);
					}, 0);
				});
				
			}
			var u = navigator.userAgent;
			var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
			var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
			function msgServers(num) {
				if(isAndroid) {
					window.mrlou.msgServer(num);
				} else if(isIos) {
					msgServer(num);
				}
			}

			$("#index_other_btn").on('click', function() {
				if(isAndroid) {
					window.mrlou.goOther();
				} else if(isIos) {
					goOther();
				}

			});
			//1是经纪服务，2是认证房源，3是整层独栋，4是楼先生独家，5是联合办公室，6是增值服务
		},

		getAllSelectOptions: function() {
			ajax.getAllSelectOption(function(data) {
				cache.metro = data.metro;
				cache.status = data.status;
				cache.type = data.type;
				for(var i = 0; i < data.region_plate.length; i++) {
					var regId = data.region_plate[i].id;
					var regName = data.region_plate[i].name;
					regionList.push({
						id: regId,
						name: regName
					})
					var child = data.region_plate[i].child || [];
					cache.region[regId] = regName;
					regionCache[regId] = data.region_plate[i];
					for(var j = 0; j < child.length; j++) {
						cache.plate[child[j].id] = child[j].name;
					}
				}
			})
		},
		getRegionList: function() {
			var that = this;
			ajax.getRegionList(function(data) {
				that.regionList = [];
				regionCache = data.list;
				for(var p in data.list) {
					that.regionList.push({
						id: p,
						name: data.list[p]
					})
				}
			})
		},
		getPlateList: function() {
			var that = this;
			ajax.getPlateList(function(data) {
				that.plateList = data.list;
				for(var i = 0; i < data.list.length; i++) {
					for(var j = 0; j < data.list[i].child.length; j++) {
						plateCache[data.list[i].child[j].id] = data.list[i].child[j].name;
					}
				}
			})
		},
		createPlate: function() {
			var list = [];
			var selected = $('#go_select_region').attr('vals') || '';
			selected = selected.split(',');
			var temp = {};
			for(var i = 0; i < selected.length; i++) {
				temp[selected[i]] = 1;
			}
			var plateList = [];
			for(var i in temp) {
				if(temp.hasOwnProperty(i)) {
					if(regionCache[i]) {
						list.push(regionCache[i]);
					}
				}
			}
			// for(var i=0;i<plateList.length;i++){
			// var id = plateList[i].id;
			// if(temp[id]) list.push(plateList[i]);
			// }
			var html = '';
			for(var i = 0; i < list.length; i++) {
				html += util.tpl.apply('select_plate', list[i]);
			}
			$('#options').html(html);
			var plates = $('#go_select_plate').attr('vals') || '';
			plates = plates.split(',');
			var _plate = {}
			for(var i = 0; i < plates.length; i++) {
				_plate[plates[i]] = 1;
			}
			$('input[name=plate]').each(function() {
				if(_plate[this.value]) {
					this.checked = true;
				}
			})
			setTimeout(function() {
				page.selectScroller.scrollTo(0, 0, 10);
				page.selectScroller.refresh();
			}, 10);
		},
		createRegion: function() {
			var html = '<div class="areas">';
			var data = cache.region;
			var list = regionList;
			var selected = $('#go_select_region').attr('vals') || '';
			if(selected) {
				selected = selected.split(',');
			} else {
				selected = [];
			}
			var temp = {};
			for(var i = 0; i < selected.length; i++) {
				temp[selected[i]] = 1;
			}
			for(var i = 0; i < list.length; i++) {
				list[i].keyName = 'region';
				html += util.tpl.apply('select_option', list[i]);
			}
			html += '</div>';
			$('#options').html(html);
			if(selected.length) {
				$('input[name=region]').each(function() {
					if(temp[this.value]) {
						this.checked = true;
					}
				})
				$('#select_all').attr('checked', false);
			} else {
				$('#select_all').attr('checked', 'checked');
			}
			var checkboxs = $('input[name=region]');
			checkboxs.change(function() {
				// if($('input[name=region]:checked').size() >= 4){
				// alert('只能选择3个区域');
				// this.checked = false;
				// return false;
				// }
				if(this.checked) {
					$('#select_all').attr('checked', false);
				} else {
					var selected = [];
					checkboxs.each(function() {
						selected.push(this.value);
					})
					if(selected.length) {
						$('#select_all').attr('checked', false);
					} else {
						$('#select_all').attr('checked', 'checked');
					}
				}
			})
			setTimeout(function() {
				page.selectScroller.scrollTo(0, 0, 10);
				page.selectScroller.refresh();
			}, 10);
		},
		createOtherSelect: function() {
			var html = '<div class="areas">';
			var data = cache[this.selecting];
			var list = [];
			for(var i in data) {
				if(data.hasOwnProperty(i)) {
					list.push({
						id: i,
						name: data[i]
					})
				}
			}
			var selected = $('#go_select_' + this.selecting).attr('vals') || '';
			if(selected) {
				selected = selected.split(',');
			} else {
				selected = [];
			}
			var temp = {};
			for(var i = 0; i < selected.length; i++) {
				temp[selected[i]] = 1;
			}
			for(var i = 0; i < list.length; i++) {
				list[i].keyName = this.selecting;
				html += util.tpl.apply('select_option', list[i]);
			}
			html += '</div>';
			$('#options').html(html);
			var checkboxs = $('input[name=' + this.selecting + ']');
			if(selected.length) {
				checkboxs.each(function() {
					if(temp[this.value]) {
						this.checked = true;
					}
				})
				$('#select_all').attr('checked', false);
			} else {
				$('#select_all').attr('checked', 'checked');
			}
			checkboxs.change(function() {
				if(this.checked) {
					$('#select_all').attr('checked', false);
				} else {
					var selected = [];
					checkboxs.each(function() {
						selected.push(this.value);
					})
					if(selected.length) {
						$('#select_all').attr('checked', false);
					} else {
						$('#select_all').attr('checked', 'checked');
					}
				}
			})
			setTimeout(function() {
				page.selectScroller.scrollTo(0, 0, 10);
				page.selectScroller.refresh();
			}, 10);
		},
		fillSelected: function() {
			var that = this;
			var tar = $('#go_select_' + that.selecting);
			var _cache = cache[that.selecting];
			var name = [],
				ids = [];
			$('input[name=' + that.selecting + ']').each(function() {
				if(this.checked) {
					name.push(_cache[this.value]);
					ids.push(this.value);
				}
			})
			if(name.length) {
				tar.find('p').html(name.join('，'));
				tar.attr('vals', ids.join(','));
			} else {
				tar.find('p').html('全部');
				tar.attr('vals', '');
			}
		}
	}

	util.tpl.format.rentPriceFunction = function() {
		var obj = arguments[arguments.length - 1];
		if(obj.rent_price) {
			return '<p class="money">租金' + obj.rent_price + '</p>'
		}
		return '';
	}
	util.tpl.format.sellPriceFunction = function() {
		var obj = arguments[arguments.length - 1];
		if(obj.sell_price) {
			return '<p class="money">售价' + obj.sell_price + '</p>'
		}
		return '';
	}
	util.tpl.format.childSelectCreate = function() {
		var obj = arguments[arguments.length - 1];
		var child = obj.child;
		var html = '';
		for(var i = 0; i < child.length; i++) {
			child[i].keyName = page.selecting;
			html += util.tpl.apply('select_option', child[i]);
		}
		return html;
	}

	var maxWidth = (document.documentElement.clientWidth - 30) / 2;

	util.tpl.format.styleFormat = function() {
		var obj = arguments[arguments.length - 1];
		var width = maxWidth + 'px';
		var height = (obj.avatar_height / obj.avatar_width) * maxWidth + 'px';
		return 'background:' + obj.avatar_bgcolor + ';width:' + width + ';height:' + height;
	}

	window.checkSelectedRegion = function() {
		return $('#go_select_region').attr('vals') ? false : true;
	}

	/**
	    详情页
	*/
	var detail = {
		id: 0,
		is_brand : 2,
		pp_id : 0,
		init: function() {
			console.log('init')
			$("#detail").attr('bid',detail.id);
			current = detail.id;
			!this.inited && this.bindAction();
			this.clear();
			this.getBulidingInfo();
			this.getBulidingMoreInfo();

		},
		initBrand : function() {
			console.log('init2')
			$("#detail").attr('bid',detail.id);
			current = detail.id;
			!this.inited && this.bindAction();
			this.clear();
			this.getBulidingBrandInfo();
			this.getBulidingMoreInfo();		
		},
		bindAction: function() {
			var that = this;
			$('#collect_buliding').click(function() {
				if(that.id) {
					var dom = $(this);
					var collected = $(this).hasClass('collected');
					if(collected) return;
					ajax.collectBuliding(that.id, function() {
						$('#collect_buliding').toggleClass('collected');
					})
				}
			})
			$('#buliding_share').click(function() {
				ajax.createShare(2, that.id, function(data) {
					app.share(data);
					// var content = encodeURIComponent(data.content);
					// location.href = 'https://obj@share@'+content+'@'+data.url+'@'+data.avatar+'@'+data.url+'@'+data.id;
				})
			})
			this.inited = true;
		},
		getBulidingInfo: function() {
			var that = this;
			console.log('b')
			ajax.getBulidingDetail(this.id, function(data) {
				console.log("b:"+JSON.stringify(data))
				$('#go_map').unbind().click(function() {
					app.goMap(data.data);
				})
				if(data.data.display_detialinfo == '1') {
					$('.go_detail').show();
				}
				that.fillBulidingInfo(data.data);
				that.createRooms(data.data.rooms);
			})
		},
		getBulidingBrandInfo: function(){
			var that = this;
			var brand_param = {
				id : this.id,
				pp_id : this.pp_id,
				is_brand : this.is_brand
			}
			console.log('d'+JSON.stringify(brand_param))
			ajax.getBulidingBrandDetail(brand_param, function(data) {
				console.log("d:"+JSON.stringify(data))
				$('#go_map').unbind().click(function() {
					app.goMap(data.data);
				})
				if(data.data.display_detialinfo == '1') {
					$('.go_detail').show();
				}
				that.fillBulidingInfo(data.data);
				that.createRooms(data.data.rooms);
			})
		},
		getBulidingMoreInfo: function() {
			var that = this;
			ajax.getBulidingMoreInfo(this.id, function(data) {
				var isBlank = true;
				for(var p in data.data) {
					isBlank = false;
					break;
				}
				if(!isBlank) {
					// $('.go_detail').show();
					that.fillMoreInfo(data.data);
				} else {
					$('.go_detail').hide();
				}
			})
		},
		createRooms: function(list) {
			var html = '';
			for(var i = 0; i < list.length; i++) {
				html += util.tpl.apply('room', list[i]);
			}
			$('#rooms').html(html);
			$('.room_detail').click(function() {
				roomDetail.id = $(this).attr('rid');
				roomDetail.init();
			})
			page.detailScroller.refresh();
		},
		clear: function() {
			$('#collect_buliding').removeClass('collected');
			$('#avatar').attr('src', '');
			$('#buliding_img').hide();
			$('#name,#buliding_title2').html('');
			$('#address').html('');
			$('#detail .info_item').hide();
			$('#phone1,#phone2').html('');
			$('#phone1,#phone2').attr('href', '');
			$('#phone1,#phone2').attr('data-verify', '');
		},
		fillBulidingInfo: function(data) {
			$('#buliding_img').unbind().click(function() {
				localStorage.setItem('imageType','build');
				localStorage.setItem('buildImageId',detail.id);
            	setTimeout(function(){
                	app.href('image_view.html','buildid=' + detail.id,'hide');            		
            	},100);
            })
			if(data.is_collected == 1) {
				$('#collect_buliding').addClass('collected');
			} else {
				$('#collect_buliding').removeClass('collected');
			}
			$('#avatar').attr('src', data.avatar);
			if(data.avatar) {
				var img = new Image();
				img.onload = function() {
					page.detailScroller.refresh();
					img.onload = null;
				}
				img.src = data.avatar;
			}
			if(data.image_total != '0') {
				$('#buliding_img').show();
				$('#img_total_span').html(data.image_total);
			} else {
				$('#buliding_img').hide();
			}
			$('#name,#buliding_title2').html(data.name);
			$('#address').html(data.address);
			$('#detail .info_item').hide();
			var showHtml = null; //当前的list-item
			$('#detail .info_item').each(function() {
				var prop = this.id;
				if(prop == 'tags') {
					if(data.tags.length) {
						$(this).show();
						var html = '';
						for(var i = 0; i < data.tags.length; i++) {
							html += '<i>' + data.tags[i] + '</i>'
						}
						$('#tags_span').html(html);
					}
				} else {
					if(data[prop]) {
						$(this).show();
						$('#' + prop + '_span').html(data[prop]);
					}
				}
			})

			
			if(data.phones.length) {
				if(data.phones[0]) {
					$('#phone1').html(data.phones[0]);
					$('#phone1').attr('href', 'tel:' + data.phones[0]);
					$('#phone1').attr('data-verify', '2-' + data.id);
					$('#phone1').unbind().click(function() {
						var tel = $(this).html();
						var verify = $(this).attr('data-verify');
						ajax.telLog(tel, verify, function() {});
					})
				}
				if(data.phones[1]) {
					$('#phone2').html(data.phones[1]);
					$('#phone2').attr('href', 'tel:' + data.phones[1]);
					$('#phone2').attr('data-verify', '2-' + data.id);
					$('#phone2').unbind().click(function() {
						var tel = $(this).html();
						var verify = $(this).attr('data-verify');
						ajax.telLog(tel, verify, function() {});
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
			
			//判断是否独家
			if(data.unique == 1) {
				//独家
				$('#dujia').css('display', 'block');
			} else {
				$('#dujia').css('display', 'none');
			}
		},
		fillMoreInfo: function(data) {
			var html = '';
			for(var p in data) {
				html += util.tpl.apply('more_info', {
					key: p,
					value: data[p]
				})
			}
			$('#more_info').html(html);
		}
	}

	util.tpl.format.isVerified = function() {
		var obj = arguments[arguments.length - 1];
		if(obj.verified == '1') {
			// return '<span class="house_active"><i></i><span>认证</span></span>';
			/*if($(window）.height()>100){
			    
			}*/
			return '<span class="house_active"><i></i></span>';
		}
		return '';
	}
	var classArr = ['', 'looking', 'want', 'dealed', 'disabled'];
	util.tpl.format.statusClass = function(status) {
		status = parseInt(status);
		return classArr[status];
	}

	/*var userTypeArr = ['1','2','3','102','103','201','202','203'];
	util.tpl.format.userTypeClass = function(user_type){
	    user_type = parseInt(user_type);
	    return userTypeArr[user_type];
	}*/

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
			$("#room_detail").attr('rid',roomDetail.id);
			!this.inited && this.bindAction();
			this.getRoomInfo();
		},
		bindAction: function() {
			var that = this;
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
					// location.href = 'https://obj@share@'+content+'@'+data.url+'@'+data.avatar+'@'+data.url+'@'+data.id;
				});
			})

			this.inited = true;
		},
		getRoomInfo: function() {
			var that = this;
			$('#user_avatar').attr('src', '');
			ajax.getRoomInfo(this.id, function(data) {
				that.fillRoomInfo(data.data);
				$('#room_imgs').unbind().click(function() {
					localStorage.setItem('imageType','room');
					localStorage.setItem('buildImageId',roomDetail.id);
					app.href('image_view.html','roomid=' + roomDetail.id,'hide');
				})
				$('#go_map').unbind().click(function() {
					app.goMap(data.data);
					// var title = encodeURIComponent(data.data.name);
					// var address = encodeURIComponent(data.data.address);
					// location.href = 'https://obj@navmap@' + title + '@' + data.data.lat + '@' + data.data.lon + '@' + address + '@' + data.data.id;
				})

				$('.go_buidMap').unbind().click(function() {
					app.goBuidMap(data.data);
				})
			})
		},
		fillRoomInfo: function(data) {
			localStorage.setItem("room_id", data.id);
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
					page.roomDetailScroller.refresh();
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

			//显示新加的地址、类型、竣工日期、地铁、特色
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

			//点击能进入楼盘页
			$('#goLoupan').on('click', function() {
				var bid = data.building_id;
				var u = navigator.userAgent;
				var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
				var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
				if(isAndroid) {
					window.mrlou.goLoupan(bid);
				} else if(isIos) {
					goLoupan(num);
				}
			});
			var roomStatusStr = ['', '带看', '意向', '成交', '失效'];
			var statusClass = ['', 'lead_look', 'wanted', 'dealed', 'disabled'];
			$('#room_status').html(roomStatusStr[data.status]);

			$('#unit_no').html(data.unit_no);
			$('#fee_rate').html(data.fee_rate);

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
				// alert($("#fy_info").parent().html());
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
				'103': '物业公司',
				'102': '经纪人',
				'201': '小业主',
				'299': '其他',
				'104' : '企业服务商'
			}
			if(data.user.phone) {
				$('#user_phone_con').show();
				$('#user_phone').html(data.user.phone);
				$('#user_phone').attr('href', 'tel:' + data.user.phone);
				$('#user_phone').attr('data-verify', '3-' + data.id + '_1-' + data.user.id);
				$('#user_phone').unbind().click(function() {
					var tel = $(this).html();
					var verify = $(this).attr('data-verify');
					ajax.telLog(tel, verify, function() {});
				})
			}
			$('#user_avatar').attr('src', data.user.avatar);
			$('#user_avatar').attr('uid', data.user.id);
			$('#user_company').html(data.user.company);
			$('#user_type').html(typeMap[data.user.type]);
			$('#user_name').html(data.user.cn_username + data.user.en_username);

			//加V和公司红底
			if(data.user.v == 1) {
				$('#oval_pd').show();
				if(data.user.type == '102') {
					$("#work_age").html("从业" + data.user.work_age + "年");

					//跳转到公司品牌页
					$('#user_company').click(function() {
						var u = navigator.userAgent;
						var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
						var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
						var company_name = $(this).text();
						if(isAndroid) {
							window.mrlou.androidIs("2", company_name);
						} else if(isIos) {
							//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
							//							broker("2",company_name);
						}
					})

					//跳转到个人品牌页
					$("#user_avatar").click(function() {
						var u = navigator.userAgent;
						var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
						var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
						var id = $(this).attr('uid');
						if(isAndroid) {
							window.mrlou.androidIs("1", id);
						} else if(isIos) {
							//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
							//							broker("1",id);
						}
					})
				} else {
					$("#work_age").html('');
				}
			} else if(data.user.v == 0) {
				$("#work_age").html('');
				$('#oval_pd').hide();
				$('#user_type').html(typeMap[data.user.type]);
			} else {
				$("#work_age").html('');
				alert("加V出错了");
			}

			if(data.user.company_status == 1) {
				$('#user_type').removeAttr('style');
				// 红底
				if(data.user.v == 1) {
					$('#user_company').css({
						'padding': '4px',
						'border-radius': '2px',
						'background': '#bd081c',
						'color': '#fff',
						"opacity": 1,
						"display": "inline",
						'margin-left': '5px'
					});
				} else {
					$('#user_company').css({
						'background': '#fff',
						'color': '#9a9a9a',
						'margin-left': '5px'
					});
				}
			} else if(data.user.company_status == 0) {
				$('#user_company').removeAttr('style');
				// 不改变
				$(".house_info").find('#user_company').css({
					'background': '#fff',
					'color': '#9a9a9a',
					'margin-left': '10px'
				});
			} else {
				alert("公司出错了");
			}

			page.roomDetailScroller.refresh();
		}
	}
	window.detailFallback = function(pageIn, pageOut, target) {
		if(pageIn.id == 'index') {
			page.detailScroller && page.detailScroller.scrollTo(0, 0, 1);
		}
		page.roomDetailScroller && page.roomDetailScroller.scrollTo(0, 0, 1);
	}

	// alert(util.userData.sessionId)
	// if(params){
	// var search = util.parseURI(params);
	// if(search.bulidid){
	// detail.id = search.bulidid;
	// detail.init();
	// $('#index').hide();
	// Mobilebone.transition($('#detail')[0],$('#index')[0],false,{showOut:false});
	// $('#detail .go_back').attr('href','https://obj@back');
	// }
	// if(search.roomid){
	// roomDetail.id = search.roomid;
	// roomDetail.init();
	// $('#index').hide();
	// Mobilebone.transition($('#room_detail')[0],$('#index')[0],false,{showOut:false});
	// $('#room_detail .go_back').attr('href','https://obj@back');
	// }
	// }else{
	// $('#index').show();
	// $('#detail .go_back').attr('href','#index');
	// $('#room_detail .go_back').attr('href','#detail');
	// page.init();
	// }
	window.searchFallBack = function() {
		that.filterInited && that.filterInited.destroy();
	}
	ajax.isLogin(function() {

	})
	page.init();
	/*
	$('body').swipeRight(function(){
	    if(detail.id){
	        loadBuilding.prev();
	    }
	})
	$('body').swipeLeft(function(){
	    if(detail.id){
	        loadBuilding.next();
	    }
	})
	*/
	var loadedBuild = {};

	var loadBuilding = {
		loadData: function(id, callback) {
			var that = this;
			if($('#building_detail_' + id).size()) {
				callback && callback();
				return;
			}

			if(loadedBuild[id]) {
				that.createDom(loadedBuild[id]);
			} else {
				ajax.getBulidingDetail(id, function(data) {
					$('#go_map').unbind().click(function() {
						app.goMap(data.data);
					})

					that.createDom(data.data);
					callback && callback(data.data);
					changeuser();
				});
			}
		},
		createDom: function(data) {
			building_id = data.id;
			var html = util.tpl.apply('building', data);
			var dom = $(html).appendTo('body');
			if(data.display_detialinfo == '1') {
				dom.find('.go_detail').show();
			}
			dom.find('#collect_buliding').click(function() {
				if(data.id) {
					var dom = $(this);
					var collected = $(this).hasClass('collected');
					if(collected) return;
					ajax.collectBuliding(data.id, function() {
						dom.find('#collect_buliding').toggleClass('collected');
					})
				}
			})
			dom.find('#buliding_share').click(function() {
				ajax.createShare(2, data.id, function(data) {
					app.share(data);
				})
			})
			var detailScroller = new iScroll("detail_scroller_" + data.id, {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			dom.find('#buliding_img').unbind().click(function() {
				// imgView.init(data.images);
				app.href('image_view.html', 'buildid=' + detail.id, 'hide');
			})
			if(data.is_collected == 1) {
				dom.find('#collect_buliding').addClass('collected');
			} else {
				dom.find('#collect_buliding').removeClass('collected');
			}
			dom.find('#avatar').attr('src', data.avatar);
			if(data.avatar) {
				var img = new Image();
				img.onload = function() {
					detailScroller.refresh();
					img.onload = null;
				}
				img.src = data.avatar;
			}
			if(data.image_total != '0') {
				dom.find('#buliding_img').show();
				dom.find('#img_total_span').html(data.image_total);
			} else {
				dom.find('#buliding_img').hide();
			}
			dom.find('#name,#buliding_title2').html(data.name);
			dom.find('#address').html(data.address);
			dom.find('.info_item').hide();

			dom.find('.info_item').each(function() {
				var prop = this.id;
				if(prop == 'tags') {
					if(data.tags.length) {
						$(this).show();
						var html = '';
						for(var i = 0; i < data.tags.length; i++) {
							html += '<i>' + data.tags[i] + '</i>'
						}
						dom.find('#tags_span').html(html);
					}
				} else {
					if(data[prop]) {
						dom.find(this).show();
						dom.find('#' + prop + '_span').html(data[prop]);
					}
				}
			})
			if(data.phones.length) {
				//dom.find('#phones').show();
				if(data.phones[0]) {
					dom.find('#phone1').html(data.phones[0]);
					dom.find('#phone1').attr('href', 'tel:' + data.phones[0]);
					dom.find('#phone1').attr('data-verify', '2-' + data.id);
					dom.find('#phone1').unbind().click(function() {
						var tel = $(this).html();
						var verify = $(this).attr('data-verify');
						ajax.telLog(tel, verify, function() {});
					})
				}
				if(data.phones[1]) {
					dom.find('#phone2').html(data.phones[1]);
					dom.find('#phone2').attr('href', 'tel:' + data.phones[1]);
					dom.find('#phone2').attr('data-verify', '2-' + data.id);
					dom.find('#phone2').unbind().click(function() {
						var tel = $(this).html();
						var verify = $(this).attr('data-verify');
						ajax.telLog(tel, verify, function() {});
					})
				}
			}
			var html = '';
			var list = data.rooms;
			for(var i = 0; i < list.length; i++) {
				html += util.tpl.apply('room', list[i]);
			}
			dom.find('#rooms').html(html);
			dom.find('.room_detail').click(function() {
				roomDetail.id = $(this).attr('rid');
				roomDetail.init();
			})
			detailScroller.refresh();
			ajax.getBulidingMoreInfo(data.id, function(data) {
				var isBlank = true;
				for(var p in data.data) {
					isBlank = false;
					break;
				}
				if(!isBlank) {
					detail.fillMoreInfo(data.data);
				} else {
					$('.go_detail').hide();
				}
			})
		},
		next: function() {
			var idx = -1;
			for(var i = 0; i < loadedBuildingId.length; i++) {
				if(loadedBuildingId[i] == current) {
					idx = i;
				}
			}
			var nextId = loadedBuildingId[idx + 1];
			if(nextId) {
				this.loadData(nextId, function() {
					var goout = detail.id == current ? $('#detail')[0] : $('#building_detail_' + current)[0];
					var goin = detail.id == nextId ? $('#detail')[0] : $('#building_detail_' + nextId)[0];
					Mobilebone.transition(goin, goout, false);
					current = nextId;
				});
			}
		},
		prev: function() {
			var idx = -1;
			for(var i = 0; i < loadedBuildingId.length; i++) {
				if(loadedBuildingId[i] == current) {
					idx = i;
				}
			}
			var prevId = loadedBuildingId[idx - 1];
			if(prevId) {
				this.loadData(prevId, function() {
					var goin = detail.id == prevId ? $('#detail')[0] : $('#building_detail_' + prevId)[0];
					var goout = detail.id == current ? $('#detail')[0] : $('#building_detail_' + current)[0];
					Mobilebone.transition(goin, goout, true);
					current = prevId;
				});
			}
		}
	}
	window.loadBuilding = loadBuilding;
	window.page = page;
});