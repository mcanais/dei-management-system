package pt.ulisboa.tecnico.rnl.dei.dms.controllers;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pt.ulisboa.tecnico.rnl.dei.dms.services.PersonService;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.PersonDto;

@RestController
@RequestMapping("/persons")
public class PersonController {
	@Autowired
	private PersonService personService;

	@GetMapping(value = "/all", produces = "application/json; charset=utf-8")
	public List<PersonDto> getPersons() {
		return personService.getPersons();
	}

	@GetMapping(value = "/{personId}", produces = "application/json; charset=utf-8")
	public PersonDto getPerson(@PathVariable String personId) {
		return personService.getPerson(personId);
	}

	@PostMapping("/create")
	public PersonDto createPerson(@RequestBody @Valid PersonDto personDto) {
		return personService.createPerson(personDto);
	}

	@PutMapping("/update")
	public PersonDto updatePerson(@RequestBody @Valid PersonDto personDto) {
		return personService.updatePerson(personDto);
	}

	@DeleteMapping("/delete/{id}")
	public void deletePerson(@PathVariable String id) {
		personService.deletePerson(id);
	}

}
