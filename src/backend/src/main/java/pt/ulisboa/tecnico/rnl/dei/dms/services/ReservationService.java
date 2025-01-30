package pt.ulisboa.tecnico.rnl.dei.dms.services;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
	private final ResourceService resourceService;

	@Autowired
	public ReservationService(
			ReservationRepository reservationRepository, 
			PersonRepository personRepository, 
			ResourceRepository resourceRepository, 
			ReservationMapper reservationMapper,
			ResourceService resourceService
		) {
		this.reservationRepository = reservationRepository;
		this.personRepository = personRepository;
		this.resourceRepository = resourceRepository;
		this.reservationMapper = reservationMapper;
		this.resourceService = resourceService;
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

		if (this.reservationsOverlap(resource.getReservations(), reservation) || 
				this.reservationsOverlap(person.getReservations(), reservation)) {
            throw new RuntimeException("Reservations Overlap");
		}
		person.addReservation(reservation);
		resource.addReservation(reservation);

		reservation.setAssignedResourceId(resourceId);
		reservation.setAssignedPersonId(personId);
		reservation.setType(ReservationType.PERSON);

		this.updateReservationState(reservation);
		resourceService.updateResourceState(resource);
		
		reservationRepository.save(reservation);
        return reservationMapper.reservationToDto(reservation);
	}

	public ReservationDto assignMaintenance(ReservationDto reservationDto, Long resourceId) {
		Reservation reservation = reservationMapper.dtoToReservation(reservationDto);
		Resource resource = resourceRepository.findById(resourceId).get();

		if (this.reservationsOverlap(resource.getReservations(), reservation)) {
            throw new RuntimeException("Reservations Overlap");
		}
		resource.addReservation(reservation);

		reservation.setAssignedResourceId(resource.getId());
		reservation.setType(ReservationType.MAINTENANCE);

		this.updateReservationState(reservation);
		resourceService.updateResourceState(resource);

		reservationRepository.save(reservation);
		return reservationMapper.reservationToDto(reservation);
	}

	public ReservationDto cancelReservation(ReservationDto reservationDto) {
		Reservation reservation = reservationRepository.findById(reservationDto.getId()).get();
		Resource resource = resourceRepository.findById(reservation.getAssignedResourceId()).get();

		reservation.setState(ReservationState.CANCELLED);
		resourceService.updateResourceState(resource);

		reservationRepository.save(reservation);
		return reservationMapper.reservationToDto(reservation);
	}


    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }


	private boolean reservationsOverlap(List<Reservation> reservations, Reservation newReservation) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		List<Reservation> validReservations = reservations.stream()
			.filter(reservation -> (reservation.getState() == ReservationState.ACTIVE || reservation.getState() == ReservationState.PENDING))
			.collect(Collectors.toList());

		validReservations.add(newReservation);

		validReservations.sort((reservation1, reservation2) -> {
            LocalDate date1 = LocalDate.parse(reservation1.getStartDate(), formatter);
            LocalDate date2 = LocalDate.parse(reservation2.getStartDate(), formatter);

            return date1.compareTo(date2);
        });

		for (int i = 1; i < validReservations.size(); i++) {
			LocalDate startDate = LocalDate.parse(validReservations.get(i).getStartDate(), formatter); 
			LocalDate finishDate = LocalDate.parse(validReservations.get(i-1).getFinishDate(), formatter); 

			if (!startDate.isAfter(finishDate)) {
				return true;
			}
		}

		return false;
	}

	public void updateReservationState(Reservation reservation) {
		if (reservation.getState() == ReservationState.FINISHED || reservation.getState() == ReservationState.CANCELLED) {
			// Terminal state
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		LocalDate startDate = LocalDate.parse(reservation.getStartDate(), formatter); 
		LocalDate finishDate = LocalDate.parse(reservation.getFinishDate(), formatter); 
		LocalDate currentDate = LocalDate.now();

		if (!currentDate.isBefore(startDate) && !currentDate.isAfter(finishDate)) {
			reservation.setState(ReservationState.ACTIVE);
		} 
		else if (currentDate.isAfter(finishDate)) {
			reservation.setState(ReservationState.FINISHED);
		}
		else {
			reservation.setState(ReservationState.PENDING);
		}
	}
}
