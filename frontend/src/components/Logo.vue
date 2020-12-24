<template>
    <router-link to="/">
        <h2>SmartHospital
            <em>.</em>
        </h2>    
    </router-link>
</template>
<script>
export default {
    name:"Logo",
}
</script>
<style>
em{
    font-size: 44px;
    font-style: normal;
    color:cornflowerblue;
}
h2{
    color:black;    
    text-align: center;
    height:50px;
    font-size: 24px;
    font-weight: 900;
    text-transform: uppercase;
    cursor: pointer;
    margin:0px;
    padding:15px 0 35px 0;
}
</style>