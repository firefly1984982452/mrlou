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
    function pageInit(){
	    search = util.parseURI(params);
    	if (localStorage.getItem('imageType')=='room') {
	        views.roomid = localStorage.getItem('buildImageId');
    	}else{
	        views.buildid = localStorage.getItem('buildImageId');
    	}
	        views.init();
    }
    if(params || params==null || params==""){
        pageInit();
    }else{
        window.afterParams = pageInit;
    }
});


