package com.learning.aman.interview.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.learning.aman.interview.Model.WorldPopulation;
import com.learning.aman.interview.PreviewImage;
import com.learning.aman.interview.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorldPopulationAdapter extends RecyclerView.Adapter<WorldPopulationAdapter.WorldPopulationViewHolder> {

    private ArrayList<WorldPopulation> datalist;
    private Context context;

    public WorldPopulationAdapter(ArrayList<WorldPopulation> worldPopulationlist, Context context) {
        this.context = context;
        this.datalist = worldPopulationlist;
    }

    @NonNull
    @Override
    public WorldPopulationAdapter.WorldPopulationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_list, parent, false);
        return new WorldPopulationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorldPopulationAdapter.WorldPopulationViewHolder holder, final int position) {

        holder.rank.setText(datalist.get(position).getRank());
        holder.country.setText(datalist.get(position).getCountry());
        holder.population.setText(datalist.get(position).getPopulation());
//        holder.imagelink.setText(datalist.get(position).getFlag());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PreviewImage.class);
                intent.putExtra("ImageLink",datalist.get(position).getFlag());
                context.startActivity(intent);
//                Toast.makeText(context, "sdnvl", Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.get()
                .load(datalist.get(position).getFlag())
                .resize(120, 60)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class WorldPopulationViewHolder extends RecyclerView.ViewHolder{

        TextView rank, country, population, imagelink;
        ImageView mImageView;
        View mView;

        public WorldPopulationViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            rank = itemView.findViewById(R.id.rank);
            country = itemView.findViewById(R.id.country);
            population = itemView.findViewById(R.id.population);
            imagelink = itemView.findViewById(R.id.imagelink);
            mImageView = itemView.findViewById(R.id.imageView);
        }
    }
}
