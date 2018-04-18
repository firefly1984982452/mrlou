(function(){
    var _ajax = {
        getPageBulids : function(params,success,fail){
            var url = 'api/roomindex';
            ajax(url,params,success,fail);
        },
        getRoominfo : function(params,success,fail){
            var url = 'api/roominfo';
            ajax(url,params,success,fail);
        },
        addWorklogcreate : function(params,success,fail){
            var url = 'api/worklogcreate';
            ajax(url,params,success,fail);
        },
        getAllSelectOption : function(success,fail){
            var url = 'api/buildingoptions';
            ajax(url,{},success,fail);
        },
        addRoomcreate : function(params,success,fail){
			var url = 'api/roomcreates';
            ajax(url,params,success,fail);
        },
        addRoomedit : function(params,success,fail){
			var url = 'api/roomedit';
            ajax(url,params,success,fail);
        },
        searchBulidingByKeyword : function(keyword,success,fail){
            var url = 'api/buildingtipaddr';
            var data = {
                key : keyword
            }
            ajax(url,data,success,fail,'get');
        },
        searchUnitByKeyword : function(keyword,success,fail){
            var url = 'api/unittip';
            var data = {
                key : keyword
            }
            ajax(url,data,success,fail,'get');
        },
        removeNote : function(id,success,fail){
            var url = 'api/worklogremove';
            var params = {
                id : id
            }
            ajax(url,params,success,fail,'get');
        },
        getUserInfo : function(success,fail){
            var url = 'api/users';
            ajax(url,{},success,fail,'get');
        }
    };
    window.FY = function(fn){
        fn(_ajax);
    }
    $(function(){
        // location.href = 'https://obj@getuserinfo';
        util.require.apply(util,util.jss);
    })
    // window.afterGetUserInfo = function(){
        // util.require.apply(util,util.jss);
    // }
})();