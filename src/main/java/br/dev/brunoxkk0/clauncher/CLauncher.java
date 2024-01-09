package br.dev.brunoxkk0.clauncher;

import br.dev.brunoxkk0.clauncher.core.InterfaceController;
import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CLauncher extends Application {

    @Getter
    private static final Logger logger = LoggerFactory.getLogger("CLauncher");

    @Getter
    private static InterfaceController interfaceController;

    @Getter
    private static Stage stage;

    @Getter
    private static Integer uiPort;

    @Getter
    private static String uiDir;

    @Override
    public void start(Stage stage) throws IOException {

        CLauncher.stage = stage;

        uiPort = 64321;
        uiDir = "/dist";

        stage.setTitle("Hello!");
        stage.setScene(createScene(540, 320));
        stage.show();

    }

    public Scene createScene(int width, int height) throws IOException {

        WebView webView = new WebView();
        webView.setId("webView");

        webView.setPageFill(Color.TRANSPARENT);
        webView.setContextMenuEnabled(false);

        Scene scene = new Scene(webView, width, height);
        scene.setFill(Color.TRANSPARENT);

        interfaceController = new InterfaceController(scene, webView, webView.getEngine(), uiPort, uiDir);
        interfaceController.preInit();

        return scene;

    }

    public static void main(String[] args) {
        launch();
    }

}
