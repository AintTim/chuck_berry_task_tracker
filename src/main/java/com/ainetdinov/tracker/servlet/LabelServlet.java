package com.ainetdinov.tracker.servlet;

import com.ainetdinov.tracker.model.request.LabelRequest;
import com.ainetdinov.tracker.service.HttpService;
import com.ainetdinov.tracker.service.LabelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static com.ainetdinov.tracker.constant.WebConstant.*;

@WebServlet(API + LABELS + SLASH + ASTERISK)
public class LabelServlet extends HttpServlet {
    private LabelService labelService;
    private HttpService httpService;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        labelService = (LabelService) context.getAttribute(LABEL_SERVICE);
        httpService = (HttpService) context.getAttribute(HTTP_SERVICE);
        mapper = (ObjectMapper) context.getAttribute(MAPPER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute(LABELS, labelService.getEntities());
        req.getRequestDispatcher(LABELS_JSP).forward(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var label = httpService.getObjectFromRequestPath(mapper, req, LabelRequest.class);
        if (labelService.validateLabelDeletionAvailability(label)) {
            httpService.sendJsonObject(mapper, resp, labelService.getMessage(ERROR_LABEL_VALIDATION), HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }
        labelService.deleteEntity(label);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        var label = httpService.getObjectFromRequest(mapper, req, LabelRequest.class);
        labelService.updateEntity(label);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        var label = httpService.getObjectFromRequest(mapper, req, LabelRequest.class);
        if (Objects.nonNull(labelService.getLabelByName(label))) {
            httpService.sendJsonObject(mapper, resp, labelService.getMessage(ERROR_LABEL_NAME_EXISTS), HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }
        labelService.createEntity(label);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
