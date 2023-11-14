package com.example.crazydisplay;
import static com.example.crazydisplay.Data.MessageHistory;
import static com.example.crazydisplay.Data.clients;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private boolean isButtonClickable = true;
    private JSONObject msgJSON=null;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credencials_check);

        // Asignar las referencias a los elementos del diseño XML
        editTextUsername = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        buttonLogin = findViewById(R.id.button);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URI location = new URI("ws://" + Data.getIP() + ":" + "8888");
                    Data.client = new AppSocketsClient(location, (String message) ->             {
                        try {
                            this.onMessage(message);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    //serverConnected = true;
                    Data.client.connect();
                    Thread.sleep(1000);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            private void onMessage(String message) throws JSONException {
                JSONObject data = new JSONObject(message);
                String type = data.getString("type");

                switch (type) {
                    case "ok":
                        Intent intent = new Intent(LoginActivity.this, EnviarActivity.class);
                        startActivity(intent);
                        break;
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

            }
        }).start();
        // Configurar un listener para el botón de "Connectar"
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes acceder a los valores de los EditText
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "registro");
                    msgJSON.put("user", username);
                    msgJSON.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                buttonLogin.setText("Connectant...");
                isButtonClickable = false;
                buttonLogin.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Habilita el botón nuevamente después de 3 segundos.
                        isButtonClickable = true;
                        buttonLogin.setEnabled(true);
                        buttonLogin.setText("Connectar");
                    }
                }, 3000); // 3000 milisegundos = 3 segundos

            }
        });
    }
}
