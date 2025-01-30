package pt.ulisboa.tecnico.rnl.dei.dms.controllers;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	public ResourceDto createResource(@RequestBody @Valid ResourceDto resourceDto) {
		return resourceService.createResource(resourceDto);
	}

	@PutMapping("/update")
	public ResourceDto updateResource(@RequestBody @Valid ResourceDto resourceDto) {
		return resourceService.updateResource(resourceDto);
	}

	@DeleteMapping("/delete/{resourceId}")
	public void deleteResource(@PathVariable Long resourceId) {
		resourceService.deleteResource(resourceId);
	}
}
