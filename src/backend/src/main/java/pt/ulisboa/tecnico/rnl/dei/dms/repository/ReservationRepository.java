package pt.ulisboa.tecnico.rnl.dei.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.Reservation;

@Repository
@Transactional
public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
