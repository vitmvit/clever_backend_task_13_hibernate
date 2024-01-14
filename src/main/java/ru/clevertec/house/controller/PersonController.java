package ru.clevertec.house.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.house.config.ApplicationConfig;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;
import ru.clevertec.house.service.OwnerService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static ru.clevertec.house.constant.Constant.*;

@Configuration
@WebServlet
public class PersonController extends HttpServlet {

    private final OwnerService service;
    private final AnnotationConfigApplicationContext context;

    public PersonController() {
        this.context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        service = context.getBean(OwnerService.class);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uuidParam = request.getParameter("uuid");
        String pageParam = request.getParameter("page");
        String countParam = request.getParameter("count");

        // Обработка запроса по параметру "uuid"
        if (uuidParam != null) {
            try {
                PersonDto object = service.getPersonByUUID(UUID.fromString(uuidParam));
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                response.getOutputStream().println(objectMapper.writeValueAsString(object));
                response.setStatus(200);
            } catch (Exception e) {
                response.getOutputStream().println(PERSON_NOT_FOUND_ERROR);
                response.setStatus(404);
            }
            return;
        }

        // Обработка запроса по параметрам "page" и "count"
        if (pageParam != null && countParam != null) {
            try {
                List<PersonDto> objects = service.getPersonPage(Integer.parseInt(pageParam), Integer.parseInt(countParam));

                if (!objects.isEmpty()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    response.getOutputStream().println(objectMapper.writeValueAsString(objects));
                } else {
                    response.getOutputStream().println(NO_SUCH_DATA_MESSAGE);
                }
                response.setStatus(200);
            } catch (Exception e) {
                response.getOutputStream().println(PERSON_RETRIEVING_ERROR);
                response.setStatus(500);
            }
            return;
        }

        // Обработка запроса по параметру "page"
        if (pageParam != null) {
            try {
                List<PersonDto> objects = service.getPersonPage(Integer.parseInt(pageParam), PAGE_SIZE);
                if (!objects.isEmpty()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    response.getOutputStream().println(objectMapper.writeValueAsString(objects));
                } else {
                    response.getOutputStream().println(NO_SUCH_DATA_MESSAGE);
                }
                response.setStatus(200);
            } catch (Exception e) {
                response.getOutputStream().println(HOUSE_RETRIEVING_ERROR);
                response.setStatus(500);
            }
            return;
        }

        // Обработка запроса по параметру "count"
        if (countParam != null) {
            try {
                List<PersonDto> objects = service.getPersonPage(PAGE_NUMBER, Integer.parseInt(countParam));
                if (!objects.isEmpty()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    response.getOutputStream().println(objectMapper.writeValueAsString(objects));
                } else {
                    response.getOutputStream().println(NO_SUCH_DATA_MESSAGE);
                }
                response.setStatus(200);
            } catch (Exception e) {
                response.getOutputStream().println(HOUSE_RETRIEVING_ERROR);
                response.setStatus(500);
            }
            return;
        }

        response.getOutputStream().println(INVALID_REQUEST_PARAMETERS_ERROR);
        response.setStatus(400);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            StringBuilder jb = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            PersonCreateDto personCreateDto = objectMapper.readValue(jb.toString(), PersonCreateDto.class);
            PersonDto personDto = service.createPerson(personCreateDto);

            response.getOutputStream().println(objectMapper.writeValueAsString(personDto));
            response.setStatus(200);
        } catch (Exception e) {
            response.getOutputStream().println(PERSON_CREATED_ERROR);
            response.setStatus(500);
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            StringBuilder jb = new StringBuilder();
            String line = null;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            PersonDto personDto = service.updatePerson(objectMapper.readValue(jb.toString(), PersonUpdateDto.class));

            response.getOutputStream().println(objectMapper.writeValueAsString(personDto));
            response.setStatus(200);
        } catch (Exception e) {
            response.getOutputStream().println(PERSON_UPDATED_ERROR);
            response.setStatus(500);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            service.deletePerson(UUID.fromString(request.getParameter("uuid")));
            response.getOutputStream().println(PERSON_IS_DELETED_MESSAGE);
            response.setStatus(200);
        } catch (Exception e) {
            response.getOutputStream().println(PERSON_DELETED_ERROR);
            response.setStatus(500);
        }
    }
}
