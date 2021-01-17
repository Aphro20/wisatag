package com.example.wisatag.api;

public class ApiUtils {
    public static final String BASE_URL = "http://192.168.100.99:8000/";

    public static ApiService getAPIService() {
        return Client.getClient(BASE_URL).create(ApiService.class);
    }
}
