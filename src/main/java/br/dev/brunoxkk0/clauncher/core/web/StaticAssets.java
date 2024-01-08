package br.dev.brunoxkk0.clauncher.core.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StaticAssets {

    APP("/index.html", "/dist/index.html"),
    JS("/assets/index-d56ece4e.js", "/dist/assets/index-d56ece4e.js"),
    CSS("/assets/index-bac55a4c.css", "/dist/assets/index-bac55a4c.css");

//    APP("/app.html", "/ui/views/app.html"),
//    RESET("/assets/reset.css", "/ui/assets/reset.css");

    private final String path;
    private final String resource;

}
