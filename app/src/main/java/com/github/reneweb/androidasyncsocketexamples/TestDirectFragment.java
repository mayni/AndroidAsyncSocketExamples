package com.github.reneweb.androidasyncsocketexamples;

import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.reneweb.androidasyncsocketexamples.call.CallToTCP;
import com.github.reneweb.androidasyncsocketexamples.tcp.Client;
import com.santalu.emptyview.EmptyView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;


public class TestDirectFragment extends Fragment  {

    EmptyView layoutContentContainer ;
    Thread thread;

    public BackToTestListener listener;
    Button mainPump,sidePump,leftsideValve,leftmainValve,rightmainValve,rightsideValve,reload;
    Toolbar toolbar;

    EditText ipAddress,portNumber;

    String PROCESS = "Process.txt";
    String STATUS_BUTTON="statusbutton.txt";
    private static final int sizeOfIntInHalfBytes = 8;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    int[][] buttonStatus = new int[][]{
            {R.id.main_pump,0 }, //0
            {R.id.side_pump,0 }, //1
            {0,0}, //2
            {0,0}, //3
            { R.id.left_mainValve,0}, //4
            { R.id.right_mainValve,0}, //5
            {R.id.left_sideValve,0 }, //6
            {R.id.right_sideValve,0} //7
    };
    int[] bitButton = new int[]{0b00000001,0b00000010,0,0,0b00010000,0b00100000,0b01000000,0b10000000};

    public TestDirectFragment() {

    }



    public interface BackToTestListener{
        void PressBackButton(boolean bool);
        void Reload(boolean bool);
    }

    public void setListener(BackToTestListener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_direct, container, false);

        ipAddress = getActivity().findViewById(R.id.ipBed);
        portNumber = getActivity().findViewById(R.id.port);
        layoutContentContainer = view.findViewById(R.id.layoutContentContainer);

        setView(view);
        setOnclick(view);
        setOnclickReload();
        layoutContentContainer.showLoading();
        layoutContentContainer.error().setOnClickListener(new tryAgainError(view));

        setWriteStatusButton(view);
        setFileButton(view);

        setToolBar(view);

