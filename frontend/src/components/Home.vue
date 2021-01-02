<template>
<el-container>
    <el-aside>                
        <h1>Jaxon Liu</h1>    
        <el-menu
            default-active="patient-info"            
            @select = "handleSelect"
        >    
        <el-menu-item index="modify">
            <i class="el-icon-s-cooperation"></i>
            <span slot="title">Modify person information</span>
        </el-menu-item>

        <el-menu-item index="new-patient">
            <i class="el-icon-document-add"></i>
            <span slot="title">Import patient</span>
        </el-menu-item>

        <el-menu-item index="record-status">
            <i class="el-icon-s-claim"></i>
            <span slot="title">Record dairy status</span>
        </el-menu-item>

        <el-menu-item index = "patient-info">
            <i class="el-icon-user"></i>
            <span slot="title">Patient information</span>
        </el-menu-item>

        <el-menu-item index = "nurse-info">
            <i class="el-icon-user-solid"></i>
            <span slot="title">Nurse information</span>
        </el-menu-item>

        <el-menu-item index = "message">
            <i class="el-icon-message"></i>
            <span slot="title">Message</span>
        </el-menu-item>
    </el-menu>
    </el-aside>
    <el-main>
        <!-- todo: 4个不同的功能区 -->
        <modify-personal-info v-if="this.isModify"></modify-personal-info>

        <add-patient v-if="this.isNewPatient"></add-patient>

        <record-status v-if="this.isDairyStatus"></record-status>

        <patient-info v-if="this.isPatientInfo"></patient-info>

        <nurse-info v-if = "this.isNurseInfo"></nurse-info>
    </el-main>
</el-container>
</template>
<script>
import logo from '../components/Logo'
import AddPatient from './AddPatient.vue'
import ModifyPersonalInfo from './ModifyPersonalInfo.vue'
import NurseInfo from './NurseInfo.vue'
import PatientInfo from './PatientInfo.vue'
import RecordStatus from './RecordStatus.vue'

export default {
  name:"Home",
  components:{logo,ModifyPersonalInfo, AddPatient,RecordStatus, PatientInfo, NurseInfo},
  data(){
      return{
        isModify:false,
        isNewPatient:false,
        isPatientInfo:true,
        isNurseInfo:false,
        isDairyStatus:false,
        isMessage:false,          
      }
  },
  methods:{
       handleSelect(key, keyPath) {
           switch(key){
               case "modify":
                   this.isModify = true;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = false;
                   this.isPatientInfo = false;
                   this.isNurseInfo = false;                   
                   this.isMessage = false;
                   break;
                case "new-patient":
                   this.isModify = false;
                   this.isNewPatient = true;
                   this.isDairyStatus = false;
                   this.isPatientInfo = false;
                   this.isNurseInfo = false;                   
                   this.isMessage = false;
                   break;
                case "record-status":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = true;
                   this.isPatientInfo = false;
                   this.isNurseInfo = false;                   
                   this.isMessage = false;
                   break;
                case "patient-info":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = false;
                   this.isPatientInfo = true;
                   this.isNurseInfo = false;                   
                   this.isMessage = false;
                   break;
                case "nurse-info":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = false;
                   this.isPatientInfo = false;
                   this.isNurseInfo = true;                   
                   this.isMessage = false;
                   break;
                case "message":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = false;
                   this.isPatientInfo = false;
                   this.isNurseInfo = false;                   
                   this.isMessage = true;
                   break;                
           }        
      },      
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.el-aside{    
    padding:100px 0;
    height:100vh;    
    background-color:rgba(112, 152, 187, 0.1);
}
.el-menu{
    margin-top:50px;
    background-color:rgba(112, 152, 187, 0);
    text-align:left;
}
h1{
    font-size: 1.5rem;    
}

</style>