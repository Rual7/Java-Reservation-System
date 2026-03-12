package reservation.system.protocol;

public class InvalidCommand implements Command {
    private final String reason;

    public InvalidCommand(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
