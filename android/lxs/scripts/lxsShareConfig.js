var util = {};
(function(){
    var rootPath = typeof appRoot === 'undefined' ? '' : appRoot;
    var filePath = typeof appPath === 'undefined' ? rootPath + '/lxs/scripts/' : rootPath;
    var files;
    if(navigator.userAgent.indexOf('MSIE') == -1){
        files = [
            rootPath + '/resourse/scripts/jquery-1.8.3.js',
            rootPath + '/resourse/scripts/select2.min.js',
            rootPath + '/resourse/scripts/event.js',
            rootPath + '/resourse/scripts/touch.js',
            rootPath + '/resourse/scripts/iscroll.js',
            rootPath + '/resourse/scripts/mobilebone.js',
            rootPath + '/resourse/scripts/iSlider.js',
            rootPath + '/resourse/scripts/iSlider.plugin.zoompic.js',
            rootPath + '/resourse/scripts/common.js'
        ];
    }else{
        files = [
            rootPath + '/resourse/scripts/jquery-1.8.3.js',
            rootPath + '/resourse/scripts/common.js'
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
    util.jss = webLoaded;
})();