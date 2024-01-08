module clauncher {

    requires java.logging;

    requires javafx.base;
    requires javafx.web;
    requires javafx.fxml;

    requires com.google.common.jimfs;

    requires jdk.jsobject;

    requires lombok;

    requires net.yetihafen.javafx.customcaption;

    requires thymeleaf;
    requires org.slf4j;

    requires jdk.xml.dom;

    requires nanohttpd;

    exports br.dev.brunoxkk0.clauncher;
    opens br.dev.brunoxkk0.clauncher.core;
}