<template>
<div class="container">
    <logo></logo>
    <el-table
    :data="nurses.slice((currentPage- 1)*pageSize,currentPage*pageSize)"
    stripe
    style="width: 120%">
    <el-table-column
      sortable
      prop="ID"
      label="ID"
      width="60">
    </el-table-column>
    <el-table-column      
      prop="Name"
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
      prop="title"
      label="Title"      
      width="120">
    </el-table-column>            
    
    <el-table-column
      prop="patients"
      label="Responsibled Patients"      
      width="450">      
      <el-table-column
      prop="patientID1"
      label="ID"      
      width="50">
      </el-table-column> 
      <el-table-column
      prop="patientName1"
      label="Name"      
      width="100">    
      </el-table-column>     
      <el-table-column
      prop="patientID2"
      label="ID"      
      width="50">
      </el-table-column> 
      <el-table-column
      prop="patientName2"
      label="Name"      
      width="100">    
      </el-table-column> 
      <el-table-column
      prop="patientID3"
      label="ID"      
      width="50">
      </el-table-column> 
      <el-table-column
      prop="patientName3"
      label="Name"      
      width="100">    
      </el-table-column> 
    </el-table-column>            
  </el-table>

  <br/>

  <el-pagination
    :hide-on-single-page="true"
    :page-size = "pageSize"    
    :current-page.sync="currentPage"   
    layout="prev, pager, next"
    :total="total">
  </el-pagination>
</div>
</template>

<script>
import logo from "./Logo"
export default {
  name:"NurseInfo",
  components:{logo},
  data(){
  return{
      currentPage:1,
      pageSize:6,
      total:4,
      nurses:[],
    }
  },
  methods:{
  },
  created(){
    this.$axios
    .post("/getNursesInfo", {
      id: this.$store.state.user.id,      
    })
    .then(resp => {
      if (resp.status === 200) {
        console.log(resp.data);
      } else {
        console.log(error);
      }
    })
    .catch(error => {
     console.log(error);
    });
  },
  mounted(){
    for (var i = 1; i < 5; i++) {
      this.nurses.push({
        ID:i,      
        Name:"Nurse "+i,
        gender:i%2==0?'Male':'Female',
        age:i,
        title:i==1?"chief nurse":"hospital nurse",
        patientID1:1,
        patientName1:"LIU",
        patientID2:2,
        patientName2:"LIU",
        patientID3:3,
        patientName3:"LIU",
      })      
    }    
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container{
  padding:30px 20px;  
}
</style>