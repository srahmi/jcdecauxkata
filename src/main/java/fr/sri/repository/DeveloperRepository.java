package fr.sri.repository;

import fr.sri.domain.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by srahmi on 01/02/2017.
 */
@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long>{

    List<Developer> findByProgrammingLanguageName(String name);
}
