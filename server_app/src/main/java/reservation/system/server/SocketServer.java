package reservation.system.server;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.annotation.PreDestroy;
import reservation.system.services.ReservationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class SocketServer {

    private static final int PORT = 5555;

    @Inject
    ReservationService reservationService;

    private volatile boolean running = false;
    private ServerSocket serverSocket;

    private final ExecutorService pool = Executors.newCachedThreadPool();

    public void start() {
        if (running) return;
        running = true;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("[SocketServer] Pornit pe port " + PORT);

            while (running) {
                Socket client = serverSocket.accept();
                pool.submit(new ClientHandler(client, reservationService));
            }
        } catch (IOException e) {
            if (running) {
                System.out.println("[SocketServer] Eroare: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void stop() {
        running = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {}

        pool.shutdownNow();
        System.out.println("[SocketServer] Oprit.");
    }
}
