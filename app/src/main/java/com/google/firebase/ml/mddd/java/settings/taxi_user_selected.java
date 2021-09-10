package com.google.firebase.ml.mddd.java.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.ml.mddd.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class taxi_user_selected extends AppCompatActivity {

    String name,phone,location,image,destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_user_selected);

        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        phone = b.getString("phone");
        location = b.getString("location");
        image = b.getString("dp");
        destination = b.getString("destination");


        setData();
    }
    void setData()
    {
        TextView uname,uphone;
        uname = findViewById(R.id.username);
        uphone = findViewById(R.id.userphone);
        CircleImageView dpic = findViewById(R.id.pro_pic);
        uname.setText(name);
        uphone.setText(phone);
        Glide.with(this)
                .load(image)
                .into(dpic);
    }

    public void call(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+Integer.parseInt(phone)));//change the number.
        startActivity(callIntent);
    }
}
