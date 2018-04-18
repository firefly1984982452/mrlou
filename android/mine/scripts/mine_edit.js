MINE(function(ajax){
    var formTplMap = {
        '1' : 'form1',
        '2' : 'form1',
        '3' : 'form1',
        '102' : 'form2',
        '201' : '',
        '299' : ''
    }
    var typeStrMap = {
        '1' : '开发商',
        '2' : '代理商',
        '3' : '运营商',
        '102' : '经纪人',
        '201' : '小业主',
        '299' : '其他'
    }
    var workAgeMap = {
        '1' : '1年以下',
        '2' : '1~3年',
        '3' : '3~5年',
        '4' : '5~10年',
        '5' : '10年以上'
    }
    var typeArr = [
        {id : 1 , name : '开发商'},
        {id : 2 , name : '代理商'},
        {id : 3 , name : '运营商'},
        {id : 102 , name : '经纪人'},
        {id : 201 , name : '小业主'},
        {id : 299 , name : '其他'},
    ]
    var regionCache = [];
    var page = {
        init : function(){
            !this.inited && this.globalBind();
            this.getUserInfo();
        },
        globalBind : function(){
            var that = this;
            that.scroller = new IScroll("#scroller1",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            that.selectScroller = new IScroll("#select_scroller",{
                mouseWheel: true,
                click: true,
                useTransition: false,
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            
            $('#modify_submit').click(function(){
                var data = that.getFormData();
                if(data !== false){
                    ajax.modifyUserInfo(data,function(){
                        alert('修改成功');
                    })
                }
            })
            $('#logout').click(function(){
                confirm('确定要退出当前账号吗？',function(){
                    that.createForm({"phone_number":"","email":"","en_username":"","cn_username":"","type":"299"});
                    app.logOut();
                    //app.href('lxs_index.html');
                })
            });
            
            $('#select_submit').click(function(){
                var name = [] , ids = [];
                if(that.selecting == 'region'){
                    var tar = $('#go_select_region');
                    $('input[name='+that.selecting+']').each(function(){
                        if(this.checked){
                            name.push(_cache[this.value].name);
                            ids.push(this.value);
                        }
                    })
                    if(name.length){
                        tar.find('p').html(name.join('，'));
                        tar.attr('vals',ids.join(','));
                    }else{
                        tar.find('p').html('请选择');
                        tar.attr('vals','');
                    }
                }
                that.selecting = undefined;
            })
            this.inited = true;
        },
        getUserInfo : function(){
            var that = this;
            ajax.getUserInfo(function(data){
                that.createForm(data.user);
                $('#user_avatar').attr('src',data.user.avatar);
                $('#user_avatar').attr('imgid',data.user.avatar_id);
            })
        },
        createForm : function(user){
            var that = this;
            var userType = user.type;
            var tpl = formTplMap[userType];
            var html = '';
            html += util.tpl.apply('public_form',user);
            if(tpl){
                html += util.tpl.apply(tpl,user);
            }
            $('#prop_con').html(html);
            that.scroller && that.scroller.refresh();
            // 日期
            $('#work_start').date();
            // 擅长区域
            $('#go_select_region').bind('click',function(){
                $('#select_title').html('选择区域');
                that.selecting = 'region';
                that.createRegion();
            });
            // 身份类别
            // $('#go_select_type').bind('click',function(){
                // $('#select_title').html('选择区域');
                // that.selecting = 'type';
                // that.createType();
            // });
            
            // 楼盘名称查找
            var buildingFilter;
            
            $(document).on('focus','#building_name',function(){
                if(buildingFilter) return;
                var filter = companyFilter = new Filter(document.getElementById('building_name'),{
                    top : $('#building_name').offset().top + 8,
                    left : $('#building_name').offset().left - 1,
                    // minWidth : $('#formName').width() + ($.browser.mozilla || $.browser.msie ? 5 : 4) ,
                    width : $('#building_name')[0].clientWidth - 1,
                    className : 'search_buliding_option2',
                    height : 300,
                    ajax : true,
                    ajaxFn : function(fn){
                        var keyword = $('#building_name').val();
                        if(keyword == '') {
                            filter.destroy();
                            return;
                        }
                        ajax.searchBulidingByKeyword(keyword,function(data){
                            fn(data.list);
                        })
                    },
                    showFilted : function(result,con,fo){
                        var len = result.length;
                        for(var i=0;i<len;i++){
                            var a = document.createElement('a');
                            a.innerHTML = result[i];
                            a.className = 'search_buliding_option2';
                            con.appendChild(a);
                        }
                    },
                    selectFn : function(obj,ipt,fo){
                        ipt.value = obj;
                        filter.destroy();
                        return false;
                    },
                    noResult : function(){
                        filter.dom.con.innerHTML = '<p style="padding:10px 0;text-align:center;font-size:14px;color:#444;">没有搜索到结果</p>';
                    }
                })
            })
            // 公司名称查找
            var companyFilter;
            $(document).on('focus','#company',function(){
                if(buildingFilter) return;
                var filter = buildingFilter = new Filter(document.getElementById('company'),{
                    top : $('#company').offset().top + 8,
                    left : $('#company').offset().left - 1,
                    // minWidth : $('#formName').width() + ($.browser.mozilla || $.browser.msie ? 5 : 4) ,
                    width : $('#company')[0].clientWidth - 1,
                    className : 'search_buliding_option2',
                    height : 300,
                    ajax : true,
                    ajaxFn : function(fn){
                        var keyword = $('#company').val();
                        if(keyword == '') {
                            filter.destroy();
                            return;
                        }
                        ajax.searchCompanyByKeyword(keyword,function(data){
                            fn(data.list);
                        })
                    },
                    showFilted : function(result,con,fo){
                        var len = result.length;
                        for(var i=0;i<len;i++){
                            var a = document.createElement('a');
                            a.innerHTML = result[i];
                            a.className = 'search_buliding_option2';
                            con.appendChild(a);
                        }
                    },
                    selectFn : function(obj,ipt,fo){
                        ipt.value = obj;
                        filter.destroy();
                        return false;
                    },
                    noResult : function(){
                        filter.dom.con.innerHTML = '<p style="padding:10px 0;text-align:center;font-size:14px;color:#444;">没有搜索到结果</p>';
                    }
                })
            })
        },
        getFormData : function(){
            var data = {};
            var avatar = $('#user_avatar').attr('imgid');
            data.avatar = avatar;
            var zhName = $('#zh_name').val();
            if(zhName.length <2 || zhName.length > 10){
                alert('中文名长度必须在2~10之间');
                return false;
            }
            data.cn_username = zhName;
            var enName = $('#en_name').val();
            var enReg = /[^\w\s]/;
            if(enReg.test(enName)){
                alert('英文名只能由字母、数字、空格组成');
                return false;
            }
            data.en_username = enName;

            data.email = $('#email').val();
            data.company = $('#company').val();
            data.building = $('#building_name').val();
            data.intro = $('#user_desc').val();
            data.work_start = $('#work_start').val();
            
            data.region_id = $('#go_select_region').attr('vals')
            data.from = 'edit';
            
            return data;
        },
        createType : function(){
            var that = this;
            var html = '<div class="areas">';
            $('#select_title').html('选择身份');
            for(var i=0;i<typeArr.length;i++){
                var temp = {
                    keyName : 'type',
                    id : typeArr[i].id,
                    name : typeArr[i].name
                }
                html += util.tpl.apply('radio_option',temp);
            }
            html += '</div>'
            $('#options').html(html);
            that.selectScroller.refresh();
            var type = $('#go_select_type').attr('vals');
            $('input[name=type]').each(function(){
                if(type == this.value) this.checked = true;
            })
        },
        createRegion : function(){
            var that = this;
            var html = '<div class="areas">';
            $('#select_title').html('选择擅长区域');
            for(var i=0;i<regionCache.length;i++){
                var temp = {
                    keyName : 'region',
                    id : regionCache[i].id,
                    name : regionCache[i].name
                }
                html += util.tpl.apply('select_option',temp);
            }
            html += '</div>'
            $('#options').html(html);
            setTimeout(function(){
                that.selectScroller.refresh();
            },300);
            
            var region = $('#go_select_region').attr('vals');
            region = region.split(',');
            var map = {};
            for(var i=0;i<region.length;i++){
                map[region[i]] = 1;
            }
            $('input[name=region]').each(function(){
                if(map[this.value]) this.checked = true;
            })
            
            var checkboxs = $('input[name=region]');
            checkboxs.change(function(){
                if($('input[name=region]:checked').size() >= 4){
                    alert('只能选择3个区域');
                    this.checked = false;
                    return false;
                }
            });
        }
    }
    // UserInfoCallBack('1r8ucmvb066ipvd651mfv62cd4','3','102')
    // console.log(util.userData);
    // util.userData.typeId = 102;
    // location.href = 'getuserinfo';
    var _cache = {};
    ajax.getAllSelectOption(function(data){
        regionCache = data.region_plate;
        for(var i=0;i<regionCache.length;i++){
            _cache[regionCache[i].id] = regionCache[i];
        }
        page.init();
    })
    util.tpl.format.workAgeFormat = function(){
        var obj = arguments[arguments.length - 1];
        var workAge = obj.work_age;
        return workAgeMap[workAge];
    }
    util.tpl.format.regionFormat = function(){
        var obj = arguments[arguments.length - 1];
        // obj.region_id = obj.region_id || [];
        // obj.region_id_1 && obj.region_id.push(obj.region_id_1);
        // obj.region_id_2 && obj.region_id.push(obj.region_id_2);
        // obj.region_id_3 && obj.region_id.push(obj.region_id_3);
        // var regions = [] , len = obj.region_id.length;
        // for(var i=0;i<len;i++){
            // regions.push(regionCache[obj.region_id[i]]);
        // }
        return obj.region_id.length ? obj.region_id.join('，') : '请选择';
    }
    util.tpl.format.regionIdFormat = function(){
        var obj = arguments[arguments.length - 1];
        return obj.region_id_real.join(',');
    }
    
    util.tpl.format.typeFormat = function(){
        var obj = arguments[arguments.length - 1];
        return typeStrMap[obj.type];
    }
    var uploadCallBack = function(id,url){
        $('#user_avatar').attr('src',url);
        $('#user_avatar').attr('imgid',id);
    }
    window.uploadCallBack = uploadCallBack;

    window.logoutCallBack = function(){
        // alert('logoutCallBack 被调用，准备打开登陆页',function(){
            // app.openLogin();
        // })
        app.openLogin();
    }
})
