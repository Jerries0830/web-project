<template>
  <div id="base_register">
    <el-form :model="registerForm"
             :rules="rules"
             :ref="registerForm"
             class="form_container"
             label-position="left"
             label-width="0px"
             v-loading="loading"
    >
      <h3>注册</h3>
      <el-form-item prop="username">
        <el-input type="text" v-model="registerForm.username"
                  auto-complete="off" placeholder="学工号"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="registerForm.password"
                  auto-complete="off" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item prop="passwordAgain">
        <el-input type="password" v-model="registerForm.passwordAgain"
                  auto-complete="off" placeholder="请再次输入密码"></el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary"
                   onMouseover="this.style.background='#e0dcd5'"
                   onmouseout="this.style.background='#cac3b7'"
                   v-on:click="register(registerForm)">注册
        </el-button>
      </el-form-item>
    </el-form>
    <el-link type="info" :underline="false" v-on:click="login">已经有账号？点击登录</el-link>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'Register',
  data() {
    const checkPassword = (rule, value, callback) => {
      // const reg = /(?!^(\d+|[a-zA-Z]+|[-_]+)$)^[\w-]{6,32}$/
      // if (!reg.test(value)) {
      //   callback(new Error('字⺟，数字或者特殊字符（-_）⾄少包含两种'))
      // } else if (value.search(this.registerForm.username) !== -1) {
      //   callback(new Error('密码不能包含用户名'))
      // } else {
      //   return callback()
      // }
      return callback()
    }
    const checkPasswordAgain = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error('两次输入密码不一致'))
      } else {
        return callback()
      }
    }
    return {
      registerForm: {
        username: '',
        password: '',
        passwordAgain: '',
      },
      rules: {
        // blur 失去鼠标焦点时触发验证
        username: [
          {required: true, message: '请填写用户名', trigger: 'blur'},
          // {pattern: /^[0-9]{11}$/, message: '学号为11位数字', trigger: 'blur'},
          //{validator: setEmail, trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请填写密码', trigger: 'blur'},
          //{validator: checkPassword, trigger: 'blur'}
        ],
        passwordAgain: [
          {required: true, message: '请再次填写密码', trigger: 'blur'},
          //{validator: checkPasswordAgain, trigger: 'blur'}
        ]
      },
      loading: false,
      time: 60
    }
  },
  methods: {
    userTypeChange() {
    },
    register(formName) {
      //上传表单
      let api = '/user/register'
      let form_data = {
        username: this.registerForm.username,
        password: this.registerForm.password,
        rePassword: this.registerForm.passwordAgain
      }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post(
              api,
              form_data
          ).then(resp => {
            if (resp.status === 200) {

              this.register_post_success(resp.data)
            } else {
              this.$message.error('post请求失败')
            }
          }).catch(error => {
            console.log(error)
            this.$message.error('异常,详情请查看控制台')
          })
        }
      })
    },

    register_post_success(msg) {
      let _msg = JSON.parse(JSON.stringify(msg))
      console.log(_msg)
      if (_msg.code === 200) {
        let _data = _msg.data
        if (_data.success) {
          this.$message.success(_msg.message)
          this.$router.replace({path: '/login'})
        } else {
          this.$message.warning(_msg.message)
        }
      } else {
        this.$message.error(_msg.message)
      }
    },
    login() {
      this.$router.push('/login')
    },
  }
}
</script>

<style scoped>
#base_register {
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

.form_container {
  border-radius: 30px;
  background-clip: padding-box;
  margin: 90px auto 20px;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #cac3b7;
}

.form_container h3 {
  margin: 0px auto 40px auto;
  text-align: center;
}

.form_container button {
  background: #cac3b7;
  border: none;
  border-radius: 5px;
  width: 40%
}
</style>
