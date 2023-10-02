package com.example.contactsapp.api;


import android.util.Log;

import com.example.contactsapp.GenderResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIService {
    Retrofit retrofit;
    WebServiceApi webServiceApi;
    Gson gson;

    public APIService() {
        gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.genderize.io/?name=")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public void getGender(String name) {
        Call<GenderResponse> call = webServiceApi.getGender(name);
        call.enqueue(new Callback<GenderResponse>() {
            @Override
            public void onResponse(Call<GenderResponse> call, Response<GenderResponse> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<GenderResponse> call, Throwable t) {
                Log.e("API Call", "Failed: " + t.getMessage());
            }

        });
    }


}
