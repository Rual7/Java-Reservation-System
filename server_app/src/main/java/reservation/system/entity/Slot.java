package reservation.system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String time;

    @Version
    private Long version;

    public Slot() {}

    public Slot(String time) {
        this.time = time;
    }

    public Long getId() { return id; }
    public String getTime() { return time; }
    public Long getVersion() { return version; }

    public void setTime(String time) { this.time = time; }

    @Override
    public String toString() {
        return "Slot ID: " + id + " | Time: " + time;
    }
}
