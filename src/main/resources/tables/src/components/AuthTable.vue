<template>
    <div class="position">
        <table>
            <tr>
                <th><h1>Authtorize</h1></th>
            </tr>
            <tr>
                <th>Email</th>
            </tr>
            <tr>
                <td><input v-model="email"></td> 
            </tr>
            <tr>
                <th>Password</th>
            </tr>
            <tr>
                <td><input v-model="password"></td> 
            </tr>
            <tr>
                <td><button @click="logIn">Log In</button></td>
                <td><button @click="signUp">Sign Up</button></td>
            </tr>
        </table>
    </div> 
</template>

<script>
export default{
    name: "AuthTable",
    data(){
        return{
            email: "",
            password: "",
        }
    },
    methods: {
        signUp(){
            return
        },
        logIn(){
            return
        }
    }

}
</script>

<style scoped>
.position{
    position: relative;
    padding-top: 10%;
    padding-left: 45%;

}

.button_position{
    position: flex;
}


</style>
