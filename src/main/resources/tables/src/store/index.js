import { createStore } from "vuex";
import { auth } from "./auth.module";
import { tables } from "./tables.module";

const store = createStore({
  modules: {
    auth,
    tables
  },
});

export default store;
