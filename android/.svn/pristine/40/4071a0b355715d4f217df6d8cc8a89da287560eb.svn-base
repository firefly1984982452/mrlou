LXS(function(ajax){
    var views = {
        init : function(){
            if(this.roomid){
                this.getRoomInfo();
            }
            if(this.buildid){
                this.getBuildingInfo();
            }
        },
        getBuildingInfo : function(){
            ajax.getBulidingDetail(this.buildid,function(data){
                imgView.init(data.data.images);
            })
        },
        getRoomInfo : function(){
            ajax.getRoomInfo(this.roomid,function(data){
                imgView.init(data.data.image_list);
            })
        }
    }
    
    var search;
    // params = 'buildid=31002914'
    function pageInit(){
        // appDebug(params);
        search = util.parseURI(params);
        views.roomid = search.roomid;
        views.buildid = search.buildid;
        views.init();
    }
    if(params){
        pageInit();
    }else{
        window.afterParams = pageInit;
    }
});


