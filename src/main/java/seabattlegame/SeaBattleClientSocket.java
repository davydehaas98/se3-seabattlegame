package seabattlegame;
import com.google.gson.Gson;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class SeaBattleClientSocket implements ICommunicator {

    private static SeaBattleClientSocket instance = null;

    private final String uri = "ws://localhost:8095/wsbattle/";

    private Session session;

    private String message;

    private ISeaBattleGame game;

    private Gson gson = null;

    // Status of the webSocket client
    boolean isRunning = false;

    private SeaBattleClientSocket() {
        gson = new Gson();
    }

    public static SeaBattleClientSocket getInstance() {
        if (instance == null) {
            System.out.println("[WebSocket Client create singleton instance]");
            instance = new SeaBattleClientSocket();
        }
        return instance;
    }

    @Override
    public void start() {
        System.out.println("[WebSocket Client start connection]");
        if (!isRunning) {
            isRunning = true;
            startClient();
        }
    }

    @Override
    public void stop() {
        System.out.println("[WebSocket Client stop connection]");
        if (isRunning) {
            isRunning = false;
            stopClient();
        }
    }

    @OnOpen
    public void onWebSocketConnect(Session session){
        System.out.println("[WebSocket Client open session] " + session.getRequestURI());
        this.session = session;
    }

    @OnMessage
    public void onWebSocketText(String message, Session session){
        this.message = message;
        System.out.println("[WebSocket Client message received] " + message);
        //processMessage(message);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable cause) {
        System.out.println("[WebSocket Client connection error] " + cause.toString());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason){
        System.out.print("[WebSocket Client close session] " + session.getRequestURI());
        System.out.println(" for reason " + reason);
        session = null;
    }


    @Override
    public void setGame(ISeaBattleGame game) {
        this.game = game;
    }

    private void startClient() {
        System.out.println("[WebSocket Client start]");
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));

        } catch (Exception ex) {
            // do something useful eventually
            ex.printStackTrace();
        }
    }

    private void stopClient(){
        System.out.println("[WebSocket Client stop]");
        try {
            session.close();

        } catch (Exception ex){
            // do something useful eventually
            ex.printStackTrace();
        }
    }


}
