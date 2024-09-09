package com.ainetdinov.tracker.servlet;

import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.service.HttpService;
import com.ainetdinov.tracker.service.LabelService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static com.ainetdinov.tracker.constant.WebConstant.*;

@WebServlet(API + LABELS + SLASH + ASTERISK)
public class LabelServlet extends HttpServlet {
    private LabelService labelService;
    private HttpService httpService;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        labelService = (LabelService) context.getAttribute(LABEL_SERVICE);
        httpService = (HttpService) context.getAttribute(HTTP_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        httpService.prepareResponse(resp);

        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Labels: </h1>");
        if (httpService.containsPath(req)) {
            out.println("<h2>" + labelService.getLabel(httpService.extractId(req)) + "</h2>");
        } else {
            out.println("<h2>" + labelService.getLabels() + "</h2>");
        }
        out.println("</body></html>");


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        httpService.prepareResponse(resp);
        Label newLabel = new Label();
        newLabel.setLabel("Custom");
        labelService.createEntity(newLabel);
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>"+newLabel+" created </h1>");
        out.println("</body></html>");
    }
}
