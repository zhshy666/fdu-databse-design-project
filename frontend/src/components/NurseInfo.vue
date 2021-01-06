<template>
<div class="container">
    <logo></logo>    
    <el-table
    :data="nurses"
    stripe
    max-height="510"
    style="width: 100%">
    <el-table-column
      sortable
      prop="id"
      label="ID"
      width="60">
    </el-table-column>
    <el-table-column      
      prop="name"
      label="Name"
      width="120">
    </el-table-column>
    <el-table-column      
      prop="gender"
      label="Gender"
      width="120">      
    </el-table-column>
    <el-table-column
      prop="age"
      label="Age"
      sortable
      width="120">
    </el-table-column>            

    <el-table-column
      prop="type"
      label="Title"      
      width="120">
    </el-table-column>            
    
    <el-table-column
      prop="patients"
      label="Responsibled Patients"      
      width="450">   

      <el-table-column            
      prop="patients[0].patient_id"
      label="ID"      
      width="50">
      </el-table-column> 
      <el-table-column
      prop="patients[0].name"
      label="Name"      
      width="100">    
      </el-table-column>     
      <el-table-column
      prop="patients[1].patient_id"
      label="ID"      
      width="50">
      </el-table-column> 
      <el-table-column
      prop="patients[1].name"
      label="Name"      
      width="100">    
      </el-table-column> 
      <el-table-column
      prop="patients[2].patient_id"
      label="ID"      
      width="50">
      </el-table-column> 
      <el-table-column
      prop="patients[2].name"
      label="Name"      
      width="100">    
      </el-table-column> 
    </el-table-column> 
  <!-- v-if="this.$store.state.user.type=='chief_nurse'"-->
    <el-table-column            
      label="Operation"
      width="120"      
      
      >
      <template slot-scope="scope">
        <el-button 
          type="danger" 
          v-if="scope.row.type !='chief_nurse'" 
          @click="removeNurse(scope.row.id)" 
          :disabled="scope.row.patients.length>0">
        Remove
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  
  <br/>
  <el-row>
    <el-col :offset="20" :span="3">
    <el-button type="primary" @click="addNewNurse()">Add new hospital nurse</el-button>  
    </el-col>
  </el-row>
</div>
</template>

<script>
import logo from "./Logo"
export default {
  name:"NurseInfo",
  components:{logo},
  data(){
  return{      
      nurses:[],
    }
  },
  methods:{
    removeNurse(id){      
      this.$axios
      .post("/deleteHospitalNurse", {
        chief_nurse_id: this.store.state.user.id,
        nurse_id:id      
      })
      .then(resp => {
        if (resp.status === 200) {
          this.$message.success("Remove successfully");
        } else {
          this.$message.error("Something wrong");
        }
      })
      .catch(error => {
        this.$message.error("Something wrong");
        console.log(error);
      });
    },
    addNewNurse(){
      this.$prompt('Enter the nurse\'s id', 'Add new hospital nurse', {
          confirmButtonText: 'confirm',
          cancelButtonText: 'cancel',                    
        }).then(({ value }) => {
          if(value == "" || value == null){
            this.$message.error("Please input the nurse's id");
            return;
          }
          this.$axios
          .post("/addHospitalNurse", {
            chief_nurse_id: this.$store.state.user.id,            
            nurse_id:value
          })
          .then(resp => {
            if (resp.status === 200) {
              this.$message({
                type: 'success',
                message: 'Add successfully',
              });          
            } else {
              this.$message.error("Something wrong, check your input and try again");
            }
          })
          .catch(error => {
            this.$message.error("Something wrong, check your input and try again");
           console.log(error);
          });          
        }).catch(() => {                
        });
    }
  },
  created(){
    this.$axios
    .post("/getNursesInfo", {
      id: this.$store.state.user.id,      
    })
    .then(resp => {
      if (resp.status === 200) {
        console.log(resp.data);
        this.nurses = resp.data;
      } else {
        console.log(error);
      }
    })
    .catch(error => {
     console.log(error);
    });
  },  
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container{
  padding:30px 20px;  
}
</style>