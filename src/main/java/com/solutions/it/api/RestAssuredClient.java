package com.solutions.it.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * RestAssuredClient - Singleton class to manage REST API interactions
 * using REST Assured library.
 */
public class RestAssuredClient {
    private static final Logger LOGGER = LogManager.getLogger(RestAssuredClient.class);
    private static RestAssuredClient instance;
    private RequestSpecification requestSpec;

    private RestAssuredClient() {
        // Initialize default request specification
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    public static synchronized RestAssuredClient getInstance() {
        if (instance == null) {
            instance = new RestAssuredClient();
        }
        return instance;
    }

    /**
     * Sets the base URI for API requests
     * @param baseUri The base URI
     * @return RestAssuredClient instance
     */
    public RestAssuredClient setBaseUri(String baseUri) {
        requestSpec = requestSpec.baseUri(baseUri);
        LOGGER.info("Base URI set to: {}", baseUri);
        return this;
    }

    /**
     * Sets headers for API requests
     * @param headers Map of header name-value pairs
     * @return RestAssuredClient instance
     */
    public RestAssuredClient setHeaders(Map<String, String> headers) {
        requestSpec = requestSpec.headers(headers);
        LOGGER.info("Headers set: {}", headers);
        return this;
    }

    /**
     * Sets authorization header
     * @param token Authorization token
     * @return RestAssuredClient instance
     */
    public RestAssuredClient setAuthorizationToken(String token) {
        requestSpec = requestSpec.header("Authorization", "Bearer " + token);
        LOGGER.info("Authorization token set");
        return this;
    }

    /**
     * Executes GET request
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response get(String endpoint) {
        LOGGER.info("Executing GET request to: {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(endpoint);
    }

    /**
     * Executes GET request with query parameters
     * @param endpoint API endpoint
     * @param queryParams Map of query parameters
     * @return Response object
     */
    public Response get(String endpoint, Map<String, String> queryParams) {
        LOGGER.info("Executing GET request to: {} with params: {}", endpoint, queryParams);
        return RestAssured.given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .when()
                .get(endpoint);
    }

    /**
     * Executes POST request with body
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    public Response post(String endpoint, Object body) {
        LOGGER.info("Executing POST request to: {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post(endpoint);
    }

    /**
     * Executes PUT request with body
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    public Response put(String endpoint, Object body) {
        LOGGER.info("Executing PUT request to: {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .put(endpoint);
    }

    /**
     * Executes PATCH request with body
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    public Response patch(String endpoint, Object body) {
        LOGGER.info("Executing PATCH request to: {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .patch(endpoint);
    }

    /**
     * Executes DELETE request
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response delete(String endpoint) {
        LOGGER.info("Executing DELETE request to: {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .delete(endpoint);
    }
} 