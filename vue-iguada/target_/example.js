const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");

//创建web服务器
let app = express();
app.use(cors());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
const port = 80;
//设置监听端口和启动成功回调函数
app.listen(port, () => {
    console.log('start!');
});
//托管静态资源.可以访问public目录中的所有文件，路径中不含public
app.get("/", function(req, res) {
    //在命令行中查看传递过来的参数
    res.send("hello world!");
});