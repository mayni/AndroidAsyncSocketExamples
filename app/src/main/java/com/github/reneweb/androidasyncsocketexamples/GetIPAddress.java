package com.github.reneweb.androidasyncsocketexamples;
import java.net.*;
public class GetIPAddress {
    public static void main(String[] args){
        System.out.println("hello");
        InetAddress host;
        try {
            host=InetAddress.getLocalHost();
            System.out.println(host);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
