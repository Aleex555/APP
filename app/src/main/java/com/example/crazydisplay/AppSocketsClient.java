package com.example.crazydisplay;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.function.Consumer;

public class AppSocketsClient extends WebSocketClient {
    private Consumer<String> callBackOnMessage;
    public AppSocketsClient(URI serverUri,Consumer<String> onMessage) {
            super(serverUri);
            this.callBackOnMessage = onMessage;
        }
        @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {
        callBackOnMessage.accept(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }


}