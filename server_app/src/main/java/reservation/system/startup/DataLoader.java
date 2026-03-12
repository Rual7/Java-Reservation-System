package reservation.system.startup;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import reservation.system.entity.Slot;

@ApplicationScoped
public class DataLoader {

    @Inject
    EntityManager em;

    @Transactional
    void load(@Observes StartupEvent ev) {
        Long count = em.createQuery("SELECT COUNT(s) FROM Slot s", Long.class).getSingleResult();
        if (count != 0) return;

        for (int hour = 10; hour <= 17; hour++) {
            Slot s = new Slot("2026-01-25 " + (hour < 10 ? "0" : "") + hour + ":00");
            em.persist(s);
        }
    }
}
