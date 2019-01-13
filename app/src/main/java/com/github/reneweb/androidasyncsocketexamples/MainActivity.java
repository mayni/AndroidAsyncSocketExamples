package com.github.reneweb.androidasyncsocketexamples;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText message, ipaddress, port;
    String text;
    private static MainActivity instance;
    Button onwork, directcontrol, calibratework, rightwork, leftwork, bothwork ,checkmodework , clearwork ,readpressurework ,emergencywork , detail;

    String PRESSURE_SIDE = "0000",PRESSURE_MAIN = "0000";

    private RecyclerView recyclerView, recyclerViewRec ;
    private MainAdapter adapter, adapterRec ;
    private List<BaseItem> itemList, itemListRec ;

    private int hour, min;
    Calendar calendar;


    private static final int sizeOfIntInHalfBytes = 8;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    String[] CLUBS = {"45 minute","1 hour","1 hour 30 minute","2 hours"};
    String [] BITS = {"main pump","side pump","Valve main left","Valve main right","Valve side left", "Valve side right"};

    boolean[] Checkbit = new boolean[]{false, false, false, false, false, false};
    WifiManager wifi;
    String mSelected;
    Integer time = 0;

    String closedPopup = "0";

    ///// ------------------------------------ NETWORK CREDENTIALS
    String networkSSID = "TP-Link_1316";
    String networkPass = "60177094";
//    String networkSSID = "nanearnano";
//    String networkPass = "yok37491";
    WifiManager wifiManager;

    Integer hourTime=0,minuteTime =0;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        rightwork = findViewById(R.id.rightwork);
        leftwork =  findViewById(R.id.leftwork);
        bothwork =  findViewById(R.id.bothwork);
        directcontrol = findViewById(R.id.directcontrol);
        checkmodework = findViewById(R.id.checkmode);
        clearwork = findViewById(R.id.clear);
        detail = findViewById(R.id.detail);
        readpressurework = findViewById(R.id.readpressure);
        emergencywork = findViewById(R.id.emergency);
        calibratework = findViewById(R.id.calibrate);
        onwork = findViewById(R.id.onwork);

        rightwork.setOnClickListener(this);
        leftwork.setOnClickListener(this);
        bothwork.setOnClickListener(this);
        checkmodework.setOnClickListener(this);
        clearwork.setOnClickListener(this);
        readpressurework.setOnClickListener(this);
        emergencywork.setOnClickListener(this);
        calibratework.setOnClickListener(this);
        directcontrol.setOnClickListener(this);
        onwork.setOnClickListener(this);





        message =  findViewById(R.id.message);



        instance = this;

        itemList = new ArrayList<>();
        itemListRec = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MainAdapter();

