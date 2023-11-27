package com.example.crazydisplay;

import static com.example.crazydisplay.Data.MessageHistory;
import static com.example.crazydisplay.Data.lostConnection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.Menu;
import android.view.MenuItem;

public class EnviarActivity extends AppCompatActivity {


    private Button enviarButton,historialMissatges,enviarImagen,disconnect;
    private EditText inputMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data.MessageHistory=transformarStringAHashMap(readArchivetoString("messageHistory.txt"));
        setContentView(R.layout.activity_enviar);
        //
        enviarButton = findViewById(R.id.enviarButton);
        disconnect = findViewById(R.id.disconnect);
        enviarButton.setBackgroundColor(Color.parseColor("#2196f3"));
        inputMessage= findViewById(R.id.inputMessage);
        disconnect.setBackgroundColor(Color.parseColor("#FF0000"));
        historialMissatges=findViewById(R.id.historialMissatges);
        historialMissatges.setBackgroundColor(Color.parseColor("#2196f3"));
        enviarImagen=findViewById(R.id.enviarImagen);
        enviarImagen.setBackgroundColor(Color.parseColor("#2196f3"));
        //

        // Ensure network operations are done on a separate thread to avoid NetworkOnMainThreadException


        //

        enviarButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String message =inputMessage.getText().toString();
                JSONObject msgJSON=null;
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "broadcast");
                    msgJSON.put("from", "Android");
                    msgJSON.put("usuario", Data.username);
                    msgJSON.put("value", message);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //
                Date ahora = new Date();
                MessageHistory.put(message,ahora.toString());

                guardarDatos("messageHistory.txt",Data.convertirHashMapAString(MessageHistory));
                //
                try {
                    Data.client.send(msgJSON.toString());
                }catch (Exception e){
                    Toast.makeText(EnviarActivity.this, "Ha passat alguna cosa al servidor", Toast.LENGTH_SHORT).show();
                    Data.lostConnection=new Intent(EnviarActivity.this, MainActivity.class);
                    startActivity(Data.lostConnection);
                    finish();
                }
                inputMessage.setText("");
            }
        });
        //
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Desconnectat", Toast.LENGTH_SHORT).show();
                Data.client.close();
                finish();
            }
        });
        //
        historialMissatges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnviarActivity.this, MissageHistoryActivity.class);
                startActivity(intent);
            }
        });
        //
        enviarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent saltarEnviarImagen=new Intent(EnviarActivity.this, ImageActivity.class);
                startActivity(saltarEnviarImagen);
            }
        });

        //

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Manejar la acción de retroceso aquí
                new AlertDialog.Builder(EnviarActivity.this)
                        .setMessage("Tornaràs a la pantalla principal")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(EnviarActivity.this, "Desconnectat", Toast.LENGTH_SHORT).show();
                                Data.client.close();
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú; esto añade ítems al action bar si está presente.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Maneja los clics en los ítems del action bar aquí.
        int id = item.getItemId();

        if (id == R.id.action_button) {
            try {
                showDialog();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void guardarDatos(String name, String data) {
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

    public HashMap<String, String> transformarStringAHashMap(String contenido) {
        HashMap<String, String> hashMap = new HashMap<>();

        String[] lineas = contenido.split("\n");
        for (String linea : lineas) {
            // Divide la línea en clave y valor utilizando el símbolo "=" como separador
            String[] partes = linea.split("=");
            if (partes.length == 2) {
                String clave = partes[0];
                String valor = partes[1];
                hashMap.put(clave, valor);
            }
        }

        if (hashMap.isEmpty()) {
            guardarDatos("Error.txt", "El contenido está vacío");
        }

        return hashMap;
    }

    public String readArchivetoString(String filePath){
        StringBuilder content = new StringBuilder();
        try {
            FileInputStream fis = openFileInput(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de excepciones
        }

        return content.toString();


    }

    private void showDialog() throws InterruptedException {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        JSONObject msgJSON=null;
        try {
            msgJSON = new JSONObject();
            msgJSON.put("type", "list");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //
        Data.client.send(msgJSON.toString());
        Thread.sleep(50);
        // Crear ListView y Adaptador
        ListView listView = new ListView(this);
        List<String> dataList = new ArrayList<>();

        for (Map.Entry<String, String> entry : Data.personesConectades.entrySet()) {
            dataList.add("  "+entry.getKey() + " desde " + entry.getValue());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        // Configurar el AlertDialog con el ListView
        builder.setTitle("Persones Connectades")
                .setView(listView)
                .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


