<template>
<div class = "container">
    <logo></logo>
    <el-table
    :data="patients.slice((currentPage- 1)*pageSize,currentPage*pageSize)"
    stripe
    style="width: 120%">
    <el-table-column
      sortable
      prop="patientID"
      label="PatientID"
      width="105">
    </el-table-column>
    <el-table-column      
      prop="patientName"
      label="Patient Name"
      width="110">
    </el-table-column>
    <el-table-column      
      prop="gender"
      label="Gender"
      width="80">      
    </el-table-column>
    <el-table-column
      prop="age"
      label="Age"
      sortable
      width="90">
    </el-table-column>
    <el-table-column
      prop="bed"
      label="Bed"
      sortable
      width="90">
    </el-table-column>
    <el-table-column
      prop="level"
      label="Disease Level"      
      width="120">
      <template slot-scope="scope">
        <el-tag
          effect="dark"
          :type="parseType(scope.row.level)"
          disable-transitions>{{scope.row.level}}</el-tag>
      </template>
    </el-table-column>    
    <el-table-column
      prop="status"
      label="Life Status"      
      width="120">
      <template slot-scope="scope">
        <el-tag           
          :type="parseStatus(scope.row.status)"
          disable-transitions>{{scope.row.status}}</el-tag>
      </template>
    </el-table-column>    
    <el-table-column
      prop="region"
      label="Treat Region"      
      width="120">
    </el-table-column>
    <el-table-column
      prop="transfer"
      label="Wait for out"      
      width="110">
    </el-table-column>
    <el-table-column
      prop="discharge"
      label="Discharge"      
      width="130">
    </el-table-column>
  </el-table>

  <br/>

  <el-pagination
    hide-on-single-page="true"
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
  name:"PatientInfo",
  components:{logo},
  data(){
  return{
    pageSize:6,
    currentPage:1,
    total:30,
    patients:[],
    }
  },
  methods:{
    parseType(level){
      switch(level){
        case 'Light':
          return 'success';
        case 'Severe':
          return 'warning';
        case 'Critical':
          return 'danger';
      }      
    },
    parseStatus(status){
      switch(status){
        case 'Recoveried':
          return 'success';
        case 'Treating':
          return 'warning';
        case 'Dead':
          return 'info';
      }
    }
  },
  mounted(){
    for (var i = 1; i < 30; i++) {
      this.patients.push({
        patientID:i,      
        patientName:"Zhang San"+i,
        gender:i%2==0?'Male':'Female',
        age:i,
        bed:i,
        level:i%2==0?'Severe':'Critical',
        status:i%2==0?'Treating':'Dead',
        region:i%2==0?'Light':'Wating',
        transfer:i%2==0?'yes':'no',
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