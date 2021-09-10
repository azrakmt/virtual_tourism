package com.google.firebase.ml.mddd.java;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.ml.mddd.R;

import java.util.List;

public class HomeScreenAdapter extends ArrayAdapter<String> {
    List<String> mname;
    List<String> imageId;
    List<String> visited;
    Activity mContext;
    public HomeScreenAdapter(@NonNull Activity context, List<String> mname,List<String> imageId,List<String> visited) {
        super(context, R.layout.layout_inflate_home,mname);

        this.mContext = context;
        this.mname = mname;
        this.imageId = imageId;
        this.visited = visited;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        Viewholder vh = null;
        if(r==null){
            LayoutInflater layoutInflater = mContext.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.layout_inflate_home,null,true);
            vh = new Viewholder(r);
            r.setTag(vh);
        }
        else {
            vh = (Viewholder) r.getTag();
        }
        vh.txtVisited.setText(visited.get(position));
        vh.txtName.setText(mname.get(position));
        UniversalImageLoader.setImage(imageId.get(position),vh.img,null,"");
        return r;
    }
    public class Viewholder{
        SqaureImageView img;
        TextView txtName;
        TextView txtVisited;
        Viewholder(View v){
            img = v.findViewById(R.id.homeImage3);
            txtName = v.findViewById(R.id.tname3);
            txtVisited = v.findViewById(R.id.tvisited3);
        }
    }
}
