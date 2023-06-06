<template>
  <div class="container">
    <header>
      <div>
        <h1>Change users status</h1>
        <input v-model="username" placeholder="username" />
        <p>Username: {{ username }}</p>
        <input v-model="field" placeholder="role/table" />
        <p>Key: {{ field }}</p>
        <input v-model="data" placeholder="mod/class_name"/>
        <p>Value: {{ data }}</p>
        <p><button @click="changeUserField">Change user's field</button></p>
        <h3>{{ responseStatus }}</h3>
      </div>
      <div>
        <h1>Import Excel to DB</h1>
        <label>File
          <input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/>
        </label>
        <button v-on:click="submitFile()">Submit</button>
        <h3>{{ fileUploadStatus }}</h3>
      </div>
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
      fileUploadStatus: "",
      file: '',
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
    submitFile(){
    /*
            Initialize the form data
        */
        let formData = new FormData();

        /*
            Add the form data we need to submit
        */
        formData.append('file', this.file);

        UserService.postUploadFile(formData).then(
          (response) => {
            console.log(response.data)
            this.fileUploadStatus = response.data;
          },
          (error) => {
            console.log(error)
            this.fileUploadStatus = error;
          }
        );
      },

      /*
        Handles a change on the file upload
      */
      handleFileUpload(){
        this.file = this.$refs.file.files[0];
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

