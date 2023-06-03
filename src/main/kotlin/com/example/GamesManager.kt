package com.example

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

    // maps a roomId to the room which contains data about a game
    val gamesMap: MutableMap<String,Room> = HashMap()
}