(function(){
    var _ajax = {
        getCustomerList : function(page,pageSize,success,fail){
            var url = 'api/customerindex';
            var params = {
                page : page,
                per_page : pageSize
            }
            ajax(url,params,success,fail,'get');
        },
        getCustomerInfo : function(data,success,fail){
            var url = 'api/customer';
            ajax(url,data,success,fail);
        },
        createCustomer : function(data,success,fail){
            var url = 'api/customercreate';
            ajax(url,data,success,fail);
        },
        addWorklogcreate : function(params,success,fail){
            var url = 'api/worklogcreate';
            ajax(url,params,success,fail);
        },
        removeNote : function(id,success,fail){
            var url = 'api/worklogremove';
            var params = {
                id : id
            }
            ajax(url,params,success,fail,'get');
        },
        customerdelete : function(params,success,fail){
            var url = 'api/customerdelete';
            ajax(url,params,success,fail);
        },
        modifyCustomer : function(params,success,fail){
            var url = 'api/customeredit';
            ajax(url,params,success,fail);
        }
    };
    window.KEHU = function(fn){
        fn(_ajax);
    }
    $(function(){
        util.require.apply(util,util.jss);
        // location.href = 'https://obj@getuserinfo';
    })
    // window.afterGetUserInfo = function(){
        // util.require.apply(util,util.jss);
    // }
})();