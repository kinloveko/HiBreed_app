package com.example.hi_breed.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hi_breed.R;
import com.example.hi_breed.userFile.home.user_home_fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class productPetRecyclerAdapter extends RecyclerView.Adapter<productPetRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<petClass> list;



    public productPetRecyclerAdapter(Context context, ArrayList<petClass> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_row_layout,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        petClass petProduct = list.get(position);
        holder.sold.setText("Available");
        holder.reviews.setText("0");
        holder.petName.setText(petProduct.getPet_name());
        holder.price.setText(petProduct.getPet_price());
        Picasso.get().load(String.valueOf(list.get(position))).into(holder.petImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView sold,reviews,petName,price;
        private ImageView petImage,heartClick;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sold = itemView.findViewById(R.id.textView_available);
            reviews = itemView.findViewById(R.id.reviewsDisplay_rate);
            petName = itemView.findViewById(R.id.textViewDisplay_petName);
            price = itemView.findViewById(R.id.textView_price);
            petImage = itemView.findViewById(R.id.imagePetDisplay);
            heartClick =  itemView.findViewById(R.id.heartClick);

        }
    }

}
