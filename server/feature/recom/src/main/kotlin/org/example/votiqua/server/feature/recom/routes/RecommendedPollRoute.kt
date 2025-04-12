//package org.example.votiqua.server.feature.voting.routes
//
//import io.ktor.http.*
//import io.ktor.server.application.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import org.example.votiqua.repositories.PollRepository
//import org.example.votiqua.server.common.utils.getUserId
//
//fun Route.recommendedPollRoute() {
//    route("/polls") {
//        get("/recommended") {
//            val userId = call.getUserId() ?: run {
//                call.respond(HttpStatusCode.Unauthorized, "Authentication required")
//                return@get
//            }
//
//            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
//            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
//
//            val polls = PollRepository.getRecommendedPolls(userId, limit)
//            call.respond(HttpStatusCode.OK, mapOf("count" to polls.size, "results" to polls))
//        }
//    }
//}