<template>
  <div id="chatboard">
    <el-scrollbar id="scrollbar" ref="scrollbar">
      <template v-for="message in messages">
        <div class="message">
          <p class="meta">{{ message.user }}<span>{{ message.time }}</span></p>
          <p class="text">{{ message.content }}</p>
        </div>
      </template>
    </el-scrollbar>
    <div id="inputBox">
      <el-input id="input" v-model="contentInput" placeholder="Please input"/>
      <el-button id="send" @click="send">Send</el-button>
    </div>
  </div>
</template>

<script>
import {Socket} from "socket.io-client";
import moment from "moment";

export default {
  name: "ChatBoard",
  props: {
    user: String,
    socket: Socket
  },
  data() {
    return {
      contentInput: "",
      messages: []
    }
  },
  methods: {
    send() {
      this.generateMessage(this.user, this.contentInput)
      this.socket.emit(
          "chatMessage",
          {content: this.contentInput}
      )
      this.contentInput = ""
    },
    generateMessage(user, content) {
      this.messages.push({time: moment().format('h:mm a'), user: user, content: content})
      this.$nextTick(() => {
        this.$refs.scrollbar.wrap.scrollTop = this.$refs.scrollbar.wrap.scrollHeight
      })
    }
  }
}
</script>

<style scoped>
#chatboard {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 25%;
  width: 20%;
  background-color: azure;
  border-style: ridge;
  border-color: dodgerblue;
  border-width: 5px;
  border-radius: 10px 10px 0 0;
}

#scrollbar {
  height: 85%;
  width: 100%;
  padding-top: 5px;
  border-radius: 10px;
}

#inputBox{
  display: flex;
  height: 15%;
}

#input{
  max-height: 100%;
  width: 70%;
}

#send {
  height: 99%;
  width: 30%;
}

.message {
  padding: 0 10px;
  margin-bottom: 5px;
  overflow-wrap: break-word;
  text-align: left;
}

.meta {
  font-size: 15px;
  font-weight: bold;
  color: #7386ff;
  opacity: 0.7;
  margin-block-start: 0;
  margin-block-end: 0;
  margin-top:3px
}

.meta span {
  font-size: 10px;
  margin-left: 10px;
  color: #777;
}

.text {
  font-size: 12px;
  font-weight: bold;
  color: black;
  opacity: 0.7;
  margin-block-start: 0;
  margin-block-end: 0;
  margin-top:3px
}
</style>