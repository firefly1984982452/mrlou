LXS(function(ajax){
    var cache = {};
    var page = {
        pageIndex : 1,
        regions : [],
        pageSize : 20,
        init : function(){
            !this.inited && this.bindAction();
            this.queryConsultant();
            this.getRegions();
        },
        bindAction : function(){
            var that = this;
            $('.search')[0].ontouchstart = function(e){
                e.preventDefault();
            }
            var clickTime;
            $('.consultant_phone').live('click',function(e){
                e.preventDefault();
                if(clickTime) return;
                clickTime = 1;
                location.href = 'tel:'+ this.innerHTML;
                setTimeout(function(){
                    clickTime = undefined;
                },500);
            })
            $('#search_agent').click(function(){
                goSearchAgent();
            })
            $('#go_select_region').click(function(){
                that.createRegions();
            })
            $('#keyword').blur(function(){
                $('#search .content').show();
            })
            $('#keyword').keydown(function(e){
                if(e.keyCode == 13 ){
                    goSearchAgent();
                }
            })
            function goSearchAgent(){
                Mobilebone.transition($('#index')[0],$('#search')[0],true);
                that.pageIndex = 1;
                filterInited && filterInited.destroy();
                that.queryConsultant(true);
            }
            
            $('#search_go_back').click(function(){
                that.pageIndex = 1;
                $('#keyword').val('');
                $('#go_select_region p').html(name.length ? name.join('，') : '全部');
                $('#go_select_region').attr('vals','');
                that.queryConsultant(true);
            })
            
    		
            var filterInited = false;
            // var hasResult = true;
            $('#keyword').focus(function(){
                $('#search .content').hide();
                // hasResult = true;
                if(filterInited) return;
                var filter = filterInited = new Filter(document.getElementById('keyword'),{
                    top : $('.search_warper').offset().top + 8,
                    left : $('.search_warper').offset().left - 1,
                    // minWidth : $('#formName').width() + ($.browser.mozilla || $.browser.msie ? 5 : 4) ,
                    width : $('.search_warper')[0].clientWidth - 1,
                    className : 'search_buliding_option',
                    height : 300,
                    ajax : true,
                    ajaxFn : function(fn){
                        var keyword = $.trim($('#keyword').val());
                        if(keyword == '') {
                            filter.destroy();
                            return;
                        }
                        ajax.searchAgent(keyword,function(data){
                            if($('#search').is(":hidden")){
                                filter.destroy();
                                return;
                            }
                            fn(data.list);
                        })
                    },
                    showFilted : function(result,con,fo){
                        var len = result.length;
                        for(var i=0;i<len;i++){
                            var a = document.createElement('a');
                            var name = document.createElement('p');
                            name.innerHTML = [result[i].cn_username , result[i].en_username , result[i].phone ,result[i].company].join(' ');
                            name.className = '_name';
                            // var address = document.createElement('p');
                            // address.innerHTML = result[i].address;
                            // address.className = '_address'
                            a.appendChild(name);
                            // a.appendChild(address);
                            a.className = 'search_buliding_option';
                            con.appendChild(a);
                        }
                    },
                    selectFn : function(obj,ipt,fo){
                        ipt.value = obj.cn_username;
                        goSearchAgent();
                        return false;
                    },
                    noResult : function(){
                        if($('#search').is(":hidden")){
                            filter.destroy();
                            return;
                        }
                        // hasResult = false;
                        filter.dom.con.innerHTML = '<p style="padding:10px 0;text-align:center;font-size:14px;color:#444;">没有搜索到结果</p>';
                    }
                })
            })
            
            $('#search_region_agent').click(function(){
                var tar = $('#go_select_region');
                var name = [], ids = [];
                $('input[name=region]').each(function(){
                    if(this.checked){
                        name.push(cache[this.value]);
                        ids.push(this.value);
                    }
                })
                tar.attr('vals',ids.join(','));
                that.regions = ids;
                $('#go_select_region p').html(name.length ? name.join('，') : '全部');
            })

            $('#select_all').change(function(){
                if(this.checked){
                    $('input[name=region]').attr('checked',false);
                }else{
                    if($('input[name=region]:checked').size() == 0){
                        this.checked = false;
                        return false;
                    }
                }
            })
            $('#reset_search').click(function(){
                $('#keyword').val('');
                $('#go_select_region p').html(name.length ? name.join('，') : '全部');
                $('#go_select_region').attr('vals','');
            })
            $('#search .top_banner a,#select .top_banner a').each(function(){
                this.ontouchstart = function(e){
                    e.preventDefault();
                }
            })
            that.inited = true;
        },
        queryConsultant : function(clear){
            var that = this;
            var keyword = $.trim($('#keyword').val());
           var recomServer_parm = {
                building_id:localStorage.getItem("building_id"),
                limit:that.pageSize,
                session_id:util.getLocal('sessionId')
           }
           ajax.getAgentList(recomServer_parm,function(data){
                clear && $('#consultants').html('');
                that.createConsultant(data.list);
           })
        },
        createConsultant : function(list){
               console.log(list.length);
            var that = this;
            var html = '';
            for(var i=0;i<list.length;i++){
                html += util.tpl.apply('item',list[i]);
            }
            var show_html = $(html).appendTo('#consultants');
           // $('#consultants').append(html);
            $('.consultant_phone').unbind().click(function(){
                var tel = $(this).html();
                var verify = $(this).attr('data-verify');
                ajax.telLog(tel,verify,function(){});
            })
            
            
    			$(".consultant_avatar").click(function(){
					var u = navigator.userAgent;
					var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
					var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
					var id = $(this).attr('fire');
					if(isAndroid) {
						var msg = window.mrlou.androidIs("1",id);
					} else if(isIos) {
						//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
						broker("1",id);
					}    			
    			})
    			
    			$(".company_color").click(function(){
    				var u = navigator.userAgent;
					var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
					var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
					var company_name = $(this).text();
					if(isAndroid) {
						var msg = window.mrlou.androidIs("2",company_name);
					} else if(isIos) {
						//iosPhone()这个方法，ios会自动监听，并接收我传过来的值，用msg接收它传给我的值
						broker("2",company_name);
//						alert("打印："+company_name);
					}
    			})
    		
    		
            
            for (var i=0;i<list.length;i++) {                
                //加V和公司红底
                if (list[i].v==1) {
                    $(show_html).find('.oval_v2').show(); //==      
                }
                if(list[i].v==0){
                    $(show_html).find('.oval_v2').hide();
                }

                if (list[i].company_status==1) {
                    // 红底
                     $(show_html).find('.company_color').css({'padding':'4px','border-radius':'2px','background':'#bd081c','color':'#fff',"opacity":1,"display":"inline"});
                }else if (list[i].company_status==0) {
                    // 不改变
                      $(show_html).find('.company_color').css({'background':'#fff'});
                }else {
                    alert("公司出错了");
                }
            }
            that.myScroll && that.myScroll.refresh();
        },
        getRegions : function(){
            var that = this;
            ajax.getRegionList(function(data){
                that.regionList = [];
                cache = data.list;
                for(var p in data.list){
                    that.regionList.push({
                        id : p ,
                        name : data.list[p]
                    })
                }
            })
        },
        createRegions : function(){
            //var that = this;
            if(that.created){
                var selected = $('#go_select_region').attr('vals');
                selected = selected ? selected.split(',') : [];
                var temp = {};
                for(var i=0;i<selected.length;i++){
                    temp[selected[i]] = 1;
                }
                // alert(selected.join(',') +'~~~' +JSON.stringify(temp))
                if(selected.length){
                    $('input[name=region]').each(function(){
                        if(temp[this.value]){
                            this.checked = true;
                        }
                    })
                    $('#select_all').attr('checked',false);
                }else{
                    $('input[name=region]').attr('checked',false);
                    $('#select_all').attr('checked','checked');
                }
                return;
            }
            that.created = true;
            var html = '<div class="areas">';
            var data = cache;
            var list = [];
            for(var i in data){
                if(data.hasOwnProperty(i)){
                    list.push({
                        id : i,
                        name : data[i]
                    })
                }
            }
            for(var i=0;i<list.length;i++){
                list[i].keyName = 'region';
                html += util.tpl.apply('select_option',list[i]);
            }
            html += '</div>';
            $('#options').html(html);
            $('input[name=region]').change(function(){
                if(this.checked){
                    $('#select_all').attr('checked',false);
                }else{
                    if($('input[name=region]:checked').size() == 0){
                        $('#select_all').attr('checked','checked');
                    }
                }
            })
            $('#select_all').attr('checked','checked');
            setTimeout(function(){
                page.selectScroller && page.selectScroller.refresh();
                $('#select_all').attr('checked','checked');
            },10);
        }
    }
    
    page.init();
    var timer;

    
    util.tpl.format.nameAssert = function(){
        var obj = arguments[arguments.length - 1];
        return obj.cn_username + obj.en_username;
    }
    // window.initRegionSelect = function(){
        // page.createRegions();
    // }

    util.tpl.format.workAgeFormat = function(workage){
         return "从业" + workage + "年";
    }
	
	util.tpl.format.goodAtFormat = function(){
		var obj = arguments[arguments.length - 1];
		return obj.regions.join('，');
	}
	
});




