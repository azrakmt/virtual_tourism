package com.google.firebase.ml.mddd.java;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.ml.mddd.R;
import com.google.firebase.ml.mddd.Search_result;

import java.util.ArrayList;

public  class NewRecycler extends RecyclerView.Adapter<NewRecycler.Viewholder> {

    ArrayList<String> mname;
    ArrayList<String> imageId;
    ArrayList<String> visited;
    Activity mContext;
  //  private LayoutInflater mInflater;
    public NewRecycler(@NonNull Activity context, ArrayList<String> mname,ArrayList<String> imageId,ArrayList<String> visited) {
      //  super(context, R.layout.layout_inflate_home,mname);

        this.mContext = context;
        this.mname = mname;
        this.imageId = imageId;
        this.visited = visited;
    //    this.mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inflate_home, parent, false);
        NewRecycler.Viewholder holder = new NewRecycler.Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {


        Glide.with(mContext)
                .asBitmap()
                .load(imageId.get(position))
                .into(holder.img);
        holder.txtVisited.setText(visited.get(position));
        holder.txtName.setText(mname.get(position));
       // UniversalImageLoader.setImage(imageId.get(position),holder.img,null,"");

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, mname.get(position), Toast.LENGTH_SHORT).show();
                String mname1 = mname.get(position).replace(" ", "").toLowerCase();
                Intent i = new Intent(mContext, Search_result.class);
                i.putExtra("mname", mname1);
                mContext.startActivity(i);
               // Toast.makeText(home.this, m, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mname.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        SqaureImageView img;
        TextView txtName;
        TextView txtVisited;
        LinearLayout parentLayout;
        Viewholder(View v){
            super(v);
            parentLayout=v.findViewById(R.id.homeInflateParant);
            img = v.findViewById(R.id.homeImage3);
            txtName = v.findViewById(R.id.tname3);
            txtVisited = v.findViewById(R.id.tvisited3);
        }
    }


}


