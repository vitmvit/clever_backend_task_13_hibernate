package ru.clevertec.house.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.clevertec.house.config.ApplicationConfig;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
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

@Component
@WebServlet
public class HouseController extends HttpServlet {

    private final OwnerService service;
    private final AnnotationConfigApplicationContext context;

    public HouseController() {
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
                HouseDto object = service.getHouseByUUID(UUID.fromString(uuidParam));
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                response.getOutputStream().println(objectMapper.writeValueAsString(object));
                response.setStatus(200);
            } catch (Exception e) {
                response.getOutputStream().println(HOUSE_NOT_FOUND_ERROR);
                response.setStatus(404);
            }
            return;
        }

        // Обработка запроса по параметрам "page" и "count"
        if (pageParam != null && countParam != null) {
            try {
                List<HouseDto> objects = service.getHousePage(Integer.parseInt(pageParam), Integer.parseInt(countParam));

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

        // Обработка запроса по параметру "page"
        if (pageParam != null) {
            try {
                List<HouseDto> objects = service.getHousePage(Integer.parseInt(pageParam), PAGE_SIZE);
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
                List<HouseDto> objects = service.getHousePage(PAGE_NUMBER, Integer.parseInt(countParam));
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
            HouseCreateDto houseCreateDto = objectMapper.readValue(jb.toString(), HouseCreateDto.class);
            HouseDto houseDto = service.createHouse(houseCreateDto);

            response.getOutputStream().println(objectMapper.writeValueAsString(houseDto));
            response.setStatus(200);
        } catch (Exception e) {
            response.getOutputStream().println(HOUSE_CREATED_ERROR);
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
            HouseDto houseDto = service.updateHouse(objectMapper.readValue(jb.toString(), HouseUpdateDto.class));

            response.getOutputStream().println(objectMapper.writeValueAsString(houseDto));
            response.setStatus(200);
        } catch (Exception e) {
            response.getOutputStream().println(HOUSE_UPDATED_ERROR);
            response.setStatus(500);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            service.deleteHouse(UUID.fromString(request.getParameter("uuid")));
            response.getOutputStream().println(HOUSE_IS_DELETED_MESSAGE);
            response.setStatus(200);
        } catch (Exception e) {
            response.getOutputStream().println(HOUSE_DELETED_ERROR);
            response.setStatus(500);
        }
    }
}
