<template>
<div class="container">
    <logo></logo>
    <el-table
    :data="beds"
    stripe
    max-height="510"
    style="width: 100%">
    <el-table-column
      sortable
      prop="bed_id"
      label="ID"
      width="100">
    </el-table-column>
    <el-table-column      
      prop="patient_id"
      label="Patient ID"
      width="120">
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
      <template slot-scope="scope">
          <div v-if="scope.row.age == 0"></div>
          <div v-else>{{scope.row.age}}</div>        
      </template>
    </el-table-column>            
    <el-table-column
      prop="disease_level"
      label="Disease Level"      
      width="140">
    </el-table-column> 
    <el-table-column
      prop="life_status"
      label="Life Status"      
      width="120">
    </el-table-column>
    
    <el-table-column
      prop="nurse_id"
      label="Nurse ID"      
      width="120">
    </el-table-column>            
    
    <el-table-column
      prop="treatment_region_level"
      label="Treatment Region"      
      width="150">  
    </el-table-column> 
  </el-table>
</div>
</template>
<script>
import logo from "./logo"
export default {
  name:"BedsInfo",
  components:{logo},
  data(){
  return{
      beds:[]
    }
  },
  methods:{
  },
  created(){
      this.$axios
      .post("/getBedInfo", {
        chief_nurse_id: this.$store.state.user.id,
      })
      .then(resp => {
        if (resp.status === 200) {
            console.log(resp.data);      
            this.beds = resp.data;
        } else {
            this.$message.error("Something error when getting beds info");
        }
      })
      .catch(error => {
        this.$message.error("Something error when getting beds info");
        console.log(error);
      });
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>