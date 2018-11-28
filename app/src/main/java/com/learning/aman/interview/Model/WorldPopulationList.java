package com.learning.aman.interview.Model;

import com.google.gson.annotations.SerializedName;
import com.learning.aman.interview.Model.WorldPopulation;

import java.util.ArrayList;

public class WorldPopulationList {

    @SerializedName("worldpopulation")
    private ArrayList<WorldPopulation> worldPopulationlist;

    public ArrayList<WorldPopulation> getWorldPopulationlist() {
        return worldPopulationlist;
    }

}
