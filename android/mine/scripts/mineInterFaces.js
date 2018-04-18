(function(){
    var _ajax = {
        getCollection : function(type,page,pageSize,success,fail){
            var url = 'api/collectionindex';
            var data = {
                type : type,
                page : page,
                per_page : pageSize
            }
            ajax(url,data,success,fail,'get');
        },
        uncollect : function(type,id,success,fail){
            var url = 'api/uncollect';
            var data = {
                type : type,
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        changeThreadStatus : function(status,id,success,fail){
            var url = 'api/threadstatus';
            var data = {
                status : status,
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        getUserInfo : function(success,fail){
            var url = 'api/user';
            ajax(url,{},success,fail,'get');
        },
        getRegionList : function(success,fail){
            var url = 'api/regionlist';
            ajax(url,{},success,fail);
        },
        modifyUserInfo : function(data,success,fail){
            var url = 'api/user';
            ajax(url,data,success,fail);
        },
        searchBulidingByKeyword : function(keyword,success,fail){
            var url = 'api/buildingtip';
            var data = {
                key : keyword
            }
            ajax(url,data,success,fail,'get');
        },
        getAllSelectOption : function(success,fail){
            var url = 'api/buildingoptions';
            ajax(url,{},success,fail);
        },
        searchCompanyByKeyword : function(keyword,success,fail){
            var url = 'api/companytip';
            var data = {
                key : keyword
            }
            ajax(url,data,success,fail,'get');
        }
    };
    window.MINE = function(fn){
        fn(_ajax);
    }
    $(function(){
        util.require.apply(util,util.jss)
        // location.href = 'http://obj@getuserinfo';
    })
    // window.afterGetUserInfo = function(){
        // util.require.apply(util,util.jss);
    // }
})();