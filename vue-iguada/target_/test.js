const express = require("express");
/* 文件模块写路径，核心模块直接写名字 */
//const express = require("./node_modules/express")
const cors = require("cors");

const bodyParser = require("body-parser");

//console.log(arguments.callee + "");

/* 默认分页中每页的数据条数 */
let default_number = 8;
/**
 *暂存的数据，用于模拟数据库，为编写方便与真正的数据库表是不一样的，这些只是为了说明发到前端数据的格式
 */
let users = [
    {id:1,userName:'admin',password:'123456',email:'lha602@163.com',status:'正常',power:'管理员',userId:'2019302654'},
    {id:2,userName:'康弘',password:'123456',email:'234567891@qq.com',status:'正常',power:'普通用户',userId:'2019302654'},
    {id:3,userName:'刘智宇',password:'123456',email:'345678912@qq.com',status:'正常',power:'普通用户',userId:'2019302654'},
    {id:4,userName:'兰军',password:'123456',email:'456789123@qq.com',status:'正常',power:'普通用户',userId:'2019302654'},
    {id:5,userName:'李畅',password:'123456',email:'567891234@qq.com',status:'封禁中',power:'普通用户',userId:'2019302654'},
    {id:6,userName:'卜欣阳',password:'123456',email:'678912345@qq.com',status:'正常',power:'普通用户',userId:'2019302654'},
    {id:7,userName:'abc',password:'123456',email:'789123456@qq.com',status:'正常',power:'普通用户',userId:'2019302654'},
    {id:8,userName:'efg',password:'123456',email:'891234567@qq.com',status:'正常',power:'普通用户',userId:'2019302654'},
    {id:9,userName:'hig',password:'123456',email:'912345678@qq.com',status:'正常',power:'普通用户',userId:'2019302654'},
    {id:10,userName:'asfasd',password:'123456',email:'lha602@qq.com',status:'封禁中',power:'普通用户',userId:'2019302654'},
];
let times = [
    {id:1,time:'2022/5/26 10:00',start:'长安校区',end:'友谊校区',number:44,hasOrdered:true},
    {id:2,time:'2022/5/26 12:00',start:'长安校区',end:'友谊校区',number:24,hasOrdered:false},
    {id:3,time:'2022/5/26 14:00',start:'友谊校区',end:'长安校区',number:34,hasOrdered:false},
    {id:4,time:'2022/5/26 16:00',start:'长安校区',end:'友谊校区',number:14,hasOrdered:false},
    {id:5,time:'2022/5/26 18:00',start:'友谊校区',end:'长安校区',number:0,hasOrdered:false},
    {id:6,time:'2022/5/26 20:00',start:'长安校区',end:'友谊校区',number:4,hasOrdered:false},
    {id:7,time:'2022/5/26 21:00',start:'友谊校区',end:'长安校区',number:12,hasOrdered:false},
    {id:8,time:'2022/5/26 22:00',start:'长安校区',end:'友谊校区',number:34,hasOrdered:false},
    {id:9,time:'2022/5/25 10:00',start:'长安校区',end:'友谊校区',number:44,hasOrdered:false},
    {id:10,time:'2022/5/25 12:00',start:'长安校区',end:'友谊校区',number:24,hasOrdered:false},
    {id:11,time:'2022/5/25 14:00',start:'友谊校区',end:'长安校区',number:34,hasOrdered:false},
    {id:12,time:'2022/5/25 16:00',start:'长安校区',end:'友谊校区',number:14,hasOrdered:false},
    {id:13,time:'2022/5/25 18:00',start:'友谊校区',end:'长安校区',number:0,hasOrdered:false},
    {id:14,time:'2022/5/25 20:00',start:'长安校区',end:'友谊校区',number:4,hasOrdered:false},
    {id:15,time:'2022/5/25 21:00',start:'友谊校区',end:'长安校区',number:12,hasOrdered:false},
    {id:16,time:'2022/5/25 22:00',start:'长安校区',end:'友谊校区',number:34,hasOrdered:false},
    {id:17,time:'2022/5/24 10:00',start:'长安校区',end:'友谊校区',number:44,hasOrdered:false},
    {id:18,time:'2022/5/24 12:00',start:'长安校区',end:'友谊校区',number:24,hasOrdered:false},
];
let notices = [
    {id:1,title:'班车取消通知',content:'2022年5月26日的班车因天气原因暂时取消，望周知!',sender:"admin",sendTime:'2022/6/1'},
    {id:2,title:'班车增加通知',content:'2022年5月27日下午14:00新增一趟班车，望周知!',sender:"admin",sendTime:'2022/6/2'},
    {id:3,title:'招聘校车队工作人员',content:'现招聘一名校车队志愿者，有相关工作经验者优先，有意向的同学请联系李老师，邮箱:XXXXXX',sender:"admin",sendTime:'2022/6/3'},
    {id:4,title:'班车取消通知',content:'2022年5月26日的班车因天气原因暂时取消，望周知!',sender:"admin",sendTime:'2022/6/4'},
    {id:5,title:'班车增加通知',content:'2022年5月27日下午14:00新增一趟班车，望周知!',sender:"admin",sendTime:'2022/6/5'},
    {id:6,title:'招聘校车队工作人员',content:'现招聘一名校车队志愿者，有相关工作经验者优先，有意向的同学请联系李老师，邮箱:XXXXXX',sender:"admin",sendTime:'2022/6/6'},
    {id:7,title:'班车取消通知',content:'2022年5月26日的班车因天气原因暂时取消，望周知!',sender:"admin",sendTime:'2022/6/7'},
    {id:8,title:'班车增加通知',content:'2022年5月27日下午14:00新增一趟班车，望周知!',sender:"admin",sendTime:'2022/6/8'},
    {id:9,title:'招聘校车队工作人员',content:'现招聘一名校车队志愿者，有相关工作经验者优先，有意向的同学请联系李老师，邮箱:XXXXXX',sender:"admin",sendTime:'2022/6/9'},
    {id:10,title:'班车取消通知',content:'2022年5月26日的班车因天气原因暂时取消，望周知!',sender:"admin",sendTime:'2022/6/10'},
];
let myOrders = [
    {id:1,reserveId:1,time:'2022/5/26 10:00',start:'长安校区',end:'友谊校区',status:'正常'},
    {id:2,reserveId:2,time:'2022/5/26 12:00',start:'长安校区',end:'友谊校区',status:'班次被取消'},
    {id:3,reserveId:3,time:'2022/5/26 14:00',start:'友谊校区',end:'长安校区',status:'正常'},
    {id:4,reserveId:4,time:'2022/5/26 16:00',start:'长安校区',end:'友谊校区',status:'正常'},
    {id:5,reserveId:5,time:'2022/5/26 18:00',start:'友谊校区',end:'长安校区',status:'正常'},
    {id:6,reserveId:6,time:'2022/5/25 10:00',start:'长安校区',end:'友谊校区',status:'正常'},
    {id:7,reserveId:7,time:'2022/5/25 12:00',start:'长安校区',end:'友谊校区',status:'班次被取消'},
    {id:8,reserveId:8,time:'2022/5/25 14:00',start:'友谊校区',end:'长安校区',status:'正常'},
    {id:9,reserveId:9,time:'2022/5/25 16:00',start:'长安校区',end:'友谊校区',status:'正常'},
]

