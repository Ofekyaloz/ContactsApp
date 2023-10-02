package com.example.contactsapp.api;


import com.example.contactsapp.GenderResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebServiceApi {
    @GET("/")
    Call<GenderResponse> getGender(@Query("name") String name);
}
