package pt.ulisboa.tecnico.rnl.dei.dms.dtos;

import jakarta.persistence.*;
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

    private String name;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Enumerated(EnumType.STRING)
    private ResourceState state;

	private String location;

	private List<ReservationDto> reservations;
}
