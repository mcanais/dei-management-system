package pt.ulisboa.tecnico.rnl.dei.dms.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.Reservation;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.ReservationDto;


@Mapper(componentModel = "spring")
public interface ReservationMapper {

	ReservationMapper INSTANCE = Mappers.getMapper( ReservationMapper.class );

	ReservationDto reservationToDto(Reservation reservation);

    List<ReservationDto> reservationsToDtos(List<Reservation> reservations);

	Reservation dtoToReservation(ReservationDto reservationDto);

}

