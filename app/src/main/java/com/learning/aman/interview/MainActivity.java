package com.learning.aman.interview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.learning.aman.interview.Adapter.WorldPopulationAdapter;
import com.learning.aman.interview.Model.WorldPopulationList;
import com.learning.aman.interview.RESTAPI.APIClient;
import com.learning.aman.interview.RESTAPI.WorldPopulationEndPoints;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private WorldPopulationAdapter myAdapter;
    private Button mButton;

    private LinearLayout linearLayout;
    private FileWriter fileWriter;
    private File root = Environment.getExternalStorageDirectory();
    private File csvFIle = new File(root, "contacts.csv");
    private static final int BUFFER = 2048 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.linearlayout);
        mRecyclerView= findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        callWithRxJava();

        mButton = findViewById(R.id.extractContactList);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()){
                    extractContactWithRxJava();
                }
            }
        });

    }


    //This method set worldPopulation Data into recycler view using RXJava
    private void callWithRxJava() {
        WorldPopulationEndPoints apiservices =
                APIClient.getClient().create(WorldPopulationEndPoints.class);

        Observable<WorldPopulationList> observable = apiservices.getWorldPopulationDatas();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WorldPopulationList>() {
            @Override
            public void onNext(WorldPopulationList worldPopulationList) {

                myAdapter = new WorldPopulationAdapter(worldPopulationList.getWorldPopulationlist(),MainActivity.this);
                mRecyclerView.setAdapter(myAdapter);
                Log.e(TAG, "onNext: "+worldPopulationList );   //See the Resopnse of WorldPopulationList
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    //This method will call storeDataInExternalStorage() for getting phone contact using RXJava
    private void extractContactWithRxJava() {

        Observable<File> callFileObservable = Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                return storeDataInExternalStorage();
            }
        });

        callFileObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(File file) {
                        Snackbar.make(linearLayout,"Operation has been Completed",Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //Get the Phone contacts and save into csv file and call createZip() to create csv file to zip
    private File storeDataInExternalStorage() throws IOException {

        fileWriter = new FileWriter(csvFIle);
        fileWriter.append("Name");
        fileWriter.append(",");
        fileWriter.append("Phone Number\n");

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.e("MainActivity", name+" - "+phoneNumber );

            String data = name + "," + phoneNumber + "\n";

            fileWriter.write(String.format("%s\t", name));
            fileWriter.append(",");
            fileWriter.write(String.format("%s\n", phoneNumber));
        }

        fileWriter.flush();
        fileWriter.close();
        phones.close();
        createZip(new String[]{csvFIle.getPath()},"/Contacts.zip");
        return csvFIle;
    }

    // Creating Zip in this method
    private void createZip(String[] strings, String zipFileNAme) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+zipFileNAme);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < strings.length; i++) {
                Log.v("Compress", "Adding: " + strings[i]);
                FileInputStream fi = new FileInputStream(strings[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(strings[i].substring(strings[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method checkpermission and Request for permission of Read contact & Write External Storage
    private boolean checkPermission() {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(MainActivity.this, "Contact read permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, PERMISSION_ALL);
                }
                return false;
            }
        }
        return true;
    }
}
