package com.gotta_watch_them_all.app.work.infrastructure.util;

import java.net.http.HttpRequest;

public interface ApiRequestBuilder {
    HttpRequest build();

    ApiRequestBuilder setUri();

    ApiRequestBuilder setTitleToSearch(String title);

    ApiRequestBuilder setWorkIdToSearch(String id);
}
