<template>
  <div>
    <div class="head"><p style="margin-top: 0">模型选择</p></div>
    <div class="content">
      <div class="box">
        <carousel-3d
            ref="modelCarousel"
            :if="loaded"
            :autoplay="false"
            :perspective="35"
            :display="slide.display"
            :animationSpeed="200"
            :width="slide.dynamicWidth"
            :height="slide.dynamicHeight"
            :space="slide.space"
            controlsVisible
            :controlsHeight="60">
          <slide v-for="(item, i) in modelList" :index="i" :key="i">
            <template slot-scope="obj">
              <div class="image_container">
                <el-image :src="`${serverUrl}/image/${item.path}`"/>
              </div>
            </template>
          </slide>
        </carousel-3d>
      </div>
    </div>
    <div class="head"><p style="margin-top: 0">场景选择</p></div>
    <div class="content">
      <div class="box">
        <carousel-3d
            ref="sceneCarousel"
            :if="loaded"
            :autoplay="true"
            :autoplayTimeout="3000"
            :perspective="35"
            :display="slide.display"
            :animationSpeed="200"
            :width="slide.dynamicWidth"
            :height="slide.dynamicHeight"
            :space="slide.space"
            controlsVisible
            :controlsHeight="60">
          <slide v-for="(item, i) in sceneList" :index="i" :key="i">
            <template slot-scope="obj">
              <div class="image_container">
                <el-image :src="`${serverUrl}/image/${item.path}`"/>
              </div>
            </template>
          </slide>
        </carousel-3d>
      </div>
    </div>
    <el-button type="primary"
               style="background: #cac3b7;border: none;border-radius: 5px;width: 150px;margin-top: 20px"
               onMouseover="this.style.background='#e0dcd5'"
               onmouseout="this.style.background='#cac3b7'"
               v-on:click="select">选择
    </el-button>
  </div>
</template>
<script>
import {serverUrl} from "@/global/global";
import {Carousel3d, Slide} from 'vue-carousel-3d'

export default {
  name: 'browse',
  components: {
    Carousel3d,
    Slide
  },
  created() {
    this.change_slide_size_dynamic();
  },
  mounted() {
    window.onresize = () => {
      this.change_slide_size_dynamic();
    };
  },
  data() {
    return {
      serverUrl: serverUrl,
      slide: {
        display: 3,
        dynamicWidth: 320,
        dynamicHeight: 180,
        space: 640
      },
      modelList: [
        {
          path: "character1.png",
          modelPath: "models/character1.obj"
        },
        {
          path: "character2.png",
          modelPath: "models/character2.obj"
        },
        {
          path: "character3.png",
          modelPath: "models/character3.obj"
        }
      ],
      sceneList: [
        {
          sceneName: "threeScene",
          path: "threeScene.png"
        },
        {
          sceneName: "maze",
          path: "maze.png"
        },
        {
          sceneName: "colorBoard",
          path: "colorBoard.png"
        },
      ]
    }
  },
  methods: {
    select() {
      let modelIndex = this.$refs.modelCarousel.currentIndex
      let modelPath = this.modelList[modelIndex].modelPath
      let sceneIndex = this.$refs.sceneCarousel.currentIndex
      let sceneName = this.sceneList[sceneIndex].sceneName
      this.$router.push({
        name: sceneName,
        params: {
          modelPath: modelPath
        }
      })
    },
    change_slide_size_dynamic() {
      // 16:9
      this.slide.dynamicWidth = window.innerWidth / 3.2;
      this.slide.space = this.slide.dynamicWidth * (3.6 / this.slide.display)
      this.slide.dynamicHeight = this.slide.dynamicWidth / 2 + this.slide.dynamicWidth / 16;
    }
  }
}
</script>

<style scoped>
@import '../assets/css/details.css';
</style>

<style>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.image_container {
  width: 100%;
  height: 100%;
}

.image_container img {
  margin: 0 0 0 0;
  width: 100%;
  height: 100%;
}
</style>
