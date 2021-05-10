package com.gotta_watch_them_all.app.work.infrastructure.util;

import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;

import java.net.http.HttpRequest;

public interface ApiRequestBuilder {
    HttpRequest build() throws AnySearchValueFoundException;

    ApiRequestBuilder setUri() throws AnySearchValueFoundException;

    ApiRequestBuilder setTitleToSearch(String title) throws IllegalTitleGivenException;
}
