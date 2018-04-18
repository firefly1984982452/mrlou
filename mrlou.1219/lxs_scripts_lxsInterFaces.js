(function(){
    var _ajax = {
        getPageBulids : function(params,success,fail){
            var url = 'api/buildinglist';
            ajax(url,params,success,fail);
        },
        getRegionList : function(success,fail){
            var url = 'api/regionlist';
            ajax(url,{},success,fail);
        },
        getPlateList : function(success,fail){
            var url = 'api/platelist';
            ajax(url,{},success,fail);
        },
        getAllSelectOption : function(success,fail){
            var url = 'api/buildingoptions';
            ajax(url,{},success,fail);
        },
        bulidSearch : function(params,success,fail){
            var url = 'api/buildingsearch';
            ajax(url,params,success,fail);
            //
        },
        getBulidingDetail : function(id,success,fail){
            var url = 'api/building';
            var data = {
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        getBulidingBrandDetail : function(data,success,fail){
            var url = 'api/building';
            var data = {
                id : data.id,
                is_brand : data.is_brand,
                pp_id : data.pp_id,
                session_id :util.getLocal('sessionId')
            }
            ajax(url,data,success,fail,'get');
        },
        getShareBuildingDetail : function(id,success,fail){
            var url = 'api/sharebuilding';
            var data = {
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        getShareBuildingDetail2 : function(id,success,fail){
            var url = 'api/sharebuilding2';
            var data = {
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        getBulidingMoreInfo : function(id,success,fail){
            var url = 'api/buildingmore';
            var data = {
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        collectBuliding : function(id,success,fail){
            var url = 'api/collect';
            var data = {
                id : id,
                type : 2
            }
            ajax(url,data,success,fail,'get');
        },
        /*//这是他原来写的，获取经纪人，留之备份
          getAgentList : function(keyword,page,regions,per_page,success,fail){
            var url = 'api/agentlist';
            var data = {
                page : page,
                per_page : per_page
            }
            if(keyword) data.key = keyword;
            if(regions) data.regions = regions;
            ajax(url,data,success,fail,'get');
        },*/
       //自己写的，但是地址不行
         getAgentList : function(data,success,fail){
            var url = 'api/recommonagent';
            ajax(url,data,success,fail,'get');
        }, 
        getServerList : function(data,success,fail){
            var url = 'api/recommonservice';
            ajax(url,data,success,fail,'get');
        },
        getRoomInfo : function(id,success,fail){
            var url = 'api/room';
            var data = {
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        getShareRoomInfo : function(id,success,fail){
            var url = 'api/shareroom';
            var data = {
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        getShareRoomInfo2 : function(id,success,fail){
            var url = 'api/shareroom2';
            var data = {
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        collectRoom : function(id,success,fail){
            var url = 'api/collect';
            var data = {
                id : id,
                type : 3
            }
            ajax(url,data,success,fail,'get');
        },
        searchBulidingByKeyword : function(keyword,success,fail){
            var url = 'api/buildingtipaddr';
            var data = {
                key : keyword
            }
            ajax(url,data,success,fail,'get');
        },
        createShare : function(type,id,success,fail){
            var url = 'api/sharecreate';
            var data = {
                type : type ,
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        searchAgent : function(keyword,success,fail){
            var url = 'api/agenttip';
            var data = {
                key : keyword
            }
            ajax(url,data,success,fail,'get');
        },
        isLogin : function(success,fail){
            var url = 'api/islogin';
            ajax(url,{},success,fail,'get');
        },
        telLog : function(tel,verify,success,fail){
            var url = 'api/tellog';
            var data = {
                tel : tel,
                verify : verify
            }
            ajax(url,data,success,fail,'get');
        }
    };
    window.LXS = function(fn){
        fn(_ajax);
    }
    $(function(){
         // util.require.apply(util,util.jss);
        util.require.apply(util,util.jss)
       // location.href = 'https://obj@getuserinfo';
    })
    // window.afterGetUserInfo = function(){
        // util.require.apply(util,util.jss);
    // }
})();