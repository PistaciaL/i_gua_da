<template>
    <div class="back">
        <div class="info">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>联系管理员</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="title">
            <span class="el-icon-s-custom">&nbsp;消息列表</span>
        </div>
        <div>
            <el-table :data="admins">
                <el-table-column prop="id" label="用户id" width="100"></el-table-column>
                <el-table-column prop="userName" label="用户名" width="180"></el-table-column>
                <el-table-column prop="email" label="用户邮箱" width="260"></el-table-column>
                <el-table-column prop="status" label="用户状态" width="100"></el-table-column>
                <el-table-column prop="power" label="用户权限" width="100"></el-table-column>
                <el-table-column label="操作" width="100">
                    <template slot-scope="scope">
                        <el-button @click="communicate(scope.row.userName,scope.row.id)" type="text" size="small">联系</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div  class="page">
            <el-pagination background layout="prev, pager, next" :total="totalPages" :current-page.sync="currentPage" @current-change="getPageNum">
            </el-pagination>
        </div>
    </div>
</template>

<script>
export default {
    data(){
        return{
            totalPages:100,
            currentPage:1,
            admins:[
                {id:1,userName:'123',email:'lha602@163.com',status:'正常',power:'普通用户'},
                {id:2,userName:'11234',email:'lha602@163.com',status:'正常',power:'普通用户'},
                {id:3,userName:'abc',email:'lha602@163.com',status:'正常',power:'普通用户'}
            ],
        }
    },
    methods:{
        communicate(userName,id){
            this.$router.push({
                path:'/manager/communication',
                query:{
                    userName:userName,
                    id:id
                }
            })
        },
        getPageNum(value){
            this.getPage(this.currentPage);
        },
        getPage(currentPage){
            // this.$axios({
            //     method:'GET',
            //     url:'/manager/searchUsers',
            //     params:{
            //         info:this.saveInfo,
            //         page:currentPage,
            //         number:8
            //     }
            // }).then(res=>{
            //     let data = res.data;
            //     if(data.status==1){
            //         this.users = data.users;
            //         this.totalPages = data.pages*10;
            //         this.currentPage = data.currentPage;
            //     }else if(data.status==0){
            //         this.$message.error('未知错误!');
            //     }else if(data.status==2){
            //         this.$store.state.status = 2;
            //         this.$store.dispatch("setIsManager",false);
            //         this.goto("/user/userInfo");
            //     }else{
            //         this.$store.state.status = 1;
            //         this.$store.state.hasLogin = false;
            //         this.$store.state.isManager = false;
            //         this.$store.state.userName = '';
            //         this.$store.state.email = '';
            //         this.$store.state.userId = '';
            //         localStorage.clear();
            //         this.goto('/');
            //     }
            // })
        }
    },
    mounted(){
        this.getPage(this.currentPage);
    }
}
</script>

<style scoped>
.back{
    width: 100%;
    margin:0 auto;
    height: 680px;
    position: relative;
}
.info{
    margin-top:10px;
    margin-bottom: 10px;
    padding-left: 10px;
}
.title{
    margin-top: 20px;
    margin-bottom: 10px;
    text-align: center;
}
.title > span{
    color: #409EFF;
    font-size: 24px;
}
.page{
    width: 100%;
    text-align: center;
    position: absolute;
    bottom: 0;
}
</style>