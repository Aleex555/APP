package com.example.crazydisplay;

import static com.example.crazydisplay.Data.MessageHistory;
import static com.example.crazydisplay.Data.clients;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
public class EnviarActivity extends AppCompatActivity {

    private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
    public enum ConnectionStatus {
        DISCONNECTED, DISCONNECTING, CONNECTING, CONNECTED
    }
    private AppSocketsClient client;
    private Button enviarButton,historialMissatges;
    private EditText inputMessage;
    private ListView showClients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        //
        Button enviarButton = findViewById(R.id.enviarButton);
        enviarButton.setBackgroundColor(Color.parseColor("#20b16c"));
        EditText inputMessage= findViewById(R.id.inputMessage);
        ListView showClients = findViewById(R.id.showClients);
        historialMissatges=findViewById(R.id.historialMissatges);
        //

        // Ensure network operations are done on a separate thread to avoid NetworkOnMainThreadException
        new Thread(new Runnable() {
            @Override
            public void run() {
                String clientType = "android_client";
                try {
                    URI location = new URI("ws://" + Data.getIP() + ":" + "8888");
                    location = new URI(location + "/" + clientType);
                    client = new AppSocketsClient(location, (String message) ->             {
                        try {
                            this.onMessage(message);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    //serverConnected = true;
                    client.connect();
                    Thread.sleep(1000); // Esperar respuesta
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                connectionStatus = ConnectionStatus.CONNECTING;
            }
            private void onOpen (ServerHandshake handshake) {
                System.out.println("Handshake: " + handshake.getHttpStatusMessage());
                connectionStatus = ConnectionStatus.CONNECTED;
            }
            private void onMessage(String message) throws JSONException {
                JSONObject data = new JSONObject(message);
                String type = data.getString("type");

                switch (type) {
                    case "list":
                        ArrayList<String> list = new ArrayList<>();
                        String jsonlist =data.getString("list");
                        JSONArray jsonArray = new JSONArray(jsonlist);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            list.add(jsonArray.getString(i));
                        }
                        clients=list;
                        break;
                    default:
                        break;
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                EnviarActivity.this,
                                android.R.layout.simple_list_item_1,
                                clients);
                        showClients.setAdapter(adapter);
                    }
                });

                    }
        }).start();

        //
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message =inputMessage.getText().toString();
                JSONObject msgJSON=null;
                Data.userMsgs.add(message);
                try {
                     msgJSON = new JSONObject();
                    msgJSON.put("type", "broadcast");
                    msgJSON.put("value", message);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //
                Date ahora = new Date();
                System.out.println("Fecha y hora actuales: " + ahora);
                MessageHistory.put(message,ahora.toString());
                guardarDatos("messageHistory.txt",MessageHistory.toString());
                //
                client.send(msgJSON.toString());
                Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        //

        //
        historialMissatges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnviarActivity.this, MissageHistoryActivity.class);
                startActivity(intent);
            }
        });
        //
    }
    private void guardarDatos(String name,String data) {
        String dataSave= data;

        String filename = name;

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(dataSave.getBytes());
            outputStream.close();
            //Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        }
    }
}
