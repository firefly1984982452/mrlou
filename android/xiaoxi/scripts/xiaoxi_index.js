XIAOXI(function(ajax){
    var page = {
        init : function(){
            this.getEventHomeData();
        },
        getEventHomeData : function(){
            
            var that = this;
            ajax.eventHome(function(data){
                that.createEventHome(data.list);
            })
        },
        createEventHome : function(list){
            var today = (new Date()).format('YYYY-MM-DD');
            for(var i=0;i<list.length;i++){
                if(list[i].event.id){
                    var data = list[i].event;
                    var notifyTime = data.notify_time;
                    var eventDay = notifyTime.toDate('YYYY-MM-DD');
                    $('#event_time'+list[i].type).html(eventDay == today ? notifyTime.toDate('hh-mm') : notifyTime.toDate('YYYY-MM-DD hh:mm'));
                    $('#event_content'+list[i].type).html(data.content);
                    // $('#un_read_'+list[i].type).html('99+');
                    if(list[i].total != '0'){
                        $('#un_read_'+list[i].type).html(list[i].total >= 100 ? '99+' : list[i].total);
                        $('#un_read_'+list[i].type).show();
                    }else{
                        $('#un_read_'+list[i].type).hide();
                    }
                }
            }
        }
    }
    
    page.init();
    window.goMsgList = function(type){
        var num = parseInt($('#un_read_'+type).html());
        $('#un_read_'+type).html('');
        $('#un_read_'+type).hide();
        var total = parseInt(util.getLocal('msg') || '0');
        util.setLocal('msg',Math.max(0,total - num));
        app.href('xiaoxi_list','type=' + type);
    }
});

