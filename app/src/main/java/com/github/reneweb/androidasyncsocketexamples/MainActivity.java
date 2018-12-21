package com.github.reneweb.androidasyncsocketexamples;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,ConnectivityReceiver.ConnectivityReceiverListener {

    final List<String> lists = new ArrayList<String>();
    private EditText message, ipaddress, port;
    String text,selectIp;
    private static MainActivity instance;
    Button directcontrol, calibratework, rightwork, leftwork, bothwork ,checkmodework , clearwork ,readpressurework ,emergencywork , detail,
    wifibtn;
    String PRESSURE_SIDE = "0000",PRESSURE_MAIN = "0000";
    TextView wifiStatus,bedStatus;

    private RecyclerView recyclerView, recyclerViewRec ;
    private MainAdapter adapter, adapterRec ;
    private List<BaseItem> itemList, itemListRec ;


    private static final int sizeOfIntInHalfBytes = 8;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    String[] CLUBS = {"45 minute","1 hour","1 hour 30 minute","2 hours"};
    String [] BITS = {"main pump","side pump","Valve main left","Valve main right","Valve side left", "Valve side right"};
    String[] ipAddr;
    boolean[] Checkbit = new boolean[]{false, false, false, false, false, false};
    WifiManager wifi;
    String mSelected;
    Integer time = 0;

    String closedPopup = "0";
    private List<ScanResult> wifiList;
    String[] val;

    ///// ------------------------------------ NETWORK CREDENTIALS
    String networkSSID = "TP-Link_1316";
    String networkPass = "60177094";
//    String networkSSID = "DESKTOP-5HK7N4N 2635";
//    String networkPass = "580610684";
//    String networkSSID = "nanearnano";
//    String networkPass = "yok37491";
    WifiManager wifiManager;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rightwork = (Button) findViewById(R.id.rightwork);
        leftwork = (Button) findViewById(R.id.leftwork);
        bothwork = (Button) findViewById(R.id.bothwork);
        directcontrol = findViewById(R.id.directcontrol);
        checkmodework = findViewById(R.id.checkmode);
        clearwork = findViewById(R.id.clear);
        detail = findViewById(R.id.detail);
        readpressurework = findViewById(R.id.readpressure);
        emergencywork = findViewById(R.id.emergency);
        calibratework = findViewById(R.id.calibrate);

        wifibtn = findViewById(R.id.wifiBtn);
        wifiStatus = findViewById(R.id.wifiSatus);
        bedStatus = findViewById(R.id.status);

        rightwork.setOnClickListener(this);
        leftwork.setOnClickListener(this);
        bothwork.setOnClickListener(this);
        checkmodework.setOnClickListener(this);
        clearwork.setOnClickListener(this);
        readpressurework.setOnClickListener(this);
        emergencywork.setOnClickListener(this);
        calibratework.setOnClickListener(this);
        directcontrol.setOnClickListener(this);

        ipaddress = (EditText) findViewById(R.id.ip);


        port = (EditText) findViewById(R.id.port);


        message = (EditText) findViewById(R.id.message);

        instance = this;

        itemList = new ArrayList<>();
        itemListRec = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MainAdapter();

//        recyclerViewRec = findViewById(R.id.recyclerView1);
//        recyclerViewRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        adapterRec = new MainAdapter();

        if (getConnection()){
            wifibtn.setText("disconnect");
            wifiStatus.setText("Connected");
            bedStatus.setText("Connected");
            bedStatus.setTextColor(Color.GREEN);
            findViewById(R.id.sending).setEnabled(true);
        }else {
            wifibtn.setText("connect");
            findViewById(R.id.sending).setEnabled(false);
            wifiStatus.setText("Disconnect");
            bedStatus.setText("Disconnect");
            bedStatus.setTextColor(Color.RED);
        }

//        checkConnection();
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.exatools.sensors");

        wifi =(WifiManager)
        getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        getipAddress();


//
//        if(!wifi.isWifiEnabled()) {
//            Toast.makeText(getApplicationContext(),
//                    "wifi is disabled..making it enabled", Toast.LENGTH_LONG)
//                    .show();
//            wifi.setWifiEnabled(true);
//        }
//
//        ///// ---------------------------- Connecting Code
//        WifiConfiguration conf = new WifiConfiguration();
//        conf.SSID ="\""+networkSSID +"\"";
//        conf.preSharedKey ="\""+networkPass +"\"";
//        wifiManager =(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        if(wifiManager !=null) {
//            wifiManager.addNetwork(conf);
//        }

//        checkConnection();
//        if(getIntent().hasExtra("closed")){
//                closedPopup = getIntent().getStringExtra("closed");
//        }
//        if (closedPopup.equals("0")){
//            dialogConnect();
//        }

    }

    private void dialogIp(){
//        getipAddress();
//        for (int i=0;i< lists.size(); i++){
//            ipAddr[i] = lists.get(i);
//        }
        System.out.println("[Main] : ipAddr" + ipAddr);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Mattress's ipAddress");
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        builder.setItems(ipAddr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = ipAddr[which];
                Toast.makeText(getApplicationContext(),
                        selected, Toast.LENGTH_LONG).show();
                ipaddress.setText(selected);
            }
        });
        builder.setNegativeButton("cancel", null);
        builder.create();
