package com.example

import io.ktor.server.websocket.*

object GamesManager {
    fun addRoom(roomId: String) {
        if(!gamesMap.containsKey(roomId)) {
            gamesMap[roomId] = Room(roomId)
        }
    }

    fun addUserToRoom(roomId: String?, userId: String?): Boolean {
        if(roomId != null && gamesMap.containsKey(roomId)) {
            if(userId != null && gamesMap[roomId]!=null && !gamesMap[roomId]!!.hasUser(userId)) {
                gamesMap[roomId]?.addUser(User(userId,0))
                return true
            }
        }
        return false
    }

    fun addWsConnection(roomId: String?, userId: String?, defaultWebSocketServerSession: DefaultWebSocketServerSession) {
        if(roomId != null && gamesMap.containsKey(roomId)) {
            if(userId != null && gamesMap[roomId]!=null && gamesMap[roomId]!!.hasUser(userId)) {
                gamesMap[roomId]?.addWsConnection(userId, defaultWebSocketServerSession)
            }
        }
    }

    fun broadcastMessage(roomId: String?, userId: String?, message: String?) {
        if(roomId != null && gamesMap.containsKey(roomId)) {
            if(userId != null && gamesMap[roomId]!=null && gamesMap[roomId]!!.hasUser(userId)) {
                gamesMap[roomId]?.broadcastMessage(userId, message)
            }
        }
    }

    fun closeWsConnection(roomId: String?, userId: String?) {
        if(roomId != null && gamesMap.containsKey(roomId)) {
            if(userId != null && gamesMap[roomId]!=null && !gamesMap[roomId]!!.hasUser(userId)) {
                gamesMap[roomId]?.removeWsConnection(userId)
            }
        }
    }

    // maps a roomId to the room which contains data about a game
    val gamesMap: MutableMap<String,Room> = HashMap()
}