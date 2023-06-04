<template>
   <div v-if="!currentUser" class="center-screen center-screen-no-auth">
    <h1>Authtorize to see content</h1>
  </div>

  <!-- <div v-if="currentUser && !checkAdmin" class="center-screen">
    
  </div> -->

  <div v-if="currentUser" class="center-screen">
    <TablePanel :key="chartKey" v-bind:title="fixCurrentTitle">
      <button @click="exportData">Download table</button>
      <button v-if="checkAdmin" @click="saveToDB">Save to DB</button>
      <table class="table">
        <thead>
          <tr>
            <th>№</th>
            <th>Topic</th>
            <th>Description</th>
            <th>User</th>
            <th>User Status</th>
            <th>Checked</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in tableData" v-bind:key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.topic }}</td>
            <td>{{ item.description }}</td>
            <td><textarea v-model="item.user"></textarea></td>
            <td>
              <span>{{ item.userstatus }}&nbsp;&nbsp;</span>
              <input type="checkbox" v-model="item.userstatus" 
              true-value="Done" 
              false-value="Prog">
            </td>
            <td>
              <span>{{ item.checked }}&nbsp;&nbsp;</span>
              <input v-if="checkAdmin" type="checkbox" v-model="item.checked" 
              true-value="Yes" 
              false-value="No">
            </td>
          </tr>
        </tbody>
      </table>
    </TablePanel>
  </div>
</template>


<script>
import TablePanel from "./TablePanel";

import { excelParser } from "../util/exportExcel";
import UserService from "@/services/auth-header"


export default {
  components: { TablePanel},
  props: ["tableData"],
  data() {
    var currentTitle;
    try{
      currentTitle = this.tableData[0].tabletitle;
    }
    catch(e){
      console.log(e)
      currentTitle = this.tableData.tabletitle;
    }
    return {
      tableTitle: currentTitle,
    };
  },
  methods:{
    exportData(){
      var listData = this.tableData
      var fixTitle = this.fixCurrentTitleMethod()
      excelParser().exportDataFromJSON(listData, fixTitle, null);
    },
    saveToDB(){
      var json = JSON.stringify(this.tableData)
      UserService.postTabletoBD(json)
    },
    fixCurrentTitleMethod(){
      var counter = 0;
      while (counter < 100){
        try{
          var title = this.tableData[0].tabletitle;
          return title;
        }
        catch(e){
          counter++;
          continue;
        }
      }
      return "someTitle";
    }
  },
  computed: {
    currentUser(){
      return this.$store.state.auth.user;
    },
    checkAdmin() {
      if (this.currentUser && this.currentUser['role']) {
        return this.currentUser['role'].includes('admin');
      }
      return true;
    },
    fixCurrentTitle(){
      try{
        var title = this.tableData[0].tabletitle;
        return title;
      }
      catch(e){
        console.log(e);
        return "";
      }
    },
  },
};

</script>


<style scoped>
.center-screen {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: left;
  min-height: 50vh;
  left: 10%;
  position: relative;
}

.center-screen-no-auth{
  position: relative;
  left: 30%;
}
</style>

