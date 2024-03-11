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
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.repository.PersonRepository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
@AllArgsConstructor
public class PersonRepositoryImpl implements PersonRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Person getByUuid(UUID uuid) {
        TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM person p WHERE p.uuid = :uuid", Person.class);
        query.setParameter("uuid", uuid);
        return query.getSingleResult();
    }

    @Override
    public List<Person> getAll(int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        Root<Person> rootEntry = criteriaQuery.from(Person.class);
        CriteriaQuery<Person> all = criteriaQuery.select(rootEntry);
        return entityManager.createQuery(all)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public List<Person> getBySurnameContaining(String surname) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        Root<Person> rootEntry = criteriaQuery.from(Person.class);

        criteriaQuery.select(rootEntry)
                .where(criteriaBuilder.like(rootEntry.get("surname"), "%" + surname + "%"));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Person create(Person person) {
        entityManager.persist(person);
        return person;
    }

    @Override
    public Person update(Person person) {
        return entityManager.merge(person);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        jdbcTemplate.update("DELETE FROM person WHERE uuid = ?", uuid);
    }
}
