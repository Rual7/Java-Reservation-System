package reservation.system.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import reservation.system.entity.Reservation;
import reservation.system.entity.Slot;

import java.util.List;

@ApplicationScoped
public class ReservationService {

    @Inject
    EntityManager em;

    @Transactional
    public List<Slot> listAvailableSlots() {
        return em.createQuery("""
            SELECT s FROM Slot s
            WHERE s.id NOT IN (SELECT r.slot.id FROM Reservation r)
            ORDER BY s.id
        """, Slot.class).getResultList();
    }

    @Transactional
    public String reserveSlot(long slotId, String token) {
        Slot slot = em.find(Slot.class, slotId);

        if (slot == null) {
            return "Slot inexistent.";
        }

        try {
            Reservation r = new Reservation(token, slot);
            em.persist(r);
            em.flush();
            return "Rezervare realizata. Reservation ID: " + r.getId();
        } catch (PersistenceException ex) {
            return "Slot deja rezervat.";
        }
    }

    @Transactional
    public List<Reservation> getReservationsForClient(String token) {
        return em.createQuery("""
            SELECT r FROM Reservation r
            JOIN FETCH r.slot
            WHERE r.clientToken = :token
            ORDER BY r.id
        """, Reservation.class)
                .setParameter("token", token)
                .getResultList();
    }

    @Transactional
    public String cancelReservation(long reservationId, String token) {
        Reservation r = em.find(Reservation.class, reservationId);

        if (r == null) {
            return "Rezervare inexistenta.";
        }

        if (!r.getClientToken().equals(token)) {
            return "Nu poti anula rezervarea altui client.";
        }

        em.remove(r);
        em.flush();
        return "Rezervare anulata.";
    }
}
