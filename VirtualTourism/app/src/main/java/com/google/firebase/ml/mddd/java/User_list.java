package com.google.firebase.ml.mddd.java;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.ml.mddd.java.settings.taxi_user_selected;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_list extends RecyclerView.Adapter<User_list.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> u_name = new ArrayList<>();
    private ArrayList<String> dp = new ArrayList<>();
    private ArrayList<String> u_number = new ArrayList<>();
    private ArrayList<String> u_desitination = new ArrayList<>();
    private ArrayList<String> u_location = new ArrayList<>();
    private ArrayList<String> u_uid = new ArrayList<>();
    private Context mContext;

    public User_list(Context context, ArrayList<String> imageNames, ArrayList<String> images, ArrayList<String> u_number, ArrayList<String> u_location, ArrayList<String> u_desitination,ArrayList<String> u_uid ) {
        u_name = imageNames;
        dp = images;
        this.u_number = u_number;
        this.u_uid = u_uid;
        this.u_location = u_location;
        this.u_desitination = u_desitination;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflte_taximode, parent, false);
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

        holder.user_name.setText(u_name.get(position));
        holder.location.setText(u_location.get(position));
        holder.phone_number.setText(u_number.get(position));
        holder.destination.setText(u_desitination.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + u_name.get(position));

                Toast.makeText(mContext, u_name.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext,taxi_user_selected.class);
                intent.putExtra("dp", dp.get(position));
                intent.putExtra("name", u_name.get(position));
                intent.putExtra("destination", u_desitination.get(position));
                intent.putExtra("location", u_location.get(position));
                intent.putExtra("phone", u_number.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return u_name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView destination;
        TextView user_name;
        TextView location;
        TextView phone_number;
        CircleImageView imageView;
        RelativeLayout parentLayout;

        public ViewHolder(View v) {
            super(v);
            destination = v.findViewById(R.id.tmodel1);
            user_name = v.findViewById(R.id.tname);
            location = v.findViewById(R.id.tlocation1);
            phone_number = v.findViewById(R.id.tnumber1);
            imageView = v.findViewById(R.id.timage);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }

}
