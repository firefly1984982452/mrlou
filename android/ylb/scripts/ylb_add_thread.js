YLB(function(ajax){
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
                    $("#areas").attr("placeholder","房源区域，合作佣金规则(如黄浦区，1个月)。");
                }
                if($(this).val()==2){
                    $("#areas").attr("placeholder","需求区域，合作佣金规则(如杨浦区，55合作)。");
                }
                if($(this).val()==3){
                    $("#areas").attr("placeholder","请输入其它备注信息");
                }
            });
            var posting = false;
            $('#post_thread').click(function(){
                if(posting)  return;
                posting = true;
                var title = $.trim($('#title').val());
                var content = $.trim($('#content').val());
                var remark = $.trim($("#remark").val());
                var type = $('input[name=type]:checked').val();
                var imgId = $('#thread_img').attr('imgId');
                var phone = $('#phone').val();
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
                if(content.length > 200){
                    alert('帖子内容最多200个字符');
                    posting = false;
                    return;
                }
                if(!remark){
                	switch (type){
                		case 1:
                    		alert('请输入房源区域，合作佣金规则内容');
                			break;
                		case 2:
                    		alert('请输入需求区域，合作佣金规则内容');
                			break;
                		case 3:
                    		alert('请输入其它备注信息内容');
                			break;
                		default:
                			break;
                	}
                    posting = false;
                    return;
                }
                if(remark.length > 20){
                    alert('请输入区域和合作佣金规则内容最多20个字符');
                    posting = false;
                    return;
                }
                ajax.postThread(type,title,phone,content,remark,imgId,function(data){
                    alert('发表成功。',function(){
                        app.href('ylb_index');
                        // location.href = 'http://obj@href@ylb_thread.html@id='+data.id+'&back=index';
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
        $('.img_list').show();
        $('#thread_img').attr('src',url);
        $('#thread_img').attr('imgId',id);
    }
    window.uploadCallBack = uploadCallBack;
});

