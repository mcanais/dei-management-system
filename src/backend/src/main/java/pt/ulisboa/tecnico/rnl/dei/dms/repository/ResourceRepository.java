package pt.ulisboa.tecnico.rnl.dei.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.models.resource.Resource;

@Repository
@Transactional
public interface ResourceRepository extends JpaRepository<Resource, Long> {}
