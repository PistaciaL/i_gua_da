<template>
    <div class="back">
        <div class="info">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>用户管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="search">
            <el-input class="date" v-model="searchInfo" placeholder="请输入用户名或id" maxlength="16"></el-input>
            <el-button type="primary" icon="el-icon-search" size="medium" @click="search">搜索用户</el-button>
        </div>
        <div>
            <el-table :data="users">
                <el-table-column prop="id" label="用户id" width="100"></el-table-column>
                <el-table-column prop="userName" label="用户名" width="180"></el-table-column>
                <el-table-column prop="email" label="用户邮箱" width="180"></el-table-column>
                <el-table-column prop="status" label="用户状态" width="100"></el-table-column>
                <el-table-column prop="power" label="用户权限" width="100"></el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button v-if="scope.row.power==='普通用户'" @click="changePower(scope.row.id,true)" type="text" size="small">设为管理员</el-button>
                        <el-button v-else @click="changePower(scope.row.id,false)" type="text" size="small">取消管理员</el-button>
                        <el-button v-if="scope.row.status==='正常'" @click="changeStatus(scope.row.id,true)" type="text" size="small">封禁</el-button>
                        <el-button v-else @click="changeStatus(scope.row.id,false)" type="text" size="small">解封</el-button>
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
            searchInfo:'',
            saveInfo:'',
            totalPages:100,
            currentPage:1,
            users:[
            ],
        }
    },
    methods:{
        getDate(value){
            if(value==='')return '';
            let time = new Date(value);
            return time.getFullYear()+'/'+(time.getMonth()+1)+'/'+time.getDate();
        },
        print(value){
            console.log(value);
        },
        getPageNum(value){
            this.getPage(this.currentPage);
        },
        changePower(id,choice){
            this.$axios({
                method:'POST',
                url:'/manager/setPower',
                data:{
                    id:id,
                    power:choice,
                }
            }).then(res=>{
                let data = res.data;
                if(data.status==1){
                    this.$message({
                        message:"设置成功",
                        type:'success'
                    });
                    this.users.forEach(function(value,index,self){
                        if(value.id===id){
                            if(choice){
                                self[index].power='管理员';
                            }else{
                                self[index].power='普通用户';
                            }
                        }
                    })
                }else if(data.status==0){
                    this.$message({
                        message:'不能对自己进行操作!',
                        type:'warning'
                    })
                }else if(data.status==2){
                    this.$store.state.status = 2;
                    this.$store.dispatch("setIsManager",false);
                    this.goto("/user/userInfo");
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
        },
        changeStatus(id,choice){
            this.$axios({
                method:'POST',
                url:'/manager/setStatus',
                data:{
                    id:id,
                    status:choice,
                }
            }).then(res=>{
                let data = res.data;
                if(data.status==1){
                    this.$message({
                        message:"设置成功",
                        type:'success'
                    });
                    this.users.forEach(function(value,index,self){
                        if(value.id===id){
                            if(choice){
                                self[index].status='封禁中';
                            }else{
                                self[index].status='正常';
                            }
                        }
                    })
                }else if(data.status==0){
                    this.$message({
                        message:'不能对自己进行操作!',
                        type:'warning'
                    })
                }else if(data.status==2){
                    this.$store.state.status = 2;
                    this.$store.dispatch("setIsManager",false);
                    this.goto("/user/userInfo");
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
        },
        search(){
            this.saveInfo = this.searchInfo;
            this.getPage(1);
        },
        getPage(currentPage){
            this.$axios({
                method:'GET',
                url:'/manager/searchUsers',
                params:{
                    info:this.saveInfo,
                    page:currentPage,
                    number:8
                }
            }).then(res=>{
                let data = res.data;
                if(data.status==1){
                    this.users = data.users;
                    this.totalPages = data.pages*10;
                    this.currentPage = data.currentPage;
                }else if(data.status==0){
                    this.$message.error('未知错误!');
                }else if(data.status==2){
                    this.$store.state.status = 2;
                    this.$store.dispatch("setIsManager",false);
                    this.goto("/user/userInfo");
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
.search{
    margin-top: 20px;
    margin-bottom: 10px;
}
.search > .el-button{
    margin-left: 20px;
}
.search > span{
    margin-left: 10px;
    margin-right: 10px;
}
.date{
    margin-left: 480px;
    width: 200px !important;
}
.page{
    width: 100%;
    text-align: center;
    position: absolute;
    bottom: 0;
}
</style>