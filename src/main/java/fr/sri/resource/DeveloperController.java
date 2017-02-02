package fr.sri.resource;

import fr.sri.domain.Developer;
import fr.sri.domain.ProgrammingLanguage;
import fr.sri.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@RestController
@RequestMapping("developers")
public class DeveloperController {

    @Autowired
    DeveloperRepository developerRepository;


    @RequestMapping(method = RequestMethod.GET)
    public List<Developer> allDevelopers() {
        return developerRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveDeveloper(@RequestBody Developer developer) {
        Developer created = developerRepository.save(developer);

        if (developer != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(created.getId()).toUri();

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDeveloper(@PathVariable Long id, @RequestBody Developer developer) {
        Developer developerToUpdate = developerRepository.findOne(id);

        if (developerToUpdate != null) {
            developerToUpdate.setName(developer.getName());
            developerToUpdate.setProgrammingLanguage(developer.getProgrammingLanguage());

            developerRepository.save(developerToUpdate);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}/programminglanguage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProgrammingLangage(@PathVariable Long id, @RequestBody ProgrammingLanguage programmingLanguage) {
        Developer developerToUpdate = developerRepository.findOne(id);

        if (developerToUpdate != null) {
            developerToUpdate.setProgrammingLanguage(programmingLanguage);
            developerRepository.save(developerToUpdate);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "programminglanguage/{name}")
    public ResponseEntity<?> developersByProgramingLanguage(@PathVariable String name) {
        List<Developer> developers = developerRepository.findByProgrammingLanguageName(name);

        if (isNotEmpty(developers)) {

            return ResponseEntity.ok(developers);
        }

        return ResponseEntity.notFound().build();
    }

}
