package reservation.system.protocol;

public class CancelCommand implements Command {
    private final long reservationId;

    public CancelCommand(long reservationId) {
        this.reservationId = reservationId;
    }

    public long getReservationId() {
        return reservationId;
    }
}
