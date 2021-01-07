
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
      width="89">
    </el-table-column>

    <el-table-column      
      sortable
      prop="name"
      label="Name"
      width="120"
      >      
      <template slot-scope="scope">        
        <p>{{scope.row.name}}</p>
      </template>
    </el-table-column>
    
    <el-table-column      
      prop="gender"
      label="Gender"
      width="110"
      :filters="[{ text: 'male', value: 'male' }, { text: 'female', value: 'female'}]"
      :filter-method="filterGender"
      filter-placement="bottom-end">      
    </el-table-column>

    <el-table-column
      prop="age"
      label="Age"
      sortable
      width="100">
    </el-table-column>    

    <el-table-column
      prop="disease_level"
      label="Disease Level"      
      width="130"
      :filters="[{ text: 'light', value: 'light' }, { text: 'severe', value: 'severe'},{text:'critical',value:'critical'}]"
      :filter-method="filterDiseaseLevel"
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
      :filters="[{ text: 'healthy', value: 'healthy' }, { text: 'treating', value: 'treating'},{text: 'dead', value: 'dead'}]"
      :filter-method="filterLifeStatus"
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
      width="120">
      <template slot-scope="scope">    
        <div v-if="scope.row.need_transfer == 0">no</div>
        <div v-else>yes</div>
      </template>
    </el-table-column>

    <el-table-column
      prop="discharge"
      label="Discharge?"      
      :filters="[{ text: 'yes', value: '1' }, { text: 'no', value: '0'}]"
      :filter-method="filterDischarge"
      filter-placement="bottom-end"  
      width="120">    
      <template slot-scope="scope">    
        <!-- <el-button size="small" v-if="scope.row.can_be_discharged == 1 && scope.row.disease_level == 'light'" @click="permitDischarge(scope.row.patient_id)">Permit</el-button> -->
        <div v-if ="scope.row.can_be_discharged == 1" >yes</div>
        <div v-else>no</div>
      </template>
    </el-table-column>

    <el-table-column      
      label=""      
      width="130" v-if="this.$store.state.user.type == 'doctor'">
      <template slot-scope="scope">            
        <el-button @click="lookUpPatient(scope.row.patient_id)">See more</el-button>
      </template>
    </el-table-column>

  </el-table>    
  
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
        case 'healthy':
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
    filterLifeStatus(value, row) {      
      return row.life_status === value;
    },
    filterDiseaseLevel(value,row){
      return row.disease_level === value;
    },
    filterTransfer(value,row){
      return row.need_transfer == value;
    },
    filterDischarge(value,row){
      return row.can_be_discharged == value;
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
    getAllPatientsInfo(){
      this.$axios
      .post("/getAllPatientsInfo", {
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
    getRelatedPatientsInfo(){

    },
    lookUpPatient(id){
      this.specifiedPatientId=id;
    },
    permitDischarge(id){
      this.$axios
      .post("/permitDischarged", {
        doctor_id: this.$store.state.user.id,
        patient_id:id,   
      })
      .then(resp => {
        if (resp.status === 200) {
          this.$message.success("Opetate successfully");
        } else {
          this.$message.error("Something wrong");      
        }
      })
      .catch(error => {
        this.$message.error("Something wrong");      
      });
    }
    // addNewChecklist(id){            
    //   this.$axios
    //   .post("/newChecklist", {
    //     doctor_id: this.$store.state.user.id,
    //     patient_id:id
    //   })
    //   .then(resp => {
    //     if (resp.status === 200) {
    //       this.$message.success("Add successfully!");
    //     } else {
    //       this.$message.error("Something wrong!");
    //       console.log(error);
    //     }
    //   })
    //   .catch(error => {
    //     this.$message.error("Something wrong!");
    //     console.log(error);
    //   });
    // }
  },

  created(){  
    switch(this.$store.state.user.type) {
      case 'chief_nurse':
      case 'doctor':
        this.getPatientInfo();
        return;
      case 'emergency_nurse':
        this.getAllPatientsInfo();
        return;
      case 'hosputal_nurse':
        this.getRelatedPatientsInfo();
        return;
    }    
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