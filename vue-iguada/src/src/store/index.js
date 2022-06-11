import Vuex from 'vuex'
import Vue from 'vue'

Vue.use(Vuex);
/* 用于响应组件中的动作 */
const actions = {
    setHasLogin(context, value) {
        context.commit('setHasLogin', value);
    },
    setIsManager(context, value) {
        context.commit('setIsManager', value);
    },
    setUserName(context, value) {
        context.commit('setUserName', value);
    },
    setEmail(context, value) {
        context.commit('setEmail', value);
    },
    setUserId(context, value) {
        context.commit("setUserId", value);
    },
    setId(context, value) {
        context.commit('setId', value);
    }
};
/* 用于操作数据 */
const mutations = {
    setHasLogin(state, value) {
        state.hasLogin = value;
        localStorage.setItem('hasLogin', state.hasLogin);
    },
    setIsManager(state, value) {
        state.isManager = value;
        localStorage.setItem("isManager", state.isManager);
    },
    setUserName(state, value) {
        state.userName = value;
        localStorage.setItem("userName", state.userName);
    },
    setEmail(state, value) {
        state.email = value;
        localStorage.setItem("email", state.email);
    },
    setUserId(state, value) {
        state.userId = value;
        localStorage.setItem("userId", state.userId);
    },
    setId(state, value) {
        state.Id = value;
        localStorage.setItem("Id", state.Id);
    }
};
/* 用于存储数据 */
const state = {
    publicKey: 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIF5IbJvY2JmmH49jlUCsYhycBaq9U5oYy3Ooek7Os4T0lcE7KBTXcFxT0afQeZCt289yYxurQ1149MOOuycX00CAwEAAQ',
    hasLogin: false,
    isManager: false,
    userName: '',
    email: '',
    userId: '',
    status: 0,
    Id: '',
};

let hasLogin = localStorage.getItem('hasLogin');
let isManager = localStorage.getItem('isManager');
let userName = localStorage.getItem('userName');
let email = localStorage.getItem('email');
let userId = localStorage.getItem('userId');
let Id = localStorage.getItem('Id');
if (hasLogin != null) {
    if (hasLogin == 'true') {
        state.hasLogin = true;
    } else {
        state.hasLogin = false;
    }
}
if (isManager != null) {
    if (isManager == 'true') {
        state.isManager = true;
    } else {
        state.isManager = false;
    }
}
if (userName != null) {
    state.userName = userName;
}
if (email != null) {
    state.email = email;
}
if (userId != null) {
    state.userId = userId;
}
if (Id != null) {
    state.Id = Id;
}
const store = new Vuex.Store({ actions, mutations, state });
export default store;