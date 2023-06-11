package com.example.plugins

import com.example.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json.Default.decodeFromString

fun Application.configureRouting() {
    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        get("/random_words") {
            val words = getRandomWords()
            call.respond(RandomWordsResponse(words))
        }

        post("/select_word") {
            val parameters = call.receiveParameters()
            val word = parameters["word"]
            val roomId = parameters["room_id"]
            val userId = parameters["user_id"]
            // Process the received word and roomId as needed
            call.respond(HttpStatusCode.OK, "Word selected successfully")
        }

        get("/create_room") {
            val roomId = generateRoomId()
            GamesManager.addRoom(roomId)
            call.respondText(roomId)
        }

        post("/add_user_to_room") {
            val parameters = call.receiveParameters()
            println(parameters)
            if(GamesManager.addUserToRoom(parameters["room_id"], parameters["user_id"])) {
                call.respond(HttpStatusCode.OK, "User added to room successfully")
            } else {
                call.respond(HttpStatusCode.OK, "failed")
            }
            // Process the received roomId and userId as needed
        }

        webSocket("/websocket") {
            try {
                var connectionRoomId: String? = null
                var connectionUserId: String? = null
                for(frame in incoming) {
                    if(frame is Frame.Text) {
                        val receivedText = frame.readText()
                        val data = decodeFromString(MessageData.serializer(), receivedText)
                        connectionRoomId = connectionRoomId ?: data.room_id
                        connectionUserId = connectionUserId ?: data.user_id
                        GamesManager.addWsConnection(data.room_id, data.user_id, this)
                        GamesManager.broadcastMessage(data.room_id, data.user_id, data.message)
                    } else if(frame is Frame.Close) {
                        GamesManager.closeWsConnection(connectionRoomId, connectionUserId)
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }


        webSocket("/echo") {
            send("Please enter your name")
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                if (receivedText.equals("bye", ignoreCase = true)) {
                    close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                } else {
                    send(Frame.Text("Hi, $receivedText!"))
                }
            }
        }
    }
}



