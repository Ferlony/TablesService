<template>
  <div class="container">
    <header>
      <h1>Change users status</h1>
      <input v-model="username" placeholder="username" />
      <p>Username: {{ username }}</p>
      <input v-model="field" placeholder="role/table" />
      <p>Key: {{ field }}</p>
      <input v-model="data" placeholder="mod/class_name"/>
      <p>Value: {{ data }}</p>
      <p><button @click="changeUserField">Change user's field</button></p>
      <h3>{{ responseStatus }}</h3>
    </header>
    <div>
      <h1>Users:</h1>
      {{ content }}
    </div>
  </div>
</template>

<script>
import UserService from "../services/auth-header";

export default {
  name: "Admin",
  data() {
    return {
      content: "",
      responseStatus: "",
      username: "",
      field: "",
      data: "",
    };
  },
  methods:{
    changeUserField(){
      var json = ("{" + this.field + ":" + this.data + "," + "username:" + this.username + "}");
      UserService.postChangeUserField(json).then(
        (response) => {
          this.responseStatus = response.data;
        },
        (error) => {
          console.log(error);
          this.responseStatus = error;
        },
      );
    },
  },    
  mounted() {
    UserService.getAdminBoard().then(
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

