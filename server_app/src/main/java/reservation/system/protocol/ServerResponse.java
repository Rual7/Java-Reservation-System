package reservation.system.protocol;

public class ServerResponse {

    private final String message;
    private final boolean success;
    private final boolean closeConnection;

    public ServerResponse(String message, boolean success) {
        this(message, success, false);
    }

    public ServerResponse(String message, boolean success, boolean closeConnection) {
        this.message = message;
        this.success = success;
        this.closeConnection = closeConnection;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean shouldCloseConnection() {
        return closeConnection;
    }

    public static ServerResponse close(String message) {
        return new ServerResponse(message, true, true);
    }
}
