package me.ix.noskillv2.utils.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {
    private String content;
    private String customCSS;

    private int delay;

    public WebServer(int port, int delay) throws IOException {
        super(port);
        this.delay = delay;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("web server started on port " + port);
    }

    public String getHTML() {
        try {
            String text = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/me/ix/noskillv2/utils/webserver/resources/index.html"), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (session.getUri().equals("/content")) {
            return serveContent();
        } else {
            String htmlResponse = getHTML().replace("{customCSS}", customCSS).replace("{delay}", this.delay + "");
            return newFixedLengthResponse(Response.Status.OK, "text/html", htmlResponse);
        }
    }

    public Response serveContent() {
        Map<String, String> json = new HashMap<>();
        json.put("content", content);
        String jsonString = new Gson().toJson(json);
        Response response = newFixedLengthResponse(Response.Status.OK, "application/json", jsonString);
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        return response;
    }

    public void setCustomCSS(String customCSS) {
        this.customCSS = customCSS;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
