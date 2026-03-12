package reservation.system.services;

public class ClientSession {
    private final String token;

    public ClientSession(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
