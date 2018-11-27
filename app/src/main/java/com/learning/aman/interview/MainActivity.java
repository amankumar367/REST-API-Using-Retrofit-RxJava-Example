package com.learning.aman.interview;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.learning.aman.interview.Adapter.WorldPopulationAdapter;
import com.learning.aman.interview.Model.WorldPopulation;
import com.learning.aman.interview.Model.WorldPopulationList;
import com.learning.aman.interview.RESTAPI.APIClient;
import com.learning.aman.interview.RESTAPI.WorldPopulationEndPoints;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private WorldPopulationAdapter myAdapter;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        WorldPopulationEndPoints apiservices =
                APIClient.getClient().create(WorldPopulationEndPoints.class);

        Call<WorldPopulationList> call = apiservices.getWorldPopulationData();

        /**Dispaly URL in Log*/
        Log.e("URL Called", call.request().url() + "");

        call.enqueue(new Callback<WorldPopulationList>() {
            @Override
            public void onResponse(Call<WorldPopulationList> call, Response<WorldPopulationList> response) {
                loadDataToRecyclerView(response.body().getWorldPopulationlist());
            }

            @Override
            public void onFailure(Call<WorldPopulationList> call, Throwable t) {

            }
        });

    }

    private void loadDataToRecyclerView(ArrayList<WorldPopulation> worldPopulationlist) {

        myAdapter = new WorldPopulationAdapter(worldPopulationlist);
        mRecyclerView.setAdapter(myAdapter);

    }
}
