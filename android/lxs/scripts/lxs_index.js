LXS(function(ajax) {
	//所有的页面内容都在左部分和右部分
	var leftPart = $('#left_part');
	var rightPart = $('#right_part');
	var cache = {
		region: {}, //区域
		plate: {}, //模块
		status: {}, //交付列表
		metro: {}, //地铁列表
		type: {} //类型列表
	}
	var regionList = [];
	var regionCache = {};
	var totalHeight = {
		left: 0,
		right: 0
	}
	var keywords = [];
	var current;
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
			!that.inited && this.bindAction();
			this.searchBulids();
			this.getAllSelectOptions();
		},
		bindAction: function() {
			var that = this;
			page.roomDetailScroller = new IScroll("#room_detail_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			page.selectScroller = new IScroll("#select_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			page.detailMoreScroller = new IScroll("#detail_more_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			page.searchScroller = new IScroll("#search_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			page.detailScroller = new IScroll("#detail_scroller", {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
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
			setTimeout(
					function() {
						myScroll = that.myScroll = new IScroll('#bulidingListArea', {
							mouseWheel: true,
							click: true,
							useTransition: false,
							hScrollbar: false,
							vScrollbar: false,
							hideScrollbar: true,
							topOffset: pullDownOffset,
							startY: -pullDownOffset
						});
						myScroll.on('refresh', function() {
							if(pullDownEl.className.match('loading')) {
								pullDownEl.className = '';
								pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉更新数据...';
							} else if(pullUpEl.className.match('loading')) {
								pullUpEl.className = '';
								pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉获取更多...';
							}
						});
						myScroll.on('scrollEnd', function() {
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
							// location.href = 'http://obj@showtabbar';
						});
						myScroll.on('scrollMove', function() {
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
							// location.href = 'http://obj@hidetabbar';
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
				$('#search_go_back').attr('href', '#index');
				refreshSearchScroller();
				// e.preventDefault();
				// e.stopPropagation();
			})
			if (util.getLocal('userType')=='299') {
				$("#ylb_show").hide();
			}else{
				$("#ylb_show").show();
			}
			
			$('#bulidingdetail_search_btn').click(function() {
				$('#keyword').val('');
				$('#search_go_back').attr('href', '#detail');
				refreshSearchScroller();
			})
			$('#roomdetail_search_btn').click(function() {
					$('#keyword').val('');
					$('#search_go_back').attr('href', '#room_detail');
					refreshSearchScroller();
				})
				// $('#select_search_btn').click(function(){
				// $('#search_go_back').attr('href','#room_detail');
				// })
			$('#search_go_back').click(function() {
				if($('#search_go_back').attr('href') == '#index') {
					that.pageIndex = 1;
					loadedBuildingId.length = 0;
					that.searching = false;
					that.searchBulids();
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
			$('#search_buliding').click(function() {
				that.pageIndex = 1;
				loadedBuildingId.length = 0;
				ajaxing = false;
				that.searching = true;
				leftPart.html('');
				rightPart.html('');
				that.searchBulids();
				setTimeout(function() {
					page.myScroll.refresh();
				})
			})
			$('input[name=trade]').change(function() {
				var trade = $('input[name=trade]:checked').val();
				// 'm²'
				if(trade == '2') {
					$('#price_unit').html('元/m²');
					$('#prop4,#prop5').addClass('disabled');
					$('#prop1,#prop2,#prop3').removeClass('disabled');
					$('#prop4_ipt,#prop5_ipt').attr('checked', false);
				} else {
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
			})
			$('#keyword').keydown(function(e) {
				if(!hasResult && e.keyCode == 13) {
					goSearchBuliding();
				}
			})
			var filterInited = false;
			var hasResult = true;
			var isFriend = true;
			$('#keyword').focus(function() {
				isFriend = false;
				$(".invite_btn").text("新增楼盘");
				$('#search .content').hide().on("click", function() {
					isFriend = false
				});
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
							//存储用户输入的搜索房子条目
							if(localStorage.pagecount < 5) {
								localStorage.pagecount = Number(localStorage.pagecount) + 1;
								localStorage.setItem('hourse' + localStorage.pagecount, keyword);
							} else {
								localStorage.pagecount = 1;
								localStorage.setItem('hourse' + localStorage.pagecount, keyword);
							}
							//                          此处是搜索后显示所有的待选项
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
			$("#keyword").blur(function() {
				$(".invite_btn").text("邀请朋友");
			})

			$(".invite_btn").on("click", function() {
				if(isFriend) {
					$(this).val("href", "javascript:app.href('lxs_add_houses.html');");
					console.log("friend:" + $(this).attr("href"));
				} else {
					$(this).val("href", "javascript:app.href('lxs_invite_friends.html');");
					console.log("friend:" + $(this).attr("href"));
				}
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
			params.meter = $('#acreage').val();
			params.price = $('#budget').val();
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
		//搜索
		searchBulids: function() {
			var that = this;
			var params = this.getSearchFormData();
			params.page = that.pageIndex;
			params.per_page = that.pageSize;
			if(!that.searching) {
				//如果不是由搜索进，直接请求数据
				that.queryBulidList();
			} else {
				ajax.bulidSearch(params, function(data) {
					if(that.pageIndex == 1) {
						leftPart.html('');
						rightPart.html('');
					}
					//搜索完根据返回数据来创建列表
					that.createBulids(data.list);
				})
			}
		},
		//创建数据列表
		createBulids: function(list) {
			//创建datalist的option值（即最近搜索条目）
			var count = localStorage.pagecount;
			$("#search_list").children().remove();
			for(var i = 5; i > 0; i--) {
				var keyword = localStorage.getItem('hourse' + i);

				$("#search_list").append("<option>" + keyword + "</option>");
			}

			var that = this;
			if(that.pageIndex == 1) {
				totalHeight.left = 0;
				totalHeight.right = 0;
			}
			var target, temp;
			for(var i = 0; i < list.length; i++) {
				loadedBuildingId.push(list[i].id);
				target = totalHeight.left <= totalHeight.right ? leftPart : rightPart;
				temp = $(util.tpl.apply('bulid_item', list[i]));
				//创建
				target.append(temp);
				if(totalHeight.left <= totalHeight.right) {
					totalHeight.left += temp.height();
				} else {
					totalHeight.right += temp.height();
				}
				//class为buliding_link的所有图片的点击事件
				$(temp).find('#image_more').click(function() {
					detail.id = $(this).attr('bid');
					detail.init();
					if($(this).parent().attr("class")=="item_img"){
						$(".mui-grid-view").children().remove();
						var imgs = new Array();
						var recom_url = "http://test.imrlou.com/api/recommonagent";
						var recom_parm = {
							building_id: detail.id,
							limit: 6
						}
						$.getJSON(recom_url, recom_parm, function(data) {
							var img_len = data.list;
//							alert(img_len.length);
							if(img_len.length!=0) {
								$("#curid").text(localStorage.getItem("curId"))
									//获取图片
								var li_start = '<li class="mui-table-view-cell mui-media mui-col-xs-2"><a id="consul_recom" href="javascript:app.href(\'lxs_consultant_recommand.html\',building_id='+detail.id+')"><img class="mui-media-object" src="';
								var li_end = '" /></a></li>'
								var i = 0;
								while(i>img_len.length){
									alert(img_len.length+"ee");
									imgs[i] = img_len[i].avatar;
									$(".mui-grid-view").append(li_start + imgs[i] + li_end);
									i++;
								}
								/*for(var i = 0; i < img_len.length; i++) {
//									alert(img_len.length);
									alert(img_len.length+"ee");
								imgs[i] = img_len[i].avatar;
								$(".mui-grid-view").append(li_start + imgs[i] + li_end);
						  	}*/
							}
						})
						
					}
				})

			}
			that.myScroll && that.myScroll.refresh();
			that.firstPageLoaded = true;
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
	    楼盘详情页
	*/
	var detail = {
		id: 0,
		init: function() {
			current = detail.id;
			localStorage.setItem("curId", current);
			!this.inited && this.bindAction();
			this.clear();
			this.getBulidingInfo();
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
					// location.href = 'http://obj@share@'+content+'@'+data.url+'@'+data.avatar+'@'+data.url+'@'+data.id;
				})
			})
			this.inited = true;
		},
		getBulidingInfo: function() {
			var that = this;
			ajax.getBulidingDetail(this.id, function(data) {
				$('#go_map').unbind().click(function() {
					app.goMap(data.data);
					// var title = encodeURIComponent(data.data.name);
					// var address = encodeURIComponent(data.data.address);
					// location.href = 'http://obj@navmap@' + title + '@' + data.data.lat + '@' + data.data.lon + '@' + address + '@' + data.data.id;
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
				// imgView.init(data.images);
				app.href('image_view.html', 'buildid=' + detail.id, 'hide');
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
			$('#phone1').html('');
			$('#phone2').html('');
			if(data.phones.length) {
				if (util.getLocal('userType')=='299') {
	                $('#phones').hide();
	                $('#hzyj').hide();
	                //hzyj为合作佣金
	            }else {
	                $('#phones').show();
	                $('#hzyj').show();
	            }
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
			//          return '<span class="house_active"><i></i><span>认证</span></span>';
			return '<span class="house_active"><i></i></span>';
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
	    房源详情页
	*/
	var roomDetail = {
		id: 0,
		init: function() {
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
					// location.href = 'http://obj@share@'+content+'@'+data.url+'@'+data.avatar+'@'+data.url+'@'+data.id;
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
					// imgView.init(data.data.image_list);
					app.href('image_view.html', 'roomid=' + roomDetail.id, 'hide');
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

			$('#room_buliding_name').html(data.building_name);
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
			
			if (util.getLocal('userType')=='299') {
                $("#fy_info").hide();
            }else{
                $("#fy_info").show();
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
				'299': '其他'
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
			$('#user_company').html(data.user.company);
			$('#user_type').html(typeMap[data.user.type]);
			$('#user_name').html(data.user.cn_username + data.user.en_username);
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
	// $('#detail .go_back').attr('href','http://obj@back');
	// }
	// if(search.roomid){
	// roomDetail.id = search.roomid;
	// roomDetail.init();
	// $('#index').hide();
	// Mobilebone.transition($('#room_detail')[0],$('#index')[0],false,{showOut:false});
	// $('#room_detail .go_back').attr('href','http://obj@back');
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
				});
			}
		},
		createDom: function(data) {
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
			var detailScroller = new IScroll("#detail_scroller_" + data.id, {
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
				dom.find('#phones').show();
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
});