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
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.parent.BaseModel;
import ru.clevertec.house.model.entity.parent.UuidModel;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.service.impl.HouseServiceImpl;
import ru.clevertec.house.util.HouseTestBuilder;
import ru.clevertec.house.util.Patcher;

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
public class HouseServiceTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseConverter houseConverter;

    @Mock
    private PersonConverter personConverter;

    @Mock
    private Patcher patcher;

    @InjectMocks
    private HouseServiceImpl houseService;

    @Captor
    private ArgumentCaptor<House> argumentCaptor;


    @Test
    void getByUuidShouldReturnExpectedHouseWhenFound() {
        House expected = HouseTestBuilder.builder().build().buildHouse();
        HouseDto houseDto = HouseTestBuilder.builder().build().buildHouseDto();
        UUID uuid = expected.getUuid();

        when(houseRepository.getByUuid(uuid)).thenReturn(expected);
        when(houseConverter.convert(expected)).thenReturn(houseDto);

        HouseDto actual = houseService.getByUuid(uuid);

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

        List<HouseDto> actualList = houseService.getAll(OFFSET, LIMIT);

        assertEquals(0, actualList.size());
        verify(houseRepository, times(1)).getAll(OFFSET, LIMIT);
    }

    @Test
    void getAllResidentsShouldReturnExpectedListPersonDto() {
        UUID uuid = HouseTestBuilder.builder().build().getUuid();
        House house = HouseTestBuilder.builder().build().buildHouse();

        when(houseRepository.getByUuid(uuid)).thenReturn(house);

        List<PersonDto> result = houseService.getAllResidents(uuid);

        assertEquals(2, result.size());
        verify(houseRepository).getByUuid(uuid);
    }

    @Test
    void createShouldInvokeRepositoryWithoutHouseId() {
        House houseToSave = HouseTestBuilder.builder().withId(null).build().buildHouse();
        House expected = HouseTestBuilder.builder().build().buildHouse();
        HouseCreateDto dto = HouseTestBuilder.builder().build().buildHouseCreateDto();

        doReturn(expected).when(houseRepository).create(houseToSave);
        when(houseConverter.convert(dto)).thenReturn(houseToSave);

        houseService.create(dto);

        verify(houseRepository).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(BaseModel.Fields.id, null);
    }


    @Test
    void updateShouldCallsMergeAndSaveWhenHouseFound() {
        UUID uuid = HouseTestBuilder.builder().build().getUuid();
        HouseUpdateDto dto = HouseTestBuilder.builder().build().buildHouseUpdateDto();
        House house = HouseTestBuilder.builder().build().buildHouse();

        when(houseRepository.getByUuid(uuid)).thenReturn(house);
        houseService.update(dto);

        verify(houseRepository, times(1)).getByUuid(uuid);
        verify(houseConverter, times(1)).merge(argumentCaptor.capture(), eq(dto));
        assertSame(house, argumentCaptor.getValue());
        verify(houseRepository, times(1)).update(house);
    }

    @Test
    void patchShouldCallsMergeAndSaveWhenHouseFound() {
        UUID uuid = HouseTestBuilder.builder().build().getUuid();
        HouseUpdateDto dto = HouseTestBuilder.builder().build().buildHouseUpdateDto();
        House house = new House();

        when(houseRepository.getByUuid(uuid)).thenReturn(house);
        houseService.patch(dto);

        verify(houseRepository, times(1)).getByUuid(uuid);
        verify(houseConverter, times(1)).convert(argumentCaptor.capture());
        assertSame(house, argumentCaptor.getValue());
        verify(houseRepository, times(1)).update(house);
    }

    @Test
    void delete() {
        UUID uuid = HouseTestBuilder.builder().build().getUuid();
        houseService.delete(uuid);
        verify(houseRepository).deleteByUuid(uuid);
    }
}
