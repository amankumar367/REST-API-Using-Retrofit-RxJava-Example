package com.learning.aman.interview.Model;

import com.google.gson.annotations.SerializedName;

public class WorldPopulation {

    @SerializedName("rank")
    private String rank;

    @SerializedName("population")
    private String population;

    @SerializedName("flag")
    private String flag;

    @SerializedName("country")
    private String country;


    public WorldPopulation(String rank, String population, String flag, String country) {
        this.rank = rank;
        this.population = population;
        this.flag = flag;
        this.country = country;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
