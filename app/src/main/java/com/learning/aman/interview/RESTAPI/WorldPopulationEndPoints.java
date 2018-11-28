package com.learning.aman.interview.RESTAPI;

import com.learning.aman.interview.Model.WorldPopulationList;

import java.util.Observable;
import java.util.Observer;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WorldPopulationEndPoints {

    @GET("tutorial/jsonparsetutorial.txt")
    Call<WorldPopulationList> getWorldPopulationData();

    @GET("tutorial/jsonparsetutorial.txt")
    io.reactivex.Observable<WorldPopulationList> getWorldPopulationDatas();
}
