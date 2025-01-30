package pt.ulisboa.tecnico.rnl.dei.dms.models.resource;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.Reservation;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationType;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationState;

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
}
