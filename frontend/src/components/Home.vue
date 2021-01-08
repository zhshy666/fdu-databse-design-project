<template>
<el-container>
    <el-aside style="width:245px">                 
        <h1>{{this.$store.state.user.name}}</h1>            
        <h3>{{this.$store.state.user.type.toUpperCase()}}&nbsp;{{this.$store.state.user.id}}</h3>        
        <el-menu
            default-active="patient-info"            
            @select = "handleSelect"
        >    
        <el-menu-item index="modify">
            <i class="el-icon-s-cooperation"></i>
            <span slot="title">Modify person information</span>
        </el-menu-item>

        <el-menu-item index="new-patient" v-if="this.isEmergencyNurse">        
            <i class="el-icon-document-add"></i>
            <span slot="title">Import patient</span>
        </el-menu-item>

        <el-menu-item index="record-status" v-if="this.isHospitalNurse">
            <i class="el-icon-s-claim"></i>
            <span slot="title">Record patient status</span>
        </el-menu-item>

        <el-menu-item index = "checklist" v-if="this.isHospitalNurse">
            <i class="el-icon-tickets"></i>
            <span slot="title">Record Check Result</span>
        </el-menu-item>

        <el-menu-item index = "patient-info" >
            <i class="el-icon-user"></i>
            <span slot="title">Patient information</span>
        </el-menu-item>

        <el-menu-item index = "nurse-info" v-if="this.isDoctor||this.isChiefNurse">
            <i class="el-icon-user-solid"></i>
            <span slot="title">Nurse information</span>
        </el-menu-item>

        <el-menu-item index="beds-info" v-if="this.isChiefNurse">
            <i class="el-icon-c-scale-to-original"></i>
            <span slot="title">Beds information</span>
        </el-menu-item>        

        
        <el-menu-item index = "message" v-if="!this.isEmergencyNurse">            
            <i class="el-icon-message"></i>                        
            <span slot="title">
                <el-badge :is-dot="hasMessage" class="badge">Message</el-badge>
            </span>            
        </el-menu-item>        

        <el-menu-item index = "logout">  
            
            <i class="el-icon-circle-close"></i>
            <span slot="title">Logout</span>
        </el-menu-item>
    </el-menu>
    </el-aside>
    <el-main>        
        <modify-personal-info v-if="this.isModify"></modify-personal-info>

        <add-patient v-if="this.isNewPatient"></add-patient>

        <record-status v-if="this.isDairyStatus"></record-status>

        <record-checklist v-if="this.isChecklist"></record-checklist>

        <patients-info v-if="this.isPatientInfo"></patients-info>

        <nurse-info v-if="this.isNurseInfo"></nurse-info>

        <beds-info v-if="this.isBedsInfo"></beds-info>   

        <message  v-if="this.isMessage" @hasMessage="isMessageUpdate"></message>
        
    </el-main>
</el-container>
</template>
<script>
import logo from '../components/Logo'
import AddPatient from './AddPatient.vue'
import BedsInfo from './BedsInfo.vue'
import Message from './Message.vue'
import ModifyPersonalInfo from './ModifyPersonalInfo.vue'
import NurseInfo from './NurseInfo.vue'
import PatientsInfo from './PatientsInfo.vue'
import RecordChecklist from './RecordChecklist.vue'
import RecordStatus from './RecordStatus.vue'

export default {
  name:"Home",
  components:{logo,ModifyPersonalInfo, AddPatient,RecordStatus, PatientsInfo, NurseInfo, BedsInfo,RecordChecklist,Message},
  data(){
      return{ 
        isDoctor:false,
        isChiefNurse:false,
        isEmergencyNurse:false,
        isHospitalNurse:false,

        isModify:false,
        isNewPatient:false,
        isPatientInfo:true,
        isNurseInfo:false,
        isDairyStatus:false,
        isMessage:false,  
        isBedsInfo:false,        
        isChecklist:false,

        hasMessage:false,
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
                   this.isBedsInfo = false;
                   this.isChecklist = false;
                   break;
                case "new-patient":
                   this.isModify = false;
                   this.isNewPatient = true;
                   this.isDairyStatus = false;
                   this.isPatientInfo = false;
                   this.isNurseInfo = false;                   
                   this.isMessage = false;
                   this.isBedsInfo = false;
                   this.isChecklist = false;
                   break;
                case "record-status":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = true;
                   this.isPatientInfo = false;
                   this.isNurseInfo = false;                   
                   this.isBedsInfo = false;
                   this.isMessage = false;
                   this.isChecklist = false;
                   break;
                case "patient-info":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = false;
                   this.isPatientInfo = true;
                   this.isNurseInfo = false;                   
                   this.isBedsInfo = false;
                   this.isMessage = false;
                   this.isChecklist = false;
                   break;
                case "nurse-info":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = false;
                   this.isPatientInfo = false;
                   this.isNurseInfo = true;                   
                   this.isBedsInfo = false;
                   this.isMessage = false;
                   this.isChecklist = false;
                   break;
                case "message":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = false;
                   this.isPatientInfo = false;
                   this.isNurseInfo = false;                   
                   this.isBedsInfo = false;
                   this.isMessage = true;
                   this.isChecklist = false;
                   break;                
                case "beds-info":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = false;
                   this.isPatientInfo = false;
                   this.isNurseInfo = false;                   
                   this.isBedsInfo = true;
                   this.isMessage = false;
                   this.isChecklist = false;
                   break;                   
                case "checklist":
                   this.isModify = false;
                   this.isNewPatient = false;                   
                   this.isDairyStatus = false;
                   this.isPatientInfo = false;
                   this.isNurseInfo = false;                   
                   this.isBedsInfo = false;
                   this.isMessage = false;
                   this.isChecklist = true;
                   break;
                case "logout":
                    this.$store.commit("logout");
                    this.$router.push("/login"); 
           }        
      }, 
      isMessageUpdate(v){
          console.log(v);
          this.hasMessage = v;
      }           
  },
  created(){
      switch(this.$store.state.user.type){
        case "doctor":
            this.isDoctor = true;              
            break;
        case "chief_nurse":
            this.isChiefNurse = true;
            break;
        case "emergency_nurse":
            this.isEmergencyNurse = true;
            break;
        case "hospital_nurse":
            this.isHospitalNurse = true;
            break;        
      }
      this.$axios
      .post("/getMessages", {
        id: this.$store.state.user.id,        
      })
      .then(resp => {
        if (resp.status === 200) {            
            if(resp.data.length>0){
                for(let message of resp.data){
                    if(message.status == 0){
                        this.hasMessage=true;
                        return;
                    }
                }                
            }
        } else {
            console.log(error);      
        }
      })
      .catch(error => {
       console.log(error);
      });
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.el-aside{        
    padding:100px 0;
    height:100%;    
    background-color:rgba(112, 152, 187, 0.1);
    position: fixed;
}
.el-menu{
    margin-top:50px;
    background-color:rgba(112, 152, 187, 0);
    text-align:left;
}
h1{
    font-size: 1.5rem;    
}
.el-badge{
    line-height: 20px;    
    padding:5px;
}
</style>