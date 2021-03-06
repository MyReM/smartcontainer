
import store from '@/store'

export default{
  inserted(el, binding, vnode) {
    const { value } = binding
    // const roles = store.getters && store.getters.roles
    const permissions = store.getters && store.getters.permissions

    if (value && value instanceof Array && value.length > 0) {
      const rolePermissions = value

      const hasPermission = permissions.some(permission => {
        return rolePermissions.includes(permission)
      })

      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(`need roles! Like v-permission="['admin','editor']"`)
    }
  }
}
