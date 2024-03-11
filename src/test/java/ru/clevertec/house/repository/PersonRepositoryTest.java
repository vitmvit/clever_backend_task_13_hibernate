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
import ru.clevertec.house.model.entity.Person;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.clevertec.house.constant.Constant.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Hammer2Config.class)
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void getByUuidShouldReturnExpectedHouse() {
        UUID uuid = UUID.fromString("faa3c8d8-b6f8-4100-b253-3cd453a03da7");

        Person actual = personRepository.getByUuid(uuid);

        assertEquals(uuid, actual.getUuid());
    }

    @Test
    public void getAllShouldReturnExpectedHousePage() {
        List<Person> actualPage = personRepository.getAll(OFFSET, LIMIT);

        assertEquals(3, actualPage.size());
    }

    @Test
    public void getBySurnameContainingShouldReturnHouseList() {
        List<Person> actualPage = personRepository.getBySurnameContaining(FRAGMENT_SURNAME);

        assertEquals(1, actualPage.size());
    }

    @Test
    public void updateShouldReturnUpdatedHouse() {
        Person toUpdate = personRepository.getByUuid(UUID.fromString("1cd31719-2064-4f90-a909-d7dd3b880d1e"));
        String surnameToUpdate = "SurnameNew";

        toUpdate.setSurname(surnameToUpdate);
        Person actual = personRepository.update(toUpdate);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.surname, surnameToUpdate);
    }

    @Test
    public void deleteByUuid() {
        Person toDelete = personRepository.getByUuid(UUID.fromString("9701364c-e2d4-4857-aac9-488342b66d77"));

        personRepository.deleteByUuid(toDelete.getUuid());

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM person WHERE uuid = ?", Integer.class, toDelete.getUuid());
        assertEquals(Integer.valueOf(0), count);
    }
}
