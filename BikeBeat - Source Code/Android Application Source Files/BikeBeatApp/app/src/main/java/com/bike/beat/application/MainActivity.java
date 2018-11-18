package com.bike.beat.application;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

import static android.app.PendingIntent.getActivity;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    private String vals[]={"16/05/2017"," km"," h"," km/h"," km/h"," l"," l"," km/l"};
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private TextView v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11;
    private TextView l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12;
    DatabaseHelper myDB;
    ImageButton btnViewAll;
    View.OnClickListener mOnClickListener;
    private String fuel_send = " ";

    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDB=new DatabaseHelper(this);
        final BluetoothTask bt = new BluetoothTask();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton see_res,geoact;
        Button startbtn, stopbtn;

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Eras_Demi_ITC.ttf");

        startbtn = (Button) findViewById(R.id.start);
        stopbtn = (Button) findViewById(R.id.stop);
        startbtn.setTypeface(custom_font);
        stopbtn.setTypeface(custom_font);
        //see_res= (ImageButton) findViewById(R.id.estim_next);
        geoact = (ImageButton) findViewById(R.id.geo);
        btnViewAll = (ImageButton)findViewById(R.id.button_view_records);
        v1=(TextView)findViewById(R.id.date_val);
        v1.setTypeface(custom_font);
        v2=(TextView)findViewById(R.id.dist_cov_val);
        v2.setTypeface(custom_font);
        v3=(TextView)findViewById(R.id.time_taken_val);
        v3.setTypeface(custom_font);
        v4=(TextView)findViewById(R.id.avg_speed_val);
        v4.setTypeface(custom_font);
        //v5=(TextView)findViewById(R.id.top_speed_val);
        v6=(TextView)findViewById(R.id.cur_fuel_val);
        v6.setTypeface(custom_font);
        v7=(TextView)findViewById(R.id.fuel_consumed_val);
        v7.setTypeface(custom_font);
        v8=(TextView)findViewById(R.id.fuel_avg_val);
        v8.setTypeface(custom_font);
        //l5 = (TextView) findViewById(R.id.title);
       // l5.setTypeface(custom_font);
        l1=(TextView)findViewById(R.id.date_label);
        //l1.setTypeface(custom_font);
        l2=(TextView)findViewById(R.id.distcovered_label);
        l2.setTypeface(custom_font);
        l3=(TextView)findViewById(R.id.time_taken_label);
        l3.setTypeface(custom_font);
        l4=(TextView)findViewById(R.id.avg_speed_label);
        l4.setTypeface(custom_font);
        l6=(TextView)findViewById(R.id.curr_fuel_label);
        l6.setTypeface(custom_font);
        l7=(TextView)findViewById(R.id.fuel_consumed_label);
        l7.setTypeface(custom_font);
        l8=(TextView)findViewById(R.id.Fuel_average_label);
        l8.setTypeface(custom_font);
        //Starting the Notification service
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.truncData();
                checkBluetoothOn();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    try {
                        bt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                else
                    bt.execute();
            }
        });
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changetosummary(myDB);
            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content), "Recording has Stopped", Snackbar.LENGTH_LONG)
                        .setAction("Undo", mOnClickListener)
                        .setActionTextColor(Color.RED)
                        .show();
                mAdapter.disable();
                bt.isCancelled();
                bt.cancel(true);

                /*Cursor res = myDB.getAllData();
                if(res.getCount() == 0){
                    showMessage("Error","Nothinyrjyrjg Found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                StringTokenizer st;
                while(res.moveToNext()){

                    st=new StringTokenizer(res.getString(0));
                    buffer.append(st.nextToken().toString()+"\t"+st.nextToken().toString()+"\t"+st.nextToken().toString()+"\n");
                }
                showMessage("Data Values",buffer.toString());*/
                purify(myDB);
                Cursor cur = myDB.getLastRes();
                if(cur.getCount() == 0){
                    showMessage("Error","Nothing Found");
                    return;
                }
                while(cur.moveToNext()) {
                    vals[0] = cur.getString(1);
                    vals[1] = cur.getString(2) + " km";
                    vals[2] = cur.getString(3) + " h";
                    vals[3] = cur.getString(4) + " km/h";
                    vals[4] = cur.getString(5) + " km/h";
                    vals[5] = cur.getString(6) + " l";
                    vals[6] = cur.getString(7) + " l";
                    vals[7] = cur.getString(8) + " km/l";
                    v1.setText(vals[0]);
                    v2.setText(vals[1]);
                    v3.setText(vals[2]);
                    v4.setText(vals[3]);
                    //v5.setText(vals[4]);
                    v6.setText(vals[5]);
                    v7.setText(vals[6]);
                    v8.setText(vals[7]);
                }
            }
        });

        /*see_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cur = myDB.getLastRes();
                if(cur.getCount() == 0){
                    showMessage("Error","Nothing Found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(cur.moveToNext()){
                    vals[0]=cur.getString(1);
                    vals[1]=cur.getString(2)+" km";
                    vals[2]=cur.getString(3)+" h";
                    vals[3]=cur.getString(4)+" km/h";
                    vals[4]=cur.getString(5)+" km/h";
                    vals[5]=cur.getString(6)+" l";
                    vals[6]=cur.getString(7)+" l";
                    vals[7]=cur.getString(8)+" km/l";
                    v1.setText(vals[0]);
                    v2.setText(vals[1]);
                    v3.setText(vals[2]);
                    v4.setText(vals[3]);
                    //v5.setText(vals[4]);
                    v6.setText(vals[5]);
                    v7.setText(vals[6]);
                    v8.setText(vals[7]);
                }
            }

        });*/

        geoact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent geo = new Intent(MainActivity.this,FuelActivity.class);
                Cursor cur = myDB.getLastRes();
                while(cur.moveToNext()) {
                    fuel_send = cur.getString(8);
                }
                if(fuel_send.equalsIgnoreCase(" ")){
                    Snackbar.make(findViewById(android.R.id.content), "No previously recorded Fuel value there", Snackbar.LENGTH_LONG)
                            .setAction("Undo", mOnClickListener)
                            .setActionTextColor(Color.RED)
                            .show();
                }else {
                    geo.putExtra("Fuel_Level", fuel_send);
                    startActivity(geo);
                }
            }
        });



    }

    private void changetosummary(DatabaseHelper myDB) {
        String average="",dist="",fuel="";
        Double distn=0.0,fuel_cons=0.0,avg=0.0;
        Cursor res=myDB.getResCursor();
        int cnt=0;

        if(res.getCount()==0)
        {
            showMessage("Error","No result found");
            return;
        }

        while(res.moveToNext())
        {
            distn=distn+Double.parseDouble(res.getString(2));
            fuel_cons=fuel_cons+Double.parseDouble(res.getString(8));
            cnt++;
        }
        avg=distn/fuel_cons;
        avg=Double.parseDouble(String.format("%.3f", avg));
        distn=Double.parseDouble(String.format("%.3f", distn));
        fuel_cons=Double.parseDouble(String.format("%.3f", fuel_cons));
        v1.setText("Summary");
        l8.setText("Overall Average :");
        l7.setText("Total Distance Covered");
        l6.setText("Total Fuel Consumed");
        v8.setText(avg.toString() + " km/l");
        v7.setText(distn.toString() + " km");
        v6.setText(fuel_cons.toString()+ " l");


        //Hiding the rest
        //l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);
        l4.setVisibility(View.GONE);
        //l5.setVisibility(View.GONE);
        v4.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        //v5.setVisibility(View.GONE);






    }

    private void displayValues(Cursor cur) {
        vals[0]=cur.getString(1);
        vals[1]=cur.getString(2)+" km";
        vals[2]=cur.getString(3)+" h";
        vals[3]=cur.getString(4)+" km/h";
        vals[4]=cur.getString(5)+" km/h";
        vals[5]=cur.getString(6)+" l";
        vals[6]=cur.getString(7)+" l";
        vals[7]=cur.getString(8)+" km/l";
        v1.setText(vals[0]);
        v2.setText(vals[1]);
        v3.setText(vals[2]);
        v4.setText(vals[3]);
        v5.setText(vals[4]);
        v6.setText(vals[5]);
        v7.setText(vals[6]);
        v8.setText(vals[7]);
    }

    private void checkBluetoothOn() {

        if (!mAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 3);
        }
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("key");
            while(message != null) {
                //tvStatus.setText(message);
                break;
            }
        }
    };

    public void showMessage(String title,String message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void purify(DatabaseHelper myDB)
    {
        myDB=new DatabaseHelper(this);
        StringTokenizer st2;
        Cursor res = myDB.getAllData();
        if(res.getCount()==0)
            return;
        String snow;
        int ts = 1,  initTime;
        Double d=0.0,  f = 0.0,  initDistCov, initFuelLevel, totdist = 0.0;
        Date now = new Date();

        String date = sdf1.format(now);
        do{
            res.moveToNext();
            if(isPerfect(res.getString(0)))
                break;
        }while(true);
        st2 = new StringTokenizer(res.getString(0), " ");
        initTime = Integer.parseInt(st2.nextToken().toString());
        initDistCov = Double.parseDouble(st2.nextToken().toString());
        initFuelLevel = Double.parseDouble(st2.nextToken().toString());
        initFuelLevel=initFuelLevel-12.0;
        initFuelLevel=initFuelLevel*(-1.0);
        while(res.moveToNext()) {
            if(res.getString(0)==null)
                showMessage("Error","No trip recorded");
            else {
                snow = res.getString(0).trim();
                if (isPerfect(snow) == FALSE)
                    continue;
                else {
                    st2 = new StringTokenizer(snow, " ");
                    ts = Math.abs(Integer.parseInt(st2.nextToken().toString()));
                    d = Double.parseDouble(st2.nextToken().toString());
                    f = Double.parseDouble(st2.nextToken().toString());
                    f=f-12.0;
                    f=f*(-1.0);
                }
            }
        }
        initFuelLevel=getFuel(initFuelLevel);
        f=Double.parseDouble(String.format("%.3f",Math.abs(getFuel(f))));
        Double Distance_Covered=((d-initDistCov)/1000);
        Distance_Covered=Double.parseDouble(String.format("%.3f", Distance_Covered));
        Double Time_Taken=Math.abs((ts-initTime)/3600000.0);
        Time_Taken=Double.parseDouble(String.format("%.3f", Time_Taken));
        Double Avg_Speed=Distance_Covered/Time_Taken;
        Avg_Speed=Double.parseDouble(String.format("%.3f", Avg_Speed));
        Double Fuel_consumed=(initFuelLevel-f+.001);
        Fuel_consumed=Double.parseDouble(String.format("%.3f", Fuel_consumed));
        Double Fuel_avg=Distance_Covered/Fuel_consumed;
        Fuel_avg=Double.parseDouble(String.format("%.3f", Fuel_avg));
        myDB.insertDataRes(date,Distance_Covered,Time_Taken,Avg_Speed,0.0,f,Fuel_consumed,Fuel_avg);


            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setContentTitle("Post Trip Fuel")
                            .setContentText("Fuel level is : "+ f +" Litres")
                            .setSmallIcon(R.drawable.ic_action_name);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,Intent.FILL_IN_DATA);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public Double getFuel(Double f)
    {
        Double vals[]={4.34,3.5,2.4,2.24,2.0,1.8,1.62,1.51,1.15,0.8,0.45,0.1,0.0};
        Double minDist=12.0,ret=0.0;
        for (int i=1;i<13;i++)
        {
            if(f>=4.34)
                return 0.0;
            else if((f-vals[i])<=minDist&&(f-vals[i])>0)
            {
                ret=((i-1)+(f-vals[i-1])/(vals[i]-vals[i-1]));
                minDist=(f-vals[i]);
            }
        }
        return ret;
    }

    public boolean isPerfect(String s)
    {
        StringTokenizer st2;
        st2=new StringTokenizer(s," ");
        boolean val=TRUE;
        if(st2.countTokens()!=3)
            return FALSE;
        try
        {
            Integer.parseInt(st2.nextToken().toString());
        }
        catch (NumberFormatException e)
        {
            val=FALSE;
        }
        try
        {
            Double.parseDouble(st2.nextToken().toString());
        }
        catch (NumberFormatException e)
        {
            val=FALSE;
        }
        try
        {
            Double.parseDouble(st2.nextToken().toString());
        }
        catch (NumberFormatException e)
        {
            val=FALSE;
        }
        return val;
    }

    public void CreatecardView(){

    }
    class BluetoothTask extends AsyncTask<Void, Void, Void> {
        private int REQUEST_ENABLE_BT = 3;
        BluetoothSocket mmSocket;
        BluetoothDevice mdevice;
        InputStream mmInputStream;

        private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        String data, finaldata = " ", address = "00:21:13:00:34:E0";
        StringTokenizer st;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("AsyncTask", "onPreExecute");

        }

        @Override
        protected Void doInBackground(Void... params) {
            //Bluetooth Code
            //Bluetooth Code

            int bytes;
            byte[] buffer = new byte[1024];
            // SPP UUID service - this should work for most devices
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            mdevice = mAdapter.getRemoteDevice(address);

            try {
                mmSocket = mdevice.createRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mmSocket.connect();
                Snackbar.make(findViewById(android.R.id.content), "Recording has Started", Snackbar.LENGTH_LONG)
                        .setAction("Undo", mOnClickListener)
                        .setActionTextColor(Color.RED)
                        .show();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mmInputStream = mmSocket.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Toast.makeText(this, "The Recording Service has started", Toast.LENGTH_LONG).show();


            try {
//                DataInputStream dinput = new DataInputStream(mmInputStream);
                do {
                    bytes = mmInputStream.read(buffer);//read bytes from input buffer
                    data = new String(buffer, 0, bytes);
                    st = new StringTokenizer(data, "#");
                    while (st.hasMoreTokens()) {
                        myDB.insertData(st.nextToken().toString());
                    }
                    Thread.sleep(1000);
                    buffer = new byte[1024];
                    if(isCancelled()) {
                        break;
                    }
                }
                while(true);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            //displayValues(myDB.getLastRes());
            return null;
        }

        @Override
        protected void onCancelled() {
            BluetoothTask bt = new BluetoothTask();
            bt.cancel(true);
            super.onCancelled();
        }

    }


}





