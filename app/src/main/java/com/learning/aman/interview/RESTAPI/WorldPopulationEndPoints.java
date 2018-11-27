package com.learning.aman.interview.RESTAPI;

import com.learning.aman.interview.Model.WorldPopulationList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WorldPopulationEndPoints {

    @GET("tutorial/jsonparsetutorial.txt")
    Call<WorldPopulationList> getWorldPopulationData();
}
