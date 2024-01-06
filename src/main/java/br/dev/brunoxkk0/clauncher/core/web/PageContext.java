package br.dev.brunoxkk0.clauncher.core.web;

import org.thymeleaf.context.Context;

import java.util.Date;

public class PageContext {

    public static Context getGlobalContext() {
        Context context = new Context();
        context.setVariable("date", new Date().toString());
        context.setVariable("timestamp", System.currentTimeMillis());
        context.setVariable("template", "Template ativo");
        return context;
    }

}
