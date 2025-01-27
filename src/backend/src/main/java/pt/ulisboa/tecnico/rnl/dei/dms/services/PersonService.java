package pt.ulisboa.tecnico.rnl.dei.dms.services;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ulisboa.tecnico.rnl.dei.dms.models.person.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.Resource;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.Reservation;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.PersonDto;
import pt.ulisboa.tecnico.rnl.dei.dms.repository.PersonRepository;
import pt.ulisboa.tecnico.rnl.dei.dms.mappers.PersonMapper;


@Service
public class PersonService {

	private final PersonRepository personRepository;
	private final PersonMapper personMapper;
	private final ResourceService resourceService;

	@Autowired
	public PersonService(PersonRepository personRepository, PersonMapper personMapper, ResourceService resourceService) {
		this.personRepository = personRepository;
		this.personMapper = personMapper;
		this.resourceService = resourceService;
	}
    
    public PersonDto createPerson(PersonDto personDto) {
        Person person = personMapper.dtoToPerson(personDto);
        personRepository.save(person);
        return personMapper.personToDto(person);
    }

    public List<PersonDto> getPersons() {
		List<Person> persons = personRepository.findAll();

		return personMapper.personsToDtos(persons);
    }

    public PersonDto getPerson(String personId) {
        Person person = personRepository.findById(personId).get();

		return personMapper.personToDto(person);
    }

    public PersonDto updatePerson(PersonDto personDto) {
        Person person = personRepository.findById(personDto.getId()).get();

		person.setName(personDto.getName()); 
		person.setEmail(personDto.getEmail()); 
		person.setRole(personDto.getRole()); 

        personRepository.save(person);
        return personMapper.personToDto(person);
    }

    public void deletePerson(String id) {
        Person person = personRepository.findById(id).get();
		
		List<Long> resourceIdsList = new ArrayList<>();

		for (Reservation reservation : person.getReservations()) {
			resourceIdsList.add(reservation.getAssignedResourceId());
		}

		personRepository.deleteById(id);

		resourceService.updateResourcesStates(resourceIdsList);
    }
}
