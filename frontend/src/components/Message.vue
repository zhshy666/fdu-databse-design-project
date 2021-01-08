<template>
<div class="container">
    <logo></logo>    
    <div v-if="this.messages.length>0">
        <el-card  v-for="(message,index) in this.messages" :key="index">
            <div slot="header" class="clearfix">                                    
                  <el-button                                        
                    type="text"
                    @click="mark(message.id)"
                    v-if="message.status == 0"
                  >Mark as read</el-button>                  
                  <p v-else style="font-size:0.9rem;color:grey">Has read</p>
                </div>
            <p>
                <span class="itemlabel">
                    <i class="el-icon-message"></i> Content:
                </span>
                <span v-if="message.time">
                    {{message.content}}
                </span>
            </p>
            
            <p>
                <span class="itemlabel">
                    <i class="el-icon-time"></i> Sent Time:
                </span>
                <span v-if="message.time">
                {{message.time.substring(0,10)}}
                </span>
            </p>
        </el-card>
    </div>

    <el-card v-else shadow="hover" style="padding:5px 350px">
        No message now!
    </el-card>
</div>
</template>
<script>
import logo from "./Logo"
export default {
  name:"Message",
  components:{logo},
  data(){
  return{
      messages:[],
    }
  },
  methods:{     
    mark(id){
        this.$axios
        .post("/setRead", {
          id: id,          
        })
        .then(resp => {
          if (resp.status === 200) {
            this.getMessages();                           
          } else {
            this.$message.error("Something error!");
          }
        })
        .catch(error => {
            this.$message.error("Something error!");
            console.log(error);
        });
    },
    getMessages(){
        this.$axios
        .post("/getMessages", {
            id: this.$store.state.user.id,        
        })
        .then(resp => {
            if (resp.status === 200) {  
                console.log(resp.data);
                this.messages=resp.data;
                for(let message of this.messages){
                    if(message.status == 0){
                        this.$emit("hasMessage",true);
                        return;
                    }
                    this.$emit("hasMessage",false);
                }
            } else {
                this.$message.error("Something wrong!");
            }
        })
        .catch(error => {
            this.$message.error("Something wrong!");
            console.log(error);
        });
    }
  },
  created(){      
    this.getMessages();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container{
    padding: 80px 100px 0 100px;
    text-align: left;
}
.itemlabel {
  color: cornflowerblue;
  font-weight: bold;  
}
.el-card{
    margin:20px;
}
</style>