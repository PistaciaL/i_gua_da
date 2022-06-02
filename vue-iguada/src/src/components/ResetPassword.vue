<template>
    <div class="back">
        <!-- 导航栏 -->
        <div class="nav">
            <img :src="require('../assets/img/logo.png')"/>
            <div @click="goto('/')"><span>返回首页</span></div>
        </div>
        <!-- 内容 -->
        <div class="content">
            <div class="content-form">
                <el-input prefix-icon="el-icon-message" v-model="email" placeholder="请输入邮箱" maxlength="16"></el-input>
                <el-input prefix-icon="el-icon-lock" v-model="password" placeholder="请输入密码"  show-password maxlength="16"></el-input>
                <el-input prefix-icon="el-icon-lock" v-model="passwordAgain" placeholder="请再次输入密码"  show-password maxlength="16"></el-input>
                <el-input prefix-icon="el-icon-edit" v-model="checkStr" placeholder="请输入验证码" maxlength="16"></el-input>
                <el-button type="primary" @click="sendEmail">获取验证码</el-button>
                <p class="info"></p>
                <el-button type="primary" class="btn" icon="el-icon-edit" @click="submit">提交修改</el-button>
                <p></p>
                <el-button class="btn" icon="el-icon-refresh-right" @click="goto('/')">返回首页</el-button>
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
    </div>
</template>

<script>
import { JSEncrypt } from 'jsencrypt'
let encryptor = new JSEncrypt();
export default {
    data(){
        return{
            email:'',
            password:'',
            passwordAgain:'',
            checkStr:'',
            saveEmail:'',
            lastTime:null,
            period:20,
        }
    },
    methods:{
        goto(path){
            this.$router.push({
                path:path
            })
        },
        logout(){
            this.goto('/index');
        },
        sendEmail(){
            if(this.email==''){
                this.$message({
                    message:'邮箱不能为空!',
                    type:'warning'
                })
            }else if(this.checkEmail(this.email)==false){
                this.$message({
                    message:'邮箱格式不正确!',
                    type:'warning'
                })
            }else{
                let send = false;
                if(this.lastTime!=null){
                    let now = new Date();
                    let time = now.getTime()-this.lastTime.getTime();
                    if(time<this.period*1000){
                        this.$message({
                            message:'请勿频繁获取验证码,请'+parseInt(this.period-time/1000)+'秒后重试!',
                            type:'warning'
                        })
                    }else{
                        send = true;
                    }
                }else{
                    send = true;
                }
                if(send){
                    this.saveEmail = this.email;
                    this.$axios({
                        method:'POST',
                        url:'/sendEmail',
                        data:{
                            email:this.email,
                        }
                    }).then(res=>{
                        let data = res.data;
                        if(data.status==1){
                            this.$message({
                                message:'邮件已发送',
                                type:'success'
                            });
                            this.saveEmail = this.email;
                        }else{
                            this.lastTime = new Date(new Date().getTime()-this.period*1000);
                            this.$message.error('该邮箱不存在!');
                            this.saveEmail='';
                        }
                    });
                    this.lastTime = new Date();
                }
            }
        },
        submit(){
            if(this.password==''||this.passwordAgain==''||this.checkStr==''){
                this.$message({
                    message:'输入不能为空!',
                    type:'warning'
                })
            }else if(this.password!=this.passwordAgain){
                this.$message.error('两次输入密码不一致!');
            }else if(this.saveEmail==''){
                this.$message({
                    message:'请先获取验证码!',
                    type:'warning'
                })
            }else{
                encryptor.setPublicKey(this.$store.state.publicKey);
                this.$axios({
                    method:'POST',
                    url:'/validate',
                    data:{
                        email:this.saveEmail,
                        password:this.password,
                        // password:encryptor.encrypt(this.password),
                        checkStr:this.checkStr
                    }
                }).then(res=>{
                    let data = res.data;
                    if(data.status==1){
                        this.$message({
                            message:'修改成功!',
                            type: 'success'
                        });
                        this.saveEmail='';
                    }else if(data.status==0){
                        this.$message.error('验证码错误!');
                    }else if(data.status==2){
                        this.$message.error('验证码已经失效!');
                    }else{
                        this.$message.error('修改失败,您的账户已被封禁!');
                    }
                })
            }
        },
        checkEmail(value) {
            return /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/.test(value);
        }
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
.nav>div{
    margin-left: 10px;
    height: 50px;
    float: right;
}
.nav > div:nth-child(2){
    margin-right: 16%;
}
.nav span{
    margin-left: 10px;
    cursor:pointer;
    display:block;
    color: #A0A0A0;
    line-height: 50px;
}
.nav span:hover{
    color: #409EFF;
}
/* 中间内容部分 */
.content{
    width: 70%;
    margin: 0 auto;
    height: 700px;
}
.content-form{
    display: inline-block;
    margin-left: 34%;
    margin-top:150px;
}
.content-form > .el-input:nth-child(4){
    margin-bottom: 10px;
    width: 200px;
    margin-right: 18px;
}
.content-form > .el-input:not(:nth-child(4)){
    display: block;
    margin-bottom: 10px;
    width: 330px;
}
.btn{
    margin-bottom: 10px;
    width: 330px;
    display: block;
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
</style>