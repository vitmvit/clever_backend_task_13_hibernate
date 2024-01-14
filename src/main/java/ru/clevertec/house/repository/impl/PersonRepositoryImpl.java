package ru.clevertec.house.repository.impl;

import lombok.SneakyThrows;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.house.config.DatasourceConfig;
import ru.clevertec.house.constant.SexType;
import ru.clevertec.house.exception.EntityNotFoundException;
import ru.clevertec.house.exception.SqlExecuteException;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.repository.PersonRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersonRepositoryImpl implements PersonRepository {

    private DataSource dataSource;
    private final AnnotationConfigApplicationContext context;

    public PersonRepositoryImpl() {
        this.context = new AnnotationConfigApplicationContext(DatasourceConfig.class);
        dataSource = context.getBean(DataSource.class);
    }

    @Override
    public Optional<Person> getByUUID(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT id, name, surname, sex, passport_series, passport_number, create_date, update_date FROM person where uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Person person = new Person();
                person.setUuid(uuid);
                person.setId(resultSet.getLong("id"));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setSex(SexType.valueOf(resultSet.getString("sex")));
                person.setPassportSeries(resultSet.getString("passport_series"));
                person.setPassportNumber(resultSet.getString("passport_number"));
                person.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
                person.setUpdateDate(resultSet.getTimestamp("update_date").toLocalDateTime());

                return Optional.of(person);
            }
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
        return Optional.empty();
    }

    @SneakyThrows
    @Override
    public List<Person> getAll() {
        try {
            List<Person> personList = new ArrayList<>();
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * from person");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getLong("id"));
                person.setUuid(UUID.fromString(resultSet.getString("uuid")));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setSex(SexType.valueOf(resultSet.getString("sex")));
                person.setPassportSeries(resultSet.getString("passport_series"));
                person.setPassportNumber(resultSet.getString("passport_number"));
                person.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
                person.setUpdateDate(resultSet.getTimestamp("update_date").toLocalDateTime());
                personList.add(person);
            }
            return personList;
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
    }

    @Override
    public Person create(Person person) {
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO person (uuid, name, surname, sex, passport_series, passport_number, create_date, update_date) VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, String.valueOf(person.getUuid()));
            ps.setString(2, person.getName());
            ps.setString(3, person.getSurname());
            ps.setString(4, String.valueOf(person.getSex()));
            ps.setString(5, person.getPassportSeries());
            ps.setString(6, person.getPassportNumber());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

            return getByUUID(person.getUuid()).orElseThrow(EntityNotFoundException::new);
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
    }

    @Override
    public Person update(Person person) {
        String query = "UPDATE person SET name = ?, surname = ?, sex = ?, passport_series = ?, passport_number = ?, create_date = ?, update_date = ? WHERE uuid = ?";
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, person.getName());
            ps.setString(2, person.getSurname());
            ps.setString(3, String.valueOf(person.getSex()));
            ps.setString(4, person.getPassportSeries());
            ps.setString(5, person.getPassportNumber());
            ps.setTimestamp(6, Timestamp.valueOf(person.getCreateDate()));
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, String.valueOf(person.getUuid()));
            return person;
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
    }

    @Override
    public void deleteByUUID(UUID uuid) {
        String query = "DELETE FROM person WHERE uuid = ?";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
    }
}
