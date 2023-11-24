package com.example.crazydisplay;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Data {
    static HashMap<String, String> personesConectades;
    static String id;
    static String username;

    private static int port=8888;
    private static String IP=null;
    static  HashMap<String, String> MessageHistory = new HashMap<>();
    static  AppSocketsClient client;
    static Intent lostConnection;



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







    public static String convertirHashMapAString(Map<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}

