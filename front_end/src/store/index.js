import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || null,
    isAdmin: false,
    username: localStorage.getItem('username') || null,
    modelPath: localStorage.getItem('modelPath') || null,
  },
  mutations: {
    login (state, data) {
      localStorage.setItem('token', data.token)
      localStorage.setItem('username',data.username)
      localStorage.setItem('modelPath', "models/character1.obj")
      state.token = data.token
      state.username = data.username
    },
    logout (state) {
      // 移除token
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      state.token = null
      state.isAdmin = false
    }
  },
  actions: {
  }
})
