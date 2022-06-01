const express = require("express");
const cors = require("cors");

const bodyParser = require("body-parser");

//创建web服务器
let app = express();

//处理跨域问题
app.use(cors());

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

const port = 3000;

//设置监听端口和启动成功回调函数
app.listen(port, () => {
    console.log('start!');
});

app.post("/submit",(req,res)=>{
    console.log(req.body);
    res.send("ok")
})

app.use(express.static('dist'));