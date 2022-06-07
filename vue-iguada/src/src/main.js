import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import router from './router/index.js'
import ElementUI from 'element-ui'
import "element-ui/lib/theme-chalk/index.css"
import store from './store/index.js'
import axios from 'axios'
//axios.defaults.baseURL = "http://127.0.0.1:3000"
axios.defaults.timeout = 6000
    //axios.defaults.withCredentials = true
Vue.prototype.$axios = axios

Vue.config.productionTip = false

Vue.use(VueRouter)

Vue.use(ElementUI)

new Vue({
    render: h => h(App),
    router: router,
    store: store,
}).$mount('#app')