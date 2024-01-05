package br.dev.brunoxkk0.clauncher.core;

import br.dev.brunoxkk0.clauncher.CLauncher;
import br.dev.brunoxkk0.clauncher.core.web.PageProvider;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.io.IOException;

public record InterfaceController(Scene scene, WebView webView, WebEngine webEngine) {

    private static final LauncherBridgeController bindObject = new LauncherBridgeController();

    public void onPreInit() throws IOException {

        CLauncher.getLogger().info("Trying to link the controller bridge.");

        webEngine.setJavaScriptEnabled(true);

        webEngine.getLoadWorker().stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("clauncher", bindObject);
                afterBind();
            }
        });

        CLauncher.getLogger().info("Loading index page.");
        webEngine.load(PageProvider.getPage("app.html"));
    }

    public void afterBind() {
        CLauncher.getLogger().info("LauncherBridgeController linked to page.");
        webEngine.executeScript("clauncher.onLoad();");
    }

}
