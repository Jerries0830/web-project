<template>
  <div>
    <canvas id="canvas" ref="canvas"></canvas>
    <div id="blocker">
      <div id="instructions">
        <p style="font-size:36px">
          Click to play
        </p>
        <p>
          Move: WASD<br/>
          Jump: SPACE<br/>
          Look: MOUSE<br/>
          This is a maze. Try to find the button hidden in the maze
        </p>
      </div>
    </div>
    <ChatBoard ref="chatboard" :user="user" :socket="socket"></ChatBoard>
  </div>
</template>


<script>
import {serverUrl} from '@/global/global'
import * as THREE from "three";
import {PointerLockControls} from "three/examples/jsm/controls/PointerLockControls";
import {OBJLoader} from "three/examples/jsm/loaders/OBJLoader";
import {MTLLoader} from "three/examples/jsm/loaders/MTLLoader";
import ChatBoard from "@/components/web3d/ChatBoard";
import {io} from "socket.io-client";

let hint
let interactable
let prevTime = performance.now()

// 场景构件
let scene                               // 场景
let camera                              // 摄像机
let renderer                            // 渲染器
let controls                            // 控制器

// 状态
let moveForward = false                 // 向前移动
let moveBackward = false                // 向后移动
let moveLeft = false                    // 向左移动
let moveRight = false                   // 向右移动
let canJump = true                      // 可以跳跃(在平台上)
let canInteract = true                  // 可以交互

let floorMaterial                       // 地板材质

// 检测
let horizontalDetector                  // 水平碰撞检测
let verticalDetector                    // 垂直碰撞检测
let interactionDetector                 // 交互检测

// 摄像机
let velocity = new THREE.Vector3()      // 摄像机速度
let direction = new THREE.Vector3()     // 摄像机移动方向

