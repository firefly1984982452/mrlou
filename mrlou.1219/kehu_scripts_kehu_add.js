KEHU(function(ajax){
    var priceMap = {
        '1' : '元/m²/天',
        '2' : '元/m²'
    }
    var page = {
        pageIndex : 1,
        pageSize : 20,
        init : function(){
            var that = this;
            !that.inited && that.bindAction();
        },
        bindAction : function(){
            var that = this;
            that.scroller = new iScroll("add_form",{
                hScrollbar: false,
                vScrollbar: false,
                hideScrollbar: true
            });
            $('input[name=trade]').change(function(){
                if(this.checked){
                    $('#price_unit').html(priceMap[this.value]);
                }
            })
            $('#first_access_time').date();
            var posting = false;
            $('#save').click(function(){
                if(posting)  return;
                posting = true;
                var formData = that.getFormData();
                if(formData === false) {
                    posting = false;
                    return;
                }
                ajax.createCustomer(formData,function(data){
                    posting = false;
                    alert('客户创建成功',function(){
                        app.href('kehu_index.html',{
                            back : 'index'
                        })
                    })
                })
            })
            $('#pre_square_meter,#pre_price_1').keydown(function(e){
                var c = e.keyCode;
                var val = this.value;
                if(c == 8)  return;
                if(!(c == 190 || c >=48 && c<=57)){
                    e.preventDefault();
                    return;
                }else{
                    var arr = val.split('.');
                    if(arr[1] !== undefined && arr[1].length >=2){
                        e.preventDefault();
                    }
                }
                if(c == 190 && val.indexOf('.') != -1){
                    e.preventDefault();
                }
            })
            that.inited = true;
        },
        getFormData : function(){
            var data = {};
            data.name = $('#name').val();
            if(data.name.length < 2 || data.name.length > 10){
                alert('客户名称为2~10个字符长');
                return false;
            }
            data.transaction_type = $('input[name=trade]:checked').val();
            data.first_access_time = $('#first_access_time').val();
            data.pre_square_meter = $('#pre_square_meter').val();
            data.sector = $('#sector').val();
            if(data.sector.length && (data.sector.length > 10)){
                alert('行业类别最长10个字符长');
                return false;
            }
            data.pre_room = $('#pre_room').val();
            if(data.pre_room.length && (data.pre_room.length > 200)){
                alert('意向房源为最长200个字符长');
                return false;
            }
            data.pre_price_1 = $('#pre_price_1').val();
            data.pre_price_unit_1 = priceMap[data.transaction_type];
            data.intro = $('#intro').val();
            if(data.intro && data.intro.length > 200){
                alert('客户简介最多为200个字符');
                return false;
            }
            return data;
        },
        
    }
    
    page.init();
    var totalWidth = document.documentElement.clientWidth - 20;
    
    util.tpl.format.threadTitleSplit = function(){
        var obj = arguments[arguments.length - 1];
        var title = obj.title , title = title.htmlEncode() , len = obj.title.length;
        var div = document.createElement('div');
        div.className = 'width_compute_helper';
        div.innerHTML = title;
        $('body').append(div);
        var width = div.clientWidth;
        if(width < totalWidth * 2 - 30) {
            div.parentNode.removeChild(div);
            return title;
        }
        var i = 0;
        while(width > totalWidth * 2 - 30){
            div.innerHTML = title.substr(0,len + i);
            width = div.clientWidth;
            i --;
        }
        div.parentNode.removeChild(div);
        return title.substr(0 , len + i) + (i == 0 ? '' : '……');
    }
    var statusClassMap = ['looking','looking','want','dealed','disabled'];
    util.tpl.format.statusClassFormat = function(p){
        return statusClassMap[p];
    }
})
