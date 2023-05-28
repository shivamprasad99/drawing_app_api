package com.example

import kotlinx.serialization.Serializable
import java.util.*


val lazyWordsList: List<String> by lazy {
    val stringList = mutableListOf<String>()

    val inputStream = object {}.javaClass.getResourceAsStream("/words_list.csv")
    inputStream?.reader().use { reader ->
        reader?.forEachLine { line ->
            val columns = line.split(",").map { it.trim() }
            if (columns.isNotEmpty()) {
                stringList.add(columns[0])
            }
        }
    }

    stringList
}

fun getRandomWords(): List<String> {
    val random = Random()
    return lazyWordsList.shuffled(random).take(4)
}

@Serializable
data class RandomWordsResponse(val words: List<String>)