package pt.ulisboa.tecnico.rnl.dei.dms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	public PersonDto createPerson(@RequestBody PersonDto personDto) {
		return personService.createPerson(personDto);
	}

	@PutMapping("/update")
	public PersonDto updatePerson(@RequestBody PersonDto personDto) {
		return personService.updatePerson(personDto);
	}

	//@PutMapping("/{personId}/add-resource/{resourceId}")
	//public PersonDto addResourceToPerson(@PathVariable String personId, @PathVariable Long resourceId) {
	//	return personService.addResourceToPerson(personId,resourceId);
	//}
	//
	//@PutMapping("/{personId}/remove-resource/{resourceId}")
	//public PersonDto removeResourceFromPerson(@PathVariable String personId, @PathVariable Long resourceId) {
	//	return personService.removeResourceFromPerson(personId,resourceId);
	//}

	@DeleteMapping("/delete/{id}")
	public void deletePerson(@PathVariable String id) {
		personService.deletePerson(id);
	}

}