        return view;
    }

    private void setOnclickReload() {
        reload.setOnClickListener(new OnClickReloadListener());
    }

    private void setWriteStatusButton(final View view) {
        if(fileExist(STATUS_BUTTON) == true){
            clearContent("",STATUS_BUTTON);
        }


        CallToTCP callToTCP = new CallToTCP(ipAddress.getText().toString(),Integer.parseInt(portNumber.getText().toString()),"02 0C 0E");
        callToTCP.setListener(new CallToTCP.CallToTCPListener() {
            @Override
            public void recMessageCallBack(String mes) {
                if(mes.charAt(0) != '['){
                    ArrayList<String> data = new ArrayList<>();
                    for(String a:mes.split("\\s")){
                        data.add(a);
                    }
                    Integer buttonNumber = Integer.parseInt(Integer.toBinaryString(Integer.parseInt(data.get(1),16)));
                    String str = String.format("%08d", buttonNumber);
                    System.out.println("[TestDirectFragment] "+ str );
                    for(int i=0;i<8;i++){
                        buttonStatus[i][1] = Integer.parseInt(String.valueOf(str.charAt(7-i)));
                    }
                    for(int[] btn : buttonStatus){
                        String text = btn[0] +" "+ btn[1];
                        writeTofile(text,STATUS_BUTTON);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            layoutContentContainer.showContent();
                        }
                    });
                    setBtn(view);

                }else{
                    if(isVisible()){
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                layoutContentContainer.showError();
                            }
                        });
                    }


                }
            }
        });





    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void setToolBar(View view) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity.getSupportActionBar().setTitle("Direct Control");
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setNavigationOnClickListener(new onClickListener(view));



    }


    private void setFileButton(View view) {
        if(fileExist(STATUS_BUTTON) == false){
            for(int[] btn: buttonStatus){
                String text = btn[0] +" "+ btn[1];
                writeTofile(text,STATUS_BUTTON);
            }
        }
    }

    private void setBtn(View view) {
        FileInputStream fileIn= null;
        try {
            fileIn = getActivity().openFileInput(STATUS_BUTTON);
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            BufferedReader br = new BufferedReader(InputRead);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null){
                ArrayList<Integer> press = new ArrayList<>();
                for(String str : text.split("\\s") ){
                    press.add(Integer.parseInt(str));
                }
                Button btn = view.findViewById(press.get(0));
                if(press.get(0) != 0){
                    if(press.get(1) == 0){
                        btn.setBackgroundResource(R.drawable.button);
                    }else{
                        btn.setBackgroundResource(R.drawable.button_on);
                    }
                }

            }
            InputRead.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean fileExist(String fname){
        String path = getContext().getFilesDir().getAbsolutePath() + "/" + fname;

        File file = new File(path);
        return file.exists();
    }

    private void setSending(final String msg, final String command, final View view) {
        final String ip = ipAddress.getText().toString();
        final Integer port =Integer.parseInt(portNumber.getText().toString()) ;
        CallToTCP callToTCP = new CallToTCP(ip,port,msg);
        callToTCP.setListener(new CallToTCP.CallToTCPListener() {
            @Override
            public void recMessageCallBack(String mes) {
                System.out.println("[TestDirect] "+mes);
                writeTofile(command,PROCESS);
                writeTofileUpdateProcess(command,STATUS_BUTTON,view);
            }
        });

    }
    private void writeTofileUpdateProcess(String command,String file,View view){
        Integer buttonNumber = Integer.parseInt(Integer.toBinaryString(Integer.parseInt(command,16)));
        System.out.println("[TestDirect] "+ buttonNumber);

        String str = String.format("%08d", buttonNumber);
        System.out.println(str);
        Integer len = buttonNumber.toString().length();

        System.out.println("[TestDirect] "+ len);
        String btnNum = buttonNumber.toString();
        ArrayList<Integer> index = new ArrayList<>();
        for(int i=0;i<8;i++){
            String c = String.valueOf(str.charAt(7-i));
            buttonStatus[i][1] = Integer.parseInt(c);
            System.out.print( buttonStatus[i][1] +" " );

        }

        clearContent("",STATUS_BUTTON);

        for(int[] btn: buttonStatus){
            String text = btn[0] +" "+ btn[1];
            writeTofile(text,STATUS_BUTTON);
        }
        setBtn(view);
    }


    private void setOnclick(View view) {
        mainPump.setOnClickListener(new onClickListener(view));
        sidePump.setOnClickListener(new onClickListener(view));
        leftsideValve.setOnClickListener(new onClickListener(view));
        leftmainValve.setOnClickListener(new onClickListener(view));
        rightsideValve.setOnClickListener(new onClickListener(view));
        rightmainValve.setOnClickListener(new onClickListener(view));

    }
    class onClickListener implements View.OnClickListener{
        View view;

        public onClickListener(View view) {
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == mainPump.getId()){
                setMessage(0b00000001,view,v);
            }else if(v.getId() == sidePump.getId()){
                setMessage(0b00000010, view,v);
            }else if(v.getId() == leftsideValve.getId()){
                setMessage(0b01000000, view,v);
            }else if(v.getId() == leftmainValve.getId()){
                setMessage(0b00010000, view,v);
            }else if(v.getId() == rightmainValve.getId()){
                setMessage( 0b00100000, view,v);
            }else if(v.getId() == rightsideValve.getId()){
                setMessage(0b10000000, view,v);
            }else{
                listener.PressBackButton(true);
                onDetach();
            }
        }
    }

    private void setView(View view) {
        mainPump=view.findViewById(R.id.main_pump);
        sidePump=view.findViewById(R.id.side_pump);
        leftmainValve=view.findViewById(R.id.left_mainValve);
        leftsideValve=view.findViewById(R.id.left_sideValve);
        rightmainValve=view.findViewById(R.id.right_mainValve);
        rightsideValve=view.findViewById(R.id.right_sideValve);
        toolbar =  view.findViewById(R.id.toolbar);
        reload = view.findViewById(R.id.reload);
    }
    private void setMessage(int mes, View view,View v) {
        Integer message_command=0;
        FileInputStream fileIn= null;
        Boolean sameBit = false;
        try {
            fileIn = getActivity().openFileInput(STATUS_BUTTON);
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            BufferedReader br = new BufferedReader(InputRead);
            StringBuilder sb = new StringBuilder();
            String text;
            int i = 0;
            while ((text = br.readLine()) != null){
                System.out.println(text);
                ArrayList<Integer> press = new ArrayList<>();
                for(String str : text.split("\\s") ){
                    press.add(Integer.parseInt(str));
                }
                if(press.get(0) != 0){
                    if(press.get(1) == 1){ //ในไฟล์เป็น 1
                        if(press.get(0) == v.getId()){
                            sameBit = true;
                            System.out.println("SAME");
                        }
                        message_command = message_command | bitButton[i];
                    }
                }
                i++;
            }
            InputRead.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(sameBit == true){
            System.out.println("TRUE");
            message_command = message_command ^ mes;
        }else {
            message_command = message_command | mes;
        }

        System.out.println(Integer.toString(message_command,2));
        String message = "02 "+decToHex(message_command)+" FF";
        setSending(message,decToHex(message_command),view);
    }

    private void ReadFile(String file) {
        FileInputStream fileIn= null;
        try {
            fileIn = getActivity().openFileInput(file);
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            BufferedReader br = new BufferedReader(InputRead);
            StringBuilder sb = new StringBuilder();
            String text;
            String lastLine = null;
            while ((text = br.readLine()) != null){
                lastLine = text;
            }
            System.out.println("[READ FILE]" + lastLine);
            InputRead.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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


    private void writeTofile(String dat,String file) {
        FileOutputStream fos = null;
        String data = dat + "\n";
        try {
            fos = getActivity().openFileOutput(file, MODE_APPEND);
            fos.write(data.getBytes());
//
//            fos = getActivity().openFileOutput(PROCESS, Context.MODE_PRIVATE);
//            fos.write("".getBytes());
        }
        catch (Throwable t) {

        }
    }
    private void clearContent(String dat,String file){
        FileOutputStream fos = null;
        String data = dat ;
        try {
            fos = getActivity().openFileOutput(file, MODE_PRIVATE);
            fos.write(data.getBytes());
//
//            fos = getActivity().openFileOutput(PROCESS, Context.MODE_PRIVATE);
//            fos.write("".getBytes());
        }
        catch (Throwable t) {

        }
    }


    private class tryAgainError implements View.OnClickListener {
        View view ;
        public tryAgainError(View view) {
            this.view = view;
        }

        @Override
        public void onClick(View v) {
                listener.Reload(true);
//            layoutContentContainer.showLoading();
//            setWriteStatusButton(view);
        }
    }

    private class OnClickReloadListener implements View.OnClickListener {

        public OnClickReloadListener(){


        }
        @Override
        public void onClick(View v) {
            if(v.getId() == reload.getId()){
                System.out.println("reloaddddddddd");
                listener.Reload(true);
            }

        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("onDetach");
    }
}
