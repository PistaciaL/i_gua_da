<template>
    <div class="back">
        <div class="info">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>个人信息</el-breadcrumb-item>
                <el-breadcrumb-item>修改密码</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="content clearfix">
            <img :src="require('../../assets/img/user.png')"/>
            <div class="line"></div>
            <div class="info-table">
                <div>
                    <span>原密码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <el-input prefix-icon="el-icon-delete-solid" v-model="oldPassword" placeholder="原密码" show-password maxlength="16"></el-input>
                </div>
                <div>
                    <span>密&nbsp;&nbsp;&nbsp;码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <el-input prefix-icon="el-icon-lock" v-model="newPassWord" placeholder="请输入密码"  show-password maxlength="16"></el-input>
                </div>
                <div>
                    <span>确&nbsp;&nbsp;&nbsp;认&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <el-input prefix-icon="el-icon-lock" v-model="passwordAgain" placeholder="请再次输入密码"  show-password maxlength="16"></el-input>
                </div>
                <div>
                    <el-button type="primary" icon="el-icon-check" @click="submit">修改密码</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data(){
        return{
            oldPassword:'',
            newPassWord:'',
            passwordAgain:'',
        }
    },
    methods:{
        goto(path){
            this.$router.push({
                path:path
            })
        },
        submit(){
            if(this.oldPassword==''||this.newPassWord==''||this.passwordAgain==''){
                this.$message({
                    message:'输入不能为空!',
                    type:'warning'
                })
            }else if(this.newPassWord!=this.passwordAgain){
                this.$message.error('两次输入密码不一致!');
            }else{
                this.$axios({
                    method:'POST',
                    url:'/user/resetPassword',
                    params:{
                        oldPassword:this.oldPassword,
                        newPassWord:this.newPassWord
                    }
                }).then(res=>{
                    let data = res.data;
                    if(data.status==0){
                        this.$message.error('修改失败,密码错误!');
                    }else if(data.status==1){
                        this.$message({
                            message:'修改成功!',
                            type: 'success'
                        })
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
        }
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