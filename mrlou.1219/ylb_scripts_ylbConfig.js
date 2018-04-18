var util = {};
(function(){
    var rootPath = typeof appRoot === 'undefined' ? '' : appRoot;
    var filePath = typeof appPath === 'undefined' ? rootPath + 'ylb_scripts_' : rootPath;
    var files = [
        rootPath + 'resourse_scripts_jquery-1.8.3.js',
        rootPath + 'resourse_scripts_common.js',
        rootPath + 'resourse_scripts_select2.min.js',
        rootPath + 'resourse_scripts_event.js',
        rootPath + 'resourse_scripts_touch.js',
        rootPath + 'resourse_scripts_iscroll.js',
        // rootPath + 'resourse_scripts_mobilebone.js'
    ];
    var reg = /\/([\w_]+)\.html/;
    var match = location.pathname.match(reg);
    var webLoaded = [];
    if(match){
        files.push(filePath + 'ylbInterFaces.js');
        webLoaded.push(filePath + match[1]+'.js?'+(new Date()).getTime());
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