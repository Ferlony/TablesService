<template>
  <div class="col-md-8 offset-md-2">
    <div class="container">
      <header class="text-align center-screen">
        <h3>{{ content }}</h3>
      </header>
    </div>
  </div>
</template>


<script>
import UserService from "../services/user.service";

export default {
  name: "Home",
  data() {
    return {
      content: "",
    };
  },
  mounted() {
    try {
      UserService.getPublicContent().then(
        (response) => {
          this.content = response.data;
        },
        (error) => {
          this.content =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

        }
      );
    }
    catch (e) {
      console.log(e);
      this.content = "Authtorize to view conntent";
    }
  },
};
</script>


<style scoped>
.text-align{
  text-align: center;
}

.center-screen {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  min-height: 50vh;
}
</style>
