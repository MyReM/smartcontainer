<template>
  <div>

    <el-card style="width: 30%; margin: 80px auto">

      <div slot="header" style="font-size: 18px; text-align: center">
        <h4>修改密码</h4>
      </div>

      <el-form label-position="left" :rules="rules" ref="form" label-width="80px" :model="temp">
        <el-form-item label="密码" prop="password">
          <el-input v-model="temp.password" :type="passwordType">
          </el-input>
          <span class="show-pwd" @click="showPwd"><svg-icon icon-class="eye" /></span>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="temp.confirmPassword" :type="confirmPasswordType">
          </el-input>
          <span class="show-pwd" @click="showConfirmPwd"><svg-icon icon-class="eye" /></span>
        </el-form-item>

        <!-- <el-form-item label="验证码">
          <el-input v-model="temp.code">
            <template slot="append">ABCD</template>
          </el-input>
        </el-form-item> -->

        <div>
          <el-button type="success" style="width: 40%; margin: 0 30%;" @click="submit">修改密码</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>

import { updatePassword } from '@/api/system'
export default {
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('请输入密码'))
      } else {
        if (this.temp.confirmPassword !== '') {
          this.$refs.form.validateField('confirmPassword')
        }
        callback()
      }
    }
    const validateConfirmPass = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.temp.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      passwordType: 'password',
      confirmPasswordType: 'password',
      rules: {
        password: [
          { validator: validatePass, trigger: 'blur' }
        ],
        confirmPassword: [
          { validator: validateConfirmPass, trigger: 'blur' }
        ]
      },
      temp: {
        password: '',
        confirmPassword: '',
        code: ''
      }
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    showConfirmPwd() {
      if (this.confirmPasswordType === 'password') {
        this.confirmPasswordType = ''
      } else {
        this.confirmPasswordType = 'password'
      }
    },
    submit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          updatePassword(this.temp.password).then(() => {

          })
        }
      })
    }
  }
}
</script>

<style>
  .show-pwd {
    position: absolute;
    right: 8px;
    top: 7px;
    font-size: 16px;
    color: #333333;
    cursor: pointer;
    user-select: none;
  }

</style>


