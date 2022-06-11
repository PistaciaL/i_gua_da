<template>
    <div class="back">
        <!-- 导航条 -->
        <div class="nav">
            <img :src="require('../assets/img/logo.png')"/>
            <div v-if="$store.state.hasLogin==false">
                <span class="click" @click="showLogin=true;showRegister=false;">登录</span>
                <span>&nbsp;&nbsp;或&nbsp;&nbsp;</span>
                <span class="click" @click="showRegister=true;showLogin=false;">注册</span>
            </div>
            <div v-else>
                <span>欢迎您!{{userName}}</span>
                <span class="click" @click="logout()">&nbsp;&nbsp;退出登录</span>
            </div>
        </div>
        <!-- 菜单内容 -->
        <div class="content">
            <div class="clearfix">
                <div class="menu-child">
                    <i class="el-icon-user-solid"></i>
                    <h2>个人主页</h2>
                    <p>包含个人信息，预约信息等信息，提供预约校车，留言反馈，查看校车班次等服务</p>
                    <el-button type="primary" @click="goto('/user/userInfo')">点击进入</el-button>
                </div>
                <div class="menu-child">
                    <i class="el-icon-s-promotion"></i>
                    <h2>预约校车</h2>
                    <p>可进行查询当前可预约校车，校车剩余座位，预约校车等操作</p>
                    <el-button type="primary" @click="goto('/user/busTime')">点击进入</el-button>
                </div>
                <div class="menu-child">
                    <i class="el-icon-message-solid"></i>
                    <h2>留言反馈</h2>
                    <p>留下您宝贵的意见，您的建议是我们不断前进的最大动力！</p>
                    <el-button type="primary" @click="goto('/user/message')">点击进入</el-button>
                </div>
                <div class="menu-child">
                    <i class="el-icon-s-platform"></i>
                    <h2>管理系统</h2>
                    <p>具有管理员权限的用户可以进入后台对用户，校车班次表等进行管理</p>
                    <el-button type="primary" @click="goto('/manager/busManage')">点击进入</el-button>
                </div>
            </div>
        </div>
        <!-- 通知和班车表 -->
        <div class="info clearfix">
            <div class="time">
                <h2>今日校车排班</h2>
                <el-table :data="times" :row-class-name="tableRowClassName" max-height="300" :header-cell-style="{background:'#F1F7FF'}" v-loading="timeLoading">
                    <el-table-column prop="time" label="出发时间" width="180"></el-table-column>
                    <el-table-column prop="start" label="出发车站" width="100"></el-table-column>
                    <el-table-column prop="end" label="目的车站" width="100"></el-table-column>
                    <el-table-column prop="number" label="剩余空位" width="100"></el-table-column>
                </el-table>
            </div>
            <div class="notice">
                <h2>公告通知</h2>
                <el-carousel indicator-position="outside" height="270px" v-loading="noticeLoading">
                    <el-carousel-item v-for="notice in notices" :key="notice.id">
                        <h3 class="notice-title">{{notice.title}}</h3>
                        <p class="notice-content">&nbsp;&nbsp;&nbsp;&nbsp;{{notice.content}}</p>
                    </el-carousel-item>
                </el-carousel>
            </div>
        </div>
        <!-- 底部 -->
        <div class="bottom">
            <div class="bottom-logo">
                <img class="logo2" :src="require('../assets/img/logo2.png')"/>
            </div>
            <div class="line"></div>
            <div class="bottom-info">
                <p>i瓜大开发团队,联系电话:18322805616</p>
                <p>团队成员:刘智宇,康弘,卜欣阳,董保朔,冶小东</p>
                <p>地址:西北工业大学长安校区</p>
                <p>版权所有 © i瓜大开发团队</p>
            </div>
            <div class="bottom-logo2">
                <img class="logo3" :src="require('../assets/img/logo3.png')"/>
            </div>
        </div>
        <!-- 登录框 -->
        <el-dialog :visible.sync="showLogin" width="30%" center :close-on-click-modal='false' :before-close="beforeClose" :lock-scroll="false">
            <div class="login">
                <h2>登录账号</h2>
                <el-input prefix-icon="el-icon-user" v-model="userName" placeholder="请输入用户名" maxlength="16"></el-input>
                <el-input prefix-icon="el-icon-lock" v-model="password" placeholder="请输入密码"  show-password maxlength="16"></el-input>
                <p class="forget click" @click="goto('/reset')">忘记密码？</p>
                <p>{{loginError}}</p>
                <el-button type="primary" @click="login">登录</el-button>
                <el-button @click="showRegister=true;showLogin=false;clearData();">注册账号</el-button>
                <span>登录注册即代表同意i瓜大《用户协议》和《隐私协议》</span>
            </div>
        </el-dialog>
        <el-dialog :visible.sync="showRegister" width="30%" center :close-on-click-modal='false' :before-close="beforeClose" :lock-scroll="false">
            <div class="register">
                <h2>注册账号</h2>
                <el-input prefix-icon="el-icon-user" v-model="userName" placeholder="请输入用户名" maxlength="16"></el-input>
                <el-input prefix-icon="el-icon-s-promotion" v-model="userId" placeholder="请输入学工号" maxlength="16"></el-input>
                <el-input prefix-icon="el-icon-message" v-model="email" placeholder="请输入邮箱" maxlength="16"></el-input>
                <el-input prefix-icon="el-icon-lock" v-model="registerPassword" placeholder="请输入密码"  show-password maxlength="16"></el-input>
                <el-input prefix-icon="el-icon-lock" v-model="passwordAgain" placeholder="请再次输入密码"  show-password maxlength="16"></el-input>
                <p>{{registerError}}</p>
                <el-button type="warning" @click="register">注册</el-button>
                <el-button @click="showLogin=true;showRegister=false;clearData();">已有账号</el-button>
                <span>登录注册即代表同意i瓜大《用户协议》和《隐私协议》</span>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { JSEncrypt } from 'jsencrypt'
