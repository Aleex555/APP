package com.example.crazydisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    // Patrón para validar una dirección IP.
    private static final String IPADDRESS_PATTERN =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    // Compila el patrón para reutilizarlo en cada llamada al método.
    private static final Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

    public static void validateIPAddress(String ip) throws IllegalArgumentException {
        Matcher matcher = pattern.matcher(ip);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Entrada inválida. La cadena ingresada no es una dirección IP válida.");
        }
    }


    private Button connectarButton;
    private EditText inputIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        inputIP=findViewById(R.id.inputIP);
        connectarButton=findViewById(R.id.conectButton);
        connectarButton.setBackgroundColor(Color.parseColor("#20b16c"));
        //
        connectarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String IP=inputIP.getText().toString();
                    validateIPAddress(IP);
                    //Toast.makeText(v.getContext(), "La dirección IP es válida: " + IP, Toast.LENGTH_SHORT).show();
                    Data.setIP(IP);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Socket socket = new Socket();
                                socket.connect(new InetSocketAddress(Data.getIP(), Data.getPort()), 1000); // Timeout de 1 segundo
                                Thread.sleep(2000);

                                // Si necesitas realizar alguna operación con la respuesta del socket, hazlo aquí

                            } catch (IOException e) {
                                // Usamos runOnUiThread para mostrar el Toast en el hilo principal
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (InterruptedException e) {
                                // Manejo de InterruptedException
                                Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
                            }
                        }
                    }).start();


                    Intent intent = new Intent(MainActivity.this, EnviarActivity.class);
                    startActivity(intent);
                } catch (IllegalArgumentException ex) {
                    Toast.makeText(v.getContext(), "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //
    }
}