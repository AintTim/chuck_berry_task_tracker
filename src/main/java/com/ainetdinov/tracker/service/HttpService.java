package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.constant.WebConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class HttpService {

    public String extractLastPathPart(HttpServletRequest request) {
        String path = request.getPathInfo();
        int lastSlashIndex = path.lastIndexOf(WebConstant.SLASH);
        log.debug("Extracting last part from path {}", path);
        return path.substring(lastSlashIndex + 1);
    }

    public String extractPath(HttpServletRequest request) {
        String id = request.getPathInfo().substring(1);
        int slashIndex = id.indexOf(WebConstant.SLASH);
        log.debug("Extracting info from path {}", id);
        return slashIndex == -1 ? id : id.substring(0, slashIndex);
    }

    public <T> T getObjectFromRequest(ObjectMapper mapper, HttpServletRequest request, Class<T> clazz) {
        try {
            return mapper.readValue(request.getInputStream(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getObjectFromRequestPath(ObjectMapper mapper, HttpServletRequest request, Class<T> clazz) {
        return mapper.convertValue(Long.parseLong(extractPath(request)), clazz);
    }

    public void sendJsonObject(ObjectMapper mapper, HttpServletResponse resp, Object object, int status) {
        try {
            resp.setStatus(status);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            String json = mapper.writeValueAsString(object);
            out.print(json);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
