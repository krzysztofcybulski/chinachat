package me.kcybulski.chinachat.utils

import org.java_websocket.client.WebSocketClient
import ratpack.http.client.RequestSpec

import java.util.concurrent.ArrayBlockingQueue
import java.util.function.Function

import static Utils.asString
import static io.netty.handler.codec.http.HttpHeaderNames.AUTHORIZATION
import static java.util.concurrent.TimeUnit.SECONDS

class TestUser {

    private TestServer server
    private String username
    private String token
    private WebSocketClient webSocketClient
    private messages = new ArrayBlockingQueue<Map<String, Object>>(10)

    TestUser(TestServer server, String username) {
        this.server = server
        this.username = username
        this.token = server.post('login', [username: username])["accessToken"]
    }

    Map<String, Object> post(String url, Map<String, Object> body) {
        return server.post(url, body, addAuthToken())
    }

    Map<String, Object> get(String url) {
        return server.get(url, addAuthToken())
    }

    void join(String chatId) {
        def uri = new URI("ws://${server.host}:${server.port}/chats/${chatId}?token=${token}")
        webSocketClient = new TestWebSocketClient(uri, messages)
        webSocketClient.connectBlocking()
    }

    void sendMessage(Map<String, Object> message) {
        webSocketClient.send(asString(message))
    }

    Map<String, Object> getLastMessage() {
        return messages.poll(1, SECONDS)
    }

    private Function<RequestSpec, RequestSpec> addAuthToken() {
        return (RequestSpec r) -> r.headers(headers -> headers.set(AUTHORIZATION, token))
    }

}
