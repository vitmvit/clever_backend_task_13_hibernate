package ru.clevertec.house.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.house.converter.HouseConverter;
import ru.clevertec.house.converter.PersonConverter;
import ru.clevertec.house.facade.impl.PersonFacadeImpl;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.model.entity.parent.BaseModel;
import ru.clevertec.house.model.entity.parent.UuidModel;
import ru.clevertec.house.patcher.Patcher;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.util.HouseTestBuilder;
import ru.clevertec.house.util.PersonTestBuilder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;
import static ru.clevertec.house.constant.Constant.LIMIT;
import static ru.clevertec.house.constant.Constant.OFFSET;

@ExtendWith(MockitoExtension.class)
public class PersonFacadeTest {

    @Mock
    private PersonService personRepository;

    @Mock
    private HouseService houseService;

    @Mock
    private PersonConverter personConverter;

    @Mock
    private HouseConverter houseConverter;

    @Mock
    private Patcher patcher;

    @InjectMocks
    private PersonFacadeImpl personFacade;

    @Captor
    private ArgumentCaptor<Person> argumentCaptor;

    @Test
    void getByUuidShouldReturnExpectedPersonWhenFound() {
        var expected = PersonTestBuilder.builder().build().buildPerson();
        var personDto = PersonTestBuilder.builder().build().buildPersonDto();
        var uuid = expected.getUuid();

        when(personRepository.getByUuid(uuid)).thenReturn(expected);
        when(personConverter.convert(expected)).thenReturn(personDto);

        var actual = personFacade.getByUuid(uuid);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(UuidModel.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.sex, expected.getSex());
    }

    @Test
    void getAllShouldReturnExpectedListPersons() {
        when(personRepository.getAll(OFFSET, LIMIT)).thenReturn(List.of());

        personFacade.getAll(OFFSET, LIMIT);

        verify(personRepository).getAll(OFFSET, LIMIT);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void getAllShouldReturnEmptyPageWhenEmptyPagePersons() {
        when(personRepository.getAll(OFFSET, LIMIT)).thenReturn(List.of());

        var actualList = personFacade.getAll(OFFSET, LIMIT);

        assertEquals(0, actualList.size());
        verify(personRepository, times(1)).getAll(OFFSET, LIMIT);
    }

    @Test
    void getAllHousesShouldReturnExpectedListHouse() {
        var uuid = PersonTestBuilder.builder().build().getUuid();
        var expectedHouseList = HouseTestBuilder.builder().build().buildListHouse();

        when(personRepository.getAllHouses(uuid)).thenReturn(expectedHouseList);

        var actual = personFacade.getAllHouses(uuid);

        assertEquals(2, actual.size());
    }

    @Test
    void createShouldInvokeRepositoryWithoutPersonId() {
        var dto = PersonTestBuilder.builder().withId(null).build().buildPersonCreateDto();
        var homeUuid = dto.getHomeUuid();
        var expected = PersonTestBuilder.builder().withId(null).build().buildPerson();
        var house = HouseTestBuilder.builder().build().buildHouse();
        house.setUuid(homeUuid);

        when(houseService.getByUuid(homeUuid)).thenReturn(house);
        when(personConverter.convert(dto)).thenReturn(expected);
        when(personRepository.create(expected)).thenReturn(expected);

        personFacade.create(dto);

        verify(personRepository).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(BaseModel.Fields.id, null);
        verify(houseService).getByUuid(homeUuid);
        verify(personConverter).convert(dto);
        verify(personRepository).create(expected);
    }

    @Test
    void updateShouldCallsMergeAndSaveWhenPersonFound() {
        var dto = PersonTestBuilder.builder().build().buildPersonUpdateDto();
        var uuid = dto.getUuid();
        var expected = PersonTestBuilder.builder().build().buildPerson();

        when(personRepository.getByUuid(uuid)).thenReturn(expected);

        personFacade.update(dto);

        verify(personRepository, times(1)).getByUuid(uuid);
        verify(personConverter, times(1)).merge(argumentCaptor.capture(), eq(dto));
        assertSame(expected, argumentCaptor.getValue());
        verify(personRepository, times(1)).update(expected);
    }

    @Test
    void delete() {
        var uuid = HouseTestBuilder.builder().build().getUuid();

        personFacade.delete(uuid);

        verify(personRepository).delete(uuid);
    }
}