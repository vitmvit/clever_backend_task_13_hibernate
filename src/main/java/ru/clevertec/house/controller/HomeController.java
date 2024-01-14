package ru.clevertec.house.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.clevertec.house.config.ApplicationConfig;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.service.OwnerService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static ru.clevertec.house.constant.Constant.HOUSE_NOT_FOUND_ERROR;
import static ru.clevertec.house.constant.Constant.NO_SUCH_DATA_MESSAGE;

@Component
@WebServlet
public class HomeController extends HttpServlet {

    private final OwnerService service;
    private final AnnotationConfigApplicationContext context;

    public HomeController() {
        this.context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        service = context.getBean(OwnerService.class);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uuidParam = request.getParameter("uuid");
        if (uuidParam != null) {
            try {
                List<PersonDto> objects = service.getAllPersons(UUID.fromString(uuidParam));

                if (!objects.isEmpty()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    response.getOutputStream().println(objectMapper.writeValueAsString(objects));
                } else {
                    response.getOutputStream().println(NO_SUCH_DATA_MESSAGE);
                }
                response.setStatus(200);
            } catch (Exception e) {
                response.getOutputStream().println(HOUSE_NOT_FOUND_ERROR);
                response.setStatus(404);
            }
        }
    }
}
