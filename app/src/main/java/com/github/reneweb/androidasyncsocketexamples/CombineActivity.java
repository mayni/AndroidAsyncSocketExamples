package com.github.reneweb.androidasyncsocketexamples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CombineActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
//    private Toolbar toolbar;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
    private TextView status;
    private Button findIp;
    private EditText ip,port;
    final List<String> lists = new ArrayList<String>();

    WifiManager wifi;
    WifiManager wifiManager;
    String networkSSID = "DESKTOP-5HK7N4N 2635";
    String networkPass = "580610684";

    public IpAndPortListener listener;
    String it;
    private String[] ipAddr;
    public void setListener(IpAndPortListener listener){
        this.listener = listener;
    }

    public interface IpAndPortListener{
        void ipAndportChange(String ip);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine);
        System.out.println("[Main] : createeeeeee");




        status = findViewById(R.id.statusBed);
        findIp = findViewById(R.id.findIp);
        findIp.setOnClickListener(this);
        ip = findViewById(R.id.ipBed);
        port = findViewById(R.id.port);


        wifi =(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Timer myTimer;
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            public void run() {
                checkWifi();
                System.out.println("[Main]: startttt connect");
            }
        }, 0, 600000);

        if (getConnection()){
            getipAddress();
        }


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViewPager();



      

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected  void onResume(){
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);


    }
    private void setupViewPager() {
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("USER"));
        tabLayout.addTab(tabLayout.newTab().setText("TEST"));
        tabLayout.addTab(tabLayout.newTab().setText("SETTING"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showConnected(isConnected);
    }

    private void showConnected(boolean isConnected) {
//        if (isConnected) {
//            wifiStatus.setText("Disconnect");
//            wifibtn.setText("connect");
//        } else {
//            wifiStatus.setText("Connected");
//            wifibtn.setText("disconnect");
//
//        }
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnected(isConnected);
    }


    private boolean getConnection(){
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == findIp.getId()){
            dialogIp();
        }
    }




    public void checkWifi(){
        System.out.println("[Main] : checkwiiiiifiiii");
//        thread = new Thread() {
//            @Override
//            public void run() {
//
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                String portStr = port.getText().toString();
                                int portNumber = Integer.parseInt(portStr);
                                Client client = new Client(ip.getText().toString(), portNumber, "0A FF FF");
                                client.setListener(new Client.clientMessageRecListener() {
                                    @Override
                                    public void recMessage(String mes) {

                                    }

                                    @Override
                                    public void checkConnection(Exception e) {

                                    }

                                    @Override
                                    public void checkWifi(Exception e) {
                                        final Exception err = e;
                                        final String[] mes = {null};
                                        new AsyncTask<Void, Void, String>() {
                                            @Override
                                            protected String doInBackground(Void... voids) {
                                                if (err != null) {
                                                    mes[0] = "Disconnect";

                                                } else {
                                                    mes[0] = "Connected";
                                                }
                                                return mes[0];
                                            }

                                            @Override
                                            protected void onPostExecute(String mes) {
                                                super.onPostExecute(mes);
                                                status.setText(mes);
                                                status.setTextColor((mes == "Connected" ? getColor(R.color.lightGreen): getColor(R.color.red)));
                                            }
                                        }.execute();
                                    }
                                });
                                return null;
                            }
                        }.execute();

//                }
//
//
//        };
    }

    private void dialogIp(){

        System.out.println("[Main] : ipAddr" + ipAddr);

        final AlertDialog.Builder builder = new AlertDialog.Builder(CombineActivity.this);
        builder.setTitle("Select ipAddress");
        if (ipAddr.length == 0 ){
            builder.setMessage("\nNot found Ip\n");
        }else {
            builder.setItems(ipAddr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String selected = ipAddr[which];
                    Toast.makeText(getApplicationContext(),
                            selected, Toast.LENGTH_LONG).show();
                    ip.setText(selected);
                }
            });
        }

        builder.setNeutralButton("Scan ip again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getipAddress();
            }
        });
        builder.setNegativeButton("cancel", null);
        builder.create();

        builder.show();
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
        System.out.println("[Main] : "+ipAddress);

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
//                                    System.out.println("[Main] : Lists available "+ lists);
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
                Toast.makeText(CombineActivity.this, "Successful Scan ip!", Toast.LENGTH_LONG).show();
                findViewById(R.id.findIp).setEnabled(true);
            }

        }.execute();

        System.out.println("[Main] : Lists ipAddress " + lists);


    }
    public String sendData() {
        return ip.getText().toString();
    }


}
