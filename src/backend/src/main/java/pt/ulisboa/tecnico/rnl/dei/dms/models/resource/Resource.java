package pt.ulisboa.tecnico.rnl.dei.dms.models.resource;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.Reservation;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resources")
public class Resource {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Enumerated(EnumType.STRING)
    private ResourceState state;

	private String location;


	@OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
	private List<Reservation> reservations = new ArrayList<>();

	public void addReservation(Reservation reservation) {
		reservations.add(reservation);
		reservation.setResource(this);
	}

	public void updateState() {
		if (reservations == null || reservations.isEmpty()) {
			return;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		for (Reservation reservation : this.reservations) {
			LocalDate startDate = LocalDate.parse(reservation.getStartDate(), formatter); 
			LocalDate finishDate = LocalDate.parse(reservation.getFinishDate(), formatter); 
			LocalDate currentDate = LocalDate.now();

			if (!currentDate.isBefore(startDate) && !currentDate.isAfter(finishDate)) {
				if (reservation.getType() == ReservationType.PERSON) {
					this.state = ResourceState.INUSE;
				} else if (reservation.getType() == ReservationType.MAINTENANCE) {
					this.state = ResourceState.MAINTENANCE;
				}

				return;
			}
		}

		this.state = ResourceState.AVAILABLE;
	}
}
