package com.example

fun generateRoomId(): String {
    val alphabet = ('a'..'z')
    return (1..5).map { alphabet.random() }.joinToString("")
}