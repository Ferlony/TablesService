<template>
    <div>
        <h1>TestPage</h1>
        <p>Send To test Back</p>
        <p>{{ msg }}</p>
        <p><input type="text" name="email" v-model="email"/></p>
        <p><input type="text" name="password" v-model="password"/></p>
        <button v-on:click="addUser">Add new user</button>
    </div>
</template>

<script>
import axios from "axios";
export default {
    name: "TestPage",
    testPageData(){
        return{
            email: "",
            password: "",
        };
    },
    methods: {
        async addUser(){
            let result = await axios.post("http://localhost:8050",{
                email: this.email,
                password: this.password,
            }); 
            console.warn(result).then(function(response){
                console.log(response.data)
                console.log(response.status);
                console.log(response.statusText);
                console.log(response.headers);
                console.log(response.config);
            });
        },
    },
};
</script>
