package org.xjtujavacourse.server;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ServerMain {
    private static void sendResponse(HttpExchange exchange, String resp) throws IOException {
        byte[] respContents = resp.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(200, respContents.length);
        exchange.getResponseBody().write(respContents);
        exchange.close();
    }

    public static void main(String[] args) {
        HttpServer httpServer = null;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8848), 0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String banner = "JaWa Editor Server\n" +
                                "Endpoints\n" +
                                "  /upload\n" +
                                "  /download";
                sendResponse(exchange, banner);
            }
        });

        httpServer.createContext("/upload", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                sendResponse(exchange, "Upload");
            }
        });

        httpServer.createContext("/download", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                sendResponse(exchange, "Download");
            }
        });

        httpServer.start();
        System.out.println("Server listen on 8848");
    }
}
