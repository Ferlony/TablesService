<template>
  <div class="container">
    <div>
      <h1>Users:</h1>
      {{ content }}
    </div>
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
    UserService.getModeratorBoard().then(
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

