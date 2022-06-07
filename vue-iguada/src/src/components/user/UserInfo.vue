<template>
    <div class="back">
        <div class="info">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>个人信息</el-breadcrumb-item>
                <el-breadcrumb-item>修改信息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="content clearfix">
            <img :src="require('../../assets/img/user.png')"/>
            <div class="line"></div>
            <div class="info-table">
                <div>
                    <span>用户名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <el-input prefix-icon="el-icon-user" v-model="userName" maxlength="16"></el-input>
                </div>
                <div>
                    <span>学工号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <el-input prefix-icon="el-icon-s-promotion" v-model="userId" maxlength="16"></el-input>
                </div>
                <div>
                    <span>邮&nbsp;&nbsp;&nbsp;箱&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <el-input prefix-icon="el-icon-message" v-model="email" maxlength="40"></el-input>
                </div>
                <div>
                    <el-button type="primary" icon="el-icon-check" @click="submit">提交修改</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data(){
        return{
            userName:'',
            userId:'',
            email:'',
        }
    },
    methods:{
        submit(){
            if(this.userName==''||this.userId==''||this.email==''){
                this.$message({
                    message:'输入不能为空!',
                    type:'warning'
                })
            }else if(this.checkEmail(this.email)==false){
                this.$message({
                    message:'邮箱格式不正确!',
                    type:'warning'
                })
            }else{
                this.$axios({
                    method:'POST',
                    url:'/user/resetInfo',
                    params:{
                        userName:this.userName,
                        userId:this.userId,
                        email:this.email,
                    }
                }).then(res=>{
                    let data = res.data;
                    if(data.status==1){
                        this.$message({
                            message:'修改成功!',
                            type: 'success'
                        });
                        this.userName = data.userName;
                        this.userId = data.userId;
                        this.email = data.email;
                        this.$store.dispatch("setUserName",data.userName);
                        this.$store.dispatch("setEmail",data.email);
                        this.$store.dispatch("setUserId",data.userId);
                    }else if(data.status==0){
                        this.$message({
                            message:'修改失败!用户名或邮箱已被他人使用!',
                            type:'warning'
                        });
                    }else{
                        this.$store.state.status = 1;
                        this.$store.state.hasLogin = false;
                        this.$store.state.isManager = false;
                        this.$store.state.userName = '';
                        this.$store.state.email = '';
                        this.$store.state.userId = '';
                        localStorage.clear();
                        this.goto('/');
                    }
                })
            }
        },
        checkEmail(value) {
            return /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/.test(value);
        },
        goto(path){
            this.$router.push({
                path:path
            })
        },
    },
    mounted(){
        this.userName = this.$store.state.userName;
        this.userId = this.$store.state.userId;
        this.email = this.$store.state.email;
    }
}
</script>

<style scoped>
.back{
    width: 100%;
    margin:0 auto;
}
.info{
    margin-top:10px;
    margin-bottom: 10px;
    padding-left: 10px;
}
.el-breadcrumb{
    background-color: #F7F8FA;
}
img{
    width: 150px;
    height: 150px;
    border-radius: 50%;
    margin-top: 30px;
    margin-left: 30px;
    float: left;
}
.content > div{
    float: left;
}
.info-table{
    width:600px;
}
.info-table > div{
    margin-top: 10px;
}
.el-input{
    width: 400px;
}
.el-button{
    margin-left: 36%;
}
.line{
    margin-left: 10px;
    margin-right: 10px;
    margin-top: 10px;
    height: 190px;
    width: 1px;
    background-color: #E6E6E6;
}
</style>