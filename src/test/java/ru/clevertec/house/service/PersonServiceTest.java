package ru.clevertec.house.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.house.converter.HouseConverter;
import ru.clevertec.house.converter.PersonConverter;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.model.entity.parent.BaseModel;
import ru.clevertec.house.model.entity.parent.UuidModel;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.impl.PersonServiceImpl;
import ru.clevertec.house.util.HouseTestBuilder;
import ru.clevertec.house.util.Patcher;
import ru.clevertec.house.util.PersonTestBuilder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static ru.clevertec.house.constant.Constant.LIMIT;
import static ru.clevertec.house.constant.Constant.OFFSET;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private PersonConverter personConverter;

    @Mock
    private HouseConverter houseConverter;

    @Mock
    private Patcher patcher;

    @InjectMocks
    private PersonServiceImpl personService;

    @Captor
    private ArgumentCaptor<Person> argumentCaptor;


    @Test
    void getByUuidShouldReturnExpectedPersonWhenFound() {
        Person expected = PersonTestBuilder.builder().build().buildPerson();
        PersonDto personDto = PersonTestBuilder.builder().build().buildPersonDto();
        UUID uuid = expected.getUuid();

        when(personRepository.getByUuid(uuid)).thenReturn(expected);
        when(personConverter.convert(expected)).thenReturn(personDto);

        PersonDto actual = personService.getByUuid(uuid);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(UuidModel.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.sex, expected.getSex());
    }

    @Test
    void getAllShouldReturnExpectedListPersons() {
        when(personRepository.getAll(OFFSET, LIMIT)).thenReturn(List.of());

        personService.getAll(OFFSET, LIMIT);

        verify(personRepository).getAll(OFFSET, LIMIT);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void getAllShouldReturnEmptyPageWhenEmptyPagePersons() {
        when(personRepository.getAll(OFFSET, LIMIT)).thenReturn(List.of());

        List<PersonDto> actualList = personService.getAll(OFFSET, LIMIT);

        assertEquals(0, actualList.size());
        verify(personRepository, times(1)).getAll(OFFSET, LIMIT);
    }

    @Test
    void getAllHousesShouldReturnExpectedListHouseDto() {
        UUID uuid = PersonTestBuilder.builder().build().getUuid();
        Person person = PersonTestBuilder.builder().build().buildPerson();

        when(personRepository.getByUuid(uuid)).thenReturn(person);

        List<HouseDto> result = personService.getAllHouses(uuid);

        assertEquals(2, result.size());
        verify(personRepository).getByUuid(uuid);
    }

    @Test
    void createShouldInvokeRepositoryWithoutPersonId() {
        PersonCreateDto dto = PersonTestBuilder.builder().withId(null).build().buildPersonCreateDto();
        UUID homeUuid = dto.getHomeUuid();
        Person person = PersonTestBuilder.builder().withId(null).build().buildPerson();
        House house = HouseTestBuilder.builder().build().buildHouse();
        house.setUuid(homeUuid);

        when(houseRepository.getByUuid(homeUuid)).thenReturn(house);
        when(personConverter.convert(dto)).thenReturn(person);
        when(personRepository.create(person)).thenReturn(person);

        personService.create(dto);

        verify(personRepository).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(BaseModel.Fields.id, null);
        verify(houseRepository).getByUuid(homeUuid);
        verify(personConverter).convert(dto);
        verify(personRepository).create(person);
    }

    @Test
    void updateShouldCallsMergeAndSaveWhenPersonFound() {
        PersonUpdateDto dto = PersonTestBuilder.builder().build().buildPersonUpdateDto();
        UUID uuid = dto.getUuid();
        Person person = PersonTestBuilder.builder().build().buildPerson();

        when(personRepository.getByUuid(uuid)).thenReturn(person);
        personService.update(dto);

        verify(personRepository, times(1)).getByUuid(uuid);
        verify(personConverter, times(1)).merge(argumentCaptor.capture(), eq(dto));
        assertSame(person, argumentCaptor.getValue());
        verify(personRepository, times(1)).update(person);
    }

    @Test
    void patchShouldCallsMergeAndSaveWhenPersonFound() {
        PersonUpdateDto dto = PersonTestBuilder.builder().build().buildPersonUpdateDto();
        UUID uuid = dto.getUuid();
        Person person = PersonTestBuilder.builder().build().buildPerson();

        when(personRepository.getByUuid(uuid)).thenReturn(person);
        personService.patch(dto);

        verify(personRepository, times(1)).getByUuid(uuid);
        verify(personConverter, times(1)).convert(argumentCaptor.capture());
        assertSame(person, argumentCaptor.getValue());
        verify(personRepository, times(1)).update(person);
    }

    @Test
    void delete() {
        UUID uuid = HouseTestBuilder.builder().build().getUuid();
        personService.delete(uuid);
        verify(personRepository).deleteByUuid(uuid);
    }
}

