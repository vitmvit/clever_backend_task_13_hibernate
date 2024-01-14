package ru.clevertec.house.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Класс EncodingFilter является фильтром и предназначен для установки кодировки запроса и ответа на UTF-8
 * и установки типа содержимого ответа на "application/json".
 */
public class EncodingFilter implements Filter {
    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        chain.doFilter(request, response);
    }
}
