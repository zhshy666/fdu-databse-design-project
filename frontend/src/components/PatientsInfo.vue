
<template>
<div class = "container">
    <logo></logo>   
    <el-table
    v-if="this.specifiedPatientId==null"
    :data="patients"
    max-height="510"
    stripe
    >

    <el-table-column
      sortable
      prop="patient_id"
      label="ID"
      width="60">
    </el-table-column>

    <el-table-column      
      sortable
      prop="name"
      label="Name"
      width="120">      
      <template slot-scope="scope">
        <p style="cursor:pointer" @click="lookUpPatient(scope.row.patient_id)">{{scope.row.name}}</p>
      </template>
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
      :filters="[{ text: 'yes', value: '1' }, { text: 'no', value: '0'}]"
      :filter-method="filterTransfer"
      filter-placement="bottom-end"    
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
      label="Checklist"      
      width="130">
      <template slot-scope="scope">    
        <el-button @click="addNewChecklist(scope.row.patient_id)" size="small">New Checklist</el-button>        
      </template>
    </el-table-column>

  </el-table>    

  <!-- todo: modify the condition here -->
  <patient :patientId="this.specifiedPatientId" v-else></patient>  
</div>
</template>
<script>
import logo from "./Logo"
import Patient from './Patient.vue'
export default {
  name:"PatientsInfo",
  components:{logo,Patient},
  data(){
  return{    
    patients:[],    
    specifiedPatientId:null,
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
    filterTransfer(value,row){
      return row.need_transfer == value;
    },
    getPatientInfo(){            
      this.$axios
      .post("/getPatientsInfo", {
        id: this.$store.state.user.id,        
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
    },
    lookUpPatient(id){
      this.specifiedPatientId=id;
    },
    addNewChecklist(id){            
      this.$axios
      .post("/newChecklist", {
        doctor_id: this.$store.state.user.id,
        patient_id:id
      })
      .then(resp => {
        if (resp.status === 200) {
          this.$message.success("Add successfully!");
        } else {
          this.$message.error("Something wrong!");
          console.log(error);
        }
      })
      .catch(error => {
        this.$message.error("Something wrong!");
        console.log(error);
      });
    }
  },

  created(){   
    this.getPatientInfo();             
  }  
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container{
  margin-left:250px;
  padding:30px 5px;
}
.input-new-tag {
  width: 103px;
  margin-right: 5px;
  vertical-align: bottom;
}
</style>