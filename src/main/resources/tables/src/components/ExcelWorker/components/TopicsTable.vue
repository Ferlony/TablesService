<template>
  <TablePanel :key="chartKey" v-bind:title="tableTitle">
    <gc-spread-sheets :hostClass='hostClass' @workbookInitialized='workbookInit'>
      <gc-worksheet :dataSource='tableData' :name="sheetName" :autoGenerateColumns='autoGenerateColumns'>
        <gc-column
            :width='50'
            :dataField="'id'"
            :headerText="'ID'"
            :visible = 'visible'
            :resizable = 'resizable'
          ></gc-column>
        <gc-column
            :width='200'
            :dataField="'topic'"
            :headerText="'Topic'"
            :visible = 'visible'
            :resizable = 'resizable'
          ></gc-column>
        <gc-column
            :width="320"
            :dataField="'description'"
            :headerText="'Description'"
            :visible = 'visible'
            :resizable = 'resizable'
          ></gc-column>
        <gc-column
            :width="100"
            :dataField="'user'"
            :headerText="'User'"
            :visible = 'visible'
            :formatter = 'priceFormatter'
            :resizable = 'resizable'
          ></gc-column>
          <gc-column
            :width="100"
            :dataField="'userstatus'"
            :headerText="'Userstatus'"
            :visible = 'visible'
            :resizable = 'resizable'
          ></gc-column>
          <gc-column
            :width="100"
            :dataField="'checked'"
            :headerText="'Checked'"
            :visible = 'visible'
            :resizable = 'resizable'
          ></gc-column>
          <!-- <gc-column
            :width="100"
            :dataField="'tabletitle'"
            :headerText="'Tabletitle'"
            :visible = 'visible'
            :resizable = 'resizable'
          ></gc-column> -->
      </gc-worksheet>
    </gc-spread-sheets>
    <div class="dashboardRow">
      <button class="btn btn-primary dashboardButton" @click="exportSheet">Export to Excel</button>
      <div>
        <b>(visible for admin only)</b><br>
        <b>Import Excel File:</b>
        <div>
          <input v-if="checkAdmin" type="file" class="fileSelect" @change='fileChange($event)' />
          <!-- <input type="file" class="fileSelect" @change='fileChange($event)' /> -->
        </div>
      </div>
    </div>
  <!-- </TablePanel> -->

  <!-- <TablePanel v-bind:title="tableTitle"> -->
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

import "@grapecity/spread-sheets/styles/gc.spread.sheets.excel2016colorful.css";
// SpreadJS imports
import "@grapecity/spread-sheets-vue";
import Excel from "@grapecity/spread-excelio";
import { saveAs } from 'file-saver';
import { extractSheetData } from "../util/util";

export default {
  components: { TablePanel },
  props: ["tableData"],
  data() {
    var currentTitle = this.tableData[0].tabletitle;
    return {
      tableTitle: currentTitle,
      sheetName: 'TablesData',
      hostClass: 'spreadsheet',
      autoGenerateColumns: true,
      width: 200,
      visible: true,
      resizable: true,
      priceFormatter: "$ #.00",
      chartKey: 1
    };
  },
  methods: {
    workbookInit: function (spread) {
      this._spread = spread;
    },
    fileChange: function (e) {
      if (this._spread) {
        const fileDom = e.target || e.srcElement;
        const excelIO = new Excel.IO();
        // const spread = this._spread;
        const store = this.$store;

        /*const deserializationOptions = {
          frozenRowsAsColumnHeaders: true
        };*/

        excelIO.open(fileDom.files[0], (data) => {
          console.dir(extractSheetData(data));
          const newTablesData = extractSheetData(data);
          store.commit('updaterecentTopics', newTablesData)
        });
      }
    },
    exportSheet: function () {
      const spread = this._spread;
      const fileName = "TablesData.xlsx";

      // const sheet = spread.getSheet(0);
      const excelIO = new Excel.IO();
      const json = JSON.stringify(spread.toJSON({
        includeBindingSource: true,
        columnHeadersAsFrozenRows: true,
      }));

      excelIO.save(json, (blob) => {
        saveAs(blob, fileName);
      }, function (e) {
        alert(e);
      });
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

