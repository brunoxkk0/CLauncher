package br.dev.brunoxkk0.clauncher.core;

import br.dev.brunoxkk0.clauncher.CLauncher;

import java.util.Date;

public final class LauncherBridgeController {

    public void onLoad(){
        CLauncher.getLogger().info("Loaded");
        CLauncher.getStage().show();
    }

    public void hello(String message) {
        System.out.println(message);
    }

    public String getData(){
        return new Date().toString();
    }

}
