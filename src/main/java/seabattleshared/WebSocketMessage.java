package seabattleshared;

public class WebSocketMessage {

    // Enum for command to excute
    private WebSocketMessageCommand command;

    public WebSocketMessageCommand getCommand() {
        return command;
    }

    public void setCommand(WebSocketMessageCommand command) {
        this.command = command;
    }
}
