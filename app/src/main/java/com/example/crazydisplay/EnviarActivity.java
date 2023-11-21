package com.example.crazydisplay;

import static com.example.crazydisplay.Data.MessageHistory;
import static com.example.crazydisplay.Data.clients;
import static com.example.crazydisplay.Data.lostConnection;

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
import java.util.Map;

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

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        EnviarActivity.this,
                        android.R.layout.simple_list_item_1,
                        clients);
            }
        });
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
                }
                inputMessage.setText("");
                Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        //
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Desconnectat", Toast.LENGTH_SHORT).show();
                Data.client.close();
                 lostConnection = new Intent(EnviarActivity.this, MainActivity.class);
                startActivity(lostConnection);
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
}
