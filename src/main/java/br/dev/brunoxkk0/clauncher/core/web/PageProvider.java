package br.dev.brunoxkk0.clauncher.core.web;

import br.dev.brunoxkk0.clauncher.CLauncher;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.FileRequest;
import com.google.common.jimfs.Jimfs;
import com.google.common.jimfs.JimfsFileSystem;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class PageProvider {

    private static final JimfsFileSystem FILE_SYSTEM;
    private static final Path UI_FOLDER;

    private static final HashMap<String, Path> STATIC_CONTENT = new HashMap<>();

    private static final TemplateEngine TEMPLATE_ENGINE = new TemplateEngine();
    private static final StringTemplateResolver TEMPLATE_RESOLVER = new StringTemplateResolver();

    private static final Context PAGE_PROVIDER_CONTEXT = new Context();

    public static FileRequest getFileRequestListener() {
        return (path, set, fileAttributes) -> {
            if (path.toString().equals("/testepage.html") && set.isEmpty()) {
                try {
                    Files.writeString(path, processStaticTemplate("""
                            <!DOCTYPE html>
                            <html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.w3.org/1999/xhtml">
                                                        
                            <head>
                                <meta charset="UTF-8">
                                <title>Title</title>
                                <style>
                                    :root{
                                        background-color: purple;
                                    }
                                </style>
                            </head>
                                                        
                            <body>
                                <h1>NOT FOUND</h1>
                                <p th:text='${timestamp}'>Não processado pela engine</p>
                                <p th:text='${template}'>false</p>
                                <br>
                                <a href="/ui_gen/app.html">Voltar</a>
                            </body>
                                                
                            </html>
                            """), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    System.out.println("Error ao criar um arquivo padrão para " + path);
                }
            }
        };
    }

    static {

        TEMPLATE_ENGINE.addTemplateResolver(TEMPLATE_RESOLVER);

        FILE_SYSTEM = (JimfsFileSystem) Jimfs.newFileSystem(Configuration.unix());
        FILE_SYSTEM.getDefaultView().appendRequestListener(getFileRequestListener());

        UI_FOLDER = FILE_SYSTEM.getPath("/ui_gen");

        PAGE_PROVIDER_CONTEXT.setVariable("timestamp",  System.currentTimeMillis());
        PAGE_PROVIDER_CONTEXT.setVariable("template",   "Template ativo");

        try {
            loadStaticContent(UI_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadStaticContent(Path folder) throws IOException {

        for (StaticAssets staticAssets : StaticAssets.values()) {

            if (STATIC_CONTENT.containsKey(staticAssets.getResource()))
                return;

            String text = readInputStream(PageProvider.class.getResourceAsStream(staticAssets.getResource()));
            String finalText = processStaticTemplate(text);

            Files.createDirectories(folder);

            Path file = folder.resolve(staticAssets.getPath());

            if (file.getParent() != null)
                Files.createDirectories(file.getParent());

            Files.createFile(file);

            Path path = Files.writeString(folder.resolve(staticAssets.getPath()), finalText, StandardCharsets.UTF_8);

            STATIC_CONTENT.put(staticAssets.getPath(), path);
        }
    }

    private static String readInputStream(InputStream inputStream) {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            StringBuilder builder = new StringBuilder();

            String text;

            while ((text = bufferedReader.readLine()) != null) {
                builder.append(text);
                builder.append("\n");
            }

            return builder.toString();

        } catch (IOException e) {
            return "error";
        }

    }

    private static String processStaticTemplate(String source) {
        return TEMPLATE_ENGINE.process(source, PAGE_PROVIDER_CONTEXT);
    }

    public static String getPage(String page) {

        long start = System.currentTimeMillis();

        Path targetFile = UI_FOLDER.resolve(page);

        CLauncher.getLogger().info("Page write in " + (System.currentTimeMillis() - start) + "MS");

        return targetFile.toUri().toString();
    }

}
