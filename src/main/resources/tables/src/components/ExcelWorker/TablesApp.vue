<template>
  <Dashboard />
</template>

<script>
import Dashboard from './components/Dashboard.vue'
import UserService from "@/services/auth-header"


export default {
  name: 'TablesApp',
  
  components: {
    Dashboard
  },
  mounted() {
    UserService.getPublicContent().then(
      (response) => {
        //this.$store.state.tables.recentTopics
        var result = JSON.stringify(response.data)
        this.$store.state.tables.recentTopics = JSON.parse(result);
      },
      (error) => {
        this.$store.state.tables.recentTopics = ""
          // (error.response &&
          //   error.response.data &&
          //   error.response.data.message) ||
          // error.message ||
          // error.toString();
          console.log(error);

      }
    );
  }

}
</script>

