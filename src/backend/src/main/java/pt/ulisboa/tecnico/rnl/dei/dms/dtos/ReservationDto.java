package pt.ulisboa.tecnico.rnl.dei.dms.dtos;

import jakarta.persistence.*;
import lombok.*;

import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationType;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationState;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long id;

    private String startDate;
    private String finishDate;

    @Enumerated(EnumType.STRING)
	private ReservationType type;

    @Enumerated(EnumType.STRING)
	private ReservationState state;

	private Long assignedResourceId;

	private String assignedPersonId;
}
