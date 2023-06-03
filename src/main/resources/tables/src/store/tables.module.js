import { recentTopics } from "./data";
import Vuex from "vuex";


export const tables = new Vuex.Store({
    state: {
        recentTopics
    },
    mutations: {
        updaterecentTopics(state, payload) {
            state.recentTopics = payload;
        }
    }
});
