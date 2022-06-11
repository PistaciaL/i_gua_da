<template>
    <div class="back">
        <div class="info">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>校车管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="search">
            <el-input class="date" v-model="searchInfo" placeholder="请输入公告标题或id" maxlength="16"></el-input>
            <el-button type="primary" icon="el-icon-search" size="medium" @click="search">搜索公告</el-button>
            <el-button type="primary" icon="el-icon-edit" size="medium" @click="showAdd=true">发布公告</el-button>
        </div>
        <div>
            <el-table :data="notices">
                <el-table-column prop="id" label="公告编号" width="120"></el-table-column>
                <el-table-column prop="title" label="公告标题" width="220"></el-table-column>
                <el-table-column prop="sender" label="发布者" width="140"></el-table-column>
                <el-table-column prop="sendTime" label="发布时间" width="180"></el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button @click="del(scope.row.id)" type="text" size="small" class="el-icon-delete-solid">删除</el-button>
                        <el-button @click="look(scope.row.id)" type="text" size="small" class="el-icon-search">查看</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="page">
            <el-pagination background layout="prev, pager, next" :total="totalPages" :current-page.sync="currentPage" @current-change="getPageNum">
            </el-pagination>
        </div>
        <el-dialog :visible.sync="showAdd" width="30%" center :close-on-click-modal='false' :lock-scroll="false" :before-close="close">
            <div class="add">
                <h2>发布公告</h2>
                <el-input class="input" prefix-icon="el-icon-document" v-model="title" placeholder="公告标题" maxlength="16"></el-input>
                <el-input class="input" type="textarea" :autosize="{ minRows: 5, maxRows: 10}" placeholder="公告正文" v-model="content"></el-input>
                <el-button type="primary" icon="el-icon-edit" size="medium" @click="add">发布公告</el-button>
            </div>
        </el-dialog>
        <el-dialog :title="title" :visible.sync="showLook" :before-close="close" width="30%" center :close-on-click-modal='false' :lock-scroll="false">
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{content}}</p>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data(){
        return{
            showAdd:false,
            showLook:false,
            title:'',
            content:'',
            searchInfo:'',
            saveInfo:'',
            totalPages:100,
            currentPage:1,
            notices:[
            ],
        }
    },
    methods:{
        getPageNum(value){
            this.getPage(this.currentPage);
        },
        del(id){
            this.$axios({
                method:'POST',
                url:'/manager/delNotice',
                params:{
                    id:id,
                    page:this.currentPage,
                    number:8
                }
            }).then(res=>{
                let data = res.data;
                if(data.status==1){
                    this.$message({
                        message:"删除成功,公告id为"+id,
                        type:'success'
                    });
                    this.notices = data.notices;
                    this.totalPages = data.pages*10;
                    this.currentPage = data.currentPage;
                }else if(data.status==0){
                    this.$message.error('删除失败,未知错误!');
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
                    this.$store.state.id = null;
                    localStorage.clear();
                    this.goto('/');
                }
            })
        },
        look(id){
            let that = this;
            this.notices.forEach(function(value,index,self){
                if(value.id===id){
                    that.title = value.title;
                    that.content = value.content;
                }
            });
            this.showLook = true;
        },
        add(){
            if(this.title==''||this.content==''){
                this.$message({
                    message:"输入不能为空!",
                    type:'warning'
                });
            }else{
                let time = new Date();
                let t = time.getFullYear()+'/'+(time.getMonth()+1)+'/'+time.getDate()+' '+time.getHours()+':'+time.getMinutes();
                this.$axios({
                    method:'POST',
                    url:'/manager/addNotice',
                    params:{
                        title:this.title,
                        content:this.content,
                        sendTime:t,
                        page:this.currentPage,
                        number:8
                    }
                }).then(res=>{
                    let data = res.data;
                    if(data.status==1){
                        this.$message({
                            message:"添加成功",
                            type:'success'
                        });
                        this.notices = data.notices;
                        this.totalPages = data.pages*10;
                        this.currentPage = data.currentPage;
                        this.title='';
                        this.content='';
                        this.showAdd=false;
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
                        this.$store.state.id = null;
                        localStorage.clear();
                        this.goto('/');
                    }
                })
            }
        },
        search(){
            this.saveInfo = this.searchInfo;
            this.getPage(1);
        },
        getPage(currentPage){
            this.$axios({
                method:'GET',
                url:'/manager/searchNotices',
                params:{
                    info:this.saveInfo,
                    page:currentPage,
                    number:8
                }
            }).then(res=>{
                let data = res.data;
                if(data.status==1){
                    this.notices = data.notices;
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
                    this.$store.state.id = null;
                    localStorage.clear();
                    this.goto('/');
                }
            })
        },
        close(done){
            this.title='';
            this.content='';
            done();
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
/* 搜索框部分 */
.search{
    margin-top: 20px;
    margin-bottom: 10px;
}
.search > .el-button{
    margin-left: 20px;
}
.date{
    margin-left: 360px;
    width: 200px !important;
}

.page{
    width: 100%;
    text-align: center;
    position: absolute;
    bottom: 0;
}
/* 对话框部分 */
.add h2{
    font-size: 24px;
    margin-bottom: 10px;
}
.add>span{
    margin-left: 10px;
    margin-right: 10px;
}
.add > .el-button,.input{
    margin-bottom: 10px;
    margin-left: 32px;
    width: 336px;
}
</style>