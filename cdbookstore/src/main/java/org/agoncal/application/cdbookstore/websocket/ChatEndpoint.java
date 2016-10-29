package org.agoncal.application.cdbookstore.websocket;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;

@ServerEndpoint("/chat")
public class ChatEndpoint {
    @Inject
    private Logger logger;

    @OnOpen
    public void onOpen(final Session session) {
        printNotifications();
    }

    @OnMessage
    public void message(final String message, final Session client) throws Exception {
        printNotifications();

        logger.info("message: {}", message);
        for (final Session peer : client.getOpenSessions()) {
            peer.getBasicRemote().sendText(message);
        }
    }

    @OnClose
    public void onClose(final Session session) {
        printNotifications();
    }

    @OnError
    public void onError(final Throwable throwable) {
        printNotifications();
    }

    private void printNotifications() {
        System.out.println("######################");
        System.out.println("######################");
        System.out.println("######################");
    }
}
