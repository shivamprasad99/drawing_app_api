package com.example

import kotlinx.serialization.Serializable
import java.util.*


val wordCollection = listOf(
    "apple", "banana", "orange", "grape", "pineapple", "mango", "pear", "kiwi",
    "cherry", "watermelon", "strawberry", "blueberry", "lemon", "peach", "plum"
)

fun getRandomWords(): List<String> {
    val random = Random()
    return wordCollection.shuffled(random).take(4)
}

@Serializable
data class RandomWordsResponse(val words: List<String>)