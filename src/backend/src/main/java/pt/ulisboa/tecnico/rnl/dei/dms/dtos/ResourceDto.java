package pt.ulisboa.tecnico.rnl.dei.dms.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.ResourceType;
import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.ResourceState;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDto {

    private Long id;

	@NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

	@NotNull(message = "Type cannot be null")
    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Enumerated(EnumType.STRING)
    private ResourceState state;

	private String location;

	private List<ReservationDto> reservations;
}
