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

    @RequestMapping(path = "/index.html", method = HttpUtils.Method.GET)
    public HttpResponse index() {
        return HttpResponse.ok("/index.html");
    }

    @RequestMapping(path = "/user/form.html", method = HttpUtils.Method.GET)
    public HttpResponse userForm() {
        return HttpResponse.ok("/user/form.html");
    }

    @RequestMapping(path = "/error.html", method = HttpUtils.Method.GET)
    public HttpResponse error() {
        return HttpResponse.ok("/error.html");
    }

    @RequestMapping(path = "/user/create", method = HttpUtils.Method.GET)
    public HttpResponse creatUser(Map<String, String> parameters) {
        User newUser = ModelConverter.toUser(parameters);
        User findUser = Database.findUserById(newUser.getUserId());
        if (findUser != null) {
            return HttpResponse.redirect("/error.html");
        }
        Database.addUser(newUser);
        return HttpResponse.redirect("/index.html");
    }
}
