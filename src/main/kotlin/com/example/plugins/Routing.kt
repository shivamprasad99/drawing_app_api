package com.example.plugins

import com.example.RandomWordsResponse
import com.example.getRandomWords
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/random_words") {
            val words = getRandomWords()
            call.respond(RandomWordsResponse(words))
        }
    }
}
