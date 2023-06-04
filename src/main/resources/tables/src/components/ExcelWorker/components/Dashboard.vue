<template>
    <NavBar/>
    <div class="container">
      <div class="row">
        <TopicsTable :tableData="topicsTableData"/>
      </div>
    </div>
</template>

<script>
import NavBar from "./NavBar";
import TopicsTable from "./TopicsTable";


export default {
  components: { NavBar, TopicsTable},
  computed: {
    topicsTableData() {
      return this.$store.state.tables.recentTopics;
    },
  },
};

</script>
