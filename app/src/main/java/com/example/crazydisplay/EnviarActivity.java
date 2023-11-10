package com.example.crazydisplay;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class EnviarActivity extends AppCompatActivity {
    private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
    public enum ConnectionStatus {
        DISCONNECTED, DISCONNECTING, CONNECTING, CONNECTED
    }
    private AppSocketsClient client;
    private Button enviarButton;
    private EditText inputMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        //
        Button enviarButton = findViewById(R.id.enviarButton);
        enviarButton.setBackgroundColor(Color.parseColor("#20b16c"));
        EditText inputMessage= findViewById(R.id.inputMessage);
        //

        // Ensure network operations are done on a separate thread to avoid NetworkOnMainThreadException
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URI location = new URI("ws://" + Data.getIP() + ":" + "8888");
                    client = new AppSocketsClient(location);
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
        }).start();
        /**
        if ( (connectionStatus == ConnectionStatus.CONNECTED)  ) {
            Toast.makeText(this,"Servidor connectat amb exit", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"Servidor ha fallat", Toast.LENGTH_SHORT).show();
        }

        */
        //
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message =inputMessage.getText().toString();
                JSONObject msgJSON=null;
                try {
                     msgJSON = new JSONObject();
                    msgJSON.put("type", "broadcast");
                    msgJSON.put("value", message);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
