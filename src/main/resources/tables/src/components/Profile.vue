<template>
  <div class="container">
    <header>
      <h3>
        <strong>{{currentUser.username}}</strong> Profile
      </h3>
    </header>
    <p>
      <strong>Token:</strong>
      {{currentUser.accessToken.substring(0, 20)}} ... {{currentUser.accessToken.substr(currentUser.accessToken.length - 20)}}
    </p>
    <!-- <p>
      <strong>Id:</strong>
      {{currentUser.id}}
    </p> -->
    <p>
      <strong>Email:</strong>
      {{currentUser.email}}
    </p>
    <p>
    <strong>Authorities:</strong>
    {{ currentUser.role }}
    </p>

    <!-- v-if="checkAdmin" -->
    <div> 
      <strong>Tables:</strong>
      <ul>
        <li v-for="each in currentUser.table" :key="each" class="nav-item">
          <router-link to="/home" class="nav-link" @click="setNav(each)">{{ each }} </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Profile',
  computed: {
    currentUser() {
      return this.$store.state.auth.user;   
    },
    checkAdmin() {
      if (this.currentUser && this.currentUser['role']) {
        return this.currentUser['role'].includes('admin');
      }
      return true;
    },
    currentNav(){
      return this.$store.state.tables.navigation;
    },
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }
  },
  methods: {
    setNav(data){
      this.$store.state.tables.navigation = data;
    },
  }
};
</script>
