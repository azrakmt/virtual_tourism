package com.google.firebase.ml.mddd.java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.ml.mddd.R;
import com.google.firebase.ml.mddd.java.settings.Gold_details;
import com.google.firebase.ml.mddd.java.settings.Platinum_details;
import com.google.firebase.ml.mddd.java.settings.Silver_details;

public class hotel_mode extends AppCompatActivity {
String mode,hname,hlocation,selected_grp;
Gold_details gd;
Silver_details sd;
Platinum_details pd;
    TextView rate,avail,description;

    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_mode);

        gd = new Gold_details();
        sd = new Silver_details();
        pd = new Platinum_details();

        rate = findViewById(R.id.rate);
        avail = findViewById(R.id.avail);
        description = findViewById(R.id.description);

        Bundle b = getIntent().getExtras();
        mode = b.getString("mode");
        hname = b.getString("hname");
        hlocation = b.getString("hlocation");
        selected_grp = "silver";
        switch (mode)
        {
            case "E" :
            {
                entry();
                break;
            }
            case "V" :
            {
                visitor();
                break;
            }
        }
    }
    void entry()
    {
      //  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Hotel_details");
        if(hname.equals("xxxx"))
        {
            Dialog d = new Dialog(this);
            d.setContentView(R.layout.barcode_field);
            TextView tv = d.findViewById(R.id.barcode_field_value);
            TextView tv1 = d.findViewById(R.id.barcode_field_label);
            tv1.setVisibility(View.GONE);
            tv.setText("Fill the details to complete registration");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.result_font));
            d.show();
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  String value = rate_dialog("rate");
                  classSelector("rate",value);
                }
            });
            avail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = avail_dialog();
                    classSelector("avail",value);
                }
            });
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = rate_dialog("desc");
                    classSelector("desc",value);
                }
            });


        }
        else
        {
            Toast.makeText(this, "hsjkdhsh", Toast.LENGTH_SHORT).show();
        }

    }
    void visitor()
    {

    }
    void classSelector(String detail,String value)
    {
        if(selected_grp.equals("Silver"))
        {
         setSilver(detail,value);
        }
        if(selected_grp.equals("Gold"))
        {
            setGold(detail,value);
        }
        if (selected_grp.equals("Platinum"))
        {
            setPlatinum(detail,value);
        }
    }
    void setSilver(String detail,String value)
    {
        if(detail.equals("rate"))
        {
            sd.setRate(value);
        }
        if(detail.equals("avail"))
        {
            sd.setAvailability(value);
        }
        if(detail.equals("desc"))
        {
            sd.setDescription(value);
        }
    }
    void setGold(String detail,String value)
    {
        if(detail.equals("rate"))
        {
            gd.setRate(value);
        }
        if(detail.equals("avail"))
        {
            gd.setAvailability(value);
        }
        if(detail.equals("desc"))
        {
            gd.setDescription(value);
        }
    }
    void setPlatinum(String detail,String value)
    {
        if(detail.equals("rate"))
        {
            pd.setRate(value);
        }
        if(detail.equals("avail"))
        {
            pd.setAvailability(value);
        }
        if(detail.equals("desc"))
        {
            pd.setDescription(value);
        }
    }

    String rate_dialog(String detail)
    {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_rate);

        EditText et = d.findViewById(R.id.destination);
        Button b = d.findViewById(R.id.save_rate);
        if(detail.equals("rate"))
        {
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        d.setCancelable(false);
        d.show();
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString().trim().isEmpty())
                {
                    et.setError("no data");
                }
                else
                {
                    setData(et.getText().toString().trim());
                    d.dismiss();
                }
            }
        });
        return data;
    }
    String avail_dialog()
    {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_availability);

        Switch et = d.findViewById(R.id.destination);
        Button b = d.findViewById(R.id.save_rate);
        d.setCancelable(false);
        d.show();
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.isChecked())
                {
                    setData(et.getTextOn().toString().trim());
                    d.dismiss();
                }
                else
                {
                    setData(et.getTextOff().toString().trim());
                    d.dismiss();
                }
            }
        });
        return data;
    }
void setData(String s)
{
    data=s;
}
    void setmode(TextView set,TextView tv1,TextView tv2)
    {
        set.setTextColor(Color.BLACK);
        tv1.setTextColor(Color.WHITE);
        tv2.setTextColor(Color.WHITE);
    }

    public void silver_click(View view) {
        selected_grp = "Silver";
        TextView s,g,p;
        s=findViewById(R.id.silver);
        g=findViewById(R.id.golden);
        p=findViewById(R.id.platinum);
        setmode(s,g,p);
    }

    public void gold_click(View view) {
        selected_grp = "Gold";
        TextView s,g,p;
        s=findViewById(R.id.silver);
        g=findViewById(R.id.golden);
        p=findViewById(R.id.platinum);
        setmode(g,p,s);

    }

    public void platinum_click(View view) {
        selected_grp = "Platinum";
        TextView s,g,p;
        s=findViewById(R.id.silver);
        g=findViewById(R.id.golden);
        p=findViewById(R.id.platinum);
        setmode(p,g,s);

    }
}
