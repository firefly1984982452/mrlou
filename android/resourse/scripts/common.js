Function.prototype.bind = function(){
	var a = Array.prototype.slice.call(arguments), m = this, o = a.shift();
	return function(){
		return m.apply(o == null ? this : o, a.concat( Array.prototype.slice.call(arguments)));
	}
};

String.prototype.trim = function(){
//	return this.replace(/^\s+/,"").replace(/\s\s*$/, "");
	return this.replace(/^\s+|\s+$/g, "");
};
/**
* 字符串转Date，若有format则返回Date格式化后的字符串
*/
String.prototype.toDate = function(format){
    if(this.toString()==='') return '';
    var str;
    if(this.indexOf('.') != -1){
        str = this.substring(0,this.indexOf('.'));
    }else{
        str = this.toString();
    }
    var reg = /-/g;
    str = str.replace(reg,'/');
    if(str.indexOf('T')){
        str = str.replace('T',' ');
    }
    if(format){
        return new Date(str).format(format);
    }else{
        return new Date(str);
    }
}
/**
 * 去除HTML编码 生成可以在html文档显示的字符
 */
String.prototype.htmlEncode = function(){
    // var reg = /&|<|>|'|"/g;
    // return this.replace(reg,'');
    return this.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/\"/g,"&#34;").replace(/\'/g,"&#39;");
    //return this.replace(/&/g,"&amp;").replace(/>/g,"&gt;").replace(/\"/g,"&#34;").replace(/\'/g,"&#39;");
};

/**
 * 返回字字符串的Unicode编码的长度
 */
String.prototype.uniLength = function(){
	return this.replace(/[^\x00-\xff]/g, "**").length;
};

//截取字符串中len长度的子字符串 Unicode编码
String.prototype.uniSub = function(start,len){
	var s = this.replace(/[*]/g, " ").replace(/[^\x00-\xff]/g, "**");
	len = s.substr(start, len).replace(/[*]{2}/g, " ").replace(/[*]{2}/g, "").length;
	start = s.slice(0, start).replace(/[*]{2}/g, " ").replace(/[*]{2}/g, "").length;
	return this.substr(start, len);
};
//截取左边 len长度的字符串  Unicode编码
String.prototype.uniLeft = function(len,omit){
	omit || (omit = "");
	return this.uniLength()>len?this.uniSub(0, len-omit.uniLength())+omit:this;
};

/**
 * 时间格式化
 YYYY  ： 四位年份
 YY      ： 二位年份
 MM    :  两位月份
 M       ： 月份
 DD      ： 日期两位
 D         ： 日期
 hh       ： 小时两位
 h         ： 小时
 mm      ： 分钟两位
 m         ： 分钟
 ss          ： 秒两位
 s          ：秒
 */
Date.prototype.format = function(join){
	return (join || "YYYY-MM-DD").replace(/YYYY/g,this.getFullYear())
		.replace(/YY/g,String(this.getYear()).slice(-2))
		.replace(/MM/g,("0" + (this.getMonth() + 1)).slice(-2))
		.replace(/M/g,this.getMonth() + 1)
		.replace(/DD/g,("0" + this.getDate()).slice(-2))
		.replace(/D/g,this.getDate())
		.replace(/hh/g,("0" + this.getHours()).slice(-2))
		.replace(/h/g,this.getHours())
		.replace(/mm/g,("0" + this.getMinutes()).slice(-2))
		.replace(/m/g,this.getMinutes())
		.replace(/ss/g,("0" + this.getSeconds()).slice(-2))
		.replace(/s/g,this.getSeconds());
};
//扩展
util = util || {};
void function(){
	//扩展object
	function extra(k,v){
		if(typeof k == "string"){
			this[k] = v;
			return this;
		}
		var ag = Array.prototype.slice.call(arguments), m,n;
		while(ag.length){
			m = ag.shift();
			//alert(m.onCensor);
			for(n in m){
				this[n] = m[n];
			}
		}
		return this;
	}
	//简单的对 最后一个参数的扩展（将最后一个参数前面的参数属性扩展到最后一个参数上)
	//例如：
	//var c = util.extra(a,b,{}); 产生一个c，将包含a和b的所有属性，但不对a和b产生印象
	util.extra = function(){
		return extra.apply(Array.prototype.pop.call(arguments),arguments);
	};

	//扩展this
	function extend(){
		var ag = Array.prototype.slice.call(arguments),m;
		if(typeof this == "function"){
			this.prototype.extend = extra;
			this._inits_ = [];
			while(ag.length){
				m = ag.shift();
				if(typeof m == "function"){
					extra.call(this,m);
					this._inits_.unshift(m);
					m = m.prototype;
				}
				extra.call(this.prototype,m);
			}
		}
		else{
			while(m = ag.shift()){
				if(typeof m == "function"){
					try{
						m = new m();
					}catch(e){
						m = m.prototype;
					}
				}
				extra.call(this,m);
			}
		}
		return this;
	}
	//继承与扩展
	//将前面的object扩展到最后一个函数的原型链上 用于类的继承
	//例如 ，下面讲到的 Ajax类就是继承了Event类
	util.extend = function(){
		return extend.apply(Array.prototype.pop.call(arguments),arguments);
	};
}();

