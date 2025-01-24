package pt.ulisboa.tecnico.rnl.dei.dms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import pt.ulisboa.tecnico.rnl.dei.dms.services.ReservationService;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.ReservationDto;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
	@Autowired
	private ReservationService reservationService;

	@GetMapping(value = "/all", produces = "application/json; charset=utf-8")
	public List<ReservationDto> getReservations() {
		return reservationService.getReservations();
	}

	@PostMapping("/create")
	public ReservationDto createReservation(@RequestBody ReservationDto reservationDto) {
		return reservationService.createReservation(reservationDto);
	}

	@PutMapping("/update")
	public ReservationDto updateReservation(@RequestBody ReservationDto reservationDto) {
		return reservationService.updateReservation(reservationDto);
	}

	@PutMapping("/assign/{personId}/{resourceId}")
	public ReservationDto assignReservation(@RequestBody ReservationDto reservationDto, @PathVariable String personId, @PathVariable Long resourceId) {
		return reservationService.assignReservation(reservationDto, personId, resourceId);
	}

	@PutMapping("/maintenance/{resourceId}")
	public ReservationDto assignReservation(@RequestBody ReservationDto reservationDto, @PathVariable Long resourceId) {
		return reservationService.assignMaintenance(reservationDto, resourceId);
	}

	@PutMapping("/cancel")
	public ReservationDto cancelReservation(@RequestBody ReservationDto reservationDto) {
		return reservationService.cancelReservation(reservationDto);
	}

	@DeleteMapping("/delete/{reservationId}")
	public void deleteReservation(@PathVariable Long reservationId) {
		reservationService.deleteReservation(reservationId);
	}
}
