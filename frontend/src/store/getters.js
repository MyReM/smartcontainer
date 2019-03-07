const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  userCode: state => state.user.userCode,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  permission_routers: state => state.permission.routers,
  addRouters: state => state.permission.addRouters
}
export default getters

// userCode 被用到的地方：src/views/layout/Layout.vue
