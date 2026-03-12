package reservation.system;

import java.io.*;
import java.net.Socket;

public class ReservationClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5555;

    public static void main(String[] args) {
        System.out.println("Conectare la server " + HOST + ":" + PORT + "...");

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            Thread reader = new Thread(() -> {
                try {
                    String line;
                    while ((line = serverIn.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("[Client] Conexiune intrerupta.");
                }
            }, "server-reader");

            reader.setDaemon(true);
            reader.start();

            while (true) {
                String input = console.readLine();
                if (input == null) break;

                input = input.trim();
                if (input.isEmpty()) continue;

                serverOut.println(input);

                if (input.equalsIgnoreCase("EXIT")) {
                    try { Thread.sleep(150); } catch (InterruptedException ignored) {}
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("[Client] Nu ma pot conecta la server: " + e.getMessage());
        }

        System.out.println("[Client] Inchis.");
    }
}