function getObjectsByPage(page,objs,number){
    let result = [];
    let start = (page-1)*number;
    let end = objs.length > number*page ? number*page:objs.length;
    for(var i = start;i<end;i++){
        result.push(objs[i]);
    }
    return result;
}
//创建web服务器
let app = express();

//处理跨域问题
app.use(cors());

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

const port = 3000;

//设置监听端口和启动成功回调函数
app.listen(port, () => {
    console.log('run at http://127.0.0.1:' + port);
});

/**
 * 登录接口
 */
app.post("/login",function (req,res){
    console.log("------登录接口------");
    let data = req.query;
    console.log("------登录接口收到的数据------");
    /**
     * 参数说明
     * userName 用户名
     * password 密码
     */
    console.log(data);
    let response = {
        /* status=1代表登录成功，status=0代表账户或密码错误，status=2代表该账户被封禁，禁止登录 */
        status:0,
        /* 成功登录的用户的部分信息 */
        userName:'',
        email:'',
        userId:'',
        isManager:1
    }
    users.forEach((value,index,self)=>{
        if(value.userName==data.userName&&value.password==data.password){
            response.status = 1;
            response.userName = value.userName;
            response.email = value.email;
            response.userId = value.userId;
            if(value.power=='管理员'){
                response.isManager = 0;
            }
        }
    })
    console.log("------登录接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})
/**
 * 退出登录接口
 */
app.post("/logout",function (req,res){
    console.log("------退出登录接口------");
    console.log("------退出登录接口收到的数据------");
    console.log("------退出登录接口回复的数据------");
    /* status=1表示成功退出登录 */
    console.log("{\"status\":1}");
    res.send("{\"status\":1}");
})

/**
 * 注册接口
 */
app.post("/register",function (req,res){
    console.log("------注册接口------");
    let data = req.query;
    console.log("------注册接口收到的数据------");
    /**
     * 参数说明
     * userName 用户名
     * password 密码
     * email 邮箱
     * userId 学工号
     */
    console.log(data);
    let response = {
        status:1,
        /* 刚刚成功注册用户的部分信息 */
        userName:data.userName,
        email:data.email,
        userId:data.userId,
        isManager:false
    };
    users.forEach((value,index,self)=>{
        if(value.email==data.email){
            /* status=1表示注册成功，status=0表示注册失败(一个邮箱只能注册一次) */
            response.status=0;
        }
    })
    if(response.status==1){
        users.push({id:users.length+1,userName:data.userName,password:data.password,email:data.email,status:'正常',power:'普通用户',userId:data.userId});
    }
    console.log("------注册接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 获取今日校车班次接口，用于首页展示，不需权限验证
 */
app.get("/getTodayTimes",function (req,res){
    console.log("------获取今日校车班次接口------");
    /* 查询结果，我这里就不做处理了，默认times里面前6个就是今天的校车班次 */
    let response = [];
    /**
     * 这里返回的times应至少包含属性title,content
     */
    times.forEach((value,index,self)=>{
        if(0<=index&&index<6){
            response.push({time:value.time,start:value.start,end:value.end,number:value.number})
        }
    });
    console.log("------获取今日校车班次接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 获取通知接口
 * 获取时间上最新的且未被删除的五条通知
 */
app.get("/getNoitces",function (req,res){
    console.log("------获取通知接口------");
    let response = [];
    /**
     * 此处返回的notices应至少包含属性time,start,end,number
     */
    notices.forEach((value,index,self)=>{
        if(0<=index&&index<5){
            response.push({id:value.id,title:value.title,content:value.content},);
        }
    })
    console.log("------获取通知接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 发送邮件请求接口
 */
app.post("/sendEmail",function (req,res){
    console.log("------发送邮件请求接口------");
    let data = req.query;
    console.log("------发送邮件请求接口收到的数据------");
    /**
     * 参数说明
     * email 邮箱
     */
    console.log(data);
    /* status=0代表数据库中无此邮箱，status=1代表发送成功 */
    let response = {
        status:0,
    }
    users.forEach((value,index,self)=>{
        if(value.email==data.email){
            response.status = 1;
        }
    })
    console.log("------发送邮件请求接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 验证验证码接口
 */
app.post("/validate",function (req,res){
    console.log("------验证验证码接口------");
    let data = req.query;
    /* 假的验证码，用于演示 */
    let checkStr = '1234';
    console.log("------验证验证码接口收到的数据------");
    /**
     * 参数说明
     * email 邮箱
     * password 新密码
     * checkStr 验证码
     */
    console.log(data);
    /* status=0代表验证码错误，status=1代表修改密码成功，status=2代表验证码过期,status=3表示该账户已经被封禁，修改失败 */
    let response = {
        status:0,
    }
    if(data.checkStr==checkStr){
        response.status = 1;
        users.forEach((value, index, array) => {
            if(value.email==data.email){
                value.password = data.password;
            }
        })
    }
    console.log("------验证验证码接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 用户信息修改接口
 * 已登录的用户进行修改个人信息，此处需对用户的登录状态进行校验
 */
app.post("/user/resetInfo",function (req,res){
    console.log("------用户信息修改接口------");
    let data = req.query;
    console.log("------用户信息修改接口收到的数据------");
    /**
     * userName 新用户名
     * userId 新学工号
     * email 新邮箱
     */
    console.log(data);
    /**
     * 此处需对用户的登录状态进行校验
     * status = 1代表修改成功，status=0代表邮箱或用户名已经被别人使用过，status=2代表用户session失效
     * 一个用户只能被一个用户使用，一个邮箱只能被一个用户使用
     */
    let response = {
        status:1,
        /* 修改后的新数据 */
        userName:data.userName,
        userId:data.userId,
        email:data.email,
    }
    console.log("------用户信息修改接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 用户修改密码接口
 * 已经登录的用户修改自己的密码，此处需对用户的登录状态进行校验，用户信息从session中获取而非前端传来
 */
app.post("/user/resetPassword",function (req,res){
    console.log("------用户修改密码接口------");
    let data = req.query;
    console.log("------用户修改密码接口收到的数据------");
    /**
     * 参数说明
     * oldPassword 原密码
     * newPassWord 新密码
     */
    console.log(data);
    /**
     * 此处需对用户的登录状态进行校验
     * status = 1代表修改成功，status=0代表原密码错误，status=2代表用户session失效
     */
    let response = {
        status:1
    }
    console.log("------用户修改密码接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 查询用户预约列表接口
 */
app.get("/user/searchOrders",function (req,res){
    console.log("------查询用户预约列表接口------");
    let data = req.query;
    console.log("------查询用户预约列表接口收到的数据------");
    /**
     * 参数说明
     * page 表示要返回的页码
     * number 表示每页的数据条数
     * startTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * endTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * 例如：
     * startTime="",endTime=""表示查询该用户所有预约
     * startTime="",endTime="2022/5/26"表示查询该用户发车时间小于2022/5/26 24:00的所有预约
     * startTime="2022/5/26",endTime=""表示查询该用户发车时间大于2022/5/26 00:00的所有预约
     * startTime="2022/5/26",endTime="2022/5/26"表示查询该用户发车时间在2022/5/26 00:00到2022/5/26 24:00之间的所有预约
     */
    console.log(data);
    data.number = parseInt(data.number);
    data.page = parseInt(data.page);
    /**
     * status = 1表示查询成功，status = 0表示起始时间大于终止时间或时间格式有误导致查询错误，status=2表示用户session失效
     */
    let response = {
        status:1,
        /* 当前展示页面的页码 */
        currentPage:1,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 至少应包含的属性有id,time,status,start,end
         */
        orders:[]
    }
    /**
     * 这里为了展示方便，默认整个myOrders即为查询结果
     * 处理分页时要特别注意数组越界问题
     */
    if((data.page-1)*data.number>myOrders.length){
        data.page = parseInt(myOrders.length/data.number);
    }else if((data.page-1)*data.number==myOrders.length&&myOrders.length!=0){
        data.page--;
    }
    response.currentPage = data.page;
    if(response.status==1){
        response.orders = getObjectsByPage(data.page,myOrders,data.number);
        response.pages = parseInt(myOrders.length/data.number);
        if(myOrders.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------查询用户预约列表接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 用户取消预约接口
 */
app.post("/user/cancel",function (req,res){
    console.log("------用户取消预约接口------");
    let data = req.query;
    console.log("------用户取消预约接口收到的数据------");
    /**
     * 参数说明
     * id 要删除(取消预约)的预约id
     * page 删除后返回的页面的页码，因为删除后会导致当前页面数据数目变化，所以需要重新搜索返回页面
     * 这里要特别注意当删除的元素在最后一页且最后一页只有一个元素的情况
     * number 每页的数据数目
     * startTime 格式为”yyyy/mm/dd“的字符串，表示时间，当前搜索限制条件
     * endTime 格式为”yyyy/mm/dd“的字符串，表示时间，当前搜索限制条件
     * 例如：
     * startTime="",endTime=""表示查询该用户所有预约
     * startTime="",endTime="2022/5/26"表示查询该用户发车时间小于2022/5/26 24:00的所有预约
     * startTime="2022/5/26",endTime=""表示查询该用户发车时间大于2022/5/26 00:00的所有预约
     * startTime="2022/5/26",endTime="2022/5/26"表示查询该用户发车时间在2022/5/26 00:00到2022/5/26 24:00之间的所有预约
     */
    console.log(data);
    data.page = parseInt(data.page);
    data.number = parseInt(data.number);
    let response = {
        /* 状态码status=1表示删除成功，status=0表示删除失败，status=2表示用户session失效 */
        status:1,
        /* 当前展示页面的页码 */
        currentPage:1,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 至少应包含的属性有id,time,status,start,end
         */
        orders:[]
    }
    myOrders.forEach((value,index,self)=>{
        if(value.id==data.id){
            self.splice(index,1);
        }
    });
    if((data.page-1)*data.number>myOrders.length){
        data.page = parseInt(myOrders.length/data.number);
    }else if((data.page-1)*data.number==myOrders.length&&myOrders.length!=0){
        data.page--;
    }
    response.currentPage = data.page;
    if(response.status==1){
        response.orders = getObjectsByPage(data.page,myOrders,data.number);
        response.pages = parseInt(myOrders.length/data.number);
        if(myOrders.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------用户取消预约接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 用户查询校车班次接口
 */
app.get("/user/searchTimes",function (req,res){
    console.log("------用户查询校车班次接口------");
    let data = req.query;
    console.log("------用户查询校车班次接口收到的数据------");
    data.page = parseInt(data.page);
    data.number = parseInt(data.number);
    /**
     * 参数说明
     * page 表示要返回的页码
     * number 表示每页的数据条数
     * startTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * endTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * 例如：
     * startTime="",endTime=""表示查询该用户所有班次
     * startTime="",endTime="2022/5/26"表示查询该用户发车时间小于2022/5/26 24:00的所有班次
     * startTime="2022/5/26",endTime=""表示查询该用户发车时间大于2022/5/26 00:00的所有班次
     * startTime="2022/5/26",endTime="2022/5/26"表示查询该用户发车时间在2022/5/26 00:00到2022/5/26 24:00之间的所有班次
     */
    console.log(data);
    let response = {
        /* status = 1表示查询成功，status = 0表示起始时间大于终止时间或时间格式有误导致查询错误，status=2表示用户session失效 */
        status:1,
        /* 当前展示页面的页码 */
        currentPage:1,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 至少应包括的属性有id,time,start,end,number
         */
        times:[]

    };
    if((data.page-1)*data.number>times.length){
        data.page = parseInt(times.length/data.number);
    }else if((data.page-1)*data.number==times.length&&times.length!=0){
        data.page--;
    }
    response.currentPage = data.page;
    if(response.status==1){
        response.times = getObjectsByPage(data.page,times,data.number);
        response.pages = parseInt(times.length/data.number);
        if(times.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------用户查询校车班次接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 用户预约接口
 */
app.post("/user/order",function (req,res){
    console.log("------用户预约接口------");
    let data = req.query;
    console.log("------用户预约接口收到的数据------");
    /**
     * 参数说明
     * id 要预约的班次id
     */
    console.log(data);
    data.page = parseInt(data.page);
    data.number = parseInt(data.number);
    let response = {
        /* 状态码，status=1代表预约成功，status=0代表无剩余座位，status=2代表不能重复预约，status=3代表用户session已失效 */
        status:1,
        /**
         * 预约的班次id
         */
        id:data.id,
        /**
         * 预约后该班次上剩余的座位
         */
        number:0,
        /**
         * 是否被预约标志
         */
        hasOrdered:true
    };
    times.forEach((value,index,self)=>{
        if(value.id==data.id){
            if(value.number==0){
                response.status=0;
            }else{
                response.number = value.number-1;
            }
        }
    })
    console.log("------用户预约接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 管理员查询校车班次接口
 */
app.get("/manager/searchTimes",function (req,res){
    console.log("------管理员查询校车班次接口------");
    let data = req.query;
    console.log("------管理员查询校车班次接口收到的数据------");
    data.page = parseInt(data.page);
    data.number = parseInt(data.number);
    /**
     * 参数说明，当前查询的所有数据都要满足限制条件
     * page 表示要返回的页码
     * number 表示每页的数据条数
     * startTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * endTime 格式为”yyyy/mm/dd“的字符串，表示时间
     * 例如：
     * startTime="",endTime=""表示查询该用户所有班次
     * startTime="",endTime="2022/5/26"表示查询该用户发车时间小于2022/5/26 24:00的所有班次
     * startTime="2022/5/26",endTime=""表示查询该用户发车时间大于2022/5/26 00:00的所有班次
     * startTime="2022/5/26",endTime="2022/5/26"表示查询该用户发车时间在2022/5/26 00:00到2022/5/26 24:00之间的所有班次
     */
    console.log(data);
    let response = {
        /* status=1表示查询成功，status=0表示输入的日期格式错误或起始日期大于结束日期等，status=2表示用户无管理员权限，status=3表示用户session失效 */
        status:1,
        /* 当前展示页面的页码 */
        currentPage:1,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 至少应包括的属性有id,time,start,end,number
         */
        times:[]
    }
    if((data.page-1)*data.number>times.length){
        data.page = parseInt(times.length/data.number);
    }else if((data.page-1)*data.number==times.length&&times.length!=0){
        data.page--;
    }
    response.currentPage = data.page;
    if(response.status==1){
        response.times = getObjectsByPage(data.page,times,data.number);
        response.pages = parseInt(times.length/data.number);
        if(times.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------管理员查询校车班次接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 管理员添加班次接口
 */
app.post("/manager/addTime",function (req,res){
    console.log("------管理员添加班次接口------");
    let data = req.query;
    console.log("------管理员添加班次接口收到的数据------");
    console.log(data);
    data.number = parseInt(data.number);
    /**
     * 参数说明，当前查询的所有数据都要满足限制条件
     * page 要返回的页面的页码
     * number 一页的数据条数
     * startTime 起始时间，当前查询的限制条件
     * endTime 结束时间，当前查询的限制条件
     * time 要添加的班次的出发时间
     * start 要添加的班次的始发站
     * end 要添加的班次的终点站
     */
    let response = {
        /*
         * 状态码，status=1表示添加成功，status=0表示输入数据有误，如起点站和终点站一样，发车时间在现在之前，
         * status=2表示用户无管理员权限，status=3表示用户session失效
         */
        status:1,
        /* 当前展示页面的页码 */
        currentPage:1,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 至少应包括的属性有id,time,start,end,number
         */
        times:[]
    }
    times.push({id:times.length+1,time:data.time,start:data.start,end:data.end,number:45,hasOrdered:false});
    if((data.page-1)*data.number>times.length){
        data.page = parseInt(times.length/data.number);
    }else if((data.page-1)*data.number==times.length&&times.length!=0){
        data.page--;
    }
    response.currentPage = data.page;
    if(response.status==1){
        response.times = getObjectsByPage(data.page,times,data.number);
        response.pages = parseInt(times.length/data.number);
        if(times.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------管理员添加班次接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 管理员删除班次
 */
app.post("/manager/delTime",function (req,res){
    console.log("------管理员添加班次接口------");
    let data = req.query;
    console.log("------管理员添加班次接口收到的数据------");
    data.page = parseInt(data.page);
    data.number = parseInt(data.number);
    /**
     * 参数说明，当前查询的所有数据都要满足限制条件
     * id 要删除班次的id
     * page 要返回的页面的页码
     * number 一页的数据条数
     * startTime 起始时间，当前查询的限制条件
     * endTime 结束时间，当前查询的限制条件
     */
    console.log(data);
    data.number = parseInt(data.number);
    let response = {
        /*
         * 状态码，status=1表示删除成功，status=0表示输入删除失败，
         * status=2表示用户无管理员权限，status=3表示用户session失效
         */
        status:1,
        /* 当前展示页面的页码 */
        currentPage:1,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 至少应包括的属性有id,time,start,end,number
         */
        times:[]
    }
    times.forEach((value,index,self)=>{
        if(value.id==data.id){
            self.splice(index,1);
        }
    })
    if((data.page-1)*data.number>times.length){
        data.page = parseInt(times.length/data.number);
    }else if((data.page-1)*data.number==times.length&&times.length!=0){
        data.page--;
    }
    response.currentPage = data.page;
    if(response.status==1){
        response.times = getObjectsByPage(data.page,times,data.number);
        response.pages = parseInt(times.length/data.number);
        if(times.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------管理员添加班次接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 管理员查询用户列表接口
 */
app.get("/manager/searchUsers",function (req,res){
    console.log("------管理员查询用户列表接口------");
    let data = req.query;
    console.log("------管理员查询用户列表接口收到的数据------");
    data.page = parseInt(data.page);
    /**
     * 参数说明，当前查询的所有数据都要满足限制条件
     * info 用户id或用户名，info=""则查询所有用户
     * page 要返回的页面的页码
     * number 一页的数据条数
     */
    console.log(data);
    let response = {
        /*
         * 状态码，status=1表示获取成功，status=0表示输入获取失败，
         * status=2表示用户无管理员权限，status=3表示用户session失效
         */
        status:1,
        /* 当前展示页面的页码 */
        currentPage:data.page,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 至少应包括的属性有id,userName,email,status,power
         */
        users:[]
    }
    if(response.status==1){
        response.users = getObjectsByPage(data.page,users,data.number);
        response.pages = parseInt(users.length/data.number);
        if(users.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------管理员查询用户列表接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 管理员修改用户权限接口
 */
app.post("/manager/setPower",function (req,res){
    console.log("------管理员修改用户权限接口------");
    let data = req.query;
    console.log("------管理员修改用户权限接口收到的数据------");
    /**
     * 参数说明
     * id 要修改的用户id
     * power true代表要将权限修改为管理员，false代表要将权限修改为普通用户
     */
    console.log(data);
    let response = {
        /**
         * 状态码，status=1表示修改成功，status=0表示不能对自己进行修改
         * status=2表示用户无管理员权限，status=3表示用户session失效
         */
        status:1
    }
    users.forEach(function(value,index,self){
        if(value.id===data.id){
            if(data.power){
                self[index].power='管理员';
            }else{
                self[index].power='普通用户';
            }
        }
    })
    console.log("------管理员修改用户权限接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 管理员修改用户状态接口
 */
app.post("/manager/setStatus",function (req,res){
    console.log("------管理员修改用户状态接口------");
    let data = req.query;
    console.log("------管理员修改用户状态接口收到的数据------");
    /**
     * 参数说明
     * id 要修改的用户id
     * status true代表要将状态修改为封禁中，false代表要将状态修改为正常
     */
    console.log(data);
    let response = {
        /**
         * 状态码，status=1表示修改成功，status=0表示不能对自己进行修改
         * status=2表示用户无管理员权限，status=3表示用户session失效
         */
        status:1
    }
    users.forEach(function(value,index,self){
        if(value.id===data.id){
            if(data.status){
                self[index].status='封禁中';
            }else{
                self[index].status='正常';
            }
        }
    })
    console.log("------管理员修改用户状态接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 管理员查询公告列表接口
 */
app.get("/manager/searchNotices",function (req,res){
    console.log("------管理员查询公告列表接口------");
    let data = req.query;
    console.log("------管理员查询公告列表接口收到的数据------");
    console.log(data);
    data.page = parseInt(data.page);
    /**
     * 参数说明
     * info 公告标题或id，info=”“表示查询所有公告
     * page 要返回的页面的页码
     * number 一页的数据条数
     */
    let response = {
        /*
         * 状态码，status=1表示获取成功，status=0表示获取失败，
         * status=2表示用户无管理员权限，status=3表示用户session失效
         */
        status:1,
        /* 当前展示页面的页码 */
        currentPage:data.page,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 应包括的属性有id,title,sender,sendTime,content
         */
        notices:[]
    }
    if(response.status==1){
        response.notices = getObjectsByPage(data.page,notices,data.number);
        response.pages = parseInt(notices.length/data.number);
        if(notices.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------管理员查询公告列表接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 管理员删除公告接口
 */
app.post("/manager/delNotice",function (req,res){
    console.log("------管理员删除公告接口------");
    let data = req.query;
    console.log("------管理员删除公告接口收到的数据------");
    /**
     * 参数说明，当前查询的所有数据都要满足限制条件
     * id 要删除公告的id
     * page 要返回的页面的页码
     * number 一页的数据条数
     */
    console.log(data);
    data.page = parseInt(data.page);
    data.number = parseInt(data.number);
    let response = {
        /*
         * 状态码，status=1表示删除成功，status=0表示删除失败，
         * status=2表示用户无管理员权限，status=3表示用户session失效
         */
        status:1,
        /* 当前展示页面的页码 */
        currentPage:1,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 应包括的属性有id,title,sender,sendTime,content
         */
        notices:[]
    }
    notices.forEach((value,index,self)=>{
        if(value.id==data.id){
            self.splice(index,1);
        }
    })
    if((data.page-1)*data.number>notices.length){
        data.page = parseInt(notices.length/data.number);
    }else if((data.page-1)*data.number==notices.length&&notices.length!=0){
        data.page--;
    }
    response.currentPage = data.page;
    if(response.status==1){
        response.notices = getObjectsByPage(data.page,notices,data.number);
        response.pages = parseInt(notices.length/data.number);
        if(notices.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------管理员删除公告接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})

/**
 * 管理员发布公告接口
 */
app.post("/manager/addNotice",function (req,res){
    console.log("------管理员发布公告接口------");
    let data = req.query;
    console.log("------管理员发布公告接口收到的数据------");
    /**
     * 参数说明，当前查询的所有数据都要满足限制条件
     * title 要添加公告的id
     * content 要添加公告的内容
     * sendTime 公告发布时间
     * page 要返回的页面的页码
     * number 一页的数据条数
     */
    console.log(data);
    data.page = parseInt(data.page);
    data.number = parseInt(data.number);
    let response = {
        /*
         * 状态码，status=1表示添加成功，status=0表示添加失败，
         * status=2表示用户无管理员权限，status=3表示用户session失效
         */
        status:1,
        /* 当前展示页面的页码 */
        currentPage:1,
        /* 当前搜索到数据的总页数 */
        pages:1,
        /**
         * 至少应包括的属性有id,title,sender,sendTime,content
         */
        notices:[]
    }
    notices.push({id:notices.length+1,title:data.title,content:data.content,sender:"admin",sendTime:data.sendTime});
    response.currentPage = data.page;
    if(response.status==1){
        response.notices = getObjectsByPage(data.page,notices,data.number);
        response.pages = parseInt(notices.length/data.number);
        if(notices.length/data.number>response.pages){
            response.pages++;
        }
    }
    console.log("------管理员发布公告接口回复的数据------");
    console.log(JSON.stringify(response));
    res.send(JSON.stringify(response));
})
//托管静态资源.可以访问public目录中的所有文件，路径中不含public
app.use(express.static('dist'));
//挂载路径前缀
//app.use('/dist',express.static('dist'));