package com.github.reneweb.androidasyncsocketexamples;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText message, ipaddress, port;
    String text;
    private static MainActivity instance;
    Button rightwork, leftwork, bothwork ,checkmodework , clearwork;
    String PRESSURE_SIDE = "0000",PRESSURE_MAIN = "0000";

    private RecyclerView recyclerView, recyclerViewRec;
    private MainAdapter adapter, adapterRec;
    private List<BaseItem> itemList, itemListRec;

    private static final int sizeOfIntInHalfBytes = 8;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    String[] CLUBS = {"45 minute","1 hour","1 hour 30 minute","2 hours"};
    WifiManager wifi;
    String mSelected;
    Integer time = 0;

    ///// ------------------------------------ NETWORK CREDENTIALS
//    String networkSSID = "TP-Link_1316";
//    String networkPass = "60177094";
    String networkSSID = "nanearnano";
    String networkPass = "yok37491";
    WifiManager wifiManager;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        rightwork = (Button) findViewById(R.id.rightwork);
        leftwork = (Button) findViewById(R.id.leftwork);
        bothwork = (Button) findViewById(R.id.bothwork);
        checkmodework = findViewById(R.id.checkmode);
        clearwork = findViewById(R.id.clear);


        rightwork.setOnClickListener(this);
        leftwork.setOnClickListener(this);
        bothwork.setOnClickListener(this);
        checkmodework.setOnClickListener(this);
        clearwork.setOnClickListener(this);

        message = (EditText) findViewById(R.id.message);



        instance = this;

        itemList = new ArrayList<>();
        itemListRec = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MainAdapter();

        recyclerViewRec = findViewById(R.id.recyclerView1);
        recyclerViewRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterRec = new MainAdapter();


        System.out.println("Createeeeeeeeeeeeeeeeeeeee");
        System.out.println(decToHex(6900));

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

        dialogConnect();

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

    public static MainActivity getInstance() {
        return instance;
    }
    protected void onStart(){
        super.onStart();
        System.out.println("Starttttttttttttttttttttttttt" + text);
    }
    protected  void onResume(){
        super.onResume();

//        ServerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ipaddressServer = (EditText) findViewById(R.id.ip);
//                portServer = (EditText) findViewById(R.id.port);
//                new AsyncTask<String, Void, String>() {
//                    @Override
//                    protected String doInBackground(String... params) {
//                        try {
//                            Server server = new Server(ipaddressServer.getText().toString(), Integer.parseInt(portServer.getText().toString()));
//
//                        try {
//                            text = server.setup();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
////                            new com.github.reneweb.androidasyncsocketexamples.tcp.Server(ipaddressServer.getText().toString(), Integer.parseInt(portServer.getText().toString()));
//
//                        } catch (UnknownHostException e) {
//                            e.printStackTrace();
//                        }
////                //UDP client and server (Here the client explicitly sends a message)
//////                new com.github.reneweb.androidasyncsocketexamples.udp.Server("10.80.121.55", 7001);
//////                new com.github.reneweb.androidasyncsocketexamples.udp.Client("10.80.121.55", 7001).send("haloooo");
//                        return "connected";
//                    }
//                    protected void onPostExecute (String voids){
//                        status.setText(voids);
//                    }
//                }.execute();
//
//            }
//        });


        System.out.println("Resumeeeeeeeeeeeeeeeee");
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
        message = (EditText) findViewById(R.id.message);
        final String m = message.getText().toString();
        ipaddress = (EditText) findViewById(R.id.ip);
        final String ip = ipaddress.getText().toString();
        port = (EditText) findViewById(R.id.port);
        final int p = Integer.parseInt(port.getText().toString());

        itemList.add(0,new CardViewItem()
                .setText(m));
        adapter.setItemList(itemList);
        recyclerView.setAdapter(adapter);


        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Client client = new Client(ip, p ,m);
                client.setListener(new Client.clientMessageRecListener() {
                    @Override
                    public void recMessage(final String mes) {
                        new AsyncTask<Void, Void, String>(){
                            @Override
                            protected String doInBackground(Void... voids) {
                                return String.valueOf(mes);
                            }
                            @Override
                            protected void onPostExecute(String mes) {
                                super.onPostExecute(mes);
                                itemListRec.add(0,new CardViewItem()
                                        .setText(mes));
                                adapterRec.setItemList(itemListRec);
                                recyclerViewRec.setAdapter(adapterRec);
                            }
                        }.execute();
                        System.out.println("[Main]"+mes);
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
            setTime(id);
        }else if(view.getId() == checkmodework.getId() || view.getId() == clearwork.getId()){
            if(view.getId() == checkmodework.getId()){
                String both = "42 03   00 82 93   00";
                message.setText(both);
            }else {
                String clear = "0A 00 00";
                message.setText(clear);
            }
        }

    }


    private void setTime(final Integer id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Set Time");
        builder.setSingleChoiceItems(CLUBS, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelected = CLUBS[which];
                switch (mSelected){
                    case "45 minute":
                        time = 60*45;
                        break;
                    case "1 hour":
                        time = 60*60;
                        break;
                    case "1 hour 30 minute":
                        time = (60*30) + (60*60);
                        break;
                    case "2 hours":
                        time = (120 * 60);
                        break;
                }
                if(id == rightwork.getId()){
                    String right = "0A 04 FF 82 012C 03FF 03FF 93 "+decToHex(time-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(time-600).substring(4)+" 0000 0000";
                    message.setText(right);
                }else if(id == leftwork.getId()){
                      String left = "0A 04 FF 42 012C 03FF 03FF 63 "+decToHex(time-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(time-600).substring(4)+" 0000 0000";
                      message.setText(left);
                }else if(id == bothwork.getId()){
                      String both = "0A 08 FF 42 012C 03FF 03FF 63 "+decToHex(time-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(time-600)+" 0000 0000"+" 82 012C 03FF 03FF 93 "+decToHex(time-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(time-600)+" 0000 0000";
                    message.setText(both);
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.create();
        builder.show();
//        "45 minute","1 hour","1 hour 30 minute","2 hours"


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
