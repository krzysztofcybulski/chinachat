package me.kcybulski.chinachat

class ChatSpec extends ApiSpec {

    def 'should send message'() {
        given:
        def server = testServer()
        when:
        postSecure(server.httpClient, 'chats', [name: 'Test chat'])
        then:
        def chats = getSecure(server.httpClient, 'chats')['chats'] as List
        chats.size() == 1
        cleanup:
        server.close()
    }

}
