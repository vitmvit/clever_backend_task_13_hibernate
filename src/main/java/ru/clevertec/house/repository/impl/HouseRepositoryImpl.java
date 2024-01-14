package ru.clevertec.house.repository.impl;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.clevertec.house.config.DatasourceConfig;
import ru.clevertec.house.exception.EntityNotFoundException;
import ru.clevertec.house.exception.SqlExecuteException;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.repository.HouseRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.clevertec.house.constant.Constant.HOUSE_IS_NULL_ERROR;

public class HouseRepositoryImpl implements HouseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AnnotationConfigApplicationContext context;
    private DataSource dataSource;

    public HouseRepositoryImpl() {
        this.context = new AnnotationConfigApplicationContext(DatasourceConfig.class);
        this.jdbcTemplate = new JdbcTemplate(context.getBean(DataSource.class));
        dataSource = context.getBean(DataSource.class);
    }


    @Override
    public Optional<House> getByUUID(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT id, uuid, area, country, city, street, number, create_date  FROM house WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                House house = new House();
                house.setUuid(uuid);
                house.setId(resultSet.getLong("id"));
                house.setArea(resultSet.getDouble("area"));
                house.setCountry(resultSet.getString("country"));
                house.setCity(resultSet.getString("city"));
                house.setStreet(resultSet.getString("street"));
                house.setNumber(resultSet.getInt("number"));
                house.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());

                return Optional.of(house);
            }
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<House> getAll() {
        return this.jdbcTemplate.query(
                "SELECT id, uuid, area, country, city, street, number, create_date FROM house",
                (resultSet, rowNum) -> {
                    House house = new House();
                    house.setId(resultSet.getLong("id"));
                    house.setUuid(UUID.fromString(resultSet.getString("uuid")));
                    house.setArea(resultSet.getDouble("area"));
                    house.setCountry(resultSet.getString("country"));
                    house.setCity(resultSet.getString("city"));
                    house.setStreet(resultSet.getString("street"));
                    house.setNumber(resultSet.getInt("number"));
                    house.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
                    return house;
                });
    }

    @Override
    public House create(House house) {
        if (house == null) {
            throw new IllegalArgumentException(HOUSE_IS_NULL_ERROR);
        }
        String sql = "INSERT INTO house (uuid, area, country, city, street, number, create_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, house.getUuid().toString());
            ps.setDouble(2, house.getArea());
            ps.setString(3, house.getCountry());
            ps.setString(4, house.getCity());
            ps.setString(5, house.getStreet());
            ps.setInt(6, house.getNumber());
            ps.setTimestamp(7, Timestamp.valueOf(house.getCreateDate()));
            return ps;
        }, keyHolder);
        return getByUUID(house.getUuid()).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public House update(House house) {
        String query = "UPDATE house SET area = ?, country = ?, city = ?, street = ?, number = ? WHERE uuid = ?";
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, house.getArea());
            ps.setString(2, house.getCountry());
            ps.setString(3, house.getCity());
            ps.setString(4, house.getStreet());
            ps.setInt(5, house.getNumber());
            ps.setString(6, String.valueOf(house.getUuid()));
            return house;
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
    }

    @Override
    public void deleteByUUID(UUID uuid) {
        String query = "DELETE FROM house WHERE uuid = ?";
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
