package com.bike.beat.application;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FuelActivity extends AppCompatActivity implements GeoTask.Geo{
    EditText edttxt_from,edttxt_to;
    Button btn_get;
    String str_from,str_to;
    TextView tv_result1,tv_result2,tv_result3,textViewp;
    String newString;
    CardView cv;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);
        initialize();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("Fuel_Level");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("Fuel_Level");
        }
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Eras_Demi_ITC.ttf");
        btn_get.setTypeface(custom_font);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv.setVisibility(view.VISIBLE);
                str_from=edttxt_from.getText().toString();
                //edttxt_from.setTypeface(custom_font);
                str_to=edttxt_to.getText().toString();
                //edttxt_to.setTypeface(custom_font);
                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + str_from + "&destinations=" + str_to + "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyCuAwlQRhAdo7_Ij-x--Wsh_KgDKHdJhrA";
                new GeoTask(FuelActivity.this).execute(url);

            }
        });

    }

    @Override
    public void setDouble(String result) {
        String res[]=result.split(",");
        Double min=Double.parseDouble(res[0])/60;
        int dist=Integer.parseInt(res[1])/1000;
        Double fuel_val=Double.parseDouble(newString);
        fuel_val=Double.parseDouble(String.format("0.2f",fuel_val));
        fuel_val=dist/fuel_val;
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Eras_Demi_ITC.ttf");
        tv_result1.setText("Duration= " + (int) (min / 60) + " hr " + (int) (min % 60) + " mins");
        tv_result1.setTypeface(custom_font);
        tv_result2.setText("Distance= " + dist + " kilometers");
        tv_result2.setTypeface(custom_font);
        tv_result3.setText("Fuel Required= "+ fuel_val +" liters");
        tv_result3.setTypeface(custom_font);

    }
    public void initialize()
    {
        cv =(CardView) findViewById(R.id.cv1);
        edttxt_from= (EditText) findViewById(R.id.editText_from);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Eras_Demi_ITC.ttf");
        edttxt_from.setTypeface(custom_font);
        edttxt_to= (EditText) findViewById(R.id.editText_to);
        edttxt_to.setTypeface(custom_font);
        btn_get= (Button) findViewById(R.id.button_get);
        tv_result1= (TextView) findViewById(R.id.textView_result1);
        tv_result2=(TextView) findViewById(R.id.textView_result2);
        tv_result3= (TextView) findViewById(R.id.textView_result3);
        textViewp=(TextView) findViewById(R.id.textView7);
        textViewp.setTypeface(custom_font);
    }
}