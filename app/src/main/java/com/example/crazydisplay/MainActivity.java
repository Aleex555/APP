package com.example.crazydisplay;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
        connectarButton.setBackgroundColor(Color.parseColor("#2196f3"));
        //
        connectarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String IP=inputIP.getText().toString();
                    validateIPAddress(IP);
                    //Toast.makeText(v.getContext(), "La dirección IP es válida: " + IP, Toast.LENGTH_SHORT).show();
                    Data.setIP(IP);
                    connectarButton.setText("Connectant...");
                    connectarButton.setEnabled(false);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                Socket socket = new Socket();
                                socket.connect(new InetSocketAddress(Data.getIP(), Data.getPort()), 2000); // Timeout de 1 segundo

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                // Si necesitas realizar alguna operación con la respuesta del socket, hazlo aquí

                            } catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(v.getContext(), "Error: servidor no trobat", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    }).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            connectarButton.setEnabled(true);
                            connectarButton.setText("Connectar");
                        }
                    }, 1000); // 3000 milisegundos = 3 segundos

                } catch (IllegalArgumentException ex) {
                    Toast.makeText(v.getContext(), "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Manejar la acción de retroceso aquí
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Estas segur que vols tancar?")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               finish();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}