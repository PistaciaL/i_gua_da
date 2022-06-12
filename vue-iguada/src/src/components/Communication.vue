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
            <div v-for="(message,index) in messages" :key="index">
                <div class="right clearfix" v-if="message.sender==$store.state.id">
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
            /* 对方的id */
            id:'',
            text:'',
            container:null,
            websocket:null,
            messages:[
                {id:1,content:'我是一条信息',sender:"123456",receiver:'admin'},
                {id:2,content:'我是一条信息',sender:'admin',receiver:"123456"},
                {id:3,content:'我是一条信息',sender:"123456",receiver:'admin'},
                {id:4,content:'我是一条信息',sender:'admin',receiver:"123456"},
                {id:5,content:'我是一条信息',sender:"123456",receiver:'admin'},
                {id:6,content:'我是一条信息',sender:'admin',receiver:"123456"},
                {id:7,content:'我是一条信息',sender:"123456",receiver:'admin'},
                {id:9,content:'我是一条信息',sender:'admin',receiver:"123456"},
                {id:10,content:'我是一条信息',sender:"123456",receiver:'admin'},
                {id:11,content:'我是一条信息',sender:'admin',receiver:"123456"},
            ]
        }
    },
    methods:{
        send(){
            if(this.text!=''){
                //this.websocket.send(this.id+';'+this.text);
                this.messages.push({id:this.messages.length+1,content:this.text,sender:this.$store.state.id,receiver:this.id});
                this.text='';
                setTimeout(this.update,100);
            }
        },
        receive(evt){
            this.messages.push({id:this.messages.length+1,content:evt,sender:this.id,receiver:this.$store.state.id});
            setTimeout(this.update,100);
        },
        update(){
            this.container.scrollTop = this.container.scrollHeight;
        }
    },
    mounted(){
        this.userName = this.$route.query.userName;
        this.id = this.$route.query.id;
        this.container = document.querySelector(".message");
        setTimeout(this.update,100);
        console.log(this.container.scrollTop);
        // let url = "ws://127.0.0.1:8080/sendMessage"
        // this.websocket = new WebSocket(url);
        // this.websocket.onmessage = this.receive;
        // this.$axios({
        //     method:'GET',
        //     url:'/user/getMessages',
        //     params:{
        //         startIndex:0,
        //         endIndex:99,
        //         id:this.id,
        //     }
        // }).then(res=>{
        //     let data = res.data;
        //     if(data.status==1){
        //         this.messages = data.messages;
        //         setTimeout(this.update,100);
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
        //         this.$store.state.id = null;
        //         localStorage.clear();
        //         this.goto('/');
        //     }
        // })
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