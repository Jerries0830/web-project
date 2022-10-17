<template>
  <div style="text-align: center">
    <div class="information" style="width: 400px;float: none;margin: 10px auto 30px;">
      <div class="head">账号信息</div>
      <div class="line"><span>姓名&nbsp; &nbsp; &nbsp; &nbsp;</span>：{{ userDetail.username }}</div>
      <div class="line"><span>账号&nbsp; &nbsp; &nbsp; &nbsp;</span>：{{ userDetail.userId }}</div>
      <div class="end" style="margin-bottom: 0"><span>注册时间</span>：{{ userDetail.registerTime }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "personalCenter",
  data() {
    return {
      userDetail: {
        username: '桂泽宇',
        userId: '19302010038',
        registerTime: '2022-05-02 20:54:35'
      }
    }
  },
  created() {
    this.user_get()
  },
  methods: {
    //获得用户信息请求
    user_get() {
      let api = '/user/get_user'
      this.$axios.post(
          api,
          {}
      ).then(resp => {
        if (resp.status === 200) {

          this.user_get_post_success(resp.data) //
        } else {
          this.$message.error('post请求失败')
        }
      }).catch(error => {
        console.log(error)
        this.$message.error('异常,详情请查看控制台')
      })
    },


    //响应获得用户信息
    user_get_post_success(msg) {
      let _msg = JSON.parse(JSON.stringify(msg))
      if (_msg.code === 200) {
        let _data = _msg.data
        if (_data.success) {
          let user_detail = _data.userDetail
          this.userDetail.username = user_detail.username
          this.userDetail.userId = user_detail.userId
          this.userDetail.registerTime = user_detail.registerTime
        } else {
          this.$message.warning(_msg.message)
        }
        console.log(this.userDetail) // todo:此行测试用
      } else {
        this.$message.error(_msg.message)
      }
    },


  }
}
</script>
<style scoped>
@import '../assets/css/details.css';
</style>
<style scoped>
.information {
  padding-bottom: 16px;
  text-align: left;
}

.information .line {
  margin: 16px 0;
}
.information .end {
  margin: 16px 0;
}
.information span{
  text-align: right;
  width: 60px;
  display: inline-block;
}

.information span {
  width: 100px;
}
</style>
