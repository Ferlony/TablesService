<template>
    <div class="header">
        <h1>{{header_title}}</h1>
        <span class="user_circle">
            <img alt="User Circle" src="../assets/icons8-male-user-64.png" img> <!--Should be clicked + drop menu-->
        </span>
    </div>
</template>

<script>
export default{
    name: "MyHeader",
    props: {
        header_title: String
    }
}
</script>

<style scoped>
.header{
    position: fixed;
    z-index: 1;
    background-color: rgb(243, 126, 17);
    border-bottom: 3px inset rgb(243, 126, 17);
    align-items: center;
    justify-content: center;
    top: 0px;
    left: 0px;    
    width: 100%;
    color: #FFFFFF;
    display: flex;
}

.user_circle{
    position:fixed;
    left: 95%;
}

</style>
