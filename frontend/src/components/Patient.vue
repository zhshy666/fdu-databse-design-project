<template>
<div >
  <p>Name: this is a name</p>       
</div>
</template>
<script>

export default {
  name:"Patient",
  components:{},
  props:["patientId"],
  data(){
  return{
    }
  },
  methods:{
  },
  created(){
      this.$axios
      .post("/getPatientInfo", {
        id: this.$store.state.user.id,
        patient_id:this.patientId
      })
      .then(resp => {
        if (resp.status === 200) {
            console.log(resp.data);
        } else {      
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
</style>