/**
 * @author Administrator
 */
 var xinType = 1;
/*微信分享*/
function xinShar(t){
	//注册
	xinType = t;
	//uexWeiXin.registerApp('wxd930ea5d5a258f4f');
	uexWeiXin.registerApp('wx8814f3cfe01af766');
	alert('注册成功');
	var timer = setTimeout(function(){
		clearTimeout(timer);
		isInstallXin();
	},1000);
}
//监测是否安装微信
function isInstallXin(){
	alert('检测是否安装微信');
	uexWeiXin.cbIsWXAppInstalled=function(opCode,dataType,data){
		logs("是否安装==="+data);
		if(data == 0){//安装
		alert('已安装');
				sendText();
			//isSupporApi()
		}else{//未安装
			alert("请先安装微信！");
			//getInstallUrl();
		}
	}
	uexWeiXin.isWXAppInstalled();
}
function getInstallUrl(){
	
}
//分享文本
function sendText(){
	alert('分享文本');
	logs(uexWeiXin.sendTextContent(1,"这是来自AppCan中国最大的移动中间键平台对微信支持测试"));
	uexWeiXin.cbSendTextContent = function(id,type,data){
		logs("data=="+data);
		if(data==0){
			$toast("分享成功！",2000);
		}
	}
	 var txt = "这是来自AppCan中国最大的移动中间键平台对微信支持测试";
	uexWeiXin.sendTextContent(xinType,txt);
	alert('111222');
}