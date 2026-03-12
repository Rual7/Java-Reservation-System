package reservation.system.server;

import reservation.system.entity.Reservation;
import reservation.system.entity.Slot;
import reservation.system.protocol.*;
import reservation.system.services.ClientSession;
import reservation.system.services.ReservationService;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ReservationService service;

    public ClientHandler(Socket socket, ReservationService service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        String token = UUID.randomUUID().toString().substring(0, 8);
        ClientSession session = new ClientSession(token);

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("Conectat. Token: " + token);
            out.println("Comenzi: LIST | RESERVE <slotId> | MY | CANCEL <reservationId> | EXIT");

            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    ServerResponse resp = new ServerResponse(
                            "Linie goala. Comenzi: LIST | RESERVE <slotId> | MY | CANCEL <reservationId> | EXIT",
                            false
                    );
                    out.println(resp.getMessage());
                    continue;
                }

                Command command = parseCommand(line);
                ServerResponse response;

                if (command instanceof InvalidCommand ic) {
                    response = new ServerResponse("Comanda invalida: " + ic.getReason(), false);

                } else if (command instanceof ListCommand) {
                    List<Slot> slots = service.listAvailableSlots();
                    if (slots.isEmpty()) {
                        response = new ServerResponse("Nu exista sloturi disponibile.", true);
                    } else {
                        StringBuilder sb = new StringBuilder("Sloturi disponibile:\n");
                        for (Slot s : slots) {
                            sb.append(s.getId()).append(" | ").append(s.getTime()).append("\n");
                        }
                        response = new ServerResponse(sb.toString().trim(), true);
                    }

                } else if (command instanceof ReserveCommand rc) {
                    String result = service.reserveSlot(rc.getSlotId(), session.getToken());
                    response = new ServerResponse(result, true);

                } else if (command instanceof MyCommand) {
                    List<Reservation> mine = service.getReservationsForClient(session.getToken());
                    if (mine.isEmpty()) {
                        response = new ServerResponse("Nu ai rezervari.", true);
                    } else {
                        StringBuilder sb = new StringBuilder("Rezervarile tale:\n");
                        for (Reservation r : mine) {
                            sb.append(r.getId())
                                    .append(" | ")
                                    .append(r.getSlot().getTime())
                                    .append("\n");
                        }
                        response = new ServerResponse(sb.toString().trim(), true);
                    }

                } else if (command instanceof CancelCommand cc) {
                    String result = service.cancelReservation(cc.getReservationId(), session.getToken());
                    response = new ServerResponse(result, true);

                } else if (command instanceof ExitCommand) {
                    response = ServerResponse.close("Deconectare...");

                } else {
                    response = new ServerResponse("Eroare: tip de comanda necunoscut.", false);
                }

                out.println(response.getMessage());

                if (response.shouldCloseConnection()) {
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("[ClientHandler] Eroare: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private Command parseCommand(String rawLine) {
        String[] parts = rawLine.trim().split("\\s+");
        String op = parts[0].toUpperCase();

        try {
            switch (op) {
                case "LIST":
                    return new ListCommand();

                case "MY":
                    return new MyCommand();

                case "EXIT":
                    return new ExitCommand();

                case "RESERVE":
                    if (parts.length != 2) return new InvalidCommand("Utilizare: RESERVE <slotId>");
                    return new ReserveCommand(Long.parseLong(parts[1]));

                case "CANCEL":
                    if (parts.length != 2) return new InvalidCommand("Utilizare: CANCEL <reservationId>");
                    return new CancelCommand(Long.parseLong(parts[1]));

                default:
                    return new InvalidCommand("Comandă necunoscută. Comenzi: LIST | RESERVE <slotId> | MY | CANCEL <reservationId> | EXIT");
            }
        } catch (NumberFormatException e) {
            return new InvalidCommand("Parametru numeric invalid.");
        }
    }
}
