package ru.clevertec.house.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.house.config.Hammer2Config;
import ru.clevertec.house.model.entity.House;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.clevertec.house.constant.Constant.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Hammer2Config.class)
public class HouseRepositoryTest {

    @Autowired
    private HouseRepository houseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void getByUuidShouldReturnExpectedHouse() {
        UUID uuid = UUID.fromString("9dd06f39-dba5-4533-8472-f1d7be435491");

        House actual = houseRepository.getByUuid(uuid);

        assertEquals(uuid, actual.getUuid());
    }

    @Test
    public void getAllShouldReturnExpectedHousePage() {
        List<House> actualPage = houseRepository.getAll(OFFSET, LIMIT);

        assertEquals(3, actualPage.size());
    }

    @Test
    public void getByCityContainingShouldReturnHouseList() {
        List<House> actualPage = houseRepository.getByCityContaining(FRAGMENT_CITY);

        assertEquals(1, actualPage.size());
    }

    @Test
    public void updateShouldReturnUpdatedHouse() {
        House toUpdate = houseRepository.getByUuid(UUID.fromString("e9ae795e-cc7c-48d8-9621-1a441deff256"));
        String cityToUpdate = "CityThree";

        toUpdate.setCity(cityToUpdate);
        House actual = houseRepository.update(toUpdate);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.city, cityToUpdate);
    }

    @Test
    public void deleteByUuid() {
        House toDelete = houseRepository.getByUuid(UUID.fromString("835915e7-2402-48a4-8f60-1364c6d99ed2"));

        houseRepository.deleteByUuid(toDelete.getUuid());

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM house WHERE uuid = ?", Integer.class, toDelete.getUuid());
        assertEquals(Integer.valueOf(0), count);
    }
}