// สุดท้ายอย่าลืม show() ด้วย
        builder.show();
    }




    private void dialogConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Connect Wifi " + networkSSID);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                for (WifiConfiguration i : list) {
                    if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
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
        MyApplication.getInstance().setConnectivityListener(this);
//        checkConnection();

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
        checkConnection();

        System.out.println("Stopppppppppppp" + text);
    }
    protected void onDestroy(){
        super.onDestroy();
        System.out.println("Destroyyyyyyyyyyyyyyyyyyyyyy");

    }

    protected void onPause(){
        super.onPause();
        checkConnection();
        System.out.println("Pauseeeeedddddddddd" + text);
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


                                if (mes.equals("disconnect")){
                                    bedStatus.setText("Disconnect");
                                    bedStatus.setTextColor(Color.RED);
                                    findViewById(R.id.sending).setEnabled(false);
                                }
//                                else if (mes.equals("portWrong")){
//                                    bedStatus.setText("Disconnect");
//                                    bedStatus.setTextColor(Color.RED);
//                                    findViewById(R.id.sending).setEnabled(false);
//                                }else if (mes.equals("portCorrect")){
//                                    bedStatus.setText("Connected");
//                                    bedStatus.setTextColor(Color.GREEN);
//                                    findViewById(R.id.sending).setEnabled(true);
//                                }
                                else {
                                    bedStatus.setText("Connected");
                                    findViewById(R.id.sending).setEnabled(true);
                                    bedStatus.setTextColor(getColor(R.color.lightGreen));
                                    itemList.set(0,new CardViewItem().setText(m,dateSend,mes,dateFormat.format(date)));

                                    adapter.setItemList(itemList);
                                    recyclerView.setAdapter(adapter);
    //                                itemListRec.add(0,new CardViewItem()
    //                                        .setText1(dateFormat.format(date),mes));
    //                                adapterRec.setItemList(itemListRec);
    //                                recyclerViewRec.setAdapter(adapterRec);
                                }
                               
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
        }else {
            if(view.getId() == checkmodework.getId()){
                String checkmode = "0A FF FF";
                message.setText(checkmode);
            }else if(view.getId() == clearwork.getId()){
                String clear = "0A 00 00";
                message.setText(clear);
            }else if(view.getId() == readpressurework.getId()){
                String readpress = "01";
                message.setText(readpress);
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
        if (view.getId() == wifibtn.getId()){

                if (!getConnection()){
                    System.out.println("Clicked connect");
                findViewById(R.id.sending).setEnabled(true);
                findViewById(R.id.findIp).setEnabled(true);
                connectWifi();
                checkConnection();
//                    try {
//                        Thread.sleep(2500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    getipAddress();



            }
            else {
                wifi.setWifiEnabled(false);
                findViewById(R.id.sending).setEnabled(false);
                findViewById(R.id.findIp).setEnabled(false);
                checkConnection();
//                ipAddr = new String[0];

            }
        }
        if (view.getId() == findViewById(R.id.findIp).getId()){
            System.out.println("[Main] : click detail");
//            getipAddress();

            dialogIp();
            System.out.println("[Main] : " + lists);
//            ipaddress.setText(lists.get(0));
        }

    }
    private void setDirectControl() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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


//                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.create();
        builder.show();

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
                    String right = "0A 04 FF 82 0005 03FF 03FF 93 "+decToHex(time-5).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0010 0000 0000 00 "+decToHex(time-10).substring(4)+" 0000 0000";
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

    private void connectWifi(){
        if(!wifi.isWifiEnabled()) {
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

        if(wifiManager !=null) {
            wifiManager.addNetwork(conf);
        }



//        wifi.startScan();
//        for (ScanResult sx : wifi.getScanResults()) {
//            System.out.println("Point - " + sx);
//        }

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
        checkConnection();
        if (!getConnection()){
            Toast.makeText(MainActivity.this, "Not wifi", Toast.LENGTH_LONG).show();
        }

    }

    public void getipAddress() {
        wifiManager =(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        final DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

        final String address = Formatter.formatIpAddress(dhcpInfo.ipAddress);
        final int index = address.lastIndexOf(".");
        final String subnet = address.substring(0,index+1);
        System.out.println("[Main] : DHCP "+ subnet +" Detail : " +dhcpInfo);

        final int lower = 0;
        final int upper = 255;
        final int timeout = 1 ;
        final String gateway = Formatter.formatIpAddress(dhcpInfo.gateway);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        final int[] ip = {wifiInfo.getIpAddress()};
        final String ipAddress = Formatter.formatIpAddress(ip[0]);
//        System.out.println("[Main] : "+ipAddress);

//        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//        for( WifiConfiguration i : list ) {
//            System.out.println("[Main] : SSID "+ i + " , ipAddress " );
//            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
//        int ipAddress = connectionInfo.getIpAddress();
//            int ipAddress = (connectionInfo.getIpAddress());
//            String ipString = Formatter.formatIpAddress(ipAddress);
//            System.out.println("[Main] : ipAddress "+ ipString);
//        }
                new AsyncTask<Void, String, List<String>>() {
                    @Override
                    protected List<String> doInBackground(Void... voids) {
                        for (int i = lower; i <= upper; i++) {
                            String host = subnet.concat(String.valueOf(i));

                            try {
                                InetAddress inetAddress = InetAddress.getByName(host);
                                boolean reachable = inetAddress.isReachable(timeout);
                                String hostName = inetAddress.getHostAddress();
                                System.out.println("[Main] : " + host);

                                if (reachable){
                                    if (!host.equals(gateway) ){
                                        if ( !host.equals(address)){
                                            publishProgress(host);
//                                            lists.add(host);
                                            System.out.println("[Main] : Reachable -> "+ hostName + " (" + host +") is Reachable!");
                                            System.out.println("[Main] : Lists available "+ lists);
                                        }
                                    }
                                }
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                       return null;
                    }
                    @Override
                    protected void onProgressUpdate(String... values) {
                        lists.add(values[0]);
                        System.out.println("[Main] : Update "+ values);
                    }

                    @Override
                    protected void onPostExecute(List<String> strings) {

                            ipAddr = new String[lists.size()];
                            for (int i=0; i< lists.size(); i++){
                                ipAddr[i] = lists.get(i);
                            }

                        System.out.println("[Main] : onPostttttt " + lists.toString());
                        Toast.makeText(MainActivity.this, "Successful Scan Ip!", Toast.LENGTH_LONG).show();
                    }

                }.execute();

        System.out.println("[Main] : Lists ipAddress " + lists);

//        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        assert connManager != null;
//        NetworkInfo networkInfo =
////                connManager.getActiveNetworkInfo();
//                connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if (networkInfo.isConnected()) {
//            final WifiManager wifiManager1 = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            final WifiInfo connectionInfo = wifiManager1 != null ? wifiManager1.getConnectionInfo() : null;
//            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
//                ssid = connectionInfo.getSSID();
//            }
//        }

    }




    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showConnected(isConnected);
    }

    private void showConnected(boolean isConnected) {
        if (isConnected) {
            wifiStatus.setText("Disconnect");
            bedStatus.setText("Disconnect");
            bedStatus.setTextColor(Color.RED);
            wifibtn.setText("connect");
        } else {
            wifiStatus.setText("Connected");
            wifibtn.setText("disconnect");
//            bedStatus.setText("Connected");
//            bedStatus.setTextColor(getColor(R.color.lightGreen));

        }
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnected(isConnected);
    }

    private boolean getConnection(){
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }
}


