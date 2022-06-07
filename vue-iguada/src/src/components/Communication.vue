<template>
    <div class="communication-back">
        <div class="info">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>联系管理员</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="title">
            <span>&nbsp;{{userName}}</span>
        </div>
        <div class="message">
            <div v-for="message in messages" :key="message.id">
                <div class="right clearfix" v-if="message.sender=='123456'">
                    <p>{{message.content}}</p>
                </div>
                <div class="left clearfix" v-else>
                    <p>{{message.content}}</p>
                </div>
            </div>
        </div>
        <div class="text">
            <el-input type="textarea" :rows="5" placeholder="发一条友善的建议" v-model="text"></el-input>
            <el-button type="primary" size="small" @click="send">发送</el-button>
        </div>
    </div>
</template>

<script>
export default {
    data(){
        return{
            userName:'',
            text:'',
            messages:[
                {id:1,content:'我是一条信息',sender:'123456',receiver:'admin'},
                {id:2,content:'我是一条信息',sender:'admin',receiver:'123456'},
                {id:3,content:'我是一条信息',sender:'123456',receiver:'admin'},
                {id:4,content:'我是一条信息',sender:'admin',receiver:'123456'},
                {id:5,content:'我是一条信息',sender:'123456',receiver:'admin'},
                {id:6,content:'我是一条信息',sender:'admin',receiver:'123456'},
                {id:7,content:'我是一条信息',sender:'123456',receiver:'admin'}

            ]
        }
    },
    methods:{
        send(){
            if(this.text!=''){
                this.messages.push({id:this.messages.length+1,content:this.text,sender:'123456',receiver:this.userName});
                this.text='';
            }
        }
    },
    mounted(){
        this.userName = this.$route.query.userName;
    }
}
</script>

<style scoped>
.communication-back{
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
.message{
    width: 80%;
    height: 360px;
    margin: 0 auto;
    border-radius: 10px 10px 10px 10px;
    border: 1px #DCDFE6 solid;
    overflow: auto;
    padding-bottom: 10px;
}
.message > div{
    width: 100%;
}
.text{
    width: 80%;
    height: 300px;
    margin: 20px auto;
}
.text > .el-button{
    margin-top: 10px;
    float: right;
}
.right,.left{
    width: 90%;
    margin: 0 auto;
    margin-top: 10px;
}
.right>p{
    max-width: 100%;
    word-break: break-all;
    word-wrap: break-word;
    padding: 5px;
    border-radius: 5px 5px 5px 5px;
    display: inline;
    float: right;
    background-color: #409EFF;
}
.left > p{
    max-width: 100%;
    word-break: break-all;
    word-wrap: break-word;
    padding: 5px;
    border-radius: 5px 5px 5px 5px;
    display: inline;
    float: left;
    background-color: #E5E5E5;
}
</style>