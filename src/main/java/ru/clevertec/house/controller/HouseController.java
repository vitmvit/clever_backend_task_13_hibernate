package ru.clevertec.house.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
import ru.clevertec.house.service.HouseService;

import java.util.List;
import java.util.UUID;

import static ru.clevertec.house.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.house.constant.Constant.OFFSET_DEFAULT;

@RestController
@AllArgsConstructor
@RequestMapping("/api/houses")
public class HouseController {

    private final HouseService houseService;

    @GetMapping
    public ResponseEntity<List<HouseDto>> getAll(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                 @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.getAll(offset, limit));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<HouseDto> getByUuid(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.getByUuid(uuid));
    }

    @GetMapping("residents/{uuid}")
    public ResponseEntity<List<PersonDto>> getAllResidents(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.getAllResidents(uuid));
    }

    @GetMapping("search/{city}")
    public ResponseEntity<List<HouseDto>> searchHouseByCity(@PathVariable("city") String city) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.searchByCity(city));
    }

    @PostMapping
    public ResponseEntity<HouseDto> create(@RequestBody HouseCreateDto houseCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(houseService.create(houseCreateDto));
    }

    @PutMapping
    public ResponseEntity<HouseDto> update(@RequestBody HouseUpdateDto houseUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.update(houseUpdateDto));
    }

    @PatchMapping
    public ResponseEntity<HouseDto> patch(@RequestBody HouseUpdateDto houseUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.patch(houseUpdateDto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        houseService.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
