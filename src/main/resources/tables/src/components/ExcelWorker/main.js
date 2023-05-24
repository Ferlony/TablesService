/*import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')*/

/*
import Vue from 'vue'
import Vuex from "vuex";
import App from './App.vue'
import { recentSales } from "./data/data";

import "./style.css";

Vue.config.productionTip = false;
Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    recentSales
  },
  mutations: {
    updateRecentSales(state, payload) {
      state.recentSales = payload;
    }
  }
});

new Vue({
  store,
  render: h => h(App),
}).$mount('#app')
*/

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import { createApp, h } from 'vue';
import Vuex from "vuex";
import App from './App.vue'
import { recentSales } from "./data/data";

import "./style.css";

const store = new Vuex.Store({
    state: {
        recentSales
    },
    mutations: {
        updateRecentSales(state, payload) {
            state.recentSales = payload;
        }
    }
});

const app = createApp({
    render: () => h(App)
});

app.use(store);
app.mount("#excel-app");
