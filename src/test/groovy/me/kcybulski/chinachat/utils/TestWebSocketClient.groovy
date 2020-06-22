package me.kcybulski.chinachat.utils

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake

import java.util.concurrent.BlockingQueue

import static Utils.asMap

class TestWebSocketClient extends WebSocketClient {

    private BlockingQueue<Map<String, Object>> messages

    TestWebSocketClient(URI serverUri, BlockingQueue<Map<String, Object>> messagesQueue) {
        super(serverUri)
        this.messages = messagesQueue
    }

    @Override
    void onMessage(String message) {
        messages.add(asMap(message))
    }

    @Override
    void onOpen(ServerHandshake handshakedata) {
    }

    @Override
    void onClose(int code, String reason, boolean remote) {
    }

    @Override
    void onError(Exception ex) {
    }
}
