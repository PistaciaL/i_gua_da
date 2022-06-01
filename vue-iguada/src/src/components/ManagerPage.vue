<template>
    <div class="back">
        <!-- 导航栏 -->
        <div class="nav">
            <img :src="require('../assets/img/logo.png')"/>
            <div @click="logout"><span>退出登录</span></div>
            <div @click="goto('/manager/noticeManage')"><span>公告管理</span></div>
            <div @click="goto('/manager/userManage')"><span>用户管理</span></div>
            <div @click="goto('/manager/busManage')"><span>校车管理</span></div>
        </div>
        <!-- 内容 -->
        <div class="content clearfix">
            <div class="left-content">
                <el-menu :collapse="isCollapse" class="menu" :router="true">
                    <el-menu-item  @click="isCollapse=!isCollapse">
                        <i class="el-icon-arrow-left" v-if="isCollapse==false"></i>
                        <i class="el-icon-arrow-right" v-if="isCollapse==true"></i>
                    </el-menu-item>
                    <el-menu-item index="/manager/busManage">
                        <i class="el-icon-location"></i>
                        <span slot="title">校车管理</span>
                    </el-menu-item>
                    <el-menu-item index="/manager/userManage">
                        <i class="el-icon-user-solid"></i>
                        <span slot="title">用户管理</span>
                    </el-menu-item>
                    <el-menu-item index="/manager/noticeManage">
                        <i class="el-icon-date"></i>
                        <span slot="title">公告管理</span>
                    </el-menu-item>
                    <el-menu-item index="#" disabled>
                        <i class="el-icon-chat-line-round"></i>
                        <span slot="title">留言管理</span>
                    </el-menu-item>
                    <el-menu-item index="#" disabled>
                        <i class="el-icon-message-solid"></i>
                        <span slot="title">我的消息</span>
                    </el-menu-item>
                    <el-menu-item index="/user/userInfo">
                        <i class="el-icon-refresh-left"></i>
                        <span slot="title">返回</span>
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
                url:'/logout',
                data:{
                    userName:this.$store.state.userName,
                    userId:this.$store.state.userId,
                }
            }).then(res=>{
                if(res.data.status===1){
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
    /* border: 1px red solid; */
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