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

    <el-table-column            
      label="Operation"
      width="120"
      v-if="this.$store.state.user.type='cheif_nurse'"
      >
      <template slot-scope="scope">
        <el-button type="danger">Delete</el-button>
      </template>
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