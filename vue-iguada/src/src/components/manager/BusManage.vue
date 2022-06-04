<template>
    <div class="back">
        <div class="info">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>校车管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="search">
            <el-date-picker class="date" v-model="start" type="date" placeholder="开始日期"></el-date-picker>
            <span>到</span>
            <el-date-picker class="date" v-model="end" type="date" placeholder="结束日期"></el-date-picker>
            <el-button type="primary" icon="el-icon-search" size="medium" @click="search">搜索班次</el-button>
            <el-button type="primary" icon="el-icon-edit" size="medium" @click="showAdd=true">增加班次</el-button>
        </div>
        <div>
            <el-table :data="times">
                <el-table-column prop="id" label="班次编号" width="180"></el-table-column>
                <el-table-column prop="time" label="出发时间" width="180"></el-table-column>
                <el-table-column prop="start" label="出发车站" width="100"></el-table-column>
                <el-table-column prop="end" label="目的车站" width="180"></el-table-column>
                <el-table-column prop="number" label="剩余空位" width="100"></el-table-column>
                <el-table-column label="操作" width="100">
                    <template slot-scope="scope">
                        <el-button @click="del(scope.row.id)" type="text" size="small" class="el-icon-delete-solid">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="page">
            <el-pagination  background layout="prev, pager, next" :total="totalPages" :current-page.sync="currentPage" @current-change="getPageNum">
            </el-pagination>
        </div>
        <el-dialog :visible.sync="showAdd" width="30%" center :close-on-click-modal='false' :lock-scroll="false" :before-close="clear">
            <div class="add">
                <h2>新增校车班次</h2>
                <el-date-picker class="add-date" v-model="newDate" type="date" placeholder="发车日期"></el-date-picker>
                <span>--</span>
                <el-time-select class="add-date" v-model="newTime" :picker-options="{start: '00:00',step: '1:00',end: '24:00'}" placeholder="发车时间"></el-time-select>
                <el-select class="input" v-model="newStart" placeholder="始发站">
                    <el-option label="长安校区" value="长安校区"></el-option>
                    <el-option label="友谊校区" value="友谊校区"></el-option>
                </el-select>
                <el-select class="input" v-model="newEnd" placeholder="终点站">
                    <el-option label="长安校区" value="长安校区"></el-option>
                    <el-option label="友谊校区" value="友谊校区"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-edit" size="medium" @click="add">增加班次</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data(){
        return{
            start:'',
            end:'',
            saveStart:'',
            saveEnd:'',
            showAdd:false,
            totalPages:100,
            currentPage:1,
            newDate:'',
            newTime:'',
            newStart:'',
            newEnd:'',
            times:[
            ]
        }
    },
    methods:{
        goto(path){
            this.$router.push({
                path:path
            })
        },
        getDate(value){
            if(value==='')return '';
            let time = new Date(value);
            return time.getFullYear()+'/'+(time.getMonth()+1)+'/'+time.getDate();
        },
        getPageNum(value){
            this.getPage(this.currentPage);
        },
        getTime(){
            return this.getDate(this.newDate)+' '+this.newTime;
        },
        add(){
            if(this.newDate===''||this.newTime===''||this.newStart==''||this.newEnd==''){
                this.$message({
                    message:'输入不能为空!',
                    type:'warning'
                })
            }else if(this.newStart==this.newEnd){
                this.$message({
                    message:'起点不能和终点一样!',
                    type:'warning'
                })
            }else{
                this.$axios({
                    method:'POST',
                    url:'/manager/addTime',
                    data:{
                        page:this.currentPage,
                        number:8,
                        startTime:this.getDate(this.saveStart),
                        endTime:this.getDate(this.saveEnd),
                        time:this.getTime(),
                        start:this.newStart,
                        end:this.newEnd
                    }
                }).then(res=>{
                    let data = res.data;
                    if(data.status==1){
                        this.times = data.times;
                        this.totalPages = data.pages*10;
                        this.currentPage = data.currentPage;
                        this.newDate='';
                        this.newTime='';
                        this.newStart='';
                        this.newEnd='';
                        this.showAdd=false;
                        this.$message({
                            message:"添加成功",
                            type:'success'
                        });
                    }else if(data.status==0){
                        this.$message({
                            message:'日期输入错误!',
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
            }
        },
        del(id){
            this.$axios({
                method:'POST',
                url:'/manager/delTime',
                data:{
                    id:id,
                    page:this.currentPage,
                    number:8,
                    startTime:this.getDate(this.saveStart),
                    endTime:this.getDate(this.saveEnd),
                }
            }).then(res=>{
                let data = res.data;
                if(data.status==1){
                    this.times = data.times;
                    this.totalPages = data.pages*10;
                    this.currentPage = data.currentPage;
                    this.$message({
                        message:"删除成功,校车班次id为"+id,
                        type:'success'
                    });
                }else if(data.status==0){
                    this.$message.error('删除失败!');
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
            this.saveStart = this.start;
            this.saveEnd = this.end;
            this.getPage(1);
        },
        getPage(currentPage){
            this.$axios({
                method:'GET',
                url:'/manager/searchTimes',
                params:{
                    page:currentPage,
                    number:8,
                    startTime:this.getDate(this.saveStart),
                    endTime:this.getDate(this.saveEnd)
                }
            }).then(res=>{
                let data = res.data;
                if(data.status==1){
                    this.times = data.times;
                    this.totalPages = data.pages*10;
                    this.currentPage = data.currentPage;
                }else if(data.status==0){
                    this.$message({
                        message:'日期输入有误!',
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
        clear(done){
            this.newDate='';
            this.newTime='';
            this.newStart='';
            this.newEnd='';
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
.search{
    margin-top: 20px;
    margin-bottom: 10px;
}
.search>.date:first-child{
    margin-left: 100px;
}
.search > .el-button{
    margin-left: 40px;
}
.search > span{
    margin-left: 10px;
    margin-right: 10px;
}
.date{
    width: 150px !important;
}

.page{
    width: 100%;
    text-align: center;
    position: absolute;
    bottom: 0;
}
/* 添加班次对话框 */
.add h2{
    font-size: 24px;
}
.add>span{
    margin-left: 10px;
    margin-right: 10px;
}
.add-date{
    width: 150px !important;
}
.add-date:nth-child(2){
    margin-left: 32px;
    margin-top: 10px;
    margin-bottom: 10px;
}
.add > .el-button,.input{
    margin-bottom: 10px;
    margin-left: 32px;
    width: 336px;
}
</style>