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

import org.reactivestreams.Subscription;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private WorldPopulationAdapter myAdapter;
    private ImageView mImageView;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        callWithRxJava();
//        callWithRetrofit();

    }

    private void callWithRxJava() {
        WorldPopulationEndPoints apiservices =
                APIClient.getClient().create(WorldPopulationEndPoints.class);

        Observable<WorldPopulationList> observable = apiservices.getWorldPopulationDatas();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<WorldPopulationList>() {
            @Override
            public void onNext(WorldPopulationList worldPopulationList) {

                myAdapter = new WorldPopulationAdapter(worldPopulationList.getWorldPopulationlist(),MainActivity.this);
                mRecyclerView.setAdapter(myAdapter);
                Log.e("bdgksbdg", "onNext: "+worldPopulationList );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void callWithRetrofit() {

        WorldPopulationEndPoints apiservices =
                APIClient.getClient().create(WorldPopulationEndPoints.class);

        Call<WorldPopulationList> call = apiservices.getWorldPopulationData();

        /**Dispaly URL in Log*/
        Log.e("URL Called", call.request().url() + "");

        call.enqueue(new Callback<WorldPopulationList>() {
            @Override
            public void onResponse(Call<WorldPopulationList> call, Response<WorldPopulationList> response) {
                myAdapter = new WorldPopulationAdapter(response.body().getWorldPopulationlist(),MainActivity.this);
                mRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<WorldPopulationList> call, Throwable t) {

            }
        });



    }
}
