package com.example

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json.Default.encodeToString

class Room(val id: String) {
    private val users: MutableMap<String, User> = mutableMapOf()
    private val connections: MutableMap<String, DefaultWebSocketServerSession> = mutableMapOf()

    fun addUser(user: User) {
        users[user.userId] = user
    }

    fun removeUser(user: User) {
        users.remove(user.userId)
        CoroutineScope(Dispatchers.IO).launch {
            connections[user.userId]?.close(CloseReason(CloseReason.Codes.NORMAL, "WebSocket closed"))
        }
    }

    fun hasUser(userId: String): Boolean {
        return users.containsKey(userId)
    }

    fun addWsConnection(userId: String, defaultWebSocketServerSession: DefaultWebSocketServerSession) {
        connections[userId] = defaultWebSocketServerSession
    }

    fun broadcastMessage(userId: String, message: String?) {
        val messageJson = encodeToString(MessageData.serializer(), MessageData(user_id = userId, room_id = id, message = message))
        connections.forEach {
//            if(it.key != userId) {
                val connection = it.value
                if (connection.isActive) {
                    CoroutineScope(Dispatchers.IO).launch {
                        connection.send(Frame.Text(messageJson))
                    }
                }
//            }
        }
    }

    fun removeWsConnection(userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            connections[userId]?.close(CloseReason(CloseReason.Codes.NORMAL, "WebSocket closed"))
        }
    }
}

class User(val userId: String, var score: Int)

@Serializable
data class MessageData(val user_id: String?, val room_id: String?, val message: String?)

