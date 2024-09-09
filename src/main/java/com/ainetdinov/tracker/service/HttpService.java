package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.constant.WebConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2
public class HttpService {
    private final String EMPTY_STRING = "";

    public void prepareResponse(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
    }

    public boolean containsPath(HttpServletRequest request) {
        String path = request.getPathInfo();
        log.debug("Checking path presence: {}", path);
        return Objects.nonNull(path) && !path.replace(WebConstant.SLASH, EMPTY_STRING).isEmpty();
    }

    public long extractId(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(1));
    }
}
