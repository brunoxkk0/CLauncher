package br.dev.brunoxkk0.clauncher.core;

import br.dev.brunoxkk0.clauncher.CLauncher;
import br.dev.brunoxkk0.clauncher.core.web.PageProvider;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

public record InterfaceController(Scene scene, WebView webView, WebEngine webEngine) {

    private static final LauncherBridgeController bindObject = new LauncherBridgeController();

    public void onPreInit() throws IOException {

        CLauncher.getLogger().info("Trying to link the controller bridge.");

        webEngine.setJavaScriptEnabled(true);

        webEngine.getLoadWorker().stateProperty().addListener((observableValue, oldState, newState) -> {

            if (newState == Worker.State.SUCCEEDED) {

                JSObject window = (JSObject) webEngine.executeScript("window");

                window.setMember("clauncher", bindObject);

                webEngine.executeScript("window.console = { log: function(msg) { clauncher.log(msg); } }");
                webEngine.executeScript("window.originalConsoleError = window.console.error;" +
                        "window.console.error = function(message) { " +
                        "    clauncher.error(message);" +
                        "    originalConsoleError.apply(this, arguments);" +
                        "};"
                );

                afterBind();
            }

        });

        CLauncher.getLogger().info("Loading index page.");
        new PageProvider();//PageProvider.getPage("index.html"); //PageProvider.getPage("app.html"));
        webEngine.load("http://127.0.0.1:8000/index.html");

    }


    public void reloadPage() {
        if (webEngine != null)
            webEngine.executeScript("window.location.reload();");
    }

    public void afterBind() {
        CLauncher.getLogger().info("LauncherBridgeController linked to page.");
        webEngine.executeScript("clauncher.onLoad();");
    }

    public static String getStringFromDocument(Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.setOutputProperty(OutputKeys.INDENT, "false");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.transform(domSource, result);
        return writer.toString();
    }


}
