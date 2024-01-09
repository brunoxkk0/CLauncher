module clauncher {

    requires java.logging;

    requires javafx.base;
    requires javafx.web;
    requires javafx.fxml;

    requires jdk.xml.dom;
    requires jdk.httpserver;
    requires jdk.jsobject;

    requires lombok;

    //requires net.yetihafen.javafx.customcaption;

    requires org.slf4j;

    opens br.dev.brunoxkk0.clauncher.core;
    opens br.dev.brunoxkk0.clauncher.core.controller;

    exports br.dev.brunoxkk0.clauncher;
}