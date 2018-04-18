YLB(function(ajax){
    var uploading = 'images';
    var page = {
        init : function(){
            var that = this;
            !that.inited && that.bindAction();
            that.getUserInfo();
        },
        bindAction : function(){
            var that = this;
            $('#phone').keydown(function(e){
                if(e.keyCode >= 65 && e.keyCode <= 90){
                    return false;
                }
            });
            $('#del_img').click(function(){
                $('#thread_img').attr('imgId','');
                $('.img_list').hide();
            });
            $('#phone').keyup(function(e){
                var val = this.value;
                var reg = /[^\d-]/g;
                val = val.replace(reg,'');
                this.value = val;
            });
            $(':radio').click(function(event) {
                if($(this).val()==1){
                	$("#title").attr("placeholder","标题（房源所在区域+楼盘名称及概要信息，以及合作佣金规则。)");
                	$("#content").attr("placeholder","请输入房源的具体情况及交付等信息");
                    $("#remark").attr("placeholder","房源区域");
                    $("#markLine").show();
                    $("#region_title").text('房源所在区域');
                    
                }
                if($(this).val()==2){
                	$("#title").attr("placeholder","标题（客户求租/购区域需求等信息，以及合作佣金规则)");
                	$("#content").attr("placeholder","请输入客户的详细需求及行业等信息");
                    $("#remark").attr("placeholder","需求区域");
                    $("#markLine").show();
                    $("#region_title").text('客户需求区域');
                }
                if($(this).val()==3){
                	$("#title").attr("placeholder","请输入发布主题的标题信息");
                	$("#content").attr("placeholder","请输入发表内容");
                    $("#remark").attr("placeholder","请输入其它备注信息");
                    $("#markLine").hide();
                }
            });
            
            //点击进入区域页面
            $("#go_select_region").on('click',function(){
            	$(this).attr('href','#select');
            	$("#index").attr('class','page out');
            	$("#select").attr('class','page in');
            });
            
            //返回按钮
            $("#goIndex").on('click',function(){
            	$(this).attr('href','#index');
            	$("#select").attr('class','page out');
            	$("#index").attr('class','page in');
            });
            
            //提交按钮
            $("#submit_region").on('click',function(){
            	//存数据
            	var inputs = $("input[name='region']:checked");
				var content = $(inputs).parent().find(".checkbox_title").text();
				$("#server_con").text(content).attr("data-value",inputs.val());
				//返回
            	$(this).attr('href','#index');
            	$("#select").attr('class','page out');
            	$("#index").attr('class','page in');
            });
            
            			
            var posting = false;
            $('#post_thread').click(function(){
                if(posting)  return;
                posting = true;
                var avatar_id = [];
                var title = $.trim($('#title').val());
                var content = $.trim($('#content').val());
                var type = $('input[name=type]:checked').val();
                var region = $("#server_con").attr('data-value');
                var imgId = $('#thread_img').attr('imgId');
                var phone = $('#phone').val();
                 $('#other_imgs .img_del').each(function(){
                    avatar_id.push(this.getAttribute('imgId'));
                })
                avatar_id = avatar_id.join(',');
                if(!type){
                    alert('请选择帖子主题');
                    posting = false;
                    return;
                }
                if(!title){
                    alert('请输入帖子标题');
                    posting = false;
                    return;
                }
                if(title.length > 40){
                    alert('帖子标题最多为40个字符');
                    posting = false;
                    return;
                }
                if(!content){
                    alert('请输入帖子内容');
                    posting = false;
                    return;
                }
                if(content.length > 400){
                    alert('帖子内容最多400个字符');
                    posting = false;
                    return;
                }
                if(!region){
                    alert('请选择区域');
                    posting = false;
                    return;
                }
                if(region.length > 20){
                    alert('请输入区域和合作佣金规则内容最多20个字符');
                    posting = false;
                    return;
                }
                
	            $('.img_del').click(function(e){
	            	/*模板引擎里面的删除按钮点击事件*/
	                $(this).parent().remove();
	                if($('#other_imgs .img_span').size() < 10){
	                	/*如果其它图片里面的图片<10*/
	                    $('#add_image').show();
	                }
            	})
                ajax.postThread(type,title,phone,content,region,avatar_id,function(data){
                    alert('发表成功。',function(){
                        app.goBack();
                    });
                },function(data){
                    alert(data.msg);
                    posting = false;
                })
            })
            that.inited = true;
        },
        getUserInfo : function(){
            ajax.getUserInfo(function(data){
                $('#phone').val(data.user.phone_number);
            })
        }
    }
    page.init();
    
    var uploadCallBack = function(id,url){
        if(uploading == 'images'){
        	addImages(id,url);
        }
        /*$('.img_list').show();
        $('#thread_img').attr('src',url);
        $('#thread_img').attr('imgId',id);*/
    }
    
    function addImages(id,url){
        var html = util.tpl.apply('img',{
            imgType : 'images',
            avatar : url,
            id : id
        });
        var newIn = $(html);
        $('#add_image').before(newIn);
        if($('#other_imgs .img_span').size() >= 10){
            $('#add_image').hide();
        }
        $('#image .img_del').unbind().click(function(e){
            $(this).parent().remove();
            if($('#room_cover .img_span').size() == 0){
                $('#add_cover').show();
            }
            if($('#other_imgs .img_span').size() < 10){
                $('#add_image').show();
            }
        })
    }
    
    window.addImages = addImages;
    
    window.uploadCallBack = uploadCallBack;
});

