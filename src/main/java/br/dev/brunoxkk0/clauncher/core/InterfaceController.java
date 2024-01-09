package br.dev.brunoxkk0.clauncher.core;

import br.dev.brunoxkk0.clauncher.CLauncher;
import br.dev.brunoxkk0.clauncher.core.controller.LauncherBridgeController;
import br.dev.brunoxkk0.clauncher.core.web.MinimalHttpServer;
import com.sun.net.httpserver.HttpServer;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.io.IOException;

public record InterfaceController(Scene scene, WebView webView, WebEngine webEngine,  Integer uiPort, String uiDir) {

    private static final LauncherBridgeController bindObject = new LauncherBridgeController();

    private static final String BINDING_KEY = "clauncher";

    private static final String CONSOLE_INJECT = ("""
                    window.console = {
                        log: (message) => %binding.log(message),
                        error: (message) => %binding.error(message)
                    }
            """
    );

    public void preInit() throws IOException {

        CLauncher.getLogger().info("Trying to link the controller bridge.");

        webEngine.setJavaScriptEnabled(true);

        webEngine.getLoadWorker().stateProperty().addListener((observableValue, oldState, newState) -> {

            if (newState == Worker.State.SUCCEEDED) {

                JSObject window = (JSObject) webEngine.executeScript("window");

                window.setMember(BINDING_KEY, bindObject);

                webEngine.executeScript(CONSOLE_INJECT.replaceAll("%binding", BINDING_KEY));

                bound();
            }

        });

        CLauncher.getLogger().info("Loading index page.");

        HttpServer httpServer = MinimalHttpServer.createServer(uiPort, uiDir);
        httpServer.start();

        webEngine.load("http://" + httpServer.getAddress());
    }

    public void bound() {
        CLauncher.getLogger().info("Bridge linked to the current page.");
        webEngine.executeScript(BINDING_KEY + ".load();");
    }

}
