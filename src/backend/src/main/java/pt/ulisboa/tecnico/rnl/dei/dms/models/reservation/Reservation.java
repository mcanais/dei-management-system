package pt.ulisboa.tecnico.rnl.dei.dms.models.reservation;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import pt.ulisboa.tecnico.rnl.dei.dms.models.person.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.Resource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private String startDate;
	private String finishDate;

    @Enumerated(EnumType.STRING)
	private ReservationType type;

    @Enumerated(EnumType.STRING)
	private ReservationState state;

	private Long assignedResourceId;

	@ManyToOne
	@JoinColumn(name = "person_id", nullable = true)
	private Person person;

	@ManyToOne
	@JoinColumn(name = "resource_id")
	private Resource resource;


	public void updateState() {
		if (this.state == ReservationState.FINISHED || this.state == ReservationState.CANCELLED) {
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		LocalDate startDate = LocalDate.parse(this.startDate, formatter); 
		LocalDate finishDate = LocalDate.parse(this.finishDate, formatter); 
		LocalDate currentDate = LocalDate.now();


		if (!currentDate.isBefore(startDate) && !currentDate.isAfter(finishDate)) {
			this.state = ReservationState.ACTIVE;
		} else if (this.state == ReservationState.ACTIVE) {
			this.state = ReservationState.FINISHED;
		} else {
			this.state = ReservationState.PENDING;
		}
	}
}