void function(){
	function push(arr,v){
		arr.push(v);
		return v;
	}
	function append(obj,v,k){
		obj[k] = v;
	}
	function back(){
		return arguments[1];
	}

	//o 需要循环的 数组或者对象或者list对象（argumengs，nodeList对象）
	//fn 循环是执行的函数
	//exe fn返回的数据将写入exe
	//scope fn函数固定的this指向
	util.forEach = function(o, fn, exe, scope){
		if(scope == null){
			scope = this;
		}
		if(o){
			var doExe = exe? exe.push ? push : append : back,m;
			if(typeof o == "object" && typeof o.length == "number" && o.length >= 0){
				for(var i = 0; i < o.length; i += 1){
					m = fn.call(scope, o[i], i);
					if(m === false){
						break;
					}
					doExe( exe,m, i);
				}
			}
			else{
				for(var n in o){
					m = fn.call(scope, o[n], n);
					if(m === false){
						break;
					}
					doExe( exe,m, n);
				}
			}
		}
		return exe || scope;
	}
}();

void function(){
	var iData = {};
	function get(str){
		var data = {};
		util.forEach(str.replace(/^[\s#\?&]+/, "").replace(/&+/, "&").split(/&/),function(v){
			var s = v.split("=");
			if(s[0]!=""){
				s[1] = decodeURIComponent(s[1] || "");
				if(data[s[0]] == null){
					data[s[0]] = s[1];
				}
				else if(data[s[0]].push){
					data[s[0]].push(s[1]);
				}
				else{
					data[s[0]] = [data[s[0]], s[1]];
				}
			}
		});
		return data;
	}
	util.parseURI = function(str){
		if(iData[str]){
			return iData[str];
		}
		return iData[str] = get(str);
	};
	//获取页面？后面的参数
	//x.html?a=1&b=2
	//util.getSearch()  => {a:1,b:2}
	//util.getSearch("a") ==> 1
	util.getSearch = function(str){
		var o = util.parseURI(document.location.search);
		return str == null?o:o[str];
	};

	//将obj对象转换为url参数形式
	//obj = {a:1,b:2}
	//util.stringifyURI(obj)  => a=1&b=2
	//k 默认为 &
	util.stringifyURI = function(obj,k){
		if(!obj){
			return "";
		}
		var rv = [];
		util.forEach(obj,function(m,n){
			if(Object.prototype.toString.call(m) === "[object Array]"){
				for(var i=0;i<m.length;i+=1){
					rv.push(n + "=" + encodeURIComponent(m[i]));
				}
			}
			else{
				rv.push(n + "=" + encodeURIComponent(m));
			}
		});
		return rv.join(k || "&");
	};
}();

Math.times = function(){

    var _list = arguments,
        times = 1,
        result = 1;
    for(var i =0;i<_list.length;i++){
        var y = (_list[i]+'').split(".")[1];
        if(y){
            times *= Math.pow(10,y.length) ;
//            var _times = Math.pow(10,y.length);
//            result = (result * (_list[i] * _times));
            result = result * (_list[i].toString().split(".").join("") * 1);

        }else{
            result = (result * (_list[i]));
        }

    }

    return result/times;

}
/**
 * Math添加精确加法
 * x,y = number
 */
Math.plus  = function(){

    var _list = arguments,
        times = 1,
        result = 0;
    //先获取最大倍数
    var len = _list.length;
    for(var i=0;i<len;i++){
        var y = (_list[i]+'').split(".")[1];
        if(y && times < y.length){
            times = y.length;
        }
    }

    times = Math.pow(10,times) ;

    //整数计算
    for(var i=0;i<len;i++){

        result = (result + (_list[i] * times));

    }

    return result/times;

}


//使用页面模板类
/*
	getLocal("text/tpl") 会将html页面中 type为text/tpl的script节点中的字符串作为模板来使用
	如下是定义了模板为a
	<script type="text/tpl">
	<!--^a-->
		这里是模板A的数据
		{#*} 这个替换为传入参数
		{#x} 这个替换为传入参数的x属性值
		{#x.z} 这个替换为传入参数的x属性值的z属性值
		{#x|zz} 这个是将传入参数的x属性值作为第一个参数传入format中的zz函数
		{#x|zz(t)} t将作为zz函数的第二个参数值
	<!--a$-->
	</script>
	如下是使用模板
	apply("a",1);
	apply("a",{x:1});
	apply("a",{x:{z:2}}});
*/
void function(){
    util.system = util.system || {};
    util.system.tpl = util.system.tpl || {};
	//tempMReg 获取模板值
	//tempRRe    对模板中 {#...}进行替换操作
	var tempMReg = /<!--\s*\^(\w+)-->(.*)<!--\1\$\s*-->/g, tempRRe = /\{#(.*?)(?:\|([\w\$]+)(?:\((.*?)\))?)?\}/g;

	//获取obj中对应的值
	function getValue(obj,key){
		if(key == "*"){
			return obj;
		}
		var x = key.split(/[.]+/);
		while(obj != null && x.length){
			obj = obj[x.shift()];
		}
		return obj;
	}

	//模板类
	util.Template = util.extend({
		//获取页面中模板数据
		getLocal:function(type){
			var s = document.getElementsByTagName("script"), n, i = 0;
			if(type == null){
				type = "text/puitemplate";
			}
			for(; i < s.length; i += 1){
				if((n = s[i]).getAttribute("type") == type){
					this.setModel(n.innerHTML);
				}
			}
			for(; i < s.length; i += 1){
				if((n = s[i]).getAttribute("type") == type){
                    //多次调用 防止重复设置
					n.parentNode.removeChild(n);
				}
			}
			return this;
		},
		//将一个带有模板数据的字符串转换为模板集
		setModel : function(str){
			var v = str.replace(/^\s+|\s+$|\n+|\r+/g, "").replace(/>\s+</g, "><"), arr;
			while(arr = tempMReg.exec(v)){
				this.data[arr[1]] = arr[2];
			}
			return this;
		},
		//应用模板
		apply : function(mId, obj){
            var _x = this.data[mId] || util.system.tpl[mId];
            if(!_x) return '';
			else{
				var me = this,f = me.format;
                var cache = {};
				f._data_.push(obj);
                var i =0;
				var x = _x.replace(tempRRe, function(str, key, fun, parms){
                    if(cache[str]){
                        return cache[str];
                    }
					var v = getValue(obj,key),rv = v;
					if(fun && f[fun]){
                        v = [v].concat((parms || "").split(/,/));
                        v.push(obj);
						rv = f[fun].apply(me, v);
					}
					if(rv == null){
						rv = "";
					}
                    cache[str] = rv;
					return rv;
				});
                // console.log(i)
                cache = undefined;
				f._data_.pop();
				return x;
			}
//			return "";
		},
		//获取当前的apply下的全局数据
		//默认获取当前正在被替换的数据 obj
		get:function(n){
			var d = this.format._data_;
			if(n == null){
				n = -1;
			}
			if(n < 0){
				n = d.length + n;
			}
			return d[n];
		},
		//执行format中另外的函数
		call:function(){
			var ag = Array.prototype.slice.call(arguments),key = ag.shift();
			return this.format[key].apply(this,ag);
		}
	},function(){
		this.data = {};
		this.format = util.extra(util.Template.format,{_data_:[]});
	});

	function getLeft(v,left){
		left = parseInt(left) || 0;
		if(left){
			return v.uniLeft(left,"..");
		}
		return v;
	}

	util.Template.format = {
		//运用其他模板
		apply:function(obj, mId){
			return this.apply(mId, obj);
		},
		//循环数组 引用其他模板
		applyArray:function(arr, mId, split){
			return arr?D.forEach(arr,function(obj){
				return this.apply(mId, obj);
			}, [],this).join(split || ""):"";
		},
		//格式化字符串 使得次字符串执行innerHTML的时候，按照次字符串原本的内容显示
		htmlEncode:function(v,left){
			if(v == null){
				v = "";
			}
			return getLeft(String(v).htmlEncode(),left);
		},
		//格式化字符串，并将回车替换为<br />
		htmlEncodeBr:function(v,left){
			if(v == null){
				v = "";
			}
			return getLeft(String(v).htmlEncode().replace(/\n/g,"<br />"),left);
		},
		//格式化为页面节点的属性值 比如value值
		valueEncode:function(v,left){
			if(v == null){
				v = "";
			}
			return getLeft(String(v).replace(/\"/g,"&#34;").replace(/\'/g,"&#39;"),left);
		}
	};

    $(function(){
        util.tpl = new util.Template();
        util.tpl.getLocal("text/tpl");
		if (window.innerHeight != 0) {
			$('body, .page').height(window.innerHeight);
		}
		var h = window.innerHeight;
		var u = navigator.userAgent;
		var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
		if (isAndroid) {
			$(window).resize(function(){
				var b = window.innerHeight;
				$('body').css({
				    top:b-h+'px'
				});
			})
		}

    })
}();

var server = 'http://appapi.imrlou.com/';

function ajax(url, data, success, fail, type, closeLoading) {
    // if (!closeLoading) {
        // util.openLoad(0);
    // }
    type = type || 'POST';
    dataType = 'json';
    if(url == 'api/shareroom' || url == 'api/sharebuilding' || url == 'api/sharebuilding2' ||  url == 'api/shareroom2' ){
        type = 'get';
        dataType = 'jsonp';
    }
     var url_ ="";
    if(url.indexOf("http://test.imrlou.com/")>=	0){
    	url_ = url;
    }else{
    	url_ = server + url;
    }
    $.ajax({
        url: url_,
        type: type,
        data: data,
        dataType : dataType,
        success: function(d) {
            if(d.errorid == 0){
                success(d);
                var hasMessage = d.message ? parseInt(d.message) : 0;
                if(hasMessage){
                    $('.msg').addClass('new_msg');
                    $('.buliding_msg').addClass('new_msg1');
                    util.setLocal('msg',hasMessage);
                }else{
                    $('.msg').removeClass('new_msg');
                    $('.buliding_msg').removeClass('new_msg1');
                    util.removeLocal('msg');
                }
            }else if(d.errorid == 4030){
                app.openLogin();
            }else{
                if(fail){
                    fail(d);
                }else{
                    alert(d.msg);
                }
            }
        },
        error : function(){
            alert('请求失败，请稍后重试');
        },
        beforeSend: function(request) {
            var sessionId = util.getLocal('sessionId');
            var userId = util.getLocal('userId');
            if(sessionId){
                request.setRequestHeader('X-Session-Id', sessionId);
            } else {
				request.setRequestHeader('X-Session-Id', 'null');
			}
            if(userId){
                request.setRequestHeader('user_id', userId);
            } else {
				request.setRequestHeader('user_id', 'null');
			}
        },
        error: function(d) {}
    });
}


void function(){
	//已经加载完毕的js
	var jsLoaded = {},jsPath = "/res/script/";

	//进栈 操作
	function stackPush(urls, callBack, charset){

		callBack && this.backs.push(callBack);
		if(typeof urls == "string"){
			this.jss.push([urls, stackShift, charset]);
		}
		else{
			for(var i = 0; i < urls.length; i += 1){
				this.jss.push([urls[i], stackShift, charset]);
			}
		}
		//如果没有在进行中，启动出栈
		if(this.flag==0){
			this.flag = 1;
			stackShift.call(this);
		}
	}

	//出栈
	function stackShift(){
		//如果存在待加载的js，优先加载js
		if(this.jss.length){
			//使用 shift 将前面的js先加载
			disorderJS.apply(this, this.jss.shift());
			return ;
		}
		//没有等待加载的js的时候，才进行回调出栈操作
		if(this.backs.length){
			//使用pop，将靠后进入的先出栈执行
			this.backs.pop().call(this);
			stackShift.call(this);
			return ;
		}
		//没有js和回调出栈，设置为0，表示无操作
		this.flag = 0;
	}

	//加载script脚本
	function loadJS(url, callBack, charset){
		//alert(url);
        var _url = url;
        if(url.indexOf('?') == -1){
            if(util.config && util.config.version){
                _url = _url + '?' + util.config.version;
            }
        }
		var t = document.createElement("script");
		t.setAttribute("type", "text/javascript");
		charset && t.setAttribute("charset", charset);
		t.onreadystatechange = t.onload = t.onerror = function(){
			if(!t.readyState || t.readyState == 'loaded' || t.readyState == 'complete'){
				t.onreadystatechange = t.onload = t.onerror = null;
				t = null;
				//防止回调的时候，script还没执行完毕
				callBack && setTimeout(function(){
					callBack(url);
				}, 100);
			}
		};
		t.src = _url;
		document.getElementsByTagName("head")[0].appendChild(t);
	}
	//js加载完毕后调用
	function requireJSed(url){
		var x = jsLoaded[url];
		if(x && x!==true){
			for(var i=0;i<x.length;i+=1){
				x[i][0].call(x[i][1],x[i][2]);
			}
			jsLoaded[url] = true;
		}
	}
	//加载js
	function requireJS(src, callBack, charset){
		var url, self = this;
		//替换url为真是的地址
		//./打头的src，定位在path目录中
		url = src.replace(/^\.\//, this.path || jsPath);
		if(!/\.[^\/]+$/.test(url)){
			url += ".js";
		}
		//如果这个js已经加载完毕，直接延时调用回调函数
		if(jsLoaded[url] === true){
			setTimeout(function(){
				callBack.call(self, src);
			}, 100);
			return;
		}
		//如果这个js正在加载中，添加回调函数到回调数组中
		if(jsLoaded[url]){
			jsLoaded[url].push([callBack,self,src]);
		}
		//设置js加载完毕的回调数组
		jsLoaded[url] = [[callBack,self,src]];
		//加载js
		loadJS(url,requireJSed,charset);
	}

	//无序下载
	//多个js一起加载
	function disorderJS(urls, callBack, charset){
		//单个js，直接调用requireJS
		if(typeof urls == "string"){
			requireJS.call(this, urls, callBack, charset);
			return this;
		}
		//存放被加载的js的对象
		var led = {};

		function loadBack(src){
			//加载完成一个，就删除一个
			delete led[src];
			//led为空的时候，表示全部加载完成
			for(var n in led){
				return;
			}
			callBack.call(this);
			loadBack = function(){};
		}

		//分布加载js
		for(var i = 0; i < urls.length; i += 1){
			led[urls[i]] = true;
			requireJS.call(this, urls[i], loadBack, charset);
		}
		return this;
	}

	//异步加载js的类
	util.chain = util.extend({
		//进行异步加载js
		//最后两个参数为回调和加载js的字符集设置
		//例如 require("a.js","b.js",["c1.js","c2.js"],"d.js",[function(){}],[charset]);
		//其中 a b c d 按照序列加载
		//c1 c2将同时加载，加载完毕后，再加载d
		require:function(){
			var ag = Array.prototype.slice.call(arguments), l = ag.length;
			if(l == 1){
				stackPush.call(this, ag[0]);
				return this;
			}
			l -= 1;
			if(typeof ag[l] == "function"){
				stackPush.call(this, ag.slice(0, l), ag[l]);
				return this;
			}
			l -= 1;
			if(ag[l] == null || typeof ag[l] == "function"){
				stackPush.call(this, ag.slice(0, l), ag[l], ag[l + 1]);
				return this;
			}
			stackPush.call(this, ag);
			return this;
		}
	},function(path){

		this.flag = 1;
		this.jss = [];
		this.backs = [];
		this.path = path;
		//页面ready后，执行出栈操作
        var that  = this;
		$(function(){
			//出栈操作
			stackShift.call(that);
		});
	});
	var reone = new util.chain();
	//new一个js加载线
	util.require =  reone.require.bind(reone);

	//防止在重复加载（同步加载的，调用函数设置后，可以防止重复加载）
	util.required = function(src){
		jsLoaded[src] = true;
	};
	util.loadJS = loadJS;
}();



(function(){
    var blurTimer;
    var option = {
        ajax : false,
        className : 'option',
        selectClassName : 'option select',
        data : [],
        top : 0,
        left : 0,
        width : 100,
        height : 200,
        filter : function(data,ipt){
            console.log(arguments);
        },
        selectFn : function(obj,ipt){
            console.log(arguments);
        },
        showFilted : function(result,con){
            console.log(arguments);
        },
        noResult : function(){},
        ajaxFn : function(fn){},
        ajaxCallBack : function(){}
    }
    var body = document.getElementsByTagName('body')[0];
    function Filter(ipt,_option){
        this.ipt = ipt;
        this.option = {};
        for(var p in  option){
            this.option[p] = option[p];
        }
        for(var p in  _option){
            this.option[p] = _option[p];
        }
        var con = document.createElement('div');
        this.dom = {};
        this.dom.con = con;
        // con.onclick = function(){
            // if(blurTimer){
                // clearTimeout(blurTimer);
                // blurTimer = undefined;
            // }
        // }
        this.init();
        Filter.filters.push(this);
    }
    Filter.filters = [];
    document.onclick = function(){
        for(var i=0;i<Filter.filters.length;i++){
            Filter.filters[i].destroy();
        }
    }
    var fn = Filter.prototype;
    fn.init = function(){
        var opt = this.option , that = this;
        this.idx = -1;
        this.ipt.onclick = function(e){
            e = e || window.event;
            if(e.stopPropagation){
                e.stopPropagation();
            }else{
                e.cancelBubble = true;
            }
        }
        if(!opt.ajax){
            function showFilted(e){
                e = e || window.event;
                var result = opt.filter(opt.data,this,that) , len = result.length;
                if(len == 0){
                    that.destroy();
                }else{
                    if(!that.showing){
                        that.showCon();
                    }
                    if(e.keyCode == 38){
                        var change = 0;
                        that.idx --;
                        if(that.idx < 0){
                            that.idx = len - 1;
                        }
                        that.dom.con.scrollTop = that.dom.con.childNodes[that.idx].offsetTop - that.dom.con.childNodes[that.idx].offsetHeight;
                        that.setSelectClass();
                        return;
                    }
                    if(e.keyCode == 40){
                        that.idx ++;
                        if(that.idx >= len ){
                            that.idx = 0;
                        }
                        that.dom.con.scrollTop = that.dom.con.childNodes[that.idx].offsetTop - that.dom.con.childNodes[that.idx].offsetHeight;
                        that.setSelectClass();
                        return;
                    }
                    if(e.keyCode == 13){
                        if(that.idx == -1) return;
                        var fnr = opt.selectFn(result[that.idx],this,that);
                        that.destroy();
                        that.idx = -1;
                        if(fnr === false){
                            if(e.stopPropagation){
                                e.stopPropagation();
                            }else{
                                e.cancelBubble = true;
                            }
                        }
                    }
                    if(len == 1){
                        that.idx = 0;
                    }
                    that.dom.con.innerHTML = '';
                    opt.showFilted(result,that.dom.con,that);
                    var childs = that.dom.con.childNodes;
                    for(var i=0;i<childs.length;i++){
                        childs[i].onmouseover = (function(_idx){
                            return function(){
                                that.idx = _idx;
                                that.setSelectClass();
                            }
                        })(i);
                        childs[i].onmouseout = (function(_idx){
                            return function(){
                                this.className = opt.className;
                            }
                        })(i);
                        childs[i].onmousedown = (function(_idx){
                            return function(e){
                                that.idx = _idx;
                                opt.selectFn(result[that.idx],that.ipt,that);
                                that.idx = -1;
                                that.destroy()
                            }
                        })(i);
                    }
                }
            }
            // this.ipt.onkeydown = showFilted;
            this.ipt.onkeyup = showFilted;
            this.ipt.onfocus = showFilted;
        }else{
            var timer , result , len;
            function ajaxShowFilted(e){
                if(timer){
                    clearTimeout(timer);
                    timer = undefined;
                }
                e = e || window.event;
                var keyCode = e.keyCode;
                if(keyCode == 38){
                    that.idx --;
                    if(that.idx < 0){
                        that.idx = len - 1;
                    }
                    that.setSelectClass();
                    return;
                }
                if(keyCode == 40){
                    that.idx ++;
                    if(that.idx >= len ){
                        that.idx = 0;
                    }
                    that.setSelectClass();
                    return;
                }
                if(keyCode == 13){
                    opt.selectFn(result[that.idx],this,that);
                    that.destroy();
                }
                timer = setTimeout(function(){
                    opt.ajaxFn(function(data){
                        opt.ajaxCallBack();
                        result = data ;
                        len = result.length;
                        if(len == 0){
                            that.showCon();
                            opt.noResult(that);
                            that.showing = true;
                        }else{
                            if(!that.showing){
                                that.showCon();
                            }
                            that.dom.con.innerHTML = '';
                            opt.showFilted(result,that.dom.con,that);
                            var childs = that.dom.con.childNodes;
                            for(var i=0;i<childs.length;i++){
                                childs[i].onmouseover = (function(_idx){
                                    return function(){
                                        that.idx = _idx;
                                        that.setSelectClass();
                                    }
                                })(i);
                                childs[i].onmouseout = (function(_idx){
                                    return function(){
                                        this.className = opt.className;
                                    }
                                })(i);
                                childs[i].onmousedown = (function(_idx){
                                    return function(e){
                                        that.idx = _idx;
                                        opt.selectFn(result[that.idx],that.ipt,that);
                                        that.idx = -1;
                                    }
                                })(i);
                            }
                        }
                    });
                },500);
            }
            // this.ipt.onkeyup = ajaxShowFilted;
            this.ipt.oninput = ajaxShowFilted;
            this.ipt.onfocus = ajaxShowFilted;
            // opt.ajaxFn(function(data){

            // })
        }
        this.ipt.onblur = function(e){
            if(window.attachEvent){
                setTimeout(function(){
                    if(flag){
                        that.destroy();
                    }
                },200)
            }else{
                that.destroy();
            }
        }
    }
    fn.showCon = function(){
        if(this.showing) return;
        var con = this.dom.con;
        var opt = this.option;
        body.appendChild(con);
        con.style.position = 'absolute';
        con.style.height = 'auto';
        con.style.maxHeight = (opt.height || 200) + 'px';
        con.style.minWidth = opt.width + 1 + 'px';
        con.style.maxWidth = opt.width + 1 + 'px';
        con.style.wdth = opt.width + 1 + 'px';
        con.style.backgroundColor = '#fff';
        con.style.top = opt.top + this.ipt.offsetHeight + 'px';
        con.style.left = opt.left + 'px';
        con.style.borderLeft = '1px solid #dadada';
        con.style.borderRight = '1px solid #dadada';
        con.style.borderBottom = '1px solid #dadada';
        con.style.overflowY = 'auto';
        con.style.overflowX = 'hidden';
        // con.style.paddingTop = '5px';
        con.style.borderTop = '1px solid #dadada';
        this.showing = true;
    }
    fn.destroy = function(){
        if(this.showing){
            body.removeChild(this.dom.con);
            this.showing = false;
            this.idx = -1;
        }
    }
    fn.setSelectClass = function(){
        var childs = this.dom.con.childNodes;
        for(var i=0;i<childs.length;i++){
            childs[i].className = this.option.className;
        }
        if(childs[this.idx]){
            childs[this.idx].className = [this.option.className,this.option.selectClassName].join(' ');
        }
    }
    var flag = true;
    document.onmousedown = function(){
        flag = false;
    }
    document.onmouseup = function(){
        flag = true;
    }
    window.Filter = Filter;
})();

util.userData = {};
var alert = appDebug = (function(){
    var current;
    return function(str,callBack){
        if(current) return;
        current = document.createElement('div');
        current.className = 'alert_div';
        var title = document.createElement('div');
        title.className = 'title';
        title.innerHTML = '提示';
        var content = document.createElement('div');
        content.className = 'tip_content';
        content.innerHTML = str;
        var button = document.createElement('a');
        button.className = 'button';
        button.innerHTML = '确定';
        button.onclick = function(){
            $(current).remove();
            $(mask).remove();
            current = undefined;
            callBack && callBack();
        }
        current.style.zIndex = '1000';
        current.appendChild(title);
        current.appendChild(content);
        current.appendChild(button);
        $('body').append(current);
        $(current).css({
            marginTop : -(current.clientHeight / 2)
        })
        var mask = document.createElement('div');
        mask.style.position = 'fixed';
        mask.style.top = '0px';
        mask.style.left = '0px';
        mask.style.right = '0px';
        mask.style.bottom = '0px';
        mask.style.zIndex = '999';
        mask.style.background = 'rgba(0,0,0,.8)';
        $('body').append(mask);
    }
})();
var confirm  = (function(){
    var current;
    return function(str,callBack,cancelCallBack){
        if(current) return;
        var current;
        current = document.createElement('div');
        current.className = 'alert_div';
        var title = document.createElement('div');
        title.className = 'title';
        title.innerHTML = '提示';
        var content = document.createElement('div');
        content.className = 'tip_content';
        content.innerHTML = str;
        var btnCon = document.createElement('div');
        btnCon.className = 'confirm_btns';
        var button = document.createElement('a');
        button.className = 'submit';
        button.innerHTML = '确定';
        var cancel = document.createElement('a');
        cancel.className = 'cancel';
        cancel.innerHTML = '取消';
        cancel.onclick = function(){
            $(current).remove();
            $(mask).remove();
            current = undefined;
            cancelCallBack && cancelCallBack();
        }
        button.onclick = function(){
            $(current).remove();
            $(mask).remove();
            current = undefined;
            callBack && callBack();
        }
        current.style.zIndex = '10';
        current.appendChild(title);
        current.appendChild(content);
        btnCon.appendChild(button);
        btnCon.appendChild(cancel);
        current.appendChild(btnCon);
        $('body').append(current);
        $(current).css({
            marginTop : -(current.clientHeight / 2)
        })
        var mask = document.createElement('div');
        mask.style.position = 'fixed';
        mask.style.top = '0px';
        mask.style.left = '0px';
        mask.style.right = '0px';
        mask.style.bottom = '0px';
        mask.style.zIndex = '9';
        mask.style.background = 'rgba(0,0,0,.8)';
        $('body').append(mask);
    }
})();
var params = '' , i=0;
function byParams(str){
    params = str;
    window.afterParams && window.afterParams();
}

function loginbytokenCompleteCallBack(){
    // appDebug('app loginByToken 成功，获取用户资料',function(){
        // app.getUserInfo();
    // })
    app.getUserInfo();
}

function loginbytokenFailCallBack(msg){ // 登陆失败弹出提示
    // location.href = 'http://obj@openlogin';
    alert('token登陆失败，msg ' + msg);
    // alert(msg);
}

util.getLocal = function(key){
    var val = localStorage.getItem(key);
    var result = '';
    if(val){
        try{
            result = JSON.parse(val);
        }catch(e){
            result = val;
        }
    }else{
        return '';
    }
    return result;
}
util.setLocal = function(key,val){
    var str = JSON.stringify(val);
    localStorage.setItem(key,str);
}
util.removeLocal = function(key){
    localStorage.removeItem(key);
}
function UserInfoCallBack(session_id,user_id,type_id){
    var reg = /^\((.+)\)$/g;
    session_id = session_id.replace(reg,function(v1,v2){
        return v2;
    })
    user_id = user_id.replace(reg,function(v1,v2){
        return v2;
    })
    type_id = type_id.replace(reg,function(v1,v2){
        return v2;
    })
    if(session_id == 'null' || session_id == '(null)'){
        app.openLogin();
        return;
    }

    util.setLocal('sessionId',session_id);
    util.setLocal('userId',user_id);
    util.setLocal('userType',type_id);
    // alert('收到session_id，点击确定不关闭登陆页直接跳转至信息补充,data:'+[session_id,user_id,type_id].join('，'),function(){
        // util.userData.sessionId = session_id;
        // util.userData.userId = user_id;
        // util.userData.typeId = type_id;
        // window.afterGetUserInfo && window.afterGetUserInfo();
    // })
    util.userData.sessionId = session_id;
    util.userData.userId = user_id;
    util.userData.typeId = type_id;
    window.afterGetUserInfo && window.afterGetUserInfo();
}
if('ontouchstart' in window == true){
    var oldClick = $.fn.click;
    $.fn.click = function(){
        $(this).each(function(){
            this.ontouchstart = function(e){
                e.preventDefault();
            }
        })
        return $.fn.tap.apply(this,arguments);
    }

}

$.fn.reszieTextarea = function(options){
    options = options || {};
    function resizeText(el,oneRowHeight){
        var fontSize = getComputedStyle(document.getElementById('intro')).fontSize;
        oneRowHeight = oneRowHeight || 24;
        var val = $(el).val();
        var width = el.clientWidth;
        var div = document.createElement('div');
        div.className = 'width_compute_helper';
        div.style.fontSize = fontSize;
        div.innerHTML = val;
        $('body').append(div);
        var contentWidth = div.clientWidth;
        var times = Math.max(1,Math.ceil(contentWidth / width));
        el.style.height = times * oneRowHeight + 'px';
        div.parentNode.removeChild(div);
        options.callback && options.callback();
    }
    return $(this).each(function(){
        resizeText(this,options.lineHeight);
        $(this).keydown(function(){
            resizeText(this,options.lineHeight);
        })
    })
}
var imgView = (function(){
    var con , islider;
    return {
        init : function(list){
            var _list = [];
            for(var i=0;i<list.length;i++){
                _list[i] = {
                    content : list[i]
                }
            }
            if(con) return;
            con = document.createElement('div');
            con.id = 'iSlider-wrapper';
            $('body').append(con);
            islider = new iSlider({
                data: _list,
                dom: document.getElementById("iSlider-wrapper"),
                isLooping: 1,
                isOverspread: false,
                animateType: 'default',
                animateTime: 800, // ms
                plugins: [['zoompic', {zoomFactor: 3}]],
                onItemCreated : function(li){
                    li.ontouchstart = function(e){
                        e.preventDefault();
                    }
                    $(li).click(function(e){
                        if(!islider) return;
                        imgView.destroy();
                        return false;
                    })
                },
                onslidechange : function(){
                    var cur = document.getElementById('cur');
                    if(cur){
                        cur.innerHTML = (this.slideIndex + 1) + '/' + list.length;
                    }
                }
            });
            var cur = document.createElement('p');
            cur.id = 'cur';
            cur.style.position = 'relative';
            cur.style.fontSize = '14px';
            cur.style.textAlign = 'center';
            cur.style.zIndex = '9999999';
            cur.style.color = '#fff';
            cur.innerHTML = 1 + '/' + list.length;
            $('#iSlider-wrapper').prepend(cur);
        },
        destroy : function(){
            islider.destroy();
            $(con).remove();
            con = undefined;
            islider = undefined;
            app.goBack();
        }
    }
})();
/**
    app与webview交互兼容IOS与安卓
*/
(function(){
    var app = {};
    var u = navigator.userAgent;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
    var isIos = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    var tabBarIsHided = false;
    
    var androidCss = [
        'body, div, span,object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, address, cite, code, del, dfn, em, img, ins, kbd, q, samp, small, strong, sub, sup, var, b, i, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td {',
        '    letter-spacing: normal;',
        '}',
        'a,input,textarea,label{letter-spacing: -1px;}'
    ]

    if(isAndroid){
        var doc = document;  
        var style = doc.createElement("style");  
        style.setAttribute("type", "text/css");  
      
        if(style.styleSheet){// IE  
            style.styleSheet.cssText = androidCss.join('');  
        } else {// w3c  
            var cssText = doc.createTextNode(androidCss.join(''));  
            style.appendChild(cssText);  
        }  
        var heads = doc.getElementsByTagName("head");  
        if(heads.length)  
            heads[0].appendChild(style);  
        else  
            doc.documentElement.appendChild(style);  
    }
    var calledQueue = [] , lastCall = {
        name : '',
        hasBacked : true,
        args : ''
    };
    var callBackMap = {
        logOut : 'afterLogout'
    }
    app.isMobile = function(){
        return isIos || isAndroid;
    }
    window.nextcall = window.nextCall = function(){
        var lastCalled = lastCall.name;
        if(callBackMap[lastCalled]){
            if(typeof callBackMap[lastCalled] === 'function'){
                callBackMap[lastCalled].apply(undefined,arguments);
            }
            if(typeof callBackMap[lastCalled] === 'string'){
                var fn = window[callBackMap[lastCalled]];
                fn && fn.apply(undefined,arguments);
            }
        }
        lastCall.name = '';
        lastCall.hasBacked = true;
        lastCall.args = '';
        var next = calledQueue.shift();
        next && next.oldFn.apply(undefined,next.args);
    }
    
    app.goBack = function(){
        if(isIos){
            location.href = 'mrlou@back';
        }
        if(isAndroid){
            window.mrlou.back();
        }
    }
    app.href = function(pageName,params,hideBar,isHtml5){
        if(pageName.indexOf('.html') == -1){
            pageName = pageName + '.html';
        }
        var parr = [] , pstr = '';
        hideBar = hideBar != undefined? 'hide' : '';
        switch(typeof params){
            case 'object' : 
                for(var p in params){
                    parr.push(p + '=' + params[p]);
                }
                pstr += parr.join('&');
                break;
            case 'undefined':
                pstr = '';
                break;
            case 'string' :
                pstr += params;
            default:
                break;
        }
        if(isIos){
            if(pstr){
                pstr = '@' + pstr;
            }
            if(hideBar){
                hideBar = '@' + hideBar;
            }
            location.href = 'mrlou@href@' + pageName + pstr + hideBar;
        }
        
        if(isAndroid){
        	//if(isHtml5){
        	//	location.href=pageName+"?"+pstr;
        	//}else{
        		window.mrlou.href(pageName,pstr,hideBar);
        	//}
            //Androidt跳转页面。传入的是页面的地址和参数
            
        }
    }
// 回退，跳转，地图，分享，隐藏底部栏，打开登陆页，关闭登陆页，图片上传
    app.goMap = function(data){
        var title = encodeURIComponent(data.name);
        var address = encodeURIComponent(data.address);
        if(isIos){
            location.href = 'mrlou@navmap@' + title + '@' + data.lat + '@' + data.lon + '@' + address + '@' + data.id;
        }
        if(isAndroid){
            window.mrlou.navmap(title,data.lat,data.lon,address,data.id);
        }
    }
    app.share = function(data){
        var content = encodeURIComponent(data.content);
        var title = encodeURIComponent(data.title);
        if(isIos){
            location.href = 'mrlou@share@'+title+'@'+data.url+'@'+data.avatar+'@'+content+'@'+data.id;
        }
        if(isAndroid){
            window.mrlou.share(title,data.url,data.avatar,content,data.id);
        }
    }
    app.hideTabBar = function(){
        return;
        if(tabBarIsHided){
            return;
        }
        if(isIos){
            location.href = 'mrlou@hidetabbar';
        }
        if(isAndroid){
            
        }
        tabBarIsHided = true;
    }
    app.showTabBar = function(){
        return;
        if(!tabBarIsHided){
            return;
        }
        if(isIos){
            location.href = 'mrlou@showtabbar';
        }
        if(isAndroid){
            
        }
        tabBarIsHided = false;
    }
    app.openLogin = function(){
        if(isIos){
            location.href = 'mrlou@openlogin';
        }
        if(isAndroid){
            window.mrlou.openlogin();
        }
    }
    app.closeLogin = function(){
        if(isIos){
            location.href = 'mrlou@closelogin';
        }
        if(isAndroid){
            window.mrlou.closelogin();
        }
    }
    app.loginByToken = function(token){
        if(isIos){
            location.href = 'mrlou@loginbytoken@' + token;
        }
        if(isAndroid){
            window.mrlou.loginbytoken(token);
        }
    }
    /**
        type:
            图片所属对象的类型
            1：头像 （需要先登录）
            2：楼盘图
            3：房源图
            4：帖子图
        objId:
            图片所属对象的id,  可不填
            1 ：是
            2：否
        isAvatar:
            是否是封面
            1：是
            2：否
        mark:
            是否需要打水印
            1：需要
            2：不需要
    */
    app.upload = function(type,objId,isAvatar,mark){
        if(!type){
            alert('图片所属对象的id必传');
            return;
        }
        if(!isAvatar){
            isAvatar = 2;
        }
        if(!mark){
            mark = 2;
        }
        var arr;
		if(type == 1) {
			arr = [type,objId,isAvatar,mark];
		}else{
			arr = [type,objId,isAvatar,mark, ''];
		}
        if(isIos){
            location.href = 'mrlou@iphoto@' + arr.join('@');
        }
        if(isAndroid){
            window.mrlou.iphoto(type,objId,isAvatar,mark,type == 1 ? mark : '');
        }
    }
    app.logOut = function(){
        util.removeLocal('sessionId');
        util.removeLocal('userId');
        util.removeLocal('userType');
        if(isIos){
            location.href = 'mrlou@logout';
        }
        if(isAndroid){
            window.mrlou.logout();
        }
        // util.openLogin();
    }
    app.getUserInfo = function(){
        if(isIos){
            location.href = 'mrlou@getuserinfo';
        }
        if(isAndroid){
            window.mrlou.getuserinfo();
        }
    };
    
    // for(var p in app){
        // app[p] = (function(fnName){
            // var old = app[fnName];
            // return function(){
                // if(lastCall.hasBacked == true){
                    // lastCall.name = fnName;
                    // lastCall.hasBacked = false;
                    // lastCall.args = arguments;
                    // old.apply(app,arguments);
                // }else{
                    // calledQueue.push({
                        // name : fnName,
                        // args : arguments,
                        // oldFn : app[fnName]
                    // })
                // }
            // }
        // })(p);
    // }
    
    window.app = app;
})();

(function(){
    var msg = util.getLocal('msg') || '0';
    msg = parseInt(msg);
    if(msg){
        $('.msg').addClass('new_msg');
        $('.buliding_msg').addClass('new_msg1');
        // util.setLocal('msg',d.message);
    }else{
        $('.msg').removeClass('new_msg');
        $('.buliding_msg').removeClass('new_msg1');
        util.removeLocal('msg');
    }
})();