package com.gotta_watch_them_all.app.work.unit.infrastructure.util;


import com.gotta_watch_them_all.app.work.core.exception.BadHttpRequestException;
import com.gotta_watch_them_all.app.work.infrastructure.util.ApiRequester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ApiRequesterTest {

    private ApiRequester sut;
    private final String apiUrl = "https://cat-fact.herokuapp.com/facts";
    private HttpRequest request;

    HttpClient httpClientMock;
    HttpResponse httpResponseMock;

    @BeforeEach
    public void setup() {
        httpClientMock = Mockito.mock(HttpClient.class);
        httpResponseMock = Mockito.mock(HttpResponse.class);
        sut = new ApiRequester(httpClientMock);

        request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();
    }

    @Test
    public void request_should_get_data_response() throws IOException, InterruptedException, BadHttpRequestException {
        String expectedData = "[{\"status\":{\"verified\":true,\"sentCount\":1,\"feedback\":\"\"},\"type\":\"cat\",\"deleted\":false,\"_id\":\"5887e1d85c873e0011036889\",\"user\":\"5a9ac18c7478810ea6c06381\",\"text\":\"Cats make about 100 different sounds. Dogs make only about 10.\",\"__v\":0,\"source\":\"user\",\"updatedAt\":\"2020-09-03T16:39:39.578Z\",\"createdAt\":\"2018-01-15T21:20:00.003Z\",\"used\":true},{\"status\":{\"verified\":true,\"sentCount\":1},\"type\":\"cat\",\"deleted\":false,\"_id\":\"588e746706ac2b00110e59ff\",\"user\":\"588e6e8806ac2b00110e59c3\",\"text\":\"Domestic cats spend about 70 percent of the day sleeping and 15 percent of the day grooming.\",\"__v\":0,\"source\":\"user\",\"updatedAt\":\"2020-08-26T20:20:02.359Z\",\"createdAt\":\"2018-01-14T21:20:02.750Z\",\"used\":true},{\"status\":{\"verified\":true,\"sentCount\":1},\"type\":\"cat\",\"deleted\":false,\"_id\":\"58923f2fc3878c0011784c79\",\"user\":\"5887e9f65c873e001103688d\",\"text\":\"I don't know anything about cats.\",\"__v\":0,\"source\":\"user\",\"updatedAt\":\"2020-08-23T20:20:01.611Z\",\"createdAt\":\"2018-02-25T21:20:03.060Z\",\"used\":false},{\"status\":{\"verified\":true,\"sentCount\":1},\"type\":\"cat\",\"deleted\":false,\"_id\":\"5894af975cdc7400113ef7f9\",\"user\":\"5a9ac18c7478810ea6c06381\",\"text\":\"The technical term for a cat’s hairball is a bezoar.\",\"__v\":0,\"source\":\"user\",\"updatedAt\":\"2020-11-25T21:20:03.895Z\",\"createdAt\":\"2018-02-27T21:20:02.854Z\",\"used\":true},{\"status\":{\"verified\":true,\"sentCount\":1},\"type\":\"cat\",\"deleted\":false,\"_id\":\"58e007cc0aac31001185ecf5\",\"user\":\"58e007480aac31001185ecef\",\"text\":\"Cats are the most popular pet in the United States: There are 88 million pet cats and 74 million dogs.\",\"__v\":0,\"source\":\"user\",\"updatedAt\":\"2020-08-23T20:20:01.611Z\",\"createdAt\":\"2018-03-01T21:20:02.713Z\",\"used\":false}]";
        Mockito.when(httpClientMock.send(Mockito.any(), Mockito.any()))
                .thenReturn(httpResponseMock);

        Mockito.when(httpResponseMock.body()).thenReturn(expectedData);
        assertEquals(expectedData, sut.request(request));
    }

    @Test
    public void request_should_call_http_client_once() throws IOException, InterruptedException, BadHttpRequestException {
        Mockito.when(httpClientMock.send(Mockito.any(), Mockito.any()))
                .thenReturn(httpResponseMock);
        sut.request(request);
        verify(httpClientMock, times(1)).send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void request_should_throw_exception_if_request_not_correct() throws IOException, InterruptedException {
        Mockito.when(httpClientMock.send(Mockito.any(), Mockito.any())).thenThrow(IOException.class);

        assertThrows(BadHttpRequestException.class, () -> sut.request(HttpRequest
                .newBuilder()
                .uri(URI.create(apiUrl))
                .build()));
    }

    @Test
    public void request_should_throw_exception_if_request_not_correct_2() throws IOException, InterruptedException {
        Mockito.when(httpClientMock.send(Mockito.any(), Mockito.any())).thenThrow(InterruptedException.class);

        assertThrows(BadHttpRequestException.class, () -> sut.request(HttpRequest
                .newBuilder()
                .uri(URI.create(apiUrl))
                .build()));
    }

    @Test
    public void request_should_throw_exception_if_request_is_null() {
        assertThrows(BadHttpRequestException.class, () -> sut.request(null));
    }

}
