package br.dev.brunoxkk0.clauncher.core.controller;

import br.dev.brunoxkk0.clauncher.CLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LauncherBridgeController implements Bridge {

    private static final Logger logger = LoggerFactory.getLogger("Bridge");

    public void load() {
        CLauncher.getLogger().info("Page loaded");
        if (!CLauncher.getStage().isShowing())
            CLauncher.getStage().show();
    }

    public void log(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.error(message);
    }

}
