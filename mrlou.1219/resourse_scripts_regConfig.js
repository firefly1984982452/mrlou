var util = {};
(function(){
    var rootPath = typeof appRoot === 'undefined' ? '' : appRoot;
    var filePath = typeof appPath === 'undefined' ? rootPath + 'resourse_scripts_' : rootPath;
    var files = [
        rootPath + 'resourse_scripts_jquery-1.8.3.js',
        rootPath + 'resourse_scripts_event.js',
        rootPath + 'resourse_scripts_touch.js',
        rootPath + 'resourse_scripts_iscroll.js',
        rootPath + 'resourse_scripts_mobilebone.js',
        rootPath + 'resourse_scripts_date.js',
        rootPath + 'resourse_scripts_common.js'
    ];
    var reg = /\/([\w_]+)\.html/;
    var match = location.pathname.match(reg);
    var webLoaded = [];
    if(match){
        files.push(filePath + 'regInterFaces.js');
        webLoaded.push(filePath + match[1]+'.js');
        files.push(filePath + 'login.js');
    }
    var scripts = document.getElementsByTagName('script');
    var self = scripts[scripts.length - 1];
    var jss = self.getAttribute('js');
    if(jss){
        var arr = jss.split(',');
        [].push.apply(webLoaded,arr);
    }
	if(location.href.indexOf('register_step3') != -1){
		webLoaded.push(rootPath + 'resourse_scripts_reg.js');
	}
    for(var i=0;i<files.length;i++){
        document.write('<script src="'+files[i]+'"></script>');
    }
    util.jss = webLoaded;
})();