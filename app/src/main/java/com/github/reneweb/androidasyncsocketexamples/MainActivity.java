package com.github.reneweb.androidasyncsocketexamples;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView status ,textView;
    private EditText ipaddressServer,portServer,messageServer,message,ipaddress,port;
    String text;
    private static MainActivity instance;
    Button rightwork,leftwork,bothwork;

    private RecyclerView recyclerView , recyclerViewRec;
    private MainAdapter adapter , adapterRec;
    private List<BaseItem> itemList , itemListRec;





    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = (TextView) findViewById(R.id.status);


        rightwork = (Button) findViewById(R.id.rightwork);
        leftwork = (Button) findViewById(R.id.leftwork);
        bothwork = (Button) findViewById(R.id.bothwork);


        rightwork.setOnClickListener(this);
        leftwork.setOnClickListener(this);
        bothwork.setOnClickListener(this);

        message = (EditText) findViewById(R.id.message);

        instance = this;

        itemList = new ArrayList<>();
        itemListRec = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MainAdapter();

        recyclerViewRec = findViewById(R.id.recyclerView1);
        recyclerViewRec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapterRec = new MainAdapter();



        System.out.println("Createeeeeeeeeeeeeeeeeeeee");

        Intent intent = getPackageManager().getLaunchIntentForPackage("com.exatools.sensors");
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
//        list.add(0,m);
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

//
//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
//
//        listView = (ListView) findViewById(R.id.listview);
//        listView.setAdapter(itemsAdapter);
//        message.setText("");
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == rightwork.getId()){
            String right = "42 63   00 42 63   00";
            message.setText(right);
        }else if(view.getId() == leftwork.getId()){
            String left = "82 93   00 82 93   00";
            message.setText(left);
        }else if(view.getId() == bothwork.getId()){
            String both = "42 03   00 82 93   00";
            message.setText(both);
        }
    }




}