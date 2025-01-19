package pt.ulisboa.tecnico.rnl.dei.dms.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.Resource;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.ResourceDto;


@Mapper(componentModel = "spring")
public interface ResourceMapper {

	ResourceMapper INSTANCE = Mappers.getMapper( ResourceMapper.class );

	ResourceDto resourceToDto(Resource resource);

    List<ResourceDto> resourcesToDtos(List<Resource> resources);

	Resource dtoToResource(ResourceDto resourceDto);

}

