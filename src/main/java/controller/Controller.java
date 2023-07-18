package controller;

import annotation.RequestMapping;
import converter.ModelConverter;
import db.Database;
import http.HttpResponse;
import model.User;
import util.HttpUtils;

import java.util.Map;

public class Controller {

    private Controller() {
    }

    private static class SingletonHelper {
        private static final Controller INSTANCE = new Controller();
    }

    public static Controller getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @RequestMapping(path = "/user/create", method = HttpUtils.Method.POST)
    public HttpResponse creatUser(Map<String, String> parameters) {
        User newUser = ModelConverter.toUser(parameters);
        Database.findUserById(newUser.getUserId()).ifPresent(user -> {
            throw new IllegalArgumentException("중복된 userId입니다.");
        });
        Database.addUser(newUser);
        return HttpResponse.redirect("/index.html");
    }
}
