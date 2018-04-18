(function() {
	var _ajax = {
		getSendsms: function(params, success, fail) {
			var url = 'api/sendsms';
			ajax(url, params, success, fail);
		},
		getUserByMobilePhone: function(params, success, fail) {
			var url = 'api/userByMobilePhone';
			ajax(url, params, success, fail);
		},
		getUser: function(params, success, fail) {
			var url = 'api/users';
			ajax(url, params, success, fail);
		},
		getAllSelectOption: function(success, fail) {
			var url = 'api/buildingoptions';
			ajax(url, {}, success, fail);
		},
		searchBulidingByKeyword: function(keyword, success, fail) {
			var url = 'api/buildingtip';
			var data = {
				key: keyword
			}
			ajax(url, data, success, fail, 'get');
		},
		/*searchCompanyByKeyword: function(keyword, success, fail) {
			var url = 'api/companytip';
			var data = {
				key: keyword
			}
			ajax(url, data, success, fail, 'get');
		},*/
		searchCompanyByKeyword: function(keyword, success, fail) {
			var url = 'https://appapi.imrlou.com/api/newcompanytip';
			var data = {
				company: keyword
			}
			ajax(url, data, success, fail, 'get');
		},
		saveType: function(type) {
			var url = 'api/savetype?type=' + type;
			ajax(url, {}, {}, {}, 'get');
		}
	};
	window.FY = function(fn) {
		fn(_ajax);
	};
	$(function() {
		util.require.apply(util, util.jss);
	})
})();
$(function() {
	FY(function(ajax) {
		var cache = {
			region: {},
			plate: {},
			status: {},
			metro: {},
			type: {},
			data: {}
		}
		var regionCache = {};
		//params = 'type=201'
		//var type = 1;
var uploading = 'cover';
		var page = {
			isinit: false,
			index: 1,
			data: {},
			harems: [],
			count: false,
			type: '',
			init: function(opt) {
				var that = this;
				var _temple = '';
				switch(that.type * 1) {
					case 1:
						_temple = 'regCompany';
						break;
					case 2:
						_temple = 'regCompany';
						break;
					case 3:
						_temple = 'regCompany';
						break;
					case 103:
						_temple = 'regCompany';
						break;
					case 102:
						_temple = 'regManager';
						break;
					case 104:
						_temple = 'facilitator';
						break;
						/*case 201:
							_temple = 'regPersonal';
							break;*/
						/*case 299:
                        _temple = 'regPersonal';
						break;*/
					default:
						break;
				}
				var form = util.tpl.apply(_temple, {});
				$('#add .company_reg_form').html(form);
				that.getAllSelectOptions();
				that.globalBind();
				that.formScroller.refresh();
				ajax.saveType(that.type);
			},
			globalBind: function() {
				var that = this;
				//$('#select .top_banner a').each(function(){
				//	this.ontouchstart = function(e){
				//		e.preventDefault();
				//	}
				//})
				$('#work_start').date();
				$(document).on('click', '.submit_btn1', function() {
					var params = that.getSearchFormData();
					if(params) {
						ajax.getUser(params, function(data) {
							if(data.errorid == 0) {
								// app.href('lxs_index');
								app.closeLogin();
								// window.location.href="https://obj@href@lxs_index.html";
								
							}
						})
					}
				})
				
				
				$("body").on('click', function(e) {
					var that = e.target;
					var imgt = "";
					if(that.nodeName=="SPAN"){
						var parc = $(that).parent();
						var lazz = parc.attr("class");
						if(lazz=="upload_avatar"){
							imgt = parc.find(".add_img").attr("imgt");
							show(imgt+"1");
						}
					}else if(that.className=="upload_avatar"){
						var clazz=$(that).find(".add_img");
						imgt = clazz.attr("imgt");
						show(imgt+"2");
					}
					uploading = imgt;
				})
				function show(imgt){
//					alert(imgt);
				}
				
				$('#add').find('#go_select_region').bind('click', function() {
					$('#select_title').html('选择区域');
					that.selecting = 'region';
					that.createRegion();
				})
				$('#select .go_back,#select .submit').bind('click', function() {
					// $('.page').hide();
					// $(this).parents('.page').prev().show();
					if(this.className == 'submit') {
						console.log('fillSelected');
						that.fillSelected();
					}
					that.selecting = '';
					//that.parent().click();
				})
				var buildingFilter = false;
				$(document).on('focus', '#building_name', function() {
					if(buildingFilter) return;
					var filter = buildingFilter = new Filter(document.getElementById('building_name'), {
						top: $('#building_name').offset().top + 8,
						left: $('#building_name').offset().left - 1,
						// minWidth : $('#formName').width() + ($.browser.mozilla || $.browser.msie ? 5 : 4) ,
						width: $('#building_name')[0].clientWidth - 1,
						className: 'search_buliding_option2',
						height: 300,
						ajax: true,
						ajaxFn: function(fn) {
							var keyword = $('#building_name').val();
							if(keyword == '') {
								filter.destroy();
								return;
							}
							ajax.searchBulidingByKeyword(keyword, function(data) {
								fn(data.list);
							})
						},
						showFilted: function(result, con, fo) {
							var len = result.length;
							for(var i = 0; i < len; i++) {
								var a = document.createElement('a');
								a.innerHTML = result[i];
								a.className = 'search_buliding_option2';
								con.appendChild(a);
							}
						},
						selectFn: function(obj, ipt, fo) {
							ipt.value = obj;
							filter.destroy();
							return false;
						},
						noResult: function() {
							filter.dom.con.innerHTML = '<p style="padding:10px 0;text-align:center;font-size:14px;color:#444;">没有搜索到结果</p>';
						}
					})
				})
				this.formScroller = new iScroll("reg_scroller", {
					mouseWheel: true,
					click: true,
					useTransition: false,
					hScrollbar: false,
					vScrollbar: false,
					hideScrollbar: true
				});
				this.selectScroller = new iScroll("select_scroller", {
					mouseWheel: true,
					click: true,
					useTransition: false,
					hScrollbar: false,
					vScrollbar: false,
					hideScrollbar: true
				});
				var companyFilter = false;
				$('#reg_scroller').on('focus', '#company', function() {
					if(companyFilter) return;
					var filter = companyFilter = new Filter(document.getElementById('company'), {
						top: $('#company').offset().top + 8,
						left: $('#company').offset().left - 1,
						// minWidth : $('#formName').width() + ($.browser.mozilla || $.browser.msie ? 5 : 4) ,
						width: $('#company')[0].clientWidth - 1,
						className: 'search_buliding_option2',
						height: 300,
						ajax: true,
						ajaxFn: function(fn) {
							var keyword = $('#company').val();
							if(keyword == '') {
								filter.destroy();
								return;
							}
							ajax.searchCompanyByKeyword(keyword, function(data) {
								fn(data.list);
							})
						},
						showFilted: function(result, con, fo) {
							var len = result.length;
							for(var i = 0; i < len; i++) {
								var a = document.createElement('a');
								a.innerHTML = result[i];
								a.className = 'search_buliding_option2';
								con.appendChild(a);
							}
						},
						selectFn: function(obj, ipt, fo) {
							ipt.value = obj;
							filter.destroy();
							return false;
						},
						noResult: function() {
							filter.dom.con.innerHTML = '<p style="padding:10px 0;text-align:center;font-size:14px;color:#444;">没有搜索到结果</p>';
						}
					})
				})

			},
			getSearchFormData: function() {
				var that = this;
				var params = {};
				params.is_chk = 0;
				params.type = that.type;
				params.cn_username = $('#add').find('#cn_username').val();
				params.en_username = $('#add').find('#en_username').val();
				params.email = $('#add').find('#email').val();
				params.company = $('#add').find('#company').val();
				params.intro = $('#add').find('#intro').val();
				params.building_name = $('#add').find('#building_name').val();				

				params.work_start = $('#add').find('#work_start').val();
				params.region_id = $('#add').find('#go_select_region').attr('vals');

				params.wechat = $("#add").find('#weixin').val();
				params.post_card = $('.img_del_card').next().attr('src');
				params.post_card_id = $('.img_spancover').find('.img_del').attr('imgId');
				params.avatar = $('.img_spanidCard').find('.img_del').attr('imgId');				
				//增值服务商：服务类型
				params.service_type = $("#server_con").attr('data-value');
				//经纪人：擅长类型
//				params.good_type = $("#type_con").attr('data-value');			
				
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
				if(!params.cn_username) {

					alert('中文名必填');
					return false;

				}
				if(!params.en_username) {
					/*
					alert('英文必填');
					return false;
                     */
				}
				var parent = /^[A-Za-z]+$/;
				if(!parent.test(params.en_username)) {

					alert('请正确填写英文名');
					return false;

				}
				if(!params.email) {

					alert('邮箱必填');
					return false;

				}

				var Regex = /^(?:\w+\.?)*\w+@(?:\w+\.?)*\w+$/;
				if(!params.company) {

				} else if(!Regex.test(params.email)) {
					alert('请正确填写email格式');
					return false;
				}

				if(that.type == '1' || that.type == '2' || that.type == '3') {

					if(!params.building_name) {
						alert('楼盘名称必填');
						return false;
					}

					if(!params.company) {

						alert('公司简称必填');
						return;

					} else {
						/*
						var len = 0;
						for(var i=0;i<params.company.length;i++){
							var t = params.company;
							if(t.chatCodeAt(i) > 255){
								len += 2;
							}else{
								len ++
							}
						}
						*/
						if(params.company.length > 20) {
							alert('公司简称超出长度限制');
							return false;
						}
					}
				}
				if(that.type == '102') {

					/*if(!params.intro){
						alert('本人简介必填');
						return false;
					}*/
					if(!params.work_start) {
						alert('入行日期必填');
						return false;
					}

					if(!params.company) {

						alert('公司简称必填');
						return;

					} else {
						/*
						var len = 0;
						for(var i=0;i<params.company.length;i++){
							var t = params.company ;
							if(t.chatCodeAt(i) > 255){
								len += 2;
							}else{
								len ++
							}
						}
						*/
						if(params.company.length > 20) {
							alert('公司简称超出长度限制');
							return false;
						}
					}

					if(!params.region_id) {

						alert('擅长区域必填');
						return false;

					}
				}

				/*if(that.type == '104'){
					//增值服务商
					if (!params.server_con) {
						alert('服务类型必填')!
						return false;
					}
				}*/

				if(!params.avatar) {
					
					/*alert('请上传一张图片作为您的头像');
					return;*/
					
				}

				if(!params.post_card_id){
					/*alert('请上传一张图片作为您的名片');
					return false;*/
				}

				return params;
			},
			createRegion: function() {
				var html = '<div class="areas">';
				var data = cache.region;
				var list = [];
				for(var i in data) {
					if(data.hasOwnProperty(i)) {
						list.push({
							id: i,
							name: data[i]
						})
					}
				}
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
					if($('input[name=region]:checked').size() > 3) {
						alert('只能选择3个区域');
						this.checked = false;
						return false;
					}
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
				}, 100);
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
				selected = selected.split(',');
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
				$('input[name=' + this.selecting + ']').each(function() {
					if(temp[this.value]) {
						this.checked = true;
					}
				})

				setTimeout(function() {
					if(page.selectScroller) {
						page.selectScroller.scrollTo(0, 0, 10);
						page.selectScroller.refresh();
					}
				}, 10);
			},
			getAllSelectOptions: function() {
				ajax.getAllSelectOption(function(data) {
					cache.metro = data.metro;
					cache.status = data.status;
					cache.type = data.type;
					for(var i = 0; i < data.region_plate.length; i++) {
						var regId = data.region_plate[i].id;
						var regName = data.region_plate[i].name;
						var child = data.region_plate[i].child;
						cache.region[regId] = regName;
						regionCache[regId] = data.region_plate[i];
						for(var j = 0; j < child.length; j++) {
							cache.plate[child[j].id] = child[j].name;
						}
					}
				})
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
		var search, type;
		//params = 'type=102';
		function pageInit() {
			search = util.parseURI(params);
			page.type = search.type;
			page.init();
		}
		if(params) {
			pageInit();
		} else {
			window.afterParams = pageInit;
		}

		Array.prototype.del = function(n) {
			if(n < 0) {
				return this;
			} else {
				return this.slice(0, n).concat(this.slice(n + 1, this.length));
			}
		}
		
		
		function uploadCallBack(id, url) {
			if(uploading == 'cover') {
				setCover(id, url);
			} else if(uploading == 'idCard') {
				addIDImages(id, url);
			}
		}
		
		function setCover(id, url) {
			$('.cover').remove();
			$('#add_cover').hide();
			var html = util.tpl.apply('img', {
				imgType: 'cover',
				avatar: url,
				id: id
			});
			var newIn = $(html);
			$('#add_cover').before(newIn);
		}
		
		function addIDImages(id, url) {
			var html = util.tpl.apply('img', {
				imgType: 'idCard',
				avatar: url,
				id: id
			});
			var newIn = $(html);
			$('#add_IDcard').before(newIn);
		}
		
		window.addIDImages = addIDImages;
		window.setCover = setCover;
		window.uploadCallBack = uploadCallBack;
		
	})

});

