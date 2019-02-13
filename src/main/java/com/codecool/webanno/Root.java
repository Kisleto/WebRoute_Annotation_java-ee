package com.codecool.webanno;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import java.util.stream.Collectors;

public class Root implements HttpHandler {
    private WebController webController;

    public Root(WebController webController) {
        this.webController = webController;
    }



    public void handle(HttpExchange httpExchange) {


        String path = httpExchange.getRequestURI().getPath();
        String firstSegment = getFirstSegmentOfURI(path);

        Class<WebController> aClass = WebController.class;

        for (Method method : aClass.getDeclaredMethods()){
            if (method.isAnnotationPresent(WebRoute.class)){
                Annotation annotation = method.getAnnotation(WebRoute.class);
                WebRoute webRoute = (WebRoute) annotation;
                if(webRoute.path().equals(firstSegment)){
                    try {
                        method.invoke(webController, httpExchange);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                }
            }
    }

    private String getFirstSegmentOfURI(String path) {
        String[] segments = path.split("/");
        return segments.length == 0 ? "/" : "/" + segments[1];
    }
}
