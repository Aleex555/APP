package com.example.crazydisplay;

import static com.example.crazydisplay.Data.MessageHistory;
import java.util.Locale;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
        // Inicialitzem model
        records = new ArrayList<Record>();

        for (HashMap.Entry<String, String> entry : Data.MessageHistory.entrySet()) {
            Record record =new Record(entry.getKey(),entry.getValue());
            records.add(record);
            // Hacer algo con la clave y el valor
        }
        records=ordenarRecordsPorTiempo(records);
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record recordSeleccionado = records.get(position);
                String mensaje = recordSeleccionado.mensaje;
                String tiempo = recordSeleccionado.tiempo;
                mostrarDialogoDeConfirmacion(mensaje);
            }
        });
        // botó per afegir entrades a la ListView
        Button b = findViewById(R.id.atras);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.client.close();
                Intent intent = new Intent(MissageHistoryActivity.this, EnviarActivity.class);
                startActivity(intent);

            }
        });


    }
    private void mostrarDialogoDeConfirmacion(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmació");
        builder.setMessage("Enviar de nou ?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject msgJSON=null;
                Data.userMsgs.add(message);
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
                    Toast.makeText(MissageHistoryActivity.this, "Ha passat alguna cosa al servidor", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(MissageHistoryActivity.this, "Enviat", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Manejar la confirmación negativa aquí
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    class Record {
        public String mensaje;
        public String tiempo;
        public String getTiempo() {
            return tiempo;
        }
        public Record(String mensaje, String tiempo) {
            this.mensaje = mensaje;
            this.tiempo = tiempo;
        }


    }
    public static ArrayList<Record> ordenarRecordsPorTiempo(ArrayList<Record> records) {
        // Crear un comparador personalizado para ordenar de forma descendente por tiempo
        Comparator<Record> comparator = new Comparator<Record>() {
            @Override
            public int compare(Record record1, Record record2) {
                try {
                    // Formato para convertir la cadena de tiempo en un objeto Date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

                    // Parsear las cadenas de tiempo en objetos Date
                    Date date1 = dateFormat.parse(record1.getTiempo());
                    Date date2 = dateFormat.parse(record2.getTiempo());

                    // Comparar los objetos Date en orden descendente
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        };

        // Clonar el ArrayList original para no modificarlo directamente
        ArrayList<Record> recordsClonados = new ArrayList<>(records);

        // Ordenar el ArrayList clonado utilizando el comparador
        Collections.sort(recordsClonados, comparator);

        return recordsClonados;
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
}
