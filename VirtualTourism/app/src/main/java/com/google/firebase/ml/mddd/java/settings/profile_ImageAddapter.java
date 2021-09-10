package com.google.firebase.ml.mddd.java.settings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.ml.mddd.R;

import java.util.ArrayList;

public class profile_ImageAddapter extends RecyclerView.Adapter<profile_ImageAddapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> t_name = new ArrayList<>();
    private ArrayList<String> dp = new ArrayList<>();
    private Context mContext;

    public profile_ImageAddapter(Context context, ArrayList<String> imageNames, ArrayList<String> images ) {
        t_name = imageNames;
        dp = images;

        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_imageview_inflator, parent, false);
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

        holder.Mon_name.setText(t_name.get(position));

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

        TextView Mon_name;
        ImageView imageView;
        RelativeLayout parentLayout;

        public ViewHolder(View v) {
            super(v);
            Mon_name = v.findViewById(R.id.m_Asso);
            imageView = v.findViewById(R.id.user_image);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
