package com.gotta_watch_them_all.app.work.infrastructure.util;

import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;
import com.gotta_watch_them_all.app.work.core.exception.TooManySearchArgumentsException;

import java.net.http.HttpRequest;

public interface ApiRequestBuilder {
    HttpRequest build() throws AnySearchValueFoundException, TooManySearchArgumentsException;

    ApiRequestBuilder setUri() throws AnySearchValueFoundException, TooManySearchArgumentsException;

    ApiRequestBuilder setTitleToSearch(String title) throws IllegalTitleGivenException;

    ApiRequestBuilder setWorkIdToSearch(String id) throws IllegalImdbIdGivenException;
}
