package com.gotta_watch_them_all.app.unit.work.infrastructure.util;

import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;
import com.gotta_watch_them_all.app.work.core.exception.TooManySearchArgumentsException;
import com.gotta_watch_them_all.app.work.infrastructure.util.ApiRequestBuilderMovieDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringBootConfiguration
@TestPropertySource(properties = {
        "moviedb.api.key=key",
        "moviedb.api.host=movie-database-imdb-alternative.p.rapidapi.com",
})
class ApiRequestBuilderMovieDbTest {

    @Value("${moviedb.api.key}")
    private String apiKey;

    @Value("${moviedb.api.host}")
    private String apiHost;

    private ApiRequestBuilderMovieDb sut;

    private HttpRequest requestModel;

    private String apiUrl;

    @BeforeEach
    public void setup() {
        sut = new ApiRequestBuilderMovieDb();
        ReflectionTestUtils.setField(sut, "apiKey", apiKey);
        ReflectionTestUtils.setField(sut, "apiHost", apiHost);
        apiUrl = String.format("https://%s", apiHost);

        requestModel = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    @Test
    public void setTitleToSearch_should_set_title_property() throws IllegalTitleGivenException {
        sut.setTitleToSearch("titre");
        Object property = ReflectionTestUtils.getField(sut, "titleToSearch");
        assertEquals("titre", property);
    }

    @Test
    public void setTitleToSearch_should_throw_exception_if_title_null() {
        assertThrows(IllegalTitleGivenException.class, () -> sut.setTitleToSearch(null));
    }

    @Test
    public void setTitleToSearch_should_throw_exception_if_title_empty() {
        assertThrows(IllegalTitleGivenException.class, () -> sut.setTitleToSearch(" "));
    }

    @Test
    public void setUri_should_set_base_url_to_start_with_https_plus_api_host() throws MalformedURLException, AnySearchValueFoundException, IllegalTitleGivenException, TooManySearchArgumentsException {
        sut.setTitleToSearch("titre").setUri();
        URI uri = (URI) ReflectionTestUtils.getField(sut, "uri");
        String url = uri.toURL().toString();
        assertTrue(url.startsWith("https://" + apiHost));
    }

    @Test
    public void setUri_should_throw_no_search_value_found_if_title_to_search_null_and_id_null() {
        assertThrows(AnySearchValueFoundException.class, () -> sut.setUri());
    }

    @Test
    public void setUri_should_not_throw_no_search_value_found_if_title_to_search_null_and_id_defined() throws IllegalImdbIdGivenException {
        sut.setWorkIdToSearch("id");
        assertDoesNotThrow(() -> sut.setUri());
    }

    @Test
    public void setUri_should_not_throw_no_search_value_found_if_title_to_search_defined_and_id_null() throws IllegalTitleGivenException {
        sut.setTitleToSearch("id");
        assertDoesNotThrow(() -> sut.setUri());
    }

    @Test
    public void setUri_should_add_title_to_uri() throws IllegalTitleGivenException, AnySearchValueFoundException, MalformedURLException, TooManySearchArgumentsException {
        sut.setTitleToSearch("titre").setUri();
        URI uri = (URI) ReflectionTestUtils.getField(sut, "uri");
        String url = uri.toURL().toString();
        assertTrue(url.contains("s=titre"));
    }

    @Test
    public void setUri_should_add_workid_to_uri() throws AnySearchValueFoundException, MalformedURLException, TooManySearchArgumentsException, IllegalImdbIdGivenException {
        sut.setWorkIdToSearch("id").setUri();
        URI uri = (URI) ReflectionTestUtils.getField(sut, "uri");
        String url = uri.toURL().toString();
        assertTrue(url.contains("i=id"));
    }

    @Test
    public void setUri_should_add_title_with_special_characters() {
        assertDoesNotThrow(() -> sut.setTitleToSearch("Les m??chants").setUri());
    }

    @Test
    public void setUri_should_set_return_type_to_json() throws IllegalTitleGivenException, AnySearchValueFoundException, MalformedURLException, TooManySearchArgumentsException {
        sut.setTitleToSearch("titre").setUri();
        URI uri = (URI) ReflectionTestUtils.getField(sut, "uri");
        String url = uri.toURL().toString();
        assertTrue(url.endsWith("&r=json"));
    }

    @Test
    public void build_should_call_set_uri_once() throws AnySearchValueFoundException, IllegalTitleGivenException, TooManySearchArgumentsException {
        ApiRequestBuilderMovieDb spy = Mockito.spy(sut);
        spy.setTitleToSearch("titre").build();
        Mockito.verify(spy, Mockito.times(1)).setUri();
    }

    @Test
    public void build_should_return_http_request_with_right_uri() throws IllegalTitleGivenException, AnySearchValueFoundException, TooManySearchArgumentsException {
        HttpRequest requestFromBuilder = sut
                .setTitleToSearch("titre")
                .build();
        URI expectedUri = URI.create(String.format("https://%s/?s=%s%s", apiHost, "titre", "&r=json"));
        assertEquals(expectedUri, requestFromBuilder.uri());
    }

    @Test
    public void build_should_return_http_request_with_right_headers() throws IllegalTitleGivenException, AnySearchValueFoundException, TooManySearchArgumentsException {
        HttpRequest requestFromBuilder = sut
                .setTitleToSearch("titre")
                .build();

        assertEquals(requestModel.headers(), requestFromBuilder.headers());
    }

    @Test
    public void build_should_return_http_request_with_right_method() throws IllegalTitleGivenException, AnySearchValueFoundException, TooManySearchArgumentsException {
        HttpRequest requestFromBuilder = sut
                .setTitleToSearch("titre")
                .build();

        assertEquals(requestModel.method(), requestFromBuilder.method());
    }

    @Test
    public void setWorkIdToSearch_should_throw_illegal_excpetion_if_id_null() {
        assertThrows(IllegalImdbIdGivenException.class, () -> sut.setWorkIdToSearch(null));
    }

    @Test
    public void setWorkIdToSearch_should_throw_illegal_excpetion_if_id_blank() {
        assertThrows(IllegalImdbIdGivenException.class, () -> sut.setWorkIdToSearch("   "));
    }

    @Test
    public void setWorkIdToSearch_should_add_work_imdb_id() throws IllegalImdbIdGivenException {
        sut.setWorkIdToSearch("ttsomething");
        var imdbId = ReflectionTestUtils.getField(sut, "imdbIdToSearch");
        assertEquals("ttsomething", imdbId);
    }

    @Test
    public void setUri_should_throw_exception_if_both_imdbId_and_title_defined() throws IllegalImdbIdGivenException, IllegalTitleGivenException {
        sut.setWorkIdToSearch("id");
        sut.setTitleToSearch("titre");
        assertThrows(TooManySearchArgumentsException.class, () -> sut.setUri());
    }

    @Test
    public void build_should_reset_imdb_to_search() {
        sut.setWorkIdToSearch("id");
        sut.build();
        assertNull(sut.getImdbIdToSearch());
    }

    @Test
    public void build_should_reset_title_to_search() {
        sut.setTitleToSearch("titre");
        sut.build();
        assertNull(sut.getTitleToSearch());
    }

}
