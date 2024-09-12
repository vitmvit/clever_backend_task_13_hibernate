package ru.clevertec.house.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.house.facade.PersonFacade;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;

import java.util.List;
import java.util.UUID;

import static ru.clevertec.house.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.house.constant.Constant.OFFSET_DEFAULT;

@RestController
@AllArgsConstructor
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonFacade personFacade;

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAll(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                  @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personFacade.getAll(offset, limit));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PersonDto> getByUuid(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personFacade.getByUuid(uuid));
    }

    @GetMapping("houses/{uuid}")
    public ResponseEntity<List<HouseDto>> getAllHouses(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personFacade.getAllHouses(uuid));
    }

    @GetMapping("search/{surname}")
    public ResponseEntity<List<PersonDto>> searchHouseByCity(@PathVariable("surname") String surname) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personFacade.searchBySurname(surname));
    }

    @PostMapping
    public ResponseEntity<PersonDto> create(@RequestBody PersonCreateDto personCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(personFacade.create(personCreateDto));
    }

    @PutMapping
    public ResponseEntity<PersonDto> update(@RequestBody PersonUpdateDto personUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personFacade.update(personUpdateDto));
    }

    @PatchMapping
    public ResponseEntity<PersonDto> patch(@RequestBody PersonUpdateDto personUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personFacade.patch(personUpdateDto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        personFacade.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}