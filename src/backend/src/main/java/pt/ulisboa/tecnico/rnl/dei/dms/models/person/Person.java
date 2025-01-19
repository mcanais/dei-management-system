package pt.ulisboa.tecnico.rnl.dei.dms.models.person;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.Reservation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons")
public class Person {

    @Id
	@NotBlank(message = "ID cannot be empty")
	@Pattern(regexp = "^ist\\d+$", 
		message = "ID has to start with 'ist' and have following numbers")
    private String id;

	@NotBlank
    private String name;

	@Email
	@NotBlank
	private String email;

    @Enumerated(EnumType.STRING)
    private PersonRole role;

	private List<String> reservationHistoric = new ArrayList<>();

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
	private List<Reservation> reservations = new ArrayList<>();
	
	//public void addResource(Resource resource) {
	//	// Only assign the resource to the person if it is available
	//	if (resource.getState() != ResourceState.AVAILABLE) {
	//		return;
	//	}
	//       resources.add(resource);
	//       resource.setPerson(this);
	//	resource.setState(ResourceState.INUSE);
	//   }
	//
	//public void removeResource(Resource resource) {
	//       resource.setPerson(null);
	//	resource.setState(ResourceState.AVAILABLE);
	//	resources.remove(resource);
	//   }
	//
	//public void clearResources() {
	//	resources.clear();
	//}
	public void addReservation(Reservation reservation) {
		reservations.add(reservation);
		reservationHistoric.add(reservation.toString());
		reservation.setPerson(this);
	}
}
