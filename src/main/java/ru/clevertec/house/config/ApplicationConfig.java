package ru.clevertec.house.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.house.converter.HouseConverter;
import ru.clevertec.house.converter.HouseConverterImpl;
import ru.clevertec.house.converter.PersonConverter;
import ru.clevertec.house.converter.PersonConverterImpl;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.repository.impl.HouseRepositoryImpl;
import ru.clevertec.house.repository.impl.PersonRepositoryImpl;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.service.OwnerService;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.service.impl.HouseServiceImpl;
import ru.clevertec.house.service.impl.OwnerServiceImpl;
import ru.clevertec.house.service.impl.PersonServiceImpl;

@Configuration
public class ApplicationConfig {

    @Bean
    public HouseRepository houseRepository() {
        return new HouseRepositoryImpl();
    }

    @Bean
    public HouseConverter houseConverter() {
        return new HouseConverterImpl();
    }

    @Bean
    public HouseService houseService() {
        return new HouseServiceImpl(houseRepository());
    }

    @Bean
    public PersonRepository personRepository() {
        return new PersonRepositoryImpl();
    }

    @Bean
    public PersonConverter personConverter() {
        return new PersonConverterImpl();
    }

    @Bean
    public PersonService personService() {
        return new PersonServiceImpl(personRepository());
    }

    @Bean
    public OwnerService ownerService() {
        return new OwnerServiceImpl(personService(), houseService(), personConverter(), houseConverter());
    }
}
