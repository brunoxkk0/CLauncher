package br.dev.brunoxkk0.clauncher.core.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RequiredArgsConstructor
public class MinimalHttpServer implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger("MinimalHttpServer");

    private final String basePath;

    public static HttpServer createServer(int port, String basePath) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/", new MinimalHttpServer(basePath));
        return httpServer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            logger.warn("{} -> Not Allowed", exchange.getRequestMethod());

            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");

            String response = "<h2>Method Not Allowed</h2>";
            exchange.sendResponseHeaders(404, response.length());

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            return;

        }

        String path = exchange.getRequestURI().getPath();

        if (path.equalsIgnoreCase("/"))
            path = "/index.html";

        logger.info("Requested -> {}", path);

        try (InputStream inputStream = MinimalHttpServer.class.getResourceAsStream(basePath + path)) {

            if (inputStream == null) {

                exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");

                String response = "<h2>Not Found</h2>";
                exchange.sendResponseHeaders(404, response.length());

                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes(StandardCharsets.UTF_8));
                outputStream.close();

                return;

            }

            String finalPath = path;
            Arrays.stream(MIMETypes.values()).filter(el ->
                    finalPath.endsWith(el.getExtension())
            ).findFirst().ifPresent(mimeTypes ->
                    exchange.getResponseHeaders().add("Content-Type", mimeTypes.getMime() + (mimeTypes.isCharsetAvailable() ? "; charset=utf-8" : ""))
            );

            byte[] cache = new byte[4096];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int read;
            while ((read = inputStream.read(cache)) != -1)
                byteArrayOutputStream.write(cache, 0, read);

            int length = byteArrayOutputStream.size();

            exchange.sendResponseHeaders(200, length);

            OutputStream outputStream = exchange.getResponseBody();

            byteArrayOutputStream.writeTo(outputStream);

            byteArrayOutputStream.close();
            outputStream.close();

        }

    }
}