export default {
  name: "ThreeScene",
  components: {ChatBoard},
  data() {
    return {
      user: this.$store.state.username,
      socket: null,
      model: serverUrl + this.$route.params.modelPath,  // 模型资源
      characterHeight: 10,                              // 模型高度

      // 初始位置
      initialX: 30,
      initialY: 5.1,
      initialZ: 30,

      // 检测距离
      horizontalDistance: 3,
      verticalDistance: 3,
      interactionDistance: 10,

      // 速度因素
      moveSpeed: 500.0,               // 加速系数
      friction: 10.0,                 // 摩擦系数
      jumpForce: 200.0,               // 跳跃高度
      gravity: 490.0,                 // 重力系数

      // 化身
      character: null,

      // 场景元素
      objects: [],                    // 可碰撞物体
      platforms: [],                  // 平台
      interactions: [],               // 可交互物体
    }
  },
  methods: {
    loadObject(model, name, callback) {
      new OBJLoader().load(model, function (mesh) {
        mesh.traverse(function (child) {
          if (child instanceof THREE.Mesh) {
            child.material.side = THREE.DoubleSide;
          }
        })
        mesh.name = name
        scene.add(mesh);
        callback()
      })
    },
    loadObjectWithMaterial(material, model, name, callback) {
      new MTLLoader().load(material, function (material) {
        material.preload()

        new OBJLoader().setMaterials(material).load(model, function (mesh) {
          mesh.traverse(function (child) {
            if (child instanceof THREE.Mesh) {
              child.material.side = THREE.DoubleSide;
            }
          })
          mesh.name = name
          scene.add(mesh);
          callback()
        })
      })
    },
    initSocket() {
      const _this = this
      this.socket = io("http://47.100.91.128:50002")
      this.socket.emit("joinRoom", {username: this.user, room: "maze", model: this.model})

      this.socket.on("chatMessage", ({username, content}) => {
        this.$refs.chatboard.generateMessage(username, content)
      })

      this.socket.on("inform", ({userId, model, position, rotation}) => {
        if (!scene.getObjectByName(userId)) {
          new OBJLoader().load(model, function (mesh) {
            mesh.traverse(function (child) {
              child.name = userId
              if (child instanceof THREE.Mesh) {
                child.material.side = THREE.DoubleSide;
              }
            })
            scene.add(mesh);
            const other = scene.getObjectByName(userId)
            other.rotation.set(rotation.x, rotation.y, rotation.z)
            other.position.set(position.x, position.y, position.z)

            const box = new THREE.Box3
            box.setFromObject(other)
            const scale = _this.characterHeight / (box.max.y - box.min.y)
            other.scale.set(scale, scale, scale)
          })
        } else {
          const other = scene.getObjectByName(userId)
          other.rotation.set(rotation.x, rotation.y, rotation.z)
          other.position.set(position.x, position.y, position.z)
        }
      })

      this.socket.on("userLeave", ({userId}) => {
        scene.remove(scene.getObjectByName(userId))
      })
    },
    initScene() {
      camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000)
      camera.position.set(this.initialX, this.initialY, this.initialZ)

      scene = new THREE.Scene()
      scene.background = new THREE.Color(0xffffff);

      renderer = new THREE.WebGLRenderer({
        canvas: this.$refs.canvas,
        antialias: true
      })
      renderer.setPixelRatio(window.devicePixelRatio);
      renderer.setSize(window.innerWidth, window.innerHeight);

      hint = this.$refs.hint
    },
    initControls() {
      const _this = this
      const blocker = document.getElementById('blocker');
      const instructions = document.getElementById('instructions');

      controls = new PointerLockControls(camera, document.body);
      controls.addEventListener('lock', function () {
        instructions.style.display = 'none';
        blocker.style.display = 'none';
      });
      controls.addEventListener('unlock', function () {
        instructions.style.display = '';
        blocker.style.display = 'block';
      });

      instructions.addEventListener('click', function () {
        controls.lock();
      });

      const onKeyDown = function (event) {
        switch (event.code) {
          case 'ArrowUp':
          case 'KeyW':
            moveForward = true;
            break;

          case 'ArrowLeft':
          case 'KeyA':
            moveLeft = true;
            break;

          case 'ArrowDown':
          case 'KeyS':
            moveBackward = true;
            break;

          case 'ArrowRight':
          case 'KeyD':
            moveRight = true;
            break;

          case 'Space':
            event.preventDefault()
            if (canJump === true) velocity.y += _this.jumpForce;
            canJump = false;
            break;

          case 'KeyE':
            if (canInteract) {
              interactable.interact()
            }
        }
      }
      const onKeyUp = function (event) {
        switch (event.code) {
          case 'ArrowUp':
          case 'KeyW':
            moveForward = false;
            break;

          case 'ArrowLeft':
          case 'KeyA':
            moveLeft = false;
            break;

          case 'ArrowDown':
          case 'KeyS':
            moveBackward = false;
            break;

          case 'ArrowRight':
          case 'KeyD':
            moveRight = false;
            break;
        }
      }

      document.addEventListener('keydown', onKeyDown);
      document.addEventListener('keyup', onKeyUp);
    },
    initDetectors() {
      horizontalDetector = new THREE.Raycaster(new THREE.Vector3(), new THREE.Vector3(0, 0, 0), 0, this.horizontalDistance);
      verticalDetector = new THREE.Raycaster(new THREE.Vector3(), new THREE.Vector3(0, -1, 0), 0, this.verticalDistance);
      interactionDetector = new THREE.Raycaster(new THREE.Vector3(), new THREE.Vector3(0, 0, 0), 0, this.interactionDistance);
    },
    initCharacter() {
      this.loadObject(this.model, "character", () => {
        this.character = scene.getObjectByName("character")
        this.character.position.set(this.initialX, this.initialY, this.initialZ)

        const box = new THREE.Box3()
        box.setFromObject(this.character)
        const scale = this.characterHeight / (box.max.y - box.min.y)
        this.character.scale.set(scale, scale, scale)
      })
    },
    initMaterials() {
      const floorTexture = new THREE.TextureLoader().load(serverUrl + 'materials/floor.jpg')
      floorTexture.wrapS = floorTexture.wrapT = THREE.RepeatWrapping
      floorMaterial = new THREE.MeshBasicMaterial({
        map: floorTexture,
        side: THREE.DoubleSide
      })
    },
    initObjects() {
      const _this = this
      const floor = new THREE.Mesh(new THREE.PlaneGeometry(300, 300), floorMaterial)
      floor.name = "floor"
      floor.rotation.set(Math.PI / 2, 0, 0)
      floor.position.set(150, 0, 150)

      this.loadObjectWithMaterial(serverUrl + "materials/maze.mtl", serverUrl + "models/maze.obj", "maze", function () {
        const maze = scene.getObjectByName("maze")
        maze.scale.set(30, 10, 30)
        maze.position.set(150, 25, 150)
        _this.objects.push(maze.children[0])
      })

      this.loadObject(serverUrl + "models/button.obj", "button", function () {
        const button = scene.getObjectByName("button")
        button.position.set(270, 2, 200)
        _this.objects.push(button.children[0])
        _this.platforms.push(button.children[0])
        _this.interactions.push(button.children[0])
        button.children[0].interact = function () {
          _this.socket.emit("findButton")
        }
      })

      scene.add(floor)
      this.platforms.push(floor)
    },
    initLight() {
      const directionalLight = new THREE.DirectionalLight(0xffffff);
      directionalLight.position.set(20, 10, 5);
      scene.add(directionalLight);

      const ambientLight = new THREE.AmbientLight(0x333333);
      scene.add(ambientLight);
    },
    animate() {
      requestAnimationFrame(this.animate)
      const pi = Math.PI

      if (controls.isLocked === true) {
        horizontalDetector.ray.origin.copy(camera.position)
        verticalDetector.ray.origin.copy(camera.position)
        interactionDetector.ray.origin.copy(camera.position)

        horizontalDetector.ray.origin.y -= this.characterHeight / 2
        verticalDetector.ray.origin.y -= this.characterHeight / 2

        const _this = this
        const time = performance.now();
        const delta = (time - prevTime) / 1000;
        prevTime = time

        // 水平方向移动
        const detectCollide = function (radius) {
          let rotationMatrix = new THREE.Matrix4();
          rotationMatrix.makeRotationY(radius);
          horizontalDetector.ray.direction.copy(controls.getDirection(new THREE.Vector3(0, 0, 0)).applyMatrix4(rotationMatrix))
          horizontalDetector.ray.direction.y = 0
          return horizontalDetector.intersectObjects(_this.objects, false).length > 0;
        }

        // 按键加速
        direction.z = Number(moveForward) - Number(moveBackward);
        direction.x = Number(moveRight) - Number(moveLeft);
        direction.normalize()

        if (moveForward || moveBackward) velocity.z -= direction.z * this.moveSpeed * delta;
        if (moveLeft || moveRight) velocity.x -= direction.x * this.moveSpeed * delta;

        // 随时间减速
        // todo xz方向减速还需要考虑移动方向, 也可以使用固定减速
        velocity.x -= velocity.x * this.friction * delta;
        velocity.z -= velocity.z * this.friction * delta;

        // 理论移动距离
        let rightDistance = -velocity.x * delta
        let forwardDistance = -velocity.z * delta

        // 计算考虑碰撞时最终移动距离
        if ((moveRight && detectCollide(-pi / 2)) || (moveLeft && detectCollide(pi / 2))) rightDistance = 0
        if ((moveForward && detectCollide(0)) || (moveBackward && detectCollide(pi))) forwardDistance = 0

        if (moveLeft || moveRight) controls.moveRight(rightDistance);
        if (moveForward || moveBackward) controls.moveForward(forwardDistance);

        // 垂直方向移动
        let verticalCollide = verticalDetector.intersectObjects(this.platforms, false)
        let onObject = verticalCollide.length > 0;

        velocity.y -= this.gravity * delta
        let upDistance = velocity.y * delta

        if (onObject === true) {
          canJump = true
          if (upDistance < 0) {
            upDistance = 0
            velocity.y = 0
            camera.position.y = verticalCollide[0].object.position.y + this.characterHeight / 2 + 0.1
          }
        }

        camera.position.y += upDistance

        if (controls.getObject().position.y < this.characterHeight / 2) {
          canJump = true
          if (upDistance < 0) {
            upDistance = 0
            velocity.y = 0
            camera.position.y = this.characterHeight / 2 + 0.1
          }
        }

        // 检测可交互物体
        interactionDetector.ray.direction.copy(controls.getDirection(new THREE.Vector3(0, 0, 0)))
        let targets = interactionDetector.intersectObjects(this.interactions, false)
        if (targets.length > 0) {
          canInteract = true
          interactable = targets[0].object
        } else {
          canInteract = false
        }

        // 调整视角和人物位置
        let cameraDirection = new THREE.Vector3()
        controls.getDirection(cameraDirection)
        cameraDirection.normalize()

        if (this.character) {
          this.character.rotation.y = Math.atan2(cameraDirection.x, cameraDirection.z)
          this.character.position.x = camera.position.x
          this.character.position.y = camera.position.y - this.verticalDistance
          this.character.position.z = camera.position.z
        }

        // 通知
        this.socket.emit("inform", {
          model: this.model,
          position: this.character.position,
          rotation: new THREE.Vector3(0, this.character.rotation.y, 0)
        })
      }

      // 调整视角和人物位置
      let cameraDirection = new THREE.Vector3()
      controls.getDirection(cameraDirection)
      cameraDirection.normalize()

      // 渲染
      camera.position.x = camera.position.x - cameraDirection.x * 6
      camera.position.y = camera.position.y - cameraDirection.y * 6 + 8
      camera.position.z = camera.position.z - cameraDirection.z * 6
      renderer.render(scene, camera)
      camera.position.x = camera.position.x + cameraDirection.x * 6
      camera.position.y = camera.position.y + cameraDirection.y * 6 - 8
      camera.position.z = camera.position.z + cameraDirection.z * 6
    }
  },
  mounted() {
    window.addEventListener('popstate', () => {
          this.socket.disconnect()
          canInteract = false
        }, false
    )
    this.initSocket()
    this.initScene()
    this.initControls()
    this.initDetectors()
    this.initCharacter()
    this.initMaterials()
    this.initObjects()
    this.initLight()
    this.animate()
  },
  destroyed() {
    window.removeEventListener('popstate', () => {
          this.socket.disconnect()
          canInteract = false
        }, false
    )
  }
}
</script>

<style scoped>
#blocker {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

#instructions {
  width: 100%;
  height: 100%;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  text-align: center;
  font-size: 14px;
  cursor: pointer;
}

#instructions p {
  color: white;
}

#canvas {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
}
</style>