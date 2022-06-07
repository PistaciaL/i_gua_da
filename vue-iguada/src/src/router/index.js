import VueRouter from 'vue-router'

const Index = () =>
    import ('@/components/Index');
const User = () =>
    import ('@/components/UserPage');
const UserInfo = () =>
    import ('@/components/user/UserInfo');
const PassWord = () =>
    import ('@/components/user/PassWord');
const BusTime = () =>
    import ('@/components/user/BusTime');
const Order = () =>
    import ('@/components/user/Order');
const Manager = () =>
    import ('@/components/ManagerPage');
const BusManage = () =>
    import ('@/components/manager/BusManage');
const UserManage = () =>
    import ('@/components/manager/UserManage');
const NoticeManage = () =>
    import ('@/components/manager/NoticeManage');
const ResetPassword = () =>
    import ('@/components/ResetPassword');
const UserMessage = () =>
    import ('@/components/user/Message');
const Communication = () =>
    import ('@/components/Communication');
const ManagerMessage = () =>
    import ('@/components/manager/Message');

const router = new VueRouter({
    routes: [{
        path: '/index',
        component: Index,
        children: [],
    }, {
        path: '/user',
        component: User,
        children: [{
            path: 'userInfo',
            component: UserInfo,
            children: [],
        }, {
            path: 'password',
            component: PassWord,
            children: [],
        }, {
            path: 'busTime',
            component: BusTime,
            children: [],
        }, {
            path: 'order',
            component: Order,
            children: [],
        }, {
            path: 'message',
            component: UserMessage,
            children: []
        }, {
            path: 'communication',
            component: Communication,
            children: []
        }],
    }, {
        path: '/manager',
        component: Manager,
        children: [{
            path: 'busManage',
            component: BusManage,
            children: [],
        }, {
            path: 'userManage',
            component: UserManage,
            children: [],
        }, {
            path: 'noticeManage',
            component: NoticeManage,
            children: [],
        }, {
            path: 'message',
            component: ManagerMessage,
            children: []
        }, {
            path: 'communication',
            component: Communication,
            children: []
        }],
    }, {
        path: '/reset',
        component: ResetPassword,
        children: [],
    }]
})

router.beforeEach((to, from, next) => {
    if (to.path === '/') {
        router.push('/index');
    } else if (to.path.indexOf('/user') == 0) {
        let power = localStorage.getItem("hasLogin");
        if (power != 'false' && power != null) {
            next();
        } else {
            router.push('/index');
        }
    } else if (to.path.indexOf('/manager') == 0) {
        let power = localStorage.getItem("isManager");
        if (power != 'false' && power != null) {
            next();
        } else {
            router.push(from);
        }
    } else {
        next();
    }
})

/* 放在跳转到当前页面相同的路由报错 */
const VueRouterPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(to) {
    return VueRouterPush.call(this, to).catch(err => err)
}
export default router