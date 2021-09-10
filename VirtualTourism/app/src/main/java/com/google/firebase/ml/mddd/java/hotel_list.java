package com.google.firebase.ml.mddd.java;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.ml.mddd.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class hotel_list extends RecyclerView.Adapter<hotel_list.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> t_name = new ArrayList<>();
    private ArrayList<String> dp = new ArrayList<>();
    ArrayList<String> t_number = new ArrayList<>();
    ArrayList<Integer> t_rating = new ArrayList<>();
    ArrayList<String> t_model = new ArrayList<>();
    private Context mContext;

    public hotel_list(Context context, ArrayList<String> imageNames, ArrayList<String> images, ArrayList<String> t_number, ArrayList<String> t_model, ArrayList<Integer> t_rating ) {
        t_name = imageNames;
        dp = images;
        this.t_number = t_number;
        this.t_model = t_model;
        this.t_rating = t_rating;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_details, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(dp.get(position))
                .into(holder.imageView);
    //    UniversalImageLoader.setImage(dp.get(position),holder.imageView,null,"");

        holder.owner_name.setText(t_name.get(position));
        holder.car_model.setText(t_model.get(position));
        holder.car_number.setText(t_number.get(position));
        holder.rating.setText(Integer.toString(t_rating.get(position)));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + t_name.get(position));

                Toast.makeText(mContext, t_name.get(position), Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(mContext, GalleryActivity.class);
//                intent.putExtra("image_url", dp.get(position));
//                intent.putExtra("image_name", t_name.get(position));
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return t_name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView car_model;
        TextView owner_name;
        TextView rating;
        TextView car_number;
        CircleImageView imageView;
        RelativeLayout parentLayout;

        public ViewHolder(View v) {
            super(v);
            car_model = v.findViewById(R.id.hdetails);
            owner_name = v.findViewById(R.id.hname);
            rating = v.findViewById(R.id.trating);
            car_number = v.findViewById(R.id.tnumber);
            imageView = v.findViewById(R.id.timage);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
