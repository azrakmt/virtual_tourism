package com.google.firebase.ml.mddd.java;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.ml.mddd.R;

import java.util.ArrayList;

public class MimageInflator extends RecyclerView.Adapter<MimageInflator.Viewholder> {

    ArrayList<String> images;
    Activity mContext;

    MimageInflator(@NonNull Activity context, ArrayList<String> images)
    {
        this.images = images;
        mContext = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_inflator, parent, false);
        MimageInflator.Viewholder holder = new MimageInflator.Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(images.get(position))
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        SqaureImageView img;
        Viewholder(View v){
            super(v);
            img = v.findViewById(R.id.image123);
        }
    }
}
