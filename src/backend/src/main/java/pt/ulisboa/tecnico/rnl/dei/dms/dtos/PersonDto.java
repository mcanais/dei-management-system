package pt.ulisboa.tecnico.rnl.dei.dms.dtos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

import pt.ulisboa.tecnico.rnl.dei.dms.models.person.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.models.person.PersonRole;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private String id;

    private String name;

	private String email;

    @Enumerated(EnumType.STRING)
    private PersonRole role;


	private List<String> reservationHistoric;
	
	private List<ReservationDto> reservations;
}
