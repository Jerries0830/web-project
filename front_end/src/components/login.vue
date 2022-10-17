<template>
  <div id="base_login">
    <el-form :model="loginForm"
             :rules="rules"
             :ref="loginForm"
             class="form_container"
             label-position="left"
             label-width="0px"
             v-loading="loading">
      <h3>登录</h3>
      <el-form-item prop="username">
        <el-input type="text"
                  v-model="loginForm.username"
                  auto-complete="off"
                  :placeholder="usr"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password"
                  v-model="loginForm.password"
                  auto-complete="off"
                  :placeholder="pwd"></el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary"
                   onMouseover="this.style.background='#e0dcd5'"
                   onmouseout="this.style.background='#cac3b7'"
                   v-on:click="login(loginForm)">登录
        </el-button>
      </el-form-item>
    </el-form>
    <el-link type="info" :underline="false" v-on:click="register">还没有账号？点击注册</el-link>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        isAdmin: false,
      },
      pwd: '密码',
      usr: '用户',
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ]
      },
      loading: false
    }
  },

  methods: {
    login(formName) {
      //上传表单
      let api = '/user/login'
      let form_data = {
        username: this.loginForm.username,
        password: this.loginForm.password
      }
      //console.log(form_data)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post(
              api,
              form_data
          ).then(resp => {
            if (resp.status === 200) {
              this.login_post_success(resp.data)
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

    login_post_success(msg) {
      let _msg = JSON.parse(JSON.stringify(msg))
      if (_msg.code === 200) {
        let _data = _msg.data
        if (_data.success) {
          this.$store.commit('login', {
            token: _data.token,
            username:this.loginForm.username
          })

          this.$message.success(_msg.message)
          this.$router.replace({path: '/select_scene'})
        } else {
          this.$message.warning(_msg.message)
        }
      } else {
        this.$message.error(_msg.message)
      }
    },
    register() {
      this.$router.push('/register')
    },
  }
}
</script>

<style scoped>
#base_login {
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

body {
  margin: 0;
  padding: 0;
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
