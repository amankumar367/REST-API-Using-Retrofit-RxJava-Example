package com.learning.aman.interview;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class PreviewImage extends AppCompatActivity {

    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);

        mImageView = (ImageView) findViewById(R.id.previewImage);
        Picasso.get()
                .load(getIntent().getStringExtra("ImageLink"))
                .resize(120, 60)
                .into(mImageView);
//        Toast.makeText(this, "LInK - "+getIntent().getStringExtra("ImageLink"), Toast.LENGTH_SHORT).show();
    }
}
