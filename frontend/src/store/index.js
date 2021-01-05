import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        user: localStorage.getItem('user') || null
    },
    mutations: {
        login(state, data) {
            localStorage.setItem('user', data)
            state.user = data;
        },
        logout(state) {
            localStorage.removeItem('user')
            state.user = null
        }
    },
    actions: {}
})