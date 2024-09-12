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
import ru.clevertec.house.facade.impl.HouseFacadeImpl;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.parent.BaseModel;
import ru.clevertec.house.model.entity.parent.UuidModel;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.util.HouseTestBuilder;
import ru.clevertec.house.util.Patcher;
import ru.clevertec.house.util.PersonTestBuilder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;
import static ru.clevertec.house.constant.Constant.LIMIT;
import static ru.clevertec.house.constant.Constant.OFFSET;

@ExtendWith(MockitoExtension.class)
public class HouseFacadeTest {

    @Mock
    private HouseService houseService;

    @Mock
    private HouseConverter houseConverter;

    @Mock
    private PersonConverter personConverter;

    @Mock
    private Patcher patcher;

    @InjectMocks
    private HouseFacadeImpl houseFacade;

    @Captor
    private ArgumentCaptor<House> argumentCaptor;

    @Test
    void getByUuidShouldReturnExpectedHouseWhenFound() {
        var expected = HouseTestBuilder.builder().build().buildHouse();
        var houseDto = HouseTestBuilder.builder().build().buildHouseDto();
        var uuid = expected.getUuid();

        when(houseService.getByUuid(uuid)).thenReturn(expected);
        when(houseConverter.convert(expected)).thenReturn(houseDto);

        var actual = houseFacade.getByUuid(uuid);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(UuidModel.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }

    @Test
    void getAllShouldReturnExpectedListHouses() {
        when(houseService.getAll(OFFSET, LIMIT)).thenReturn(List.of());

        houseFacade.getAll(OFFSET, LIMIT);

        verify(houseService).getAll(OFFSET, LIMIT);
        verifyNoMoreInteractions(houseService);
    }

    @Test
    void getAllShouldReturnEmptyPageWhenEmptyPageHouses() {
        when(houseService.getAll(OFFSET, LIMIT)).thenReturn(List.of());

        var actualList = houseFacade.getAll(OFFSET, LIMIT);

        assertEquals(0, actualList.size());
        verify(houseService, times(1)).getAll(OFFSET, LIMIT);
    }

    @Test
    void getAllResidentsShouldReturnExpectedListPerson() {
        var uuid = HouseTestBuilder.builder().build().getUuid();
        var expectedPersonList = PersonTestBuilder.builder().build().buildListPerson();

        when(houseService.getAllResidents(uuid)).thenReturn(expectedPersonList);

        var actual = houseFacade.getAllResidents(uuid);

        assertEquals(2, actual.size());
    }

    @Test
    void createShouldInvokeRepositoryWithoutHouseId() {
        var houseToSave = HouseTestBuilder.builder().withId(null).build().buildHouse();
        var expected = HouseTestBuilder.builder().build().buildHouse();
        var dto = HouseTestBuilder.builder().build().buildHouseCreateDto();

        doReturn(expected).when(houseService).create(houseToSave);
        when(houseConverter.convert(dto)).thenReturn(houseToSave);

        houseFacade.create(dto);

        verify(houseService).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(BaseModel.Fields.id, null);
    }

    @Test
    void updateShouldCallsMergeAndSaveWhenHouseFound() {
        var uuid = HouseTestBuilder.builder().build().getUuid();
        var dto = HouseTestBuilder.builder().build().buildHouseUpdateDto();
        var expected = HouseTestBuilder.builder().build().buildHouse();

        when(houseService.getByUuid(uuid)).thenReturn(expected);

        houseFacade.update(dto);

        verify(houseService, times(1)).getByUuid(uuid);
        verify(houseConverter, times(1)).merge(argumentCaptor.capture(), eq(dto));
        assertSame(expected, argumentCaptor.getValue());
        verify(houseService, times(1)).update(expected);
    }

    @Test
    void delete() {
        var uuid = HouseTestBuilder.builder().build().getUuid();

        houseFacade.delete(uuid);

        verify(houseService).delete(uuid);
    }
}