export default {
    data(){
        return{
            userName:'',
            password:'',
            registerPassword:'',
            passwordAgain:'',
            email:'',
            userId:'',
            showLogin:false,
            showRegister:false,
            loginError:'',
            registerError:'',
            timeLoading:true,
            noticeLoading:true,
            encryptor:null,
            notices:[
            ],
            times:[
            ]
        }
    },
    methods:{
        getDate(value){
            if(value==='')return '';
            let time = new Date(value);
            return time.getFullYear()+'/'+(time.getMonth()+1)+'/'+time.getDate()+' '+time.getHours()+':'+time.getMinutes();
        },
        goto(path){
            let cango = true;
            if(path.indexOf('/user') == 0){
                if(this.$store.state.hasLogin==false){
                    this.showLogin = true;
                    cango = false;
                }
            }else if(path.indexOf('/manager') == 0){
                if(this.$store.state.isManager==false){
                    this.$message.error('您无管理员权限!');
                    cango = false;
                }
            }
            if(cango){
                this.$router.push({
                    path:path
                })
            }
        },
        tableRowClassName({row, rowIndex}){
            return 'row';
        },
        logout(){
            this.$axios({
                method:'POST',
                url:'/logout'
            }).then(res=>{
                let data = res.data;
                console.log(data);
                if(data.status===1){
                    this.userName = '';
                    this.$store.state.hasLogin = false;
                    this.$store.state.isManager = false;
                    this.$store.state.userName = '';
                    this.$store.state.email = '';
                    this.$store.state.userId = '';
                    localStorage.clear();
                    this.clearData();
                }
            })
        },
        login(){
            if(this.userName==''||this.password==''){
                this.loginError="输入为空!";
            }else{
                //encryptor.setPublicKey(this.$store.state.publicKey);
                //console.log(this.encryptor.encrypt(this.password));
                this.$axios({
                    method:'POST',
                    url:'/login',
                    params:{
                        userName:this.userName,
                        password:this.password,
                        // password:this.encryptor.encrypt(this.password)
                    },
                }).then(res=>{
                    let data = res.data;
                    if(data.status=='1'){
                        this.userName = data.userName;
                        this.$store.dispatch("setUserName",data.userName);
                        this.$store.dispatch("setHasLogin",true);
                        if(data.isManager=='1') {
                            this.$store.dispatch("setIsManager",false);
                        }else{
                            this.$store.dispatch("setIsManager",true);
                        }
                        this.$store.dispatch("setEmail",data.email);
                        this.$store.dispatch("setUserId",data.userId);
                        this.$store.dispatch("setId",data.id);
                        this.clearData();
                        this.showLogin = false;
                    }else if(data.status=='0'){
                        this.loginError = '登录失败,用户名或密码错误';
                    }else{
                        this.loginError = '您的已账户被封禁，禁止登录';
                    }
                });
            }
        },
        register(){
            if(this.userName==''||this.userId==''||this.email==''||this.registerPassword==''||this.passwordAgain==''){
                this.registerError="输入不能为空!";
            }else if(this.checkEmail(this.email)==false){
                this.registerError="邮箱格式不正确!";
            }else if(this.registerPassword!=this.passwordAgain){
                this.registerError="两次输入的密码不一致";
            }else{
                //encryptor.setPublicKey(this.$store.state.publicKey);
                this.$axios({
                    method:'POST',
                    url:'/register',
                    params:{
                        userName:this.userName,
                        password:this.registerPassword,
                        // password:this.encryptor.encrypt(this.registerPassword),
                        email:this.email,
                        userId:this.userId
                    }
                }).then(res=>{
                    let data = res.data;
                    if(data.status==0){
                        this.registerError = '注册失败,该账户已被注册';
                    }else{
                        this.userName = data.userName;
                        this.$store.dispatch("setUserName",data.userName);
                        this.$store.dispatch("setHasLogin",true);
                        this.$store.dispatch("setIsManager",data.isManager);
                        this.$store.dispatch("setEmail",data.email);
                        this.$store.dispatch("setUserId",data.userId);
                        this.$store.dispatch("setId",data.id);
                        this.clearData();
                        this.showRegister = false;
                    }
                })
            }
        },
        beforeClose(done){
            this.userName = '';
            this.clearData();
            done();
        },
        clearData(){
            this.registerError='';
            this.loginError='';
            this.password='';
            this.registerPassword='';
            this.passwordAgain='';
            this.email='';
            this.userId='';
        },
        checkEmail(value) {
            return /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/.test(value);
        }
    },
    mounted(){
        this.userName = this.$store.state.userName;
        this.$axios({
            method:'GET',
            url:'/getTodayTimes',
        }).then(res=>{
            let data = res.data;
            data.forEach((value,index,self)=> {
                value.time = this.getDate(value.time)
            });
            this.times = data;
            this.timeLoading = false;
        });
        this.$axios({
            method:'GET',
            url:'/getNoitces'
        }).then(res=>{
            let data = res.data;
            this.notices = data;
            this.noticeLoading = false;
        });
        if(this.$store.state.status==1){
            this.$message.error("您的session已经失效,请重新登录!");
            this.$store.state.status=0;
        }else if(this.$store.state.status==5){
            this.$message({
                message:'修改成功!',
                type: 'success'
            });
            this.$store.state.status=0;
        }
        this.encryptor = new JSEncrypt();
        this.encryptor.setPublicKey(this.$store.state.publicKey);
    }
}
</script>

