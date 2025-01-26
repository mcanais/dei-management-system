package pt.ulisboa.tecnico.rnl.dei.dms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import pt.ulisboa.tecnico.rnl.dei.dms.services.ResourceService;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.ResourceDto;

@RestController
@RequestMapping("/resources")
public class ResourceController {
	@Autowired
	private ResourceService resourceService;

	@GetMapping(value = "/all", produces = "application/json; charset=utf-8")
	public List<ResourceDto> getResources() {
		return resourceService.getResources();
	}

	@GetMapping(value = "/{resourceId}", produces = "application/json; charset=utf-8")
	public ResourceDto getResource(@PathVariable Long resourceId) {
		return resourceService.getResource(resourceId);
	}

	@PostMapping("/create")
	public ResourceDto createResource(@RequestBody ResourceDto resourceDto) {
		return resourceService.createResource(resourceDto);
	}

	@PutMapping("/update")
	public ResourceDto updateResource(@RequestBody ResourceDto resourceDto) {
		return resourceService.updateResource(resourceDto);
	}

	//@PutMapping("/maintenance")
	//public ResourceDto resourceToMaintenance(@RequestBody ResourceDto resourceDto) {
	//	return resourceService.toMaintenance(resourceDto);
	//}
	//
	//@PutMapping("/available")
	//public ResourceDto resourceAvailable(@RequestBody ResourceDto resourceDto) {
	//	return resourceService.setAvailable(resourceDto);
	//}

	@DeleteMapping("/delete/{resourceId}")
	public void deleteResource(@PathVariable Long resourceId) {
		resourceService.deleteResource(resourceId);
	}
}
