package br.dev.brunoxkk0.clauncher.core.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StaticAssets {

    APP("/app.html", "/ui/views/app.html"),
    RESET("/assets/reset.css", "/ui/assets/reset.css");

    private final String path;
    private final String resource;

}
