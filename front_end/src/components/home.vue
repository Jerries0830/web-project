<template>
  <div>
    <el-container>
      <el-header style="font-size: 12px">
        <img src="../assets/logo.png" style="height: 50%;margin-top: 13px;float: left" alt="logo">
        <el-menu
            class="el-menu-demo"
            mode="horizontal"
            background-color="#cac3b7"
            text-color="#333"
            active-text-color="#A52A2AFF"
            router :default-active="$route.path"
            style="padding-left: 10px;float: left"
        >
          <el-submenu index="2">
            <template slot="title">{{this.$store.state.username}}</template>
            <el-menu-item index="/user/personalCenter">个人中心</el-menu-item>
            <el-menu-item index="" @click.native="quit">注销</el-menu-item>
          </el-submenu>
          <el-menu-item index="/select_scene">3D场景</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import store from '../store'
import {router} from '../router'
import axios from 'axios'

export default {
  name: 'home',
  data() {
    return {
      username: ''
    }
  },
  created() {
  },
  methods: {
    quit: function () {
      // 清除token
      store.commit('logout')
      router.currentRoute.path !== '/login' &&
      router.currentRoute.path !== '/register' &&
      router.replace({
        path: '/login',
        query: {redirect: router.currentRoute.path}
      })
      this.$message.success('注销成功')
    }
  }
}
</script>
<style scoped>
.el-header {
  background-color: #cac3b7;
  border-radius: 10px;
  color: #333;
  line-height: 60px;
}
</style>
<style scoped>
@import '../assets/css/details.css';
</style>
