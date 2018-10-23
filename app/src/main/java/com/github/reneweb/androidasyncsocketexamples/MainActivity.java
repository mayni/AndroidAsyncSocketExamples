package com.github.reneweb.androidasyncsocketexamples;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private TextView status;
    private EditText ipaddress,port,message;
    ListView listView;
    private static MainActivity instance;
    List<String> list = new ArrayList<String>();




    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = (TextView) findViewById(R.id.status);
        instance = this;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                //TCP client and server (Client will automatically send welcome message after setup and server will respond)
                try {
                    new com.github.reneweb.androidasyncsocketexamples.tcp.Server("10.80.123.168", 7000);
                    status.setText("connected");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
//
//                //UDP client and server (Here the client explicitly sends a message)
////                new com.github.reneweb.androidasyncsocketexamples.udp.Server("10.80.121.55", 7001);
////                new com.github.reneweb.androidasyncsocketexamples.udp.Client("10.80.121.55", 7001).send("haloooo");
                return null;
            }

        }.execute();

    }
    public static MainActivity getInstance() {
        return instance;
    }
    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("Stopppppppppppp");
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

//        list.add(m);
        list.add(0,m);

        ipaddress = (EditText) findViewById(R.id.ip);
        final String ip = ipaddress.getText().toString();
        port = (EditText) findViewById(R.id.port);
        final int p = Integer.parseInt(port.getText().toString());
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                new com.github.reneweb.androidasyncsocketexamples.tcp.Client(ip, p ,m);

                return null;
            }
        }.execute();
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);

        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(itemsAdapter);
        message.setText("");
    }

//    public void ClickToSend(View view){
//        new AsyncTask<Void,Void,Void>(){
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                return null;
//            }
//        }.execute();
//    }



}
