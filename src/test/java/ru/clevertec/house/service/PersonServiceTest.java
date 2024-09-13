package ru.clevertec.house.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.model.entity.parent.BaseModel;
import ru.clevertec.house.model.entity.parent.UuidModel;
import ru.clevertec.house.patcher.Patcher;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.impl.PersonServiceImpl;
import ru.clevertec.house.util.HouseTestBuilder;
import ru.clevertec.house.util.PersonTestBuilder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.clevertec.house.constant.Constant.LIMIT;
import static ru.clevertec.house.constant.Constant.OFFSET;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private Patcher patcher;

    @InjectMocks
    private PersonServiceImpl personService;

    @Captor
    private ArgumentCaptor<Person> argumentCaptor;

    @Test
    void getByUuidShouldReturnExpectedPersonWhenFound() {
        var expected = PersonTestBuilder.builder().build().buildPerson();
        var uuid = expected.getUuid();

        when(personRepository.getByUuid(uuid)).thenReturn(expected);

        var actual = personService.getByUuid(uuid);

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

        var actualList = personService.getAll(OFFSET, LIMIT);

        assertEquals(0, actualList.size());
        verify(personRepository, times(1)).getAll(OFFSET, LIMIT);
    }

    @Test
    void getAllHousesShouldReturnExpectedListHouses() {
        var uuid = PersonTestBuilder.builder().build().getUuid();
        var expected = PersonTestBuilder.builder().build().buildPerson();

        when(personRepository.getByUuid(uuid)).thenReturn(expected);

        var result = personService.getAllHouses(uuid);

        assertEquals(2, result.size());
        verify(personRepository).getByUuid(uuid);
    }

    @Test
    void createShouldInvokeRepositoryWithoutPersonId() {
        var homeUuid = PersonTestBuilder.builder().build().getHomeUuid();
        var expected = PersonTestBuilder.builder().withId(null).build().buildPerson();
        var house = HouseTestBuilder.builder().build().buildHouse();
        house.setUuid(homeUuid);

        when(personRepository.create(expected)).thenReturn(expected);

        personService.create(expected);

        verify(personRepository).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(BaseModel.Fields.id, null);
        verify(personRepository).create(expected);
    }

    @Test
    void updateShouldInvokeRepositoryWhenPersonFound() {
        var expected = PersonTestBuilder.builder().build().buildPerson();

        when(personRepository.update(expected)).thenReturn(expected);

        personService.update(expected);

        verify(personRepository, times(1)).update(expected);
    }

    @Test
    void patchShouldInvokeRepositoryWhenPersonFound() {
        var expected = PersonTestBuilder.builder().build().buildPerson();
        var uuid = expected.getUuid();

        when(personRepository.getByUuid(uuid)).thenReturn(expected);

        personService.patch(expected);

        verify(personRepository, times(1)).getByUuid(uuid);
        verify(personRepository, times(1)).update(expected);
    }

    @Test
    void delete() {
        UUID uuid = HouseTestBuilder.builder().build().getUuid();

        personService.delete(uuid);

        verify(personRepository).deleteByUuid(uuid);
    }
}