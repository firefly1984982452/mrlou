(function(){
    var _ajax = {
        eventHome : function(success,fail){
            var url = 'api/eventhome';
            ajax(url,{},success,fail,'get');
        },
        eventList : function(type,page,pageSize,success,fail){
            var url = 'api/eventlist';
            var data = {
                type : type,
                page : page,
                per_page : pageSize
            }
            ajax(url,data,success,fail,'get');
        },
        markRead : function(type,success,fail){
            
        }
    };
    window.XIAOXI = function(fn){
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