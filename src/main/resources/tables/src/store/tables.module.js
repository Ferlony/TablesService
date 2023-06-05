import { recentTopics } from "./data";
import {navigation} from "./data";
import Vuex from "vuex";


export const tables = new Vuex.Store({
    state: {
        recentTopics,
        navigation,
    },
    mutations: {
        updaterecentTopics(state, payload) {
            state.recentTopics = payload;
        },
        updaterecentNavigation(state, payload){
            state.navigation = payload
        }
    }
});
