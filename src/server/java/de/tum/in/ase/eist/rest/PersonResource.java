package de.tum.in.ase.eist.rest;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.service.PersonService;
import de.tum.in.ase.eist.util.PersonSortingOptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PersonResource {

    private final PersonService personService;

    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("persons")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        if (person.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(personService.savePerson(person));
    }

    @PutMapping("persons/{personID}")
    public ResponseEntity<Person> updatePerson(@RequestBody  Person person, @PathVariable("personID") UUID uuid) {
        if(!person.getId().equals(uuid)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(personService.savePerson(person));
    }

    @DeleteMapping("persons/{personID}")
    public ResponseEntity<Void> deletePerson(@PathVariable("personID") UUID personID) {
        personService.deletePerson(personID);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("persons")
    public ResponseEntity<List<Person>> getAllPersons(@RequestParam(value = "sortingOrder", required = false, defaultValue = "ASCENDING")PersonSortingOptions.SortingOrder sortingOrder, @RequestParam(value = "sortField", required = false, defaultValue = "ID")PersonSortingOptions.SortField sortField)  {

        return ResponseEntity.ok(personService.getAllPersons(new PersonSortingOptions(sortingOrder, sortField)));

    }

    // TODO Part 1: Implement the specified endpoints here
}
