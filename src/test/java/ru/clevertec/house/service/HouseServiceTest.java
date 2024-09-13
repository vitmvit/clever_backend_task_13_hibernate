package ru.clevertec.house.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.parent.BaseModel;
import ru.clevertec.house.model.entity.parent.UuidModel;
import ru.clevertec.house.patcher.Patcher;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.service.impl.HouseServiceImpl;
import ru.clevertec.house.util.HouseTestBuilder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.clevertec.house.constant.Constant.LIMIT;
import static ru.clevertec.house.constant.Constant.OFFSET;

@ExtendWith(MockitoExtension.class)
public class HouseServiceTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private Patcher patcher;

    @InjectMocks
    private HouseServiceImpl houseService;

    @Captor
    private ArgumentCaptor<House> argumentCaptor;

    @Test
    void getByUuidShouldReturnExpectedHouseWhenFound() {
        var expected = HouseTestBuilder.builder().build().buildHouse();
        var uuid = expected.getUuid();

        when(houseRepository.getByUuid(uuid)).thenReturn(expected);

        var actual = houseService.getByUuid(uuid);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(UuidModel.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }

    @Test
    void getAllShouldReturnExpectedListHouses() {
        when(houseRepository.getAll(OFFSET, LIMIT)).thenReturn(List.of());

        houseService.getAll(OFFSET, LIMIT);

        verify(houseRepository).getAll(OFFSET, LIMIT);
        verifyNoMoreInteractions(houseRepository);
    }

    @Test
    void getAllShouldReturnEmptyPageWhenEmptyPageHouses() {
        when(houseRepository.getAll(OFFSET, LIMIT)).thenReturn(List.of());

        var actualList = houseService.getAll(OFFSET, LIMIT);

        assertEquals(0, actualList.size());
        verify(houseRepository, times(1)).getAll(OFFSET, LIMIT);
    }

    @Test
    void getAllResidentsShouldReturnExpectedListPersons() {
        var uuid = HouseTestBuilder.builder().build().getUuid();
        var expected = HouseTestBuilder.builder().build().buildHouse();

        when(houseRepository.getByUuid(uuid)).thenReturn(expected);

        var actual = houseService.getAllResidents(uuid);

        assertEquals(2, actual.size());
        verify(houseRepository).getByUuid(uuid);
    }

    @Test
    void createShouldInvokeRepositoryWithoutHouseId() {
        var houseToSave = HouseTestBuilder.builder().withId(null).build().buildHouse();
        var expected = HouseTestBuilder.builder().build().buildHouse();

        doReturn(expected).when(houseRepository).create(houseToSave);

        houseService.create(houseToSave);

        verify(houseRepository).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(BaseModel.Fields.id, null);
    }

    @Test
    void updateShouldInvokeRepositoryWhenHouseFound() {
        var expected = HouseTestBuilder.builder().build().buildHouse();

        when(houseRepository.update(expected)).thenReturn(expected);

        houseService.update(expected);

        verify(houseRepository, times(1)).update(expected);
    }

    @Test
    void patchShouldInvokeRepositoryWhenHouseFound() {
        var uuid = HouseTestBuilder.builder().build().getUuid();
        var expected = HouseTestBuilder.builder().build().buildHouse();
        var houseUpdate = new House();

        when(houseRepository.getByUuid(uuid)).thenReturn(houseUpdate);

        houseService.patch(expected);

        verify(houseRepository, times(1)).getByUuid(uuid);
        verify(houseRepository, times(1)).update(houseUpdate);
    }

    @Test
    void delete() {
        var uuid = HouseTestBuilder.builder().build().getUuid();

        houseService.delete(uuid);

        verify(houseRepository).deleteByUuid(uuid);
    }
}