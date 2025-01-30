package pt.ulisboa.tecnico.rnl.dei.dms.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

	@NotNull(message = "ID cannot be null")
	@Pattern(regexp = "^ist\\d+$", message = "ID has to start with 'ist' and have following numbers")
    private String id;

	@NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

	@NotNull(message = "Email cannot be null")
	@Email(message = "Email must be valid")
	private String email;

	@NotNull(message = "Role cannot be null")
    @Enumerated(EnumType.STRING)
    private PersonRole role;


	private List<String> reservationHistoric;
	
	private List<ReservationDto> reservations;
}
