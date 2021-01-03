http://127.0.0.1:9526/modules/login/login.html
vois sur ton chemin

express 项目文件目录说明及功能描述
	https://www.cnblogs.com/shimily/articles/5541681.html
（webpack+express）
	https://segmentfault.com/a/1190000008644787
	https://www.cnblogs.com/shiyunfront/articles/8782558.html
	不行https://blog.csdn.net/weixin_30567471/article/details/98043949
	不行 https://www.cnblogs.com/zycbloger/p/6444030.html
单页面与多页面间的区别及优缺点
	https://www.cnblogs.com/yunyea/p/8824178.html
elementui-url
	https://unpkg.com/browse/element-ui@2.4.6/



var express = require('express');
var app = express();
var path = require('path');
app.use(express.static(path.join(__dirname, 'public')));
app.get('/', function(req, res) {
res.sendFile('/index.html');
});
app.listen(8080);