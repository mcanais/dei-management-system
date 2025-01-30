package pt.ulisboa.tecnico.rnl.dei.dms.models.person;

import jakarta.persistence.*;
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
    private String id;

    private String name;

	private String email;

    @Enumerated(EnumType.STRING)
    private PersonRole role;

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
	private List<Reservation> reservations = new ArrayList<>();
	

	public void addReservation(Reservation reservation) {
		reservations.add(reservation);
		reservation.setPerson(this);
	}
}
