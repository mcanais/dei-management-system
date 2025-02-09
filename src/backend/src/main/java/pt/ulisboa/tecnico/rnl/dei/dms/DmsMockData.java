package pt.ulisboa.tecnico.rnl.dei.dms;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import pt.ulisboa.tecnico.rnl.dei.dms.services.PersonService;
import pt.ulisboa.tecnico.rnl.dei.dms.services.ResourceService;
import pt.ulisboa.tecnico.rnl.dei.dms.services.ReservationService;

import pt.ulisboa.tecnico.rnl.dei.dms.dtos.PersonDto;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.ResourceDto;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.ReservationDto;

@Component
public class DmsMockData implements CommandLineRunner {

	private final PersonService personService;
    private final ResourceService resourceService;
    private final ReservationService reservationService;

	@Autowired
	public DmsMockData(PersonService personService, ResourceService resourceService, ReservationService reservationService) {
		this.personService = personService;
		this.resourceService = resourceService;
		this.reservationService = reservationService;
	}

    @Override
    public void run(String... args) throws Exception {
        // Load mock data into database
    }
}
