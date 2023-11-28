package com.example.crazydisplay;
import static com.example.crazydisplay.Data.client;
import static com.example.crazydisplay.Data.lostConnection;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        buttonLogin.setBackgroundColor(Color.parseColor("#2196f3"));

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
                    case "disconnected":
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Toast.makeText(LoginActivity.this, "El usuari "+data.getString("usuario")+" s'ha desconnectat desde "+data.getString("from"), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(LoginActivity.this, data.getString("conexiones")+" conexions totals", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });


                        break;
                    case "id":
                        Data.id=data.getString("value");
                    case "connected":

                        if (!(data.getString("id").equals(Data.id))){
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        Toast.makeText(LoginActivity.this, "El usuari "+data.getString("usuario")+" s'ha connectat desde "+data.getString("from"), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(LoginActivity.this, data.getString("conexiones")+" conexions restants", Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        }

                        break;
                    case "infomensaje":
                        if (data.getString("id").equals(Data.id)){
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        Toast.makeText(LoginActivity.this, "El usuari "+data.getString("usuario")+" desde "+data.getString("from")+" ha enviat un missatge", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        }
                        break;
                    case "no":
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                editTextUsername.setText("");
                                editTextPassword.setText("");
                                Toast.makeText(LoginActivity.this, "Comprova el nom o contrasenya", Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case "ok":
                        Data.username=editTextUsername.getText().toString();
                        Intent intent = new Intent(LoginActivity.this, EnviarActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "list":
                        Data.personesConectades= convertJsonToHashMap(data.getString("list"));

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
                    msgJSON.put("from", "Android");
                    msgJSON.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Data.client.send(msgJSON.toString());
                }catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Error: No s'ha pogut connectar amb el servidor", Toast.LENGTH_SHORT).show();
                    Data.lostConnection=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(Data.lostConnection);
                    finish();
                }


                buttonLogin.setText("Validant...");
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
                }, 1000); // 3000 milisegundos = 3 segundos

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
               client.close();
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public static HashMap<String, String> convertJsonToHashMap(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        HashMap<String, String> map = new HashMap<>();

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(key, jsonObject.getString(key));
        }

        return map;
    }
}
