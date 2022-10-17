import select_scene from "@/components/select_scene";
import ThreeScene from "@/components/web3d/ThreeScene";
import Maze from "@/components/web3d/Maze";
import ColorBoard from "@/components/web3d/ColorBoard";

export default [
    {
        path: '/select_scene',
        name: 'select_scene',
        component: select_scene
    },
    {
        path: '/scene/maze',
        name: 'maze',
        component: Maze
    },
    {
        path: "/scene/threeScene",
        name: "threeScene",
        component: ThreeScene
    },
    {
        path: "/scene/colorBoard",
        name: "colorBoard",
        component: ColorBoard
    },
]
