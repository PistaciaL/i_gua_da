<template>
    <div class="back">
        <!-- 导航栏 -->
        <div class="nav">
            <img :src="require('../assets/img/logo.png')"/>
            <div @click="logout"><span>退出登录</span></div>
            <div @click="goto('/manager/busManage')" v-if="this.$store.state.isManager"><span>管理员入口</span></div>
            <div @click="goto('/user/userInfo')"><span>个人信息</span></div>
            <div @click="goto('/user/busTime')"><span>预约校车</span></div>
        </div>
        <!-- 内容 -->
        <div class="content clearfix">
            <div class="left-content">
                <el-menu :collapse="isCollapse" class="menu" :router="true">
                    <el-menu-item  @click="isCollapse=!isCollapse">
                        <i class="el-icon-arrow-left" v-if="isCollapse==false"></i>
                        <i class="el-icon-arrow-right" v-if="isCollapse==true"></i>
                    </el-menu-item>
                    <el-submenu index="1">
                        <template slot="title">
                            <i class="el-icon-user-solid"></i>
                            <span slot="title">个人信息</span>
                        </template>
                        <el-menu-item index="/user/userInfo">
                            <i class="el-icon-document"></i>
                            <span slot="title">修改信息</span>
                        </el-menu-item>
                        <el-menu-item index="/user/password">
                            <i class="el-icon-lock"></i>
                            <span slot="title">修改密码</span>
                        </el-menu-item>
                    </el-submenu>
                    <el-submenu index="2">
                        <template slot="title">
                            <i class="el-icon-truck"></i>
                            <span slot="title">校车信息</span>
                        </template>
                        <el-menu-item index="/user/order">
                            <i class="el-icon-star-on"></i>
                            <span slot="title">我的预约</span>
                        </el-menu-item>
                        <el-menu-item index="/user/busTime">
                            <i class="el-icon-s-data"></i>
                            <span slot="title">校车班次</span>
                        </el-menu-item>
                    </el-submenu>
                    <el-submenu index="3">
                        <template slot="title">
                            <i class="el-icon-message-solid"></i>
                            <span slot="title">意见反馈</span>
                        </template>
                        <el-menu-item index="/user/message">
                            <i class="el-icon-phone-outline"></i>
                            <span slot="title">联系管理员</span>
                        </el-menu-item>
                    </el-submenu>
                    <el-menu-item index="/manager/busManage" v-if="this.$store.state.isManager">
                        <i class="el-icon-s-platform"></i>
                        <span slot="title">管理员入口</span>
                    </el-menu-item>
                    <el-menu-item index="/">
                        <i class="el-icon-refresh-left"></i>
                        <span slot="title">返回首页</span>
                    </el-menu-item>
                </el-menu>
            </div>
            <div class="right-content">
                <router-view></router-view>
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
export default {
    data(){
        return{
            isCollapse:false,
        }
    },
    methods:{
        goto(path){
            this.$router.push({
                path:path
            })
        },
        logout(){
            this.$axios({
                method:'POST',
                url:'/logout'
            }).then(res=>{
                let data = res.data;
                if(data.status===1){
                    this.$store.state.hasLogin = false;
                    this.$store.state.isManager = false;
                    this.$store.state.userName = '';
                    this.$store.state.email = '';
                    this.$store.state.userId = '';
                    localStorage.clear();
                }
                this.goto('/index');
            })
        },
    },
    mounted(){
        if(this.$store.state.status==2){
            this.$message.error("您无管理员权限!");
            this.$store.state.status=0;
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
    /* border: 1px red solid; */
}
.right-content{
    width: 79%;
    float: left;
    height: 700px;
}

.left-content{
    float: left;
}
.menu{
    height: 700px;
}
.menu:not(.el-menu--collapse){
    width: 210px;
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