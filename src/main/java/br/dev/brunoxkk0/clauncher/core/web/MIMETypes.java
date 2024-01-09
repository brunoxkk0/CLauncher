package br.dev.brunoxkk0.clauncher.core.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MIMETypes {

    HTML    ("text/html",                   ".html",    true ),
    JS      ("text/javascript",             ".js"  ,    true ),
    XML     ("text/xml",                    ".xml" ,    true ),
    TXT     ("text/plain",                  ".txt" ,    true ),
    SVG     ("image/svg+xml",               ".svg"  ,   false),
    ICO     ("image/vnd.microsoft.icon",    ".ico"  ,   false),
    PNG     ("image/png",                   ".png"  ,   false),
    WEBP    ("image/webp",                  ".webp" ,   false);

    private final String mime;
    private final String extension;

    private final boolean charsetAvailable;

}
