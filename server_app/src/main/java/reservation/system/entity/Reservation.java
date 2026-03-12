package reservation.system.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "reservations",
        uniqueConstraints = @UniqueConstraint(columnNames = "slot_id")
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientToken;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    public Reservation() {}

    public Reservation(String clientToken, Slot slot) {
        this.clientToken = clientToken;
        this.slot = slot;
    }

    public Long getId() { return id; }
    public String getClientToken() { return clientToken; }
    public Slot getSlot() { return slot; }

    public void setClientToken(String clientToken) { this.clientToken = clientToken; }
    public void setSlot(Slot slot) { this.slot = slot; }

    @Override
    public String toString() {
        return "Reservation ID: " + id +
                " | Client Token: " + clientToken +
                " | Slot: " + (slot != null ? slot.getTime() : "null");
    }
}
