package com.collectibles.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class PriceWebSocketServer {

    private final int port;
    private Server server;

    public PriceWebSocketServer(int port) {
        this.port = port;
    }

    public void start() {
        new Thread(() -> {
            try {
                server = new Server(port);
                WebSocketHandler wsHandler = new WebSocketHandler() {
                    @Override
                    public void configure(WebSocketServletFactory factory) {
                        factory.register(PriceWebSocketHandler.class);
                    }
                };
                server.setHandler(wsHandler);
                server.start();
                System.out.println("💬 WebSocket server running on ws://localhost:" + port);
                server.join();
            } catch (Exception e) {
                System.err.println("❌ WebSocket server failed to start:");
                e.printStackTrace();
            }
        }).start();
    }

    public void broadcastPriceUpdate(Map<String, Object> updateMsg) {
        PriceWebSocketHandler.broadcast(updateMsg);
    }

    //Internal class for handling WebSocket connections
    @WebSocket
    public static class PriceWebSocketHandler {
        private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
        private static final Gson gson = new Gson();

        @OnWebSocketConnect
        public void onConnect(Session session) {
            sessions.add(session);
            System.out.println("🟢 Client connected: " + session.getRemoteAddress());
        }

        @OnWebSocketClose
        public void onClose(Session session, int statusCode, String reason) {
            sessions.remove(session);
            System.out.println("🔴 Client disconnected: " + reason);
        }

        @OnWebSocketMessage
        public void onMessage(Session session, String message) {
            System.out.println("📩 Received message: " + message);
        }

        @OnWebSocketError
        public void onError(Session session, Throwable error) {
            System.err.println("⚠️ WebSocket error:");
            error.printStackTrace();
        }

        public static void broadcast(Map<String, Object> updateMsg) {
            String json = gson.toJson(updateMsg);
            System.out.println("📢 Broadcasting to " + sessions.size() + " clients: " + json);

            for (Session session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.getRemote().sendString(json);
                    } catch (IOException e) {
                        System.err.println("❌ Failed to send to client:");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}