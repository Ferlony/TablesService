<template>
  <div class="container">
    <header class>
      <h3>{{ content }}</h3>
    </header>
  </div>
</template>

<script>
import UserService from "../services/auth-header";

export default {
  name: "Mod",
  data() {
    return {
      content: "",
    };
  },
  mounted() {
    UserService.getUserBoard().then(
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
  },
};
</script>

