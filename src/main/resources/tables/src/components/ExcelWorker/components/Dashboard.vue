<template>
    <NavBar/>
    <div class="container">
      <div class="row">
        <SalesTable :tableData="salesTableData"/>
      </div>
    </div>
</template>

<script>
import NavBar from "./NavBar";
import SalesTable from "./TopicsTable";

export default {
  components: { NavBar, SalesTable},
  computed: {
    salesTableData() {
      return this.$store.state.recentSales;
    },
  }
};

</script>
