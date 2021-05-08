package org.xjtujavacourse.server;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.xjtujavacourse.common.Base64Serializer;
import org.xjtujavacourse.common.JaWaDocument;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ServerMain {
    private final HttpServer server;
    private final HashMap<String, JaWaDocument> docsHead;
    private final HashMap<String, JaWaDocument> docs;
    private static void sendResponse(HttpExchange exchange, String resp, int returnCode) throws IOException {
        byte[] respContents = resp.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(returnCode, respContents.length);
        exchange.getResponseBody().write(respContents);
        exchange.close();
    }

    void start() {
        server.start();
    }

    ServerMain(int port) {
        HttpServer serv = null;
        try {
            serv = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        server = serv;

        docsHead = new HashMap<String, JaWaDocument>();
        docs = new HashMap<String, JaWaDocument>();

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String banner = "JaWa Editor Server\n" +
                        "Endpoints\n" +
                        "  /upload\n" +
                        "  /download";
                sendResponse(exchange, banner, 200);
            }
        });

        server.createContext("/upload", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                InputStream is = exchange.getRequestBody();
                int len = is.available();
                byte[] bs = new byte[len];
                is.read(bs);
                System.out.println("Good Here." + len);
                JaWaDocument doc = null;
                try {
                    doc = (JaWaDocument) Base64Serializer.convertFromBytes(bs);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                    sendResponse(exchange, "Error: cannot deserialize JaWaDocument", 500);
                    return;
                }

                System.out.println("Good Here.");

                synchronized (docsHead) {
                    if (docsHead.containsKey(doc.name)) {
                        if (doc.prevVersion.equals(docsHead.get(doc.name).versionHash())) {
                            docsHead.put(doc.name, doc);
                        } else if (docs.containsKey(doc.prevVersion)) {
                            sendResponse(exchange, "Error: outdated document", 500);
                            return;
                        } else {
                            sendResponse(exchange, "Error: unknown history", 500);
                            return;
                        }
                    } else {
                        docsHead.put(doc.name, doc);
                    }

                    docs.put(doc.versionHash(), doc);
                    sendResponse(exchange, "OK: document accepted", 200);
                }
            }
        });

        server.createContext("/download", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String name = exchange.getRequestURI().getQuery();

                if (name == null || name.length() == 0) {
                    name = "";
                }

                if (docsHead.containsKey(name)) {
                    Serializable s = docsHead.get(name);
                    System.out.println("Check here 0");
                    byte[] bs = Base64Serializer.convertToBytes(s);
                    exchange.sendResponseHeaders(200, bs.length);
                    exchange.getResponseBody().write(bs);
                    exchange.close();
                } else {
                    sendResponse(exchange, "Error: file name not found", 404);
                }
            }
        });
    }

    public static void main(String[] args) {
        ServerMain s = new ServerMain(8848);
        s.start();
        System.out.println("Server listen on 8848");
    }
}
