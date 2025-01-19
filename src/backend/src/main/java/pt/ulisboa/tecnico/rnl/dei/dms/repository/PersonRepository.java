package pt.ulisboa.tecnico.rnl.dei.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.models.person.Person;

@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, String> {}
