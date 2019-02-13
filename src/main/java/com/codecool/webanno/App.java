package com.codecool.webanno;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class App {


    public static void main(String[] args) throws Exception {
        WebController webController = new WebController();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/",  new Root(webController));
        server.setExecutor(null);
        server.start();
    }



}