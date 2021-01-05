
<template>
<div class = "container">
    <logo></logo>
    <el-table
    :data="patients"
    max-height="510"
    stripe
    style="width: 120%">
    <el-table-column
      sortable
      prop="patient_id"
      label="ID"
      width="60">
    </el-table-column>
    <el-table-column      
      sortable
      prop="name"
      label="Patient Name"
      width="150">
    </el-table-column>
    <el-table-column      
      prop="gender"
      label="Gender"
      width="100"
      :filters="[{ text: 'male', value: 'male' }, { text: 'female', value: 'female'}]"
      :filter-method="filterGender"
      filter-placement="bottom-end">      
    </el-table-column>
    <el-table-column
      prop="age"
      label="Age"
      sortable
      width="90">
    </el-table-column>    
    <el-table-column
      prop="disease_level"
      label="Disease Level"      
      width="130"
      :filters="[{ text: 'light', value: 'light' }, { text: 'severe', value: 'severe'},{text:'critical',value:'critical'}]"
      :filter-method="filterGender"
      filter-placement="bottom-end">
      <template slot-scope="scope">
        <el-tag
          effect="dark"
          :type="parseType(scope.row.disease_level)"
          disable-transitions>{{scope.row.disease_level}}</el-tag>
      </template>
    </el-table-column>    
    <el-table-column
      prop="status"
      label="Life Status"      
      width="120"
      :filters="[{ text: 'recoveried', value: 'recoveried' }, { text: 'treating', value: 'treating'},{text: 'dead', value: 'dead'}]"
      :filter-method="filterTag"
      filter-placement="bottom-end">
      <template slot-scope="scope">
        <el-tag           
          :type="parseStatus(scope.row.life_status)"
          disable-transitions>{{scope.row.life_status}}</el-tag>
          <el-input
            class="input-new-tag"
            v-if="modifyInput"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
          ></el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Topic</el-button>
                
      </template>
    </el-table-column>    
    <el-table-column
      prop="treatment_region_level"
      label="Treat Region"      
      width="120">
    </el-table-column>
    <el-table-column
      prop="need_transfer"
      label="Need Trans"      
      width="110">
      <template slot-scope="scope">    
        <div v-if="scope.row.need_transfer == 0">no</div>
        <div v-else>yes</div>
      </template>
    </el-table-column>
    <el-table-column
      prop="discharge"
      label="Discharge?"      
      width="100">    
      <template slot-scope="scope">    
        <el-button size="small" v-if="scope.row.can_be_discharged == 1 && scope.row.disease_level == 'light'">Permit</el-button>        
        <div v-else>no</div>
      </template>
    </el-table-column>
    <el-table-column      
      label="Wait for out"      
      width="110">
      <template slot-scope="scope">    
        <el-button size="small">New Test</el-button>        
      </template>
    </el-table-column>
  </el-table>    

</div>
</template>
<script>
import logo from "./Logo"
export default {
  name:"PatientInfo",
  components:{logo},
  data(){
  return{
    // pageSize:6,
    // currentPage:1,
    patients:[],
    // total:0,    
    }
  },
  methods:{
    parseType(level){
      switch(level){
        case 'Light':
          return 'info';
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
    },
    filterGender(value,row){
      return row.gender === value;
    },
    filterTag(value, row) {      
      return row.life_status === value;
    },
    getPatientInfo(condition){            
      this.$axios
      .post("/getPatientsInfo", {
        id: this.$store.state.user.id,
        condition:condition,
      })
      .then(resp => {
        if (resp.status === 200) {
          console.log(resp.data);
          this.patients = resp.data;
          this.total = this.patients.length;          
        } else {
          this.$message.error("Error occurs when geting patient info");
          console.log(error);
        }
      })
      .catch(error => {
        this.$message.error("Error occurs when geting patient info");
        console.log(error);
      });      
    }
  },
  created(){   
    this.getPatientInfo("0");     
    // for(let i=0;i<20;i++){
    //   this.patients.push({
    //     patient_id:i,
    //     name:"Wang Badan"+i,
    //     age:12,
    //     gender:"male",
    //     life_status:"treating",
    //     disease_level:"Light",
    //     treatment_region_level:"Light",
    //     wait_for_out:"no",
    //     can_be_discharged:i%2,
    //   })
    // }
    console.log(this.patients);
  }  
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container{
  padding:30px 20px;
}
.input-new-tag {
  width: 103px;
  margin-right: 5px;
  vertical-align: bottom;
}
</style>