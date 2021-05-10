package com.gotta_watch_them_all.app.work.infrastructure.util;

import com.gotta_watch_them_all.app.work.core.exception.BadHttpRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class ApiRequester {

    private final HttpClient httpClient;

    public String request(HttpRequest request) throws BadHttpRequestException {
        if (request == null || request.uri() == null) {
            throw new BadHttpRequestException("URI of http request is null");
        }
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response == null) return null;
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new BadHttpRequestException(String.format("Bad http request with uri : %s", request.uri().toString()));
        }
    }
}
