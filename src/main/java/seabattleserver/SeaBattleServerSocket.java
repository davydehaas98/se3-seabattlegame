package seabattleserver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import seabattlegame.*;
import seabattleshared.WebSocketMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;

@ServerEndpoint(value = "/wsbattle/")
public class SeaBattleServerSocket {
    static HashSet<Session> sessions = new HashSet<>();
    private ISeaBattleGame game;
    private Player[] players;

    @OnOpen
    public void onConnect(Session session) {
        if (sessions.size() < 2) {
            System.out.println("[Connected] SessionID:" + session.getId());
            String message = String.format("[New client with client side session ID]: %s", session.getId());
            sessions.add(session);
            this.game = SeaBattleGame.getInstance();
            System.out.println("[#sessions]: " + sessions.size());
        }
    }

    @OnMessage
    public void onText(String message, Session session) {
        System.out.println("[Session ID] : " + session.getId() + " [Received] : " + message);
        processClientMessage(message, session);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("[Session ID] : " + session.getId() + "[Socket Closed: " + reason);
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable cause, Session session) {
        System.out.println("[Session ID] : " + session.getId() + "[ERROR]: ");
        cause.printStackTrace(System.err);
    }

    private void processClientMessage(String jsonMessage, Session session) {
        Gson gson = new Gson();
        WebSocketMessage wbMessage = null;
        try {
            wbMessage = gson.fromJson(jsonMessage,WebSocketMessage.class);
        }
        catch (JsonSyntaxException ex) {
            System.out.println("[WebSocket ERROR: cannot parse Json message " + jsonMessage);
            return;
        }
    }


    public void broadcast(String s) {
        System.out.println("[Broadcast] { " + s + " } to:");
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(s);
                System.out.println("\t\t >> Client associated with server side session ID: " + session.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[End of Broadcast]");
    }
}
