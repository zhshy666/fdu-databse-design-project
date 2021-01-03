import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        // id: localStorage.getItem('id') || null,
        // name: localStorage.getItem('name') || null,
        // title: localStorage.getItem('title') || null
        user: localStorage.getItem('user') || null
    },
    mutations: {
        login(state, data) {
            // localStorage.setItem('id', data.id)
            // localStorage.setItem('name', data.name)
            // localStorage.setItem('title', data.type)
            // state.id = data.id;
            // state.name = data.name;
            // state.title = data.type;
            localStorage.setItem('user', data)
            state.user = data;
        },
        logout(state) {
            localStorage.removeItem('id')
            localStorage.removeItem('name')
            localStorage.removeItem('title')
            state.id = null
            state.name = null
            state.title = null
        }
    },
    actions: {}
})