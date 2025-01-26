package pt.ulisboa.tecnico.rnl.dei.dms.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.Resource;
import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.ResourceState;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.ResourceDto;
import pt.ulisboa.tecnico.rnl.dei.dms.repository.ResourceRepository;
import pt.ulisboa.tecnico.rnl.dei.dms.mappers.ResourceMapper;

import pt.ulisboa.tecnico.rnl.dei.dms.repository.PersonRepository;
import pt.ulisboa.tecnico.rnl.dei.dms.models.person.Person;


@Service
public class ResourceService {

	private final ResourceRepository resourceRepository;
	private final PersonRepository personRepository;
	private final ResourceMapper resourceMapper;

	@Autowired
	public ResourceService(
			ResourceRepository resourceRepository, 
			PersonRepository personRepository, 
			ResourceMapper resourceMapper
		) {
		this.resourceRepository = resourceRepository;
		this.personRepository = personRepository;
		this.resourceMapper = resourceMapper;
	}
    
    public ResourceDto createResource(ResourceDto resourceDto) {
        Resource resource = resourceMapper.dtoToResource(resourceDto);

        resourceRepository.save(resource);
        return resourceMapper.resourceToDto(resource);
    }

    public List<ResourceDto> getResources() {
		List<Resource> resources = resourceRepository.findAll();

		return resourceMapper.resourcesToDtos(resources);
    }

    public ResourceDto getResource(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId).get();

		return resourceMapper.resourceToDto(resource);
    }

    public ResourceDto updateResource(ResourceDto resourceDto) {
        Resource resource = resourceRepository.findById(resourceDto.getId()).get();

		resource.setName(resourceDto.getName()); 
		resource.setType(resourceDto.getType()); 
		resource.setState(resourceDto.getState()); 
		resource.setLocation(resourceDto.getLocation());

        resourceRepository.save(resource);
        return resourceMapper.resourceToDto(resource);
    }

	//public ResourceDto toMaintenance(ResourceDto resourceDto) {
	//       Resource resource = resourceRepository.findById(resourceDto.getId()).get();
	//
	//	resource.setState(ResourceState.MAINTENANCE);
	//
	//	resourceRepository.save(resource);
	//	return resourceMapper.resourceToDto(resource);
	//}
	//
	//
	//public ResourceDto setAvailable(ResourceDto resourceDto) {
	//       Resource resource = resourceRepository.findById(resourceDto.getId()).get();
	//
	//	resource.setState(ResourceState.AVAILABLE);
	//
	//	resourceRepository.save(resource);
	//	return resourceMapper.resourceToDto(resource);
	//}

	
	// FIX: Not functioning, probably something to do with lazy initializing
	
	//@Scheduled(fixedRate = 60000)
	//public void updateResourceStates() {
	//	List<Resource> resources = resourceRepository.findAll();
	//
	//	for (Resource resource : resources) {
	//		resource.updateState();
	//		resourceRepository.save(resource);
	//	}
	//}

    public void deleteResource(Long resourceId) {
        resourceRepository.deleteById(resourceId);
    }
} 
