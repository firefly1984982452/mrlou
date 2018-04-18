var util = {};
(function(){
    var rootPath = typeof appRoot === 'undefined' ? '' : appRoot;
    var filePath = typeof appPath === 'undefined' ? rootPath + 'lxs_scripts_' : rootPath;
    var files;
    if(navigator.userAgent.indexOf('MSIE') == -1){
        files = [
            rootPath + 'resourse_scripts_jquery-1.8.3.js',
            rootPath + 'resourse_scripts_select2.min.js',
            rootPath + 'resourse_scripts_event.js',
            rootPath + 'resourse_scripts_touch.js',
            rootPath + 'resourse_scripts_iscroll.js',
            rootPath + 'resourse_scripts_mobilebone.js',
            rootPath + 'resourse_scripts_common.js'
        ];
    }else{
        files = [
            rootPath + 'resourse_scripts_jquery-1.8.3.js',
            rootPath + 'resourse_scripts_common.js'
        ];
    }
    var reg = /\/([\w_]+)\.html/;
    var match = location.pathname.match(reg);
    var webLoaded = [];
    if(match){
        files.push(filePath + 'lxsInterFaces.js');
        webLoaded.push(filePath + match[1]+'.js');
    }
    var scripts = document.getElementsByTagName('script');
    var self = scripts[scripts.length - 1];
    var jss = self.getAttribute('jss');
    if(jss){
        var arr = jss.split(',');
        [].push.apply(webLoaded,arr);
    }
    for(var i=0;i<files.length;i++){
        document.write('<script src="'+files[i]+'"></script>');
    }
    document.write('<script src="lxs_scripts_swiper.min.js"></script>');
    util.jss = webLoaded;
})();