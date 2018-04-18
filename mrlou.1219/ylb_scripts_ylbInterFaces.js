(function(){
    var _ajax = {
        getThreadList : function(page,pageSize,success,fail){
            var url = 'api/threadindex';
            var params = {
                page : page,
                per_page : pageSize
            }
            ajax(url,params,success,fail);
        },
        getUserInfo : function(success,fail){
            var url = 'api/users';
            ajax(url,{},success,fail,'get');
        },
        postThread : function(type,title,phone,content,region,avatar_id,success,fail){
            var url = 'api/threadcreate';
            var data = {
                subject : type,
                title : title,
                phone : phone,
                content : content,
                region : region,
                avatar_id : avatar_id
            }
            ajax(url,data,success,fail);
        },
        postReply : function(id,content,success,fail){
            var url = 'api/replycreate';
            var data = {
                thread_id : id,
                content : content
            }
            ajax(url,data,success,fail);
        },
        getThreadDetail : function(id,success,fail){
            var url = 'api/thread';
            var data = {
                id : id
            }
            ajax(url,data,success,fail,'get');
        },
        collectThread : function(id,success,fail){
            var url = 'api/collect';
            var data = {
                id : id,
                type : 4
            }
            ajax(url,data,success,fail);
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
    window.YLB = function(fn){
        fn(_ajax);
    }
    $(function(){
        util.require.apply(util,util.jss)
        // location.href = 'https://obj@getuserinfo';
    })
    // window.afterGetUserInfo = function(){
        // util.require.apply(util,util.jss);
    // }
})();