package com.example.crazydisplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class MissageHistoryActivity  extends AppCompatActivity {
    ArrayList<Record> records;
    ArrayAdapter<Record> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_message);
        String hashmapReaded=readArchivetoString("messageHistory.txt");

        // Inicialitzem model
        records = new ArrayList<Record>();
        // Afegim alguns exemples

        HashMap<String, String> MessageHistory=convertStringToHashMap(hashmapReaded);

        for (HashMap.Entry<String, String> entry : MessageHistory.entrySet()) {
            Record record =new Record(entry.getKey(),entry.getValue());
            records.add(record);
            // Hacer algo con la clave y el valor
        }

        // Inicialitzem l'ArrayAdapter amb el layout pertinent
        adapter = new ArrayAdapter<Record>(this, R.layout.list_item, records) {
            @Override
            public View getView(int pos, View convertView, ViewGroup container) {
                // getView ens construeix el layout i hi "pinta" els valors de l'element en la posició pos
                if (convertView == null) {
                    // inicialitzem l'element la View amb el seu layout
                    convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
                }

                // Obtenemos las referencias de los elementos de la vista
                TextView nomTextView = convertView.findViewById(R.id.nom);
                TextView intentsTextView = convertView.findViewById(R.id.intents);

                // "Pintem" valors (també quan es refresca)
                nomTextView.setText(getItem(pos).mensaje);
                intentsTextView.setText(getItem(pos).tiempo);


                return convertView;
            }
        };

        // busquem la ListView y li endollem el ArrayAdapter
        ListView lv = findViewById(R.id.recordsView);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // botó per afegir entrades a la ListView
        Button b = findViewById(R.id.atras);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MissageHistoryActivity.this, EnviarActivity.class);
                startActivity(intent);

            }
        });


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




    public static HashMap<String, String> convertStringToHashMap(String str) {
        HashMap<String, String> map = new HashMap<>();

        // Verifica si la cadena no está vacía y está entre corchetes
        if (str != null && str.startsWith("{") && str.endsWith("}")) {
            // Quita los corchetes de inicio y fin
            String content = str.substring(1, str.length() - 1);

            // Divide la cadena en pares clave-valor
            String[] keyValuePairs = content.split(", ");

            // Itera y divide cada par clave-valor
            for (String pair : keyValuePairs) {
                String[] entry = pair.split("=", 2);
                if (entry.length == 2) {
                    map.put(entry[0].trim(), entry[1].trim());
                }
            }
        }

        return map;
    }
    class Record {
        public String mensaje;
        public String tiempo;

        public Record(String mensaje, String tiempo) {
            this.mensaje = mensaje;
            this.tiempo = tiempo;
        }


    }


}