<style scoped>
.back{
    background-color: #F7F8FA;
}
/* 导航栏部分 */
.nav{
    background-color: #FFFFFF;
    height: 50px;
    border-bottom: 1px solid #DCDFE6;
}
.nav > img{
    margin-top: 5px;
    margin-left: 16%;
    width: 200px;
    height: 40px;
    float: left;
}
.nav > div{
    margin-left: 700px;
    margin-top: 16px;
    float: left;
}
.nav span{
    color: #595959;
}
.click{
    cursor:pointer;
}
/* 通知和班车表部分 */
.info{
    padding-top: 20px;
    padding-bottom: 20px;
    background-color: #F1F7FF;
}
.info>div{
    width: 500px;
    float: left;
}
.time{
    margin-left: 260px;
    margin-right: 10px;
}
.notice > h2,.time > h2{
    color: #4C77B6;
    margin-bottom: 10px;
}
.notice-title{
    text-align: center;
    margin: 0 auto;
}
.el-table{
    background-color: #F1F7FF;
}
/* 菜单选项部分 */
.content{
    width: 80%;
    margin: 0 auto;
    margin-top: 20px;
}
.menu-child{
    margin-left: 80px;
    width: 200px;
    float: left;
}
.menu-child>i{
    width: 140px;
    margin: 0 auto;
    font-size: 140px;
    color:#406DA4;
    display: block;
}
.menu-child>h2{
    width: 100px;
    margin: 0 auto;
    color: #656565;
    margin-top: 10px;
    margin-bottom: 10px;
}
.menu-child > .el-button{
    margin-top: 10px;
    margin-bottom: 10px;
    margin-left: 50px;
}
/* 底部部分 */
.bottom{
    height: 200px;
    background-color: #0C1D42;
}
.bottom > div{
    float: left;
}
.line{
    margin-top: 20px;
    height: 80%;
    width: 1px;
    background-color: #8F97A7;
}
.bottom p{
    color: #8F97A7;
    font-size: 12px;
    margin-bottom: 10px;
}
.bottom-logo{
    margin-left: 100px;
    margin-top: 40px;
    margin-right: 20px;
}
.logo2{
    width: 120px;
    height: 120px;
}
.bottom-info{
    width: 600px;
    margin-left: 20px;
    margin-top: 50px;
}
.bottom-logo2{
    margin-left: 280px;
    margin-top: 60px;
}
.logo3{
    width: 300px;
    height: 72px;
}
/* 登录/注册对话框 */
.login .el-input,.register .el-input{
    display: block;
    width: 80%;
    margin: 10px auto;
}
.login h2,.register h2{
    font-size: 24px;
}
.login p:not(.forget),.register p{
    margin-left: 10%;
    color: red;
    margin-bottom: 10px;
}
.forget{
    margin-left: 10%;
    color: #969B9F;
}
.login .el-button,.register .el-button{
    width: 80%;
    margin-left: 10%;
    margin-bottom: 10px;
}
.login span,.register span{
    margin-left: 10%;
}
</style>