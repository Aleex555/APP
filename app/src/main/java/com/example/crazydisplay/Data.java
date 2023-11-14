package com.example.crazydisplay;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    static ArrayList<String> clients=new ArrayList<String>() ;
    static ArrayList<String> userMsgs=new ArrayList<String>() ;
    private static int port=8888;
    private static String IP=null;
    private static AppSocketsClient client=null;
    static  HashMap<String, String> MessageHistory = new HashMap<>();





    public static void setIP(String IP) {
        Data.IP = IP;
    }
    public static void setClient(AppSocketsClient client) {
        Data.client = client;
    }
    public static String getIP() {
        return IP;
    }
    public static AppSocketsClient getClient() {
        return client;
    }
    public static int getPort() {
        return port;
    }
}
