package com.example.crazydisplay;

import static com.example.crazydisplay.Data.MessageHistory;
import android.util.Base64;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ImageActivity extends AppCompatActivity {
    private Button atras;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_enviar);

        atras= findViewById(R.id.Atras);
        imageView1 = findViewById(R.id.imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msgJSON=null;
                String imagenBase64=ImageToBase64("sample_image.jpg");
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("value", imagenBase64);
                    msgJSON.put("name", "sample_image.jpg");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), "Correcte", Toast.LENGTH_SHORT).show();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msgJSON=null;
                String imagenBase64=ImageToBase64("sample_image.jpg");
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("value", imagenBase64);
                    msgJSON.put("name", "sample_image.jpg");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), "Correcte", Toast.LENGTH_SHORT).show();
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msgJSON=null;
                String imagenBase64=ImageToBase64("sample_image.jpg");
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("value", imagenBase64);
                    msgJSON.put("name", "sample_image.jpg");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), "Correcte", Toast.LENGTH_SHORT).show();
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msgJSON=null;
                String imagenBase64=ImageToBase64("sample_image.jpg");
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("value", imagenBase64);
                    msgJSON.put("name", "sample_image.jpg");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), "Correcte", Toast.LENGTH_SHORT).show();
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msgJSON=null;
                String imagenBase64=ImageToBase64("sample_image.jpg");
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("value", imagenBase64);
                    msgJSON.put("name", "sample_image.jpg");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), "Correcte", Toast.LENGTH_SHORT).show();
            }
        });
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msgJSON=null;
                String imagenBase64=ImageToBase64("sample_image.jpg");
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("value", imagenBase64);
                    msgJSON.put("name", "sample_image.jpg");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), "Correcte", Toast.LENGTH_SHORT).show();
            }
        });
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msgJSON=null;
                String imagenBase64=ImageToBase64("sample_image.jpg");
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("value", imagenBase64);
                    msgJSON.put("name", "sample_image.jpg");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), "Correcte", Toast.LENGTH_SHORT).show();
            }
        });
        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msgJSON=null;
                String imagenBase64=ImageToBase64("sample_image.jpg");
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("value", imagenBase64);
                    msgJSON.put("name", "sample_image.jpg");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), "Correcte", Toast.LENGTH_SHORT).show();
            }
        });
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msgJSON=null;
                String imagenBase64=ImageToBase64("nepe.jpg");
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("value", imagenBase64);
                    msgJSON.put("name", "sample_image.jpg");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Data.client.send(msgJSON.toString());

                Toast.makeText(v.getContext(), "Correcte", Toast.LENGTH_SHORT).show();
            }
        });

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this, EnviarActivity.class);
                startActivity(intent);
            }
        });
        // Repite para imageView2 a imageView9
    }

    public static String ImageToBase64(String pathImagen) {
        String base64Image = null;
        try {
            // Cargar la imagen como Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(pathImagen);

            // Convertir Bitmap a ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Comprimir en formato JPEG
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            // Convertir ByteArrayOutputStream a arreglo de bytes
            byte[] imageBytes = baos.toByteArray();

            // Codificar en Base64
            base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Image;
    }



}