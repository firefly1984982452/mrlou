var util = {};
(function(){
    var rootPath = typeof appRoot === 'undefined' ? '' : appRoot;
    var filePath = typeof appPath === 'undefined' ? rootPath + 'lxs_scripts_' : rootPath;
    var files = [
        rootPath + 'resourse_scripts_jquery-1.8.3.js',
        rootPath + 'resourse_scripts_select2.min.js',
        rootPath + 'resourse_scripts_event.js',
        rootPath + 'resourse_scripts_touch.js',
        rootPath + 'resourse_scripts_iscroll.js',
        rootPath + 'resourse_scripts_mobilebone.js',
        rootPath + 'resourse_scripts_iSlider.js',
        rootPath + 'resourse_scripts_iSlider.plugin.zoompic.js',
        rootPath + 'resourse_scripts_common.js'
    ];
    var reg = /\/([\w_]+)\.html/;
    var match = location.pathname.match(reg);
    var webLoaded = [];
    if(match){
        files.push(filePath + 'lxsInterFaces.js');
        webLoaded.push(filePath + match[1]+'.js?'+(new Date()).getTime());
    }
    if(location.href.indexOf('lxs_share_buliding2') != -1){
        webLoaded.push(rootPath + 'lxs_scripts_swiper.min.js');
    }
    if(location.href.indexOf('lxs_share_room2') != -1){
        webLoaded.push(rootPath + 'lxs_scripts_swiper.min.js');
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