YLB(function(ajax) {
	var search, threadId,winData;
	var page = {
		init: function() {
			var that = this;
			!that.inited && that.bindAction();
			that.getThreadDetail();
		},
		bindAction: function() {
			var that = this;
			var scroller = that.scroller = new iScroll('thread_detail_scroller', {
				hScrollbar: false,
				vScrollbar: false,
				hideScrollbar: true
			});
			var width = $('#reply_content').width() + 20;

			/*膜拜大神*/
			var oldH = window.innerHeight;
			/*
			$('#reply_content').focus(function(){
			    window.mrlou.hidebottom();
			    setTimeout(function(){
			        var st = $(document).scrollTop();
			        var newH = window.innerHeight;
			        $('.reply_area').css({
			            position:'absolute',
			            top : newH + st - $('.reply_area').height()
			        })
			    },500);
			})
			$('#reply_content').blur(function(){
			    window.mrlou.showbottom();
			    $('.reply_area').css({
			        position:'fixed',
			        top : 'auto',
			        bottom : 0
			    });
			})
			*/

			$('#reply_content').bind('input', function() {
				var val = this.value;
				var reg = /\n/g;
				var match = val.match(reg),
					enterNum = 0;
				if(match) enterNum = match.length;
				var div = document.createElement('div');
				div.innerHTML = val.replace(reg, '');
				div.className = 'width_compute_helper';
				$('body').append(div);
				var _w = div.clientWidth;
				var lines = Math.ceil(_w / width);
				var total = Math.min(3, enterNum + lines) - 0.5;
				div.parentNode.removeChild(div);
				var totalHeight = total * 26;
				$('.reply_txt_con,#reply_content').css('height', Math.max(26, totalHeight));
			})
			var posting = false;
			$('#post_reply').click(function() {
				if(posting) return;
				var content = $.trim($('#reply_content').val());
				if(content.length > 100 || content.length < 1) {
					alert('回复内容必须在1~100个字符之间');
					return;
				}
				posting = true;
				ajax.postReply(threadId, content, function(data) {
					posting = false;
					that.createReply({
						replys: [data.data]
					}, true);
					$('.reply_txt_con,#reply_content').css('height', 26);
					$('#reply_content').val('');
				}, function(data) {
					alert(data.msg);
					posting = false;
				})
			});
			$('#collect_thread').click(function() {
				var dom = $(this);
				var collected = $(this).hasClass('collected');
				if(collected) return;
				ajax.collectThread(threadId, function() {
					$('#collect_thread').toggleClass('collected');
				})
			})

			that.inited = true;
		},
		getThreadDetail: function() {
			var that = this;
			ajax.getThreadDetail(threadId, function(data) {
				var _data = data.data;
				that.createThreadDetail(_data);
				that.createReply(_data);
			})
		},
		createThreadDetail: function(data) {
			$("#image").on('click',function(){
				$(this).attr('href','#index');
                $("#image").attr('class','page out');
                $("#index").attr('class','page in');
			});
			
			winData = data;
			var that = this;
			var user = data.user;
			if(data.is_collected == 1) {
				$('#collect_thread').addClass('collected');
			} else {
				$('#collect_thread').removeClass('collected');
			}

			//是否点赞过
			if(data.is_like == "0") {
				$("#agree_image")[0].src = 'ylb_images_praise.png';
				$("#agree_image1")[0].src = 'ylb_images_praise.png';
				$(".post_agree_btn")[0].backgroundColor = '#BD081C';
			} else {
				$("#agree_image")[0].src = 'ylb_images_praise_down.png';
				$("#agree_image1")[0].src = 'ylb_images_praise_down.png';
				$(".post_agree_btn").css('background','#DBDBDB');
			}

			//访问量
			$('#visit_total').html(data.vists);

			//评论量
			$("#reply_count2").html(data.reply_total);

			//点赞量
			$("#agree_count").html(data.like_total);

			//点赞用户显示
			var ids = [];
			var imgs = [];
			var vs = [];
			for(var i = 0; i < data.like_user.length; i++) {
				//若数据很多时，先显示前3行
				if(i < 23) {
					addBox(data.like_user[i].v, data.like_user[i].id, data.like_user[i].avatar, true)
				} else if(i == 23) {
					$("#avatars_list").append('<li id="more_show" class="user_avatar_agree"><img src="ylb_images_more.png" /></li>');
					addBox(data.like_user[i].v, data.like_user[i].id, data.like_user[i].avatar, false)
				} else {
					addBox(data.like_user[i].v, data.like_user[i].id, data.like_user[i].avatar, false)
				}
				if(i==data.like_user.length-1){
					var hide = $('<li class="user_avatar_agree" id="more_hide"><img src="ylb_images_more_up.png"</li>').hide();
					$("#avatars_list").append(hide);
				}
			}

			function addBox(isV, uid, src, isShow) {
				var Img = isV == "0" ? "" : '<img id="oval_"src="resourse_images_oval_v.png" />';
				var li = $('<li class="user_avatar_agree" uid=' + uid + '><img src=' + src + ' />' + Img + '</li>');
				if(!isShow){
					li.hide();
				}
				$("#avatars_list").append(li);
				
			}

			$('#agree_count1').html(data.like_total);
			$('#poster_avatar').attr('src', user.avatar);

			$('#poster_name').html(user.cn_username || user.en_username);
			$('#poster_phone').html(user.phone);
			if(user.phone) {
				$('#poster_phone').attr('href', 'tel:' + user.phone);
			}
			var tpl = user.type == '102' ? 'type1' : 'type2';
			$('#poster_info').html(util.tpl.apply(tpl, user));
			$('#poster_phone,#user_phone').unbind().click(function() {
				var tel = $(this).html();
				var verify = $(this).attr('data-verify');
				ajax.telLog(tel, verify, function() {});
			});
			var postTime = data.create_time.toDate();
			$('#post_time').html(postTime.format('hh:mm'));
			$('#post_date').html(postTime.format('YYYY-MM-DD'));
			$('#thread_type').html(util.tpl.format.subjectConvert(data.subject));
			$('#thread_title').html(data.title.htmlEncode());
			if(data.region == null) {
				$('#thread_remark_fu').hide();
			} else {
				$('#thread_remark').html(data.region);
			}

			//$('#thread_content').html(data.content.htmlEncode());
			$('#thread_content').html(data.content);

            var avatarLen = data.avatar.length;
			if (avatarLen>1) {
				$('#thread_img_con').show();
				$('#thread_img').attr('src', data.avatar[0]);
				$('#buliding_img').show();
                $('#img_total_span').html(avatarLen);
				var img = new Image();
				img.onload = function() {
					that.scroller && that.scroller.refresh();
					img.onload = null;
				}
				img.src = data.avatar[0];
			} else{
				$('#thread_img_con').show();
				$('#thread_img').attr('src', data.avatar);
				var img = new Image();
				img.onload = function() {
					that.scroller && that.scroller.refresh();
					img.onload = null;
				}
				img.src = data.avatar;
			}

			$('#buliding_img').unbind().click(function(){
				var u = navigator.userAgent;
				var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
				var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
				if(isIos){
					var str = "";
	                for (var i = 0; i < data.avatar.length; i++) {
	                	str+=data.avatar[i]+",";
	                }
	                str = str.substring(0,str.length-1);
					goImg(str);
//					alert('ios'+str);
				}else{
	                $(this).attr('href','#image');
	                $("#index").attr('class','page out');
	                $("#image").attr('class','page in');
	                //把图片数组给页面的image页面
	                $("#avatarLength").children().remove();
	                $("#avatarImg").children().remove();
	                for (var i = 0; i < data.avatar.length; i++) {
	                	$("#avatarLength").append('<span class="content-show"></span>'); 
	                	$("#avatarImg").append('<li class="pd-lb"><img src='+data.avatar[i]+' width="100%" /></li>');
	                }                
				}
            })
			

			that.scroller && that.scroller.refresh();

			//点赞功能
			/*$(".agree_total").click(function() {
				dianzan();
			})*/
			$("#user_phone_con").on("click", function() {
				dianzan(false);
			})
			$("#post_agree").on('click',function(){
				dianzan(true);
			})
			

			function dianzan(isDelLike) {
				var param = {
					like_id: data.id,
					type: 1,
					session_id: util.getLocal('sessionId')
				}
				if(winData.is_like == "0") {
					//点赞
					$.getJSON("https://appapi.imrlou.com/api/likebyuserid", param, function(data) {
						if(data.msg == 'success') {
							//点赞量+1
							winData.is_like = "1";
							var agreeCount = $("#agree_count").text();
							$("#agree_count").text(parseInt(agreeCount) + 1);
							$('#agree_image').attr('src', 'ylb_images_praise_down.png');
							$('#agree_image1').attr('src', 'ylb_images_praise_down.png');
							$(".post_agree_btn").css('background-color','#DBDBDB');
							var imageUrl = data.user.avatar;
							addImgs(data.user.id,data.user.avatar,data.user.v);
						}
					});
				} else {
					if (isDelLike==true) {
						//取消点赞
						$.getJSON("https://appapi.imrlou.com/api/deletemylike", param, function(data) {
							if(data.msg == 'success') {
								//点赞量-1
								winData.is_like = "0";
								var agreeCount = $("#agree_count").text();
								$("#agree_count").text(parseInt(agreeCount) - 1);
								$('#agree_image').attr('src', 'ylb_images_praise.png');
								$('#agree_image1').attr('src', 'ylb_images_praise.png');
								$(".post_agree_btn").css("background-color",'#BD081C');
								remImgs(data.user.id);								
							}						
						});						
					} else{
						//不做处理
						return true;
					}

				}
				function addImgs(uid,imageUrl,isV){
					var Img = isV == "0" ? "" : '<img id="oval_"src="resourse_images_oval_v.png" />';
					var li = $('<li  class="user_avatar_agree" uid="' + uid + '"><img src="' + imageUrl + '" />'+Img+'</li>').hide();
							if($('#avatars_list li').length > 0) {
								$('#avatars_list li:first-child').before(li);
							}
							if($('#avatars_list li').length == 0) {
								$('#avatars_list').append(li);
							}

							if($('#avatars_list li').length > 24) {
								$('#avatars_list li:last-child').remove();
							}
							li.show(200);

				}
				function remImgs(uid){
					var li = $('#avatars_list li[uid="'+uid+'"]');
					li.hide(200,function(){
						li.remove();
					});
				}
			}
			//跳转到个人品牌页
			$("#user_avatar").on("click", function() {
				var li = $(this).next();
				if(li.attr("data-v") == "1") {
					herfPerson(this);
				}

			})
			$("#avatars_list li").on("click", function() {
				var li = $(this);
				if(li.attr("id")=="more_hide"){
					li.hide(200);
					$("#avatars_list li:gt(23)").hide(200);
					$("#more_show").show();
				}else if(li.attr("id")=="more_show"){
					li.hide(200);
					$(".user_avatar_agree_display").removeClass("user_avatar_agree_display").addClass('user_avatar_agree');
					$("#avatars_list li").show();
				}else if(li.find("#oval_").length > 0) {
					herfPerson(this);
				}
			})

			function herfPerson(that) {
				var id = $(that).attr('uid');
				var u = navigator.userAgent;
				var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
				var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
				if(isAndroid) {
					var msg = window.mrlou.androidIs("1", id);
				} else if(isIos) {
					//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
					broker("1", id);
				}
			}
			if(user.company_status == 1) {
				// 红底
				$('#company_color').css({
					'padding': '4px',
					'border-radius': '2px',
					'background': '#bd081c',
					'color': '#fff',
					"opacity": 1,
					"display": "inline"
				});
			} else if(user.company_status == 0) {
				// 不改变
				$(".house_info").find('#company_color').css({
					'background': '#fff'
				});
			} else {
				alert("公司出错了");
			}

			if(user.v == 1) {
				$('#oval_v2').show().attr("data-v", "1");

				var u = navigator.userAgent;
				var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
				var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
				//跳转到公司品牌页
				$('#company_color').click(function() {
					var company_name = $(this).text();
					if(isAndroid) {
						var msg = window.mrlou.androidIs("2", company_name);
					} else if(isIos) {
						//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
						broker("2", company_name);
					}
				})

			} else if(user.v == 0) {
				$('#oval_v2').hide().attr("data-v", "0");;
				// $('#oval_v2').css({"width":0,"height":0,"opacity":0,"display":"none"});
			} else {
				alert("加V出错了");
			}

		},
		createReply: function(data, append) {
			var that = this;
			var list = data.replys,
				len = list.length;
			var html = '';
			for(var i = 0; i < len; i++) {
				html += util.tpl.apply('reply_item', list[i]);
			}
			if(append) {
				$('#reply_list').append(html);
			} else {
				$('#reply_list').html(html);
			}
			setTimeout(function() {
				that.scroller.refresh();
			})
		}
	}

	var subjectStr = {
		'1': '我有房源',
		'2': '我有客户',
		'3': '其他主题'
	}
	util.tpl.format.subjectConvert = function(subject) {
		return subjectStr[subject || '1'];
	}
	var today = (new Date()).format('YYYY-MM-DD');
	util.tpl.format.replyTimeFn = function() {
		var obj = arguments[arguments.length - 1];
		var replyTime = obj.reply_time.toDate();
		var replyDay = replyTime.format('YYYY-MM-DD');
		return replyDay == today ? replyTime.format('hh:mm') : replyTime.format('YYYY-MM-DD hh:mm');
	}
	util.tpl.format.posterNameFn = function() {
		var obj = arguments[arguments.length - 1];
		return obj.user_cn_username || obj.en_username;
	}
	util.tpl.format.companyAssert = function() {
		var obj = arguments[arguments.length - 1];
		return obj.company ? '<span class="company_name" style="margin-right:8px">' + obj.company + '</span>' : '';
	}
	var typeMap = {
		'1': '开发商',
		'2': '代理商',
		'3': '运营商',
		'102': '经纪人',
		'201': '小业主',
		'299': '其他',
        '104' : '企业服务商'
	}
	util.tpl.format.posterTypeFn = function() {
		var obj = arguments[arguments.length - 1];
		return typeMap[obj.user_type];
	}
	var thread_detail_scroller = document.getElementById('thread_detail_scroller');
	thread_detail_scroller.addEventListener('touchmove', function(e) {
		e.preventDefault();
	}, false);

	function pageInit() {
		search = util.parseURI(params);
		if(search.back == 0 || search.back === undefined) {
			// 易楼帮首页点击帖子后，回退为back
			// 其他地方跳转进帖子详情后，回退为back
			$('.go_back').attr('href', 'javascript:app.goBack();');
		}
		if(search.back == 'index') {
			// 发帖后跳转到帖子详情后，回退到易楼帮首页
			$('.go_back').attr('href', 'javascript:app.href("ylb_index");');
		}
		threadId = search.goid || search.id;
		page.init();
	}

	util.tpl.format.regionsFormat = function() {
		var obj = arguments[arguments.length - 1];
		return obj.regions.join('、');
	}
	util.tpl.format.typeFormat = function() {
		var obj = arguments[arguments.length - 1];
		return typeMap[obj.type];
	}
	util.tpl.format.posterNameFormat = function() {
			var obj = arguments[arguments.length - 1];
			return obj.cn_username + obj.en_username;
		} //phoneHide
	util.tpl.format.phoneHide = function() {
			var obj = arguments[0];
			return obj.substring(0, 3) + "****" + obj.substring(6, 10);
		}
		//params = 'id=70'
	if(params) {
		pageInit();
	} else {
		window.afterParams = pageInit;
	}

});

function getRpleyName(name) {
	var current_str = $('#reply_content').val();
	if(!current_str.endWith('@' + name)) {
		$('#reply_content').val($('#reply_content').val() + '@' + name);
	}
}
String.prototype.endWith = function(endStr) {
	var d = this.length - endStr.length;
	return(d >= 0 && this.lastIndexOf(endStr) == d)
}

// /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)