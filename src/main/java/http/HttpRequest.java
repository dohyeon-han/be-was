package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private final HttpUtils.Method method;
    private final URI uri;
    private final HttpClient.Version version;
    private final Mime mime;
    private final HttpHeader httpHeader;
    private final String body;

    public HttpRequest(BufferedReader reader) throws URISyntaxException, IOException {
        String requestLine = reader.readLine();
        String[] requestParts = requestLine.split(" ");
        this.httpHeader = new HttpHeader(reader);
        this.method = HttpUtils.Method.of(requestParts[0]);
        this.uri = HttpUtils.constructUri(this.httpHeader.get("Host"), requestParts[1]);
        this.version = HttpUtils.getHttpVersion(requestParts[2]).orElse(null);
        this.mime = HttpUtils.decideMime(this.uri.getPath());

        int contentLength = Integer.parseInt(this.httpHeader.getOrDefault("Content-Length", "0"));
        this.body = HttpUtils.parseBody(reader, contentLength, this.method);
    }

    public HttpUtils.Method method() {
        return this.method;
    }

    public URI uri() {
        return this.uri;
    }

    public HttpClient.Version version() {
        return this.version;
    }

    public Mime mime() {
        return this.mime;
    }

    public String getHeader(String key) {
        return this.httpHeader.get(key);
    }

    public String getBody() {
        return this.body;
    }

    public void printLogs() {
        StringBuilder requestBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : this.httpHeader.entrySet()) {
            requestBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append(" ");
        }
        logger.debug("Method : {}, URI : {}, Version : {}", this.method, this.uri, this.version);
        logger.debug("Headers : {}", requestBuilder);
        logger.debug("Mime : {}, Body : {}", this.mime, this.body);
    }
}
