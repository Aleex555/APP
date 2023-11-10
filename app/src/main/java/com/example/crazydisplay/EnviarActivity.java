package com.example.crazydisplay;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;
import java.net.URISyntaxException;

public class EnviarActivity extends AppCompatActivity {
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
                    client.connect();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();

        //
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),inputMessage.getText().toString() , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
