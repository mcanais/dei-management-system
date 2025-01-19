package pt.ulisboa.tecnico.rnl.dei.dms.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pt.ulisboa.tecnico.rnl.dei.dms.models.person.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.dtos.PersonDto;


@Mapper(componentModel = "spring")
public interface PersonMapper {

	PersonMapper INSTANCE = Mappers.getMapper( PersonMapper.class );

	PersonDto personToDto(Person person);

    List<PersonDto> personsToDtos(List<Person> persons);

	Person dtoToPerson(PersonDto personDto);

}

