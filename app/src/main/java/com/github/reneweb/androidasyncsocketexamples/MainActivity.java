package com.github.reneweb.androidasyncsocketexamples;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;
import com.github.reneweb.androidasyncsocketexamples.tcp.Server;


public class MainActivity extends ActionBarActivity {



    private EditText recieve_data;
    private String data_string;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv_server = (TextView) findViewById(R.id.textView);


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                data_string = "haloooooooo";
                //TCP client and server (Client will automatically send welcome message after setup and server will respond)
//                Server server = new Server("localhost", 7000 , tv_server);
//
//                Client client = new Client("localhost", 7000,data_string);


                //UDP client and server (Here the client explicitly sends a message)
                new com.github.reneweb.androidasyncsocketexamples.udp.Server("localhost", 7001);
//                new com.github.reneweb.androidasyncsocketexamples.udp.Client("localhost", 7001).send(data_string);
                return null;
            }

        }.execute();
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

    public void onClick(View view) {

        recieve_data = (EditText) findViewById(R.id.editText);
        data_string = recieve_data.getText().toString();
        System.out.println("[Debug]" + data_string);
    }
}
