package me.kcybulski.chinachat.utils

import me.kcybulski.chinachat.api.Server
import ratpack.http.client.RequestSpec
import ratpack.test.embed.EmbeddedApp

import java.util.function.Function

import static Utils.asMap
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import static ratpack.test.embed.EmbeddedApp.fromServer

class TestServer {

    private EmbeddedApp server

    TestServer(Server server) {
        this.server = fromServer(server.ratpackServer)
    }

    def stop() {
        server.close()
    }

    Map<String, Object> post(String url, Map<String, Object> body, Function<RequestSpec, RequestSpec> config = (rs -> rs)) {
        asMap(server.httpClient.requestSpec(rs ->
                config.andThen(addContentType()).apply(rs).body(b -> b.text(Utils.OBJECT_MAPPER.writeValueAsString(body)))
        ).post(getAddress(url)).body.text)
    }

    Map<String, Object> get(String url, Function<RequestSpec, RequestSpec> config = (rs -> rs)) {
        asMap(server.httpClient.requestSpec(rs ->
                config.andThen(addContentType()).apply(rs)
        ).get(getAddress(url)).body.text)
    }

    String getAddress(String suffix) {
        server.address.toString() + suffix
    }

    String getHost() {
        server.address.host
    }

    String getPort() {
        server.address.port
    }

    private Function<RequestSpec, RequestSpec> addContentType() {
        return (RequestSpec r) -> r.headers(headers -> headers.set(CONTENT_TYPE, 'application/json'))
    }

}
