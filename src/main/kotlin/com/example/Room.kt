package com.example

class Room(val id: String) {
    private val users: MutableMap<String, User> = mutableMapOf()

    fun addUser(user: User) {
        users[user.userId] = user
    }

    fun removeUser(user: User) {
        users.remove(user.userId)
    }

    fun hasUser(userId: String): Boolean {
        return users.containsKey(userId)
    }
}

class User(val userId: String, var score: Int)