//        recyclerViewRec = findViewById(R.id.recyclerView1);
//        recyclerViewRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        adapterRec = new MainAdapter();


        Intent intent = getPackageManager().getLaunchIntentForPackage("com.exatools.sensors");

        wifi =(WifiManager)
        getApplicationContext().
        getSystemService(Context.WIFI_SERVICE);
            if(!wifi.isWifiEnabled())

        {
            Toast.makeText(getApplicationContext(),
                    "wifi is disabled..making it enabled", Toast.LENGTH_LONG)
                    .show();
            wifi.setWifiEnabled(true);
        }

        ///// ---------------------------- Connecting Code
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID ="\""+networkSSID +"\"";

        conf.preSharedKey ="\""+networkPass +"\"";

        wifiManager =(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(wifiManager !=null)

        {
            wifiManager.addNetwork(conf);
        }
        if(getIntent().hasExtra("closed")){
                closedPopup = getIntent().getStringExtra("closed");
        }

        if (closedPopup.equals("0")){
//            dialogConnect();
        }





    }

    private void dialogConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Connect Wifi " + networkSSID);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                for( WifiConfiguration i : list ) {
                    if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                        wifiManager.disconnect();
                        wifiManager.enableNetwork(i.networkId, true);
                        wifiManager.reconnect();

                        break;
                    }
                }
            }
        });
        builder.show();

    }
    public void readFile(){
        try {
            FileInputStream fileIn=openFileInput("Pressure.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            BufferedReader br = new BufferedReader(InputRead);
            StringBuilder sb = new StringBuilder();
            String text;
            String ab = null;
            while ((text = br.readLine()) != null){
                System.out.println("[Pressure]" + text);
                ab = text;
            }


            InputRead.close();
            ArrayList<String> press = new ArrayList<>();
            for(String str : ab.split("\\s") ){
                press.add(str);
            }
            PRESSURE_SIDE = press.get(2);
            PRESSURE_MAIN = press.get(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }
    protected void onStart(){
        super.onStart();
        System.out.println("Starttttttttttttttttttttttttt" + text);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected  void onResume(){
        super.onResume();

    }
    protected void onStop(){
        super.onStop();

        System.out.println("Stopppppppppppp" + text);
    }
    protected void onDestroy(){
        super.onDestroy();
        System.out.println("Destroyyyyyyyyyyyyyyyyyyyyyy");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void ClickToConnect(View view) {
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        message = (EditText) findViewById(R.id.message);
        final String m = message.getText().toString();
        ipaddress = (EditText) findViewById(R.id.ip);
        final String ip = ipaddress.getText().toString();
        port = (EditText) findViewById(R.id.port);
        final int p = Integer.parseInt(port.getText().toString());

        final String dateSend = dateFormat.format(date);

        itemList.add(0,new CardViewItem()
                .setText(m,dateFormat.format(date),"",""));
        adapter.setItemList(itemList);
        recyclerView.setAdapter(adapter);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Client client = new Client(ip, p,m);
                client.setListener(new Client.clientMessageRecListener() {
                    @Override
                    public void recMessage(final String mes) {
                        new AsyncTask<Void, Void, String>(){
                            @Override
                            protected String doInBackground(Void... voids) {
                                System.out.println("[Main] rec" + mes.trim());
                                return String.valueOf(mes.trim());
                            }
                            @Override
                            protected void onPostExecute(String mes) {
                                final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                Date date = new Date();
                                super.onPostExecute(mes);
                                itemList.set(0,new CardViewItem().setText(m,dateSend,mes,dateFormat.format(date)));

                                adapter.setItemList(itemList);
                                recyclerView.setAdapter(adapter);
//                                itemListRec.add(0,new CardViewItem()
//                                        .setText1(dateFormat.format(date),mes));
//                                adapterRec.setItemList(itemListRec);
//                                recyclerViewRec.setAdapter(adapterRec);
                            }
                        }.execute();

                        System.out.println("[Main]"+mes);
                    }

                    @Override
                    public void checkConnection(Exception e) {

                    }
                });
                return null;
            }



        }.execute();


        message.setText("");
    }


    @Override
    public void onClick(final View view) {
        Integer id = view.getId();
        if(view.getId() == rightwork.getId() || view.getId() == leftwork.getId() || view.getId() == bothwork.getId()){
            readFile();
            setTime(id);

        }else {
            if(view.getId() == checkmodework.getId()){
                String checkmode = "0A FF FF";
                message.setText(checkmode);
            }else if(view.getId() == clearwork.getId()){
                String clear = "0A 00 00";
                message.setText(clear);
            }else if(view.getId() == readpressurework.getId()){
                Intent intent = new Intent(MainActivity.this,PressureActivity.class);
                startActivity(intent);
            }else if(view.getId() == emergencywork.getId()){
                String emerg = "EE";
                message.setText(emerg);
            }else if(view.getId() == calibratework.getId()){
                Intent intent = new Intent(this,CalibrateActivity.class);
                startActivity(intent);
            }else if(view.getId() ==  directcontrol.getId()){
                setDirectControl();
            }
        }

        if (view.getId() == detail.getId() ){
            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            System.out.println("---------------------");
            String min = Integer.toString(time/60);
            intent.putExtra("time",min);
            startActivity(intent);
        }
        if(view.getId() == onwork.getId()){
            String on = "0B 01 FF 00 0000 00 0000";
            message.setText(on);
        }

    }

    private void setDirectControl() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Set Direct Control");

        builder.setMultiChoiceItems(BITS, Checkbit, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Checkbit[which] = isChecked;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double res = 0;
                Integer plus = 2;
                int count = 0;
                for(boolean chbit : Checkbit){
                    if(chbit == false){
                        count++;
                    }
                }
                if(count != Checkbit.length){
                    for(int i=0;i<Checkbit.length;i++){
                        if(Checkbit[i] == true){
                            if(i>=2) {
                                res += Math.pow(2,i+plus);
                            }else{
                                res += Math.pow(2,i);
                            }
                        }
                    }
                    Integer dec = (int) res;
                    String Hex = decToHex(dec);
                    message.setText("02 " +  Hex.substring(6) + " FF");
                }else{
                    message.setText("");
                }

            }
        });

        builder.setNeutralButton("Stop All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                message.setText("02 00 FF");
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.create();
        builder.show();

    }


    private void setTime(final Integer id) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View theView = inflater.inflate(R.layout.time_picker, null);

        NumberPicker time = theView.findViewById(R.id.time1);
        NumberPicker time1 = theView.findViewById(R.id.time2);

        time.setMaxValue(2);
        time.setMinValue(0);


        time1.setMaxValue(59);
        time1.setMinValue(0);





        time.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
                hourTime = newVal;
            }
        });
        time1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
                minuteTime = newVal;
            }
        });
        builder.setTitle("Set time");
        builder.setView(theView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            int timeTime = (hourTime*60*60)+(minuteTime*60);
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(id == rightwork.getId()){
                    String right = "0A 04 FF 82 012C 03FF 03FF 93 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600).substring(4)+" 0000 0000";
                    message.setText(right);
                }else if(id == leftwork.getId()){
                      String left = "0A 04 FF 42 012C 03FF 03FF 63 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600).substring(4)+" 0000 0000";
                      message.setText(left);
                }else if(id == bothwork.getId()){
                      String both = "0A 08 FF 42 012C 03FF 03FF 63 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600)+" 0000 0000"+" 82 012C 03FF 03FF 93 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600)+" 0000 0000";
                    message.setText(both);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL",null);
        builder.create();
        builder.show();




//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Set Time");
//        builder.setSingleChoiceItems(CLUBS, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                mSelected = CLUBS[which];
//                switch (mSelected){
//                    case "45 minute":
//                        time = 60*45;
//                        break;
//                    case "1 hour":
//                        time = 60*60;
//                        break;
//                    case "1 hour 30 minute":
//                        time = (60*30) + (60*60);
//                        break;
//                    case "2 hours":
//                        time = (120 * 60);
//                        break;
//                }
//                if(id == rightwork.getId()){
//                    String right = "0A 04 FF 82 012C 03FF 03FF 93 "+decToHex(time-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(time-600).substring(4)+" 0000 0000";
//                    message.setText(right);
//                }else if(id == leftwork.getId()){
//                      String left = "0A 04 FF 42 012C 03FF 03FF 63 "+decToHex(time-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(time-600).substring(4)+" 0000 0000";
//                      message.setText(left);
//                }else if(id == bothwork.getId()){
//                      String both = "0A 08 FF 42 012C 03FF 03FF 63 "+decToHex(time-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(time-600)+" 0000 0000"+" 82 012C 03FF 03FF 93 "+decToHex(time-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(time-600)+" 0000 0000";
//                    message.setText(both);
//                }
//            }
//        });
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton("CANCEL", null);
//        builder.create();
//        builder.show();
////        "45 minute","1 hour","1 hour 30 minute","2 hours"


    }


    public static String decToHex(int dec) {
        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
        {
            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= numberOfBitsInAHalfByte;
        }
        return hexBuilder.toString();
    }







}
