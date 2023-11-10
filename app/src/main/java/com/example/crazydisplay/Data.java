package com.example.crazydisplay;
public class Data {
    private static int port=8888;
    private static String IP=null;
    private static AppSocketsClient client=null;
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
