(function(){
    var _ajax = {
        getSendsms : function(params,success,fail){
            var url = 'api/sendsms';
            ajax(url,params,success,fail);
        },
        getUserByMobilePhone : function(params,success,fail){
            var url = 'api/userByMobilePhone';
            ajax(url,params,success,fail);
        },
        searchBulidingByKeyword : function(keyword,success,fail){
            var url = 'api/buildingtip';
            var data = {
                key : keyword
            }
            ajax(url,data,success,fail,'get');
        }
    };
    window.REG = function(fn){
        fn(_ajax);
    }
    $(function(){
        util.require.apply(util,util.jss);
    })
})();