package de.tum.in.ase.eist.controller;

import de.tum.in.ase.eist.model.Note;
import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.util.PersonSortingOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PersonController {

    private final WebClient webClient;

    private final List<Person> persons;

    public PersonController() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.persons = new ArrayList<>();
    }

    public void addPerson(Person person, Consumer<List<Person>> personsConsumer) {

        webClient.post()
                .uri("persons")
                .bodyValue(person)
                .retrieve()
                .bodyToMono(Person.class)
                .onErrorStop()
                .subscribe(newPerson -> {
                    persons.add(newPerson);
                    personsConsumer.accept(persons);
                });

        // TODO Part 2: Make an http post request to the server
    }

    public void updatePerson(Person person, Consumer<List<Person>> personsConsumer) {

        webClient.put()
                .uri("persons/" + person.getId())
                .bodyValue(person)
                .retrieve()
                .bodyToMono(Person.class)
                .onErrorStop()
                .subscribe(newPerson -> {
                    persons.replaceAll(oldPerson -> oldPerson.getId().equals(newPerson.getId()) ? newPerson : oldPerson);
                    personsConsumer.accept(persons);
                });

        // TODO Part 2: Make an http put request to the server
    }

    public void deletePerson(Person person, Consumer<List<Person>> personsConsumer) {

        webClient.delete()
                .uri("persons/" + person.getId())
                .retrieve()
                .toBodilessEntity()
                .onErrorStop()
                .subscribe(x -> {
                    persons.remove(person);
                    personsConsumer.accept(persons);

                });
        // TODO Part 2: Make an http delete request to the server
    }

    public void getAllPersons(PersonSortingOptions sortingOptions, Consumer<List<Person>> personsConsumer) {

        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("persons")
                        .queryParam("sortingOrder", sortingOptions.getSortingOrder())
                        .queryParam("sortField", sortingOptions.getSortField())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Person>>() {})
                .onErrorStop()
                .subscribe(newPersons -> {
                    persons.clear();
                    persons.addAll(newPersons);
                    personsConsumer.accept(persons);
                });

        // TODO Part 2: Make an https get request to the server
    }
}
