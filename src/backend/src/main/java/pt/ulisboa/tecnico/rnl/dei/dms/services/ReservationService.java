package pt.ulisboa.tecnico.rnl.dei.dms.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.Reservation;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationType;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationState;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.ReservationDto;
import pt.ulisboa.tecnico.rnl.dei.dms.repository.ReservationRepository;
import pt.ulisboa.tecnico.rnl.dei.dms.mappers.ReservationMapper;

import pt.ulisboa.tecnico.rnl.dei.dms.models.person.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.repository.PersonRepository;

import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.Resource;
import pt.ulisboa.tecnico.rnl.dei.dms.repository.ResourceRepository;

@Service
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final PersonRepository personRepository;
	private final ResourceRepository resourceRepository;
	private final ReservationMapper reservationMapper;

	@Autowired
	public ReservationService(
			ReservationRepository reservationRepository, 
			PersonRepository personRepository, 
			ResourceRepository resourceRepository, 
			ReservationMapper reservationMapper
		) {
		this.reservationRepository = reservationRepository;
		this.personRepository = personRepository;
		this.resourceRepository = resourceRepository;
		this.reservationMapper = reservationMapper;
	}
    
    public ReservationDto createReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationMapper.dtoToReservation(reservationDto);

        reservationRepository.save(reservation);
        return reservationMapper.reservationToDto(reservation);
    }

    public List<ReservationDto> getReservations() {
		List<Reservation> reservations = reservationRepository.findAll();

		return reservationMapper.reservationsToDtos(reservations);
    }

	public ReservationDto updateReservation(ReservationDto reservationDto) {
		Reservation reservation = reservationRepository.findById(reservationDto.getId()).get();

		reservation.setStartDate(reservationDto.getStartDate()); 
		reservation.setFinishDate(reservationDto.getFinishDate()); 

		reservationRepository.save(reservation);
		return reservationMapper.reservationToDto(reservation);
	}


	public ReservationDto assignReservation(ReservationDto reservationDto, String personId, Long resourceId) {
		Person person = personRepository.findById(personId).get();
		Resource resource = resourceRepository.findById(resourceId).get();

		Reservation reservation = reservationMapper.dtoToReservation(reservationDto);

		person.addReservation(reservation);
		resource.addReservation(reservation);

		reservation.setAssignedResourceId(resourceId);
		reservation.setAssignedPersonId(personId);
		reservation.setType(ReservationType.PERSON);
		reservation.updateState();
		resource.updateState();
		
		reservationRepository.save(reservation);
        return reservationMapper.reservationToDto(reservation);
	}

	public ReservationDto assignMaintenance(ReservationDto reservationDto, Long resourceId) {
		Reservation reservation = reservationMapper.dtoToReservation(reservationDto);
		Resource resource = resourceRepository.findById(resourceId).get();

		resource.addReservation(reservation);

		reservation.setAssignedResourceId(resource.getId());
		reservation.setType(ReservationType.MAINTENANCE);
		reservation.updateState();
		resource.updateState();

		reservationRepository.save(reservation);
		return reservationMapper.reservationToDto(reservation);
	}

	public ReservationDto cancelReservation(ReservationDto reservationDto) {
		Reservation reservation = reservationRepository.findById(reservationDto.getId()).get();
		Resource resource = resourceRepository.findById(reservation.getAssignedResourceId()).get();

		reservation.setState(ReservationState.CANCELLED);
		resource.updateState();

		reservationRepository.save(reservation);
		return reservationMapper.reservationToDto(reservation);
	}

    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
