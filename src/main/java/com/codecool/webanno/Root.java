package com.codecool.webanno;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Root implements HttpHandler {
    private WebController webController;

    public Root(WebController webController) {
        this.webController = webController;
    }

    HashMap<Method, String> roots = new HashMap<>();

    public String getRoutes(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String firstSegment = getFirstSegmentOfURI(path);

        Class<WebController> aClass = WebController.class;

        for (Method method : aClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                WebRoute webRoute = method.getAnnotation(WebRoute.class);
                String paths = webRoute.path();
                if (webRoute.path().equals(firstSegment)) {
                    try {
                        method.invoke(webController, httpExchange);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                return roots.put(method, paths);
            }
        }
        return null;
    }




    public void handle(HttpExchange httpExchange) {

        getRoutes(httpExchange);
    }

    private String getFirstSegmentOfURI(String path) {
        String[] segments = path.split("/");
        return segments.length == 0 ? "/" : "/" + segments[1];
    }
}
