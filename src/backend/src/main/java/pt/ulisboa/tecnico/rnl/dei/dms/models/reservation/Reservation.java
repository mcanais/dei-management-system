package pt.ulisboa.tecnico.rnl.dei.dms.models.reservation;

import jakarta.persistence.*;
import lombok.*;

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

	private String assignedPersonId;

	@ManyToOne
	@JoinColumn(name = "person_id", nullable = true)
	private Person person;

	@ManyToOne
	@JoinColumn(name = "resource_id")
	private Resource resource;
}
