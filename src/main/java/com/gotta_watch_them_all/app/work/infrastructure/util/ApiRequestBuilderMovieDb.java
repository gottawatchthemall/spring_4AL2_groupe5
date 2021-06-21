package com.gotta_watch_them_all.app.work.infrastructure.util;

import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;
import com.gotta_watch_them_all.app.work.core.exception.TooManySearchArgumentsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

@Service
public class ApiRequestBuilderMovieDb implements ApiRequestBuilder {

    private URI uri;
    private String titleToSearch;
    private String imdbIdToSearch;

    @Value("${moviedb.api.key}")
    private String apiKey;

    @Value("${moviedb.api.host}")
    private String apiHost;

    @Override
    public HttpRequest build() {
        setUri();
        return HttpRequest
                .newBuilder()
                .uri(uri)
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .build();
    }

    @Override
    public ApiRequestBuilderMovieDb setUri() {
        checkSearchExceptions();

        String url = String.format("https://%s", apiHost);
        if (titleToSearch != null) {
            url = url.concat(String.format("/?s=%s", URLEncoder.encode(titleToSearch, StandardCharsets.UTF_8)));
        } else {
            url = url.concat(String.format("/?i=%s", URLEncoder.encode(imdbIdToSearch, StandardCharsets.UTF_8)));
        }

        uri = URI.create(url.concat("&r=json"));
        return this;
    }

    private void checkSearchExceptions() {
        if (titleToSearch == null && imdbIdToSearch == null) {
            throw new AnySearchValueFoundException("A searching criteria must be defined");
        }
        if (titleToSearch != null && imdbIdToSearch != null) {
            throw new TooManySearchArgumentsException("A title to search and an id to search must not be both defined at the same time");
        }
    }

    @Override
    public ApiRequestBuilderMovieDb setTitleToSearch(String title) {
        if (title == null) throw new IllegalTitleGivenException("Title to search can not be null");
        if (title.isBlank()) throw new IllegalTitleGivenException("Title to search can not be empty");
        titleToSearch = title;
        return this;
    }

    @Override
    public ApiRequestBuilder setWorkIdToSearch(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalImdbIdGivenException("ImdbId can not be null or blank");
        }
        imdbIdToSearch = id;
        return this;
    }
}
