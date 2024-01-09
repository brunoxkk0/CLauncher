package br.dev.brunoxkk0.clauncher.core.web.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MIMETypes {

    HTML(   "text/html",                true,   ".html"),
    JS(     "text/javascript",          true,   ".js"),
    XML(    "text/xml",                 true,   ".xml"),
    TXT(    "text/plain",               true,   ".txt"),
    SVG(    "image/svg+xml",            false,  ".svg"),
    ICO(    "image/vnd.microsoft.icon", false,  ".ico"),
    PNG(    "image/png",                false,  ".png"),
    WEBP(   "image/webp",               false,  ".webp");

    private final String mime;
    private final boolean charsetAvailable;
    private final String extension;

}
