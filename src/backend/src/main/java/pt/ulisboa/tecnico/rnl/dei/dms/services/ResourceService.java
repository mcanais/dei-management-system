package pt.ulisboa.tecnico.rnl.dei.dms.services;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.Resource;
import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.ResourceState;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.ResourceDto;
import pt.ulisboa.tecnico.rnl.dei.dms.repository.ResourceRepository;
import pt.ulisboa.tecnico.rnl.dei.dms.mappers.ResourceMapper;

import pt.ulisboa.tecnico.rnl.dei.dms.models.person.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.repository.PersonRepository;

import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.Reservation;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationState;
import pt.ulisboa.tecnico.rnl.dei.dms.models.reservation.ReservationType;

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


    public void deleteResource(Long resourceId) {
        resourceRepository.deleteById(resourceId);
    }

	public void updateResourceListState(List<Long> resourceIdsList) {
		for (Long resourceId : resourceIdsList) {
        	Resource resource = resourceRepository.findById(resourceId).get();
			this.updateResourceState(resource);
			
			resourceRepository.save(resource);
		}
	}

	public void updateResourceState(Resource resource) {
		if (resource.getReservations() == null || resource.getReservations().isEmpty()) {
			resource.setState(ResourceState.AVAILABLE);
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		for (Reservation reservation : resource.getReservations()) {
			if (reservation.getState() == ReservationState.FINISHED || reservation.getState() == ReservationState.CANCELLED) {
				continue;
			}

			LocalDate startDate = LocalDate.parse(reservation.getStartDate(), formatter); 
			LocalDate finishDate = LocalDate.parse(reservation.getFinishDate(), formatter); 
			LocalDate currentDate = LocalDate.now();

			if (!currentDate.isBefore(startDate) && !currentDate.isAfter(finishDate)) {
				if (reservation.getType() == ReservationType.PERSON) {
					resource.setState(ResourceState.INUSE);
				} else if (reservation.getType() == ReservationType.MAINTENANCE) {
					resource.setState(ResourceState.MAINTENANCE);
				}

				return;
			}
		}

		resource.setState(ResourceState.AVAILABLE);
	}
	
	
	//@Scheduled(fixedRate = 60000)
	//public void updateResourceStates() {
	//	List<Resource> resources = resourceRepository.findAll();
	//
	//	for (Resource resource : resources) {
	//		resource.updateState();
	//		resourceRepository.save(resource);
	//	}
	//}
} 
