package org.otus.education.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.sun.istack.NotNull;
import org.otus.education.data.core.model.User;
import org.otus.education.data.core.service.DBServiceUser;
import org.otus.education.hw12.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersApiServlet extends HttpServlet {


    private static final Logger logger = LoggerFactory.getLogger(UsersApiServlet.class);

    private final DBServiceUser dbServiceUser;
    private final Gson gson;


    public UsersApiServlet(DBServiceUser dbServiceUser, Gson gson) {
        if (dbServiceUser == null) {
            throw new IllegalArgumentException("dbServiceUser mustn't be null.");
        }
        if (gson == null) {
            throw new IllegalArgumentException("gson mustn't be null.");
        }

        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null || "/".equals(req.getPathInfo())) {
            resp.setContentType("application/json;charset=UTF-8");
            gson.toJson(dbServiceUser.getUsers(), resp.getWriter());
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getPathInfo() == null || "/".equals(req.getPathInfo())) {
                dbServiceUser.saveUser(gson.fromJson(req.getReader(), User.class));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
