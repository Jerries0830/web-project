const express = require('express');
const socketio = require('socket.io');
const http = require("http");

const app = express();
const server = http.createServer(app);
const io = socketio(server, {
    cors: {
        origin: "*",
        methods: ["GET", "POST"]
    }
});

const users = {}

let red = 0
let green = 0
let blue = 0

io.on('connection', socket => {

    socket.on("joinRoom", ({username, room, model}) => {
        users[socket.id] = {username, room}
        socket.join(room)
        socket.emit("chatMessage", chat("Bot", "Welcome to chat!"))
        socket.broadcast.to(room).emit("chatMessage", chat("Bot", `${username} join chat`))
        socket.broadcast.to(room).emit("newUser", {userId: socket.id, model: model})

        if (room === "colorBoard") socket.emit("alterColor", {red, green, blue})
    })

    socket.on("chatMessage", ({content}) => {
        let user = users[socket.id]
        socket.broadcast.to(user.room).emit("chatMessage", chat(user.username, content))
    })

    socket.on("inform", ({model, position, rotation}) => {
        let user = users[socket.id]
        socket.broadcast.to(user.room).emit("inform", {
            userId: socket.id,
            model: model,
            position: position,
            rotation: rotation
        })
    })

    socket.on("findButton", () => {
        let user = users[socket.id]
        socket.emit("chatMessage", chat("Bot", "congratulations, you've found the button!"))
        socket.broadcast.to(user.room).emit("chatMessage", chat("Bot", `${user.username} finds button!`))
    })

    socket.on("disconnect", () => {
        let user = users[socket.id]
        socket.broadcast.to(user.room).emit("chatMessage", chat("Bot", `${user.username} leave chat`))
        socket.broadcast.to(user.room).emit("userLeave", {userId: socket.id})
        socket.leave(user.room)
        delete users[socket.id]
    })

    socket.on("rUp", () => {
        let user = users[socket.id]
        if (red < 255) {
            red += 1
            io.to(user.room).emit("alterColor", {red, green, blue})
        } else {
            socket.emit("chatMessage", chat("Bot", "red already max"))
        }
    })

    socket.on("rDown", () => {
        let user = users[socket.id]
        if (red > 0) {
            red -=
                io.to(user.room).emit("alterColor", {red, green, blue})
        } else {
            socket.emit("chatMessage", chat("Bot", "red already min"))
        }
    })

    socket.on("gUp", () => {
        let user = users[socket.id]
        if (green < 255) {
            green += 1
            io.to(user.room).emit("alterColor", {red, green, blue})
        } else {
            socket.emit("chatMessage", chat("Bot", "green already max"))
        }
    })

    socket.on("gDown", () => {
        let user = users[socket.id]
        if (green > 0) {
            green -= 1
            io.to(user.room).emit("alterColor", {red, green, blue})
        } else {
            socket.emit("chatMessage", chat("Bot", "green already min"))
        }
    })

    socket.on("bUp", () => {
        let user = users[socket.id]
        if (blue < 255) {
            blue += 1
            io.to(user.room).emit("alterColor", {red, green, blue})
        } else {
            socket.emit("chatMessage", chat("Bot", "blue already max"))
        }
    })

    socket.on("bDown", () => {
        let user = users[socket.id]
        if (blue > 0) {
            blue -= 1
            io.to(user.room).emit("alterColor", {red, green, blue})
        } else {
            socket.emit("chatMessage", chat("Bot", "blue already min"))
        }
    })
})

server.listen(3000, () => console.log(`Server running on port 3000`))

function chat(username, content) {
    return {username, content}
}