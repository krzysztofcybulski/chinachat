import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class ChatSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:5050")
    .wsBaseUrl("ws://localhost:5050")
    .acceptHeader("text/json")

  val headers = Map("Authorization" -> "Bearer ${accessToken}",
    "Content-Type" -> "application/json")

  val username = Random.alphanumeric.take(14).mkString

  val scn = scenario("BasicSimulation")
    .exec(http("request_1")
      .post("/login")
      .body(StringBody(s"""{ "username": "${username}" }"""))
      .asJson
      .check(jsonPath("$..accessToken").exists.saveAs("accessToken"))
    )
    .exec(http("get_alerts")
      .get("/chats")
      .headers(headers)
      .asJson
      .check(jsonPath("$..chats[0].id").exists.saveAs("chatId"))
    )
    .exec(ws("Connect WS")
      .connect("/chats/${chatId}?token=${accessToken}")
    )
    .repeat(1000) {
      exec(ws("Connect WS")
        .sendText("""{ "action": "message", "content": "Hello" }""")
      )
    }

  setUp(
    scn.inject(atOnceUsers(50))
  ).protocols(httpProtocol)
}
