LXS(function(ajax){
	//所有的页面内容都在左部分和右部分
    var leftPart = $('#left_part');
    var rightPart = $('#right_part');
    var cache = {
        region : {},	//区域
        plate : {},		//模块
        status : {},	//交付列表
        metro : {},		//地铁列表
        type : {}		//类型列表
    }
    var regionList = [];
    var regionCache = {};
    var totalHeight = {
        left : 0,
        right : 0
    }
    var current;
    var loadedBuildingId = [];
//创建数据列表
        createBulids : function(list){
            var that = this;
            if(that.pageIndex == 1){
                totalHeight.left = 0;
                totalHeight.right = 0;
            }
            var target , temp;
            for(var i=0;i<list.length;i++){
                loadedBuildingId.push(list[i].id);
                target = totalHeight.left <= totalHeight.right? leftPart : rightPart;
                temp = $(util.tpl.apply('bulid_item',list[i]));
                //创建
                target.append(temp);
                if(totalHeight.left <= totalHeight.right){
                    totalHeight.left += temp.height();
                }else{
                    totalHeight.right += temp.height();
                }
                //class为buliding_link的所有图片的点击事件
                $(temp).find('.buliding_link').click(function(){
                    detail.id = $(this).attr('bid');
                    detail.init();
                })
            }
            that.myScroll && that.myScroll.refresh();
            that.firstPageLoaded = true;
        },
        
}