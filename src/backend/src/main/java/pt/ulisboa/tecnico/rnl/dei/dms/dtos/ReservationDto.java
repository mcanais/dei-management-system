package pt.ulisboa.tecnico.rnl.dei.dms.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationType;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationState;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long id;

	@NotNull(message = "Start Date cannot be null")
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$",
		message = "Start Date must be valid")
    private String startDate;
	
	@NotNull(message = "Finish Date cannot be null")
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$",
		message = "Finish Date must be valid")
    private String finishDate;

    @Enumerated(EnumType.STRING)
	private ReservationType type;

    @Enumerated(EnumType.STRING)
	private ReservationState state;

	private Long assignedResourceId;

	private String assignedPersonId;
}
