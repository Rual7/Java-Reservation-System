package reservation.system.protocol;

public class ReserveCommand implements Command {
    private final long slotId;

    public ReserveCommand(long slotId) {
        this.slotId = slotId;
    }

    public long getSlotId() {
        return slotId;
    }
}
