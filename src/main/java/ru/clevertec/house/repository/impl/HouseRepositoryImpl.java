package ru.clevertec.house.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.repository.HouseRepository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
@AllArgsConstructor
public class HouseRepositoryImpl implements HouseRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public House getByUuid(UUID uuid) {
        TypedQuery<House> query = entityManager.createQuery("SELECT h FROM house h WHERE h.uuid = :uuid", House.class);
        query.setParameter("uuid", uuid);
        return query.getSingleResult();
    }

    @Override
    public List<House> getAll(int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<House> criteriaQuery = criteriaBuilder.createQuery(House.class);
        Root<House> rootEntry = criteriaQuery.from(House.class);
        CriteriaQuery<House> all = criteriaQuery.select(rootEntry);
        return entityManager.createQuery(all)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public List<House> getByCityContaining(String city) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<House> criteriaQuery = criteriaBuilder.createQuery(House.class);
        Root<House> rootEntry = criteriaQuery.from(House.class);

        criteriaQuery.select(rootEntry)
                .where(criteriaBuilder.like(rootEntry.get("city"), "%" + city + "%"));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public House create(House house) {
        entityManager.persist(house);
        return house;
    }

    @Override
    public House update(House house) {
        return entityManager.merge(house);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        jdbcTemplate.update("DELETE FROM house WHERE uuid = ?", uuid);
    }
}
