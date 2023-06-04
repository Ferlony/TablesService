<template>
  <TablePanel :key="chartKey" v-bind:title="tableTitle">
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
<!-- 
          <td>{{ item.user }}</td>
          <td>{{ item.userstatus }}</td>
          <td>{{ item.checked }}</td> -->
          
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
</template>

<script>
import TablePanel from "./TablePanel";

import { excelParser } from "../util/exportExcel";


export default {
  components: { TablePanel },
  props: ["tableData"],
  data() {
    var currentTitle = this.tableData[0].tabletitle;
    return {
      tableTitle: currentTitle,
    };
  },
  methods:{
    exportData(){
      var listData = this.tableData
      excelParser().exportDataFromJSON(listData, null, null);
    },
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
  },
};

</script>


<style scoped>
.spreadsheet {
  width: 100%;
  height: 400px;
  border: 1px solid lightgray;
}

.fileSelect {
  width: 100%;
  margin-top: 20px;
}
</style>

