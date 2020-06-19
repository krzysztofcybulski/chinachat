package chinachat


import chinachat.api.Server
import chinachat.domain.ChatFactory
import chinachat.domain.ChatsList
import chinachat.domain.Security
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import ratpack.http.client.ReceivedResponse
import ratpack.http.client.RequestSpec
import ratpack.test.embed.EmbeddedApp
import ratpack.test.http.TestHttpClient
import spock.lang.Specification

import java.util.function.Function

import static io.netty.handler.codec.http.HttpHeaderNames.AUTHORIZATION
import static ratpack.test.embed.EmbeddedApp.fromServer

class ApiSpec extends Specification {

    def objectMapper = new ObjectMapper()

    static EmbeddedApp testServer() {
        def objectMapper = new ObjectMapper().registerModule(new KotlinModule())
        def security = new Security()
        def server = new Server(new ChatsList(), new ChatFactory(), security, objectMapper)
        return fromServer(server.ratpackServer)
    }

    String getToken(TestHttpClient client, String username) {
        post(client, "login", [username: username])["accessToken"]
    }

    Map<String, Object> post(TestHttpClient client, String url, Map<String, Object> body, Function<RequestSpec, RequestSpec> config = (rs -> rs)) {
        asMap(client.requestSpec(rs ->
                config(rs.body(b -> b.text(objectMapper.writeValueAsString(body))))
        ).post(client.applicationUnderTest.address.toString() + url))
    }

    Map<String, Object> postSecure(TestHttpClient client, String url, Map<String, Object> body) {
        post(client, url, body, rs -> rs.headers(headers -> headers.set(AUTHORIZATION, getToken(client, "username"))))
    }

    Map<String, Object> getSecure(TestHttpClient client, String url) {
        asMap(client.requestSpec(rs ->
                rs.headers(headers -> headers.set(AUTHORIZATION, getToken(client, "username")))
        ).get(client.applicationUnderTest.address.toString() + url))
    }

    private asMap(ReceivedResponse response) {
        objectMapper.readValue(response.body.text, Map<String, Object>)
    }

}
