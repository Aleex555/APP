package com.example.crazydisplay;

import static com.example.crazydisplay.Data.MessageHistory;
import static com.example.crazydisplay.ImageActivity.mThumbIds;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Base64;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
public class ImageActivity extends AppCompatActivity {
    private Button Atras;
     public static Integer[] mThumbIds = {
            R.drawable.sample_image, R.drawable.sample_image,
            R.drawable.doly, R.drawable.pinguins   ,
             R.drawable.duck, R.drawable.gato
            // ... Add as many images as you have
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_enviar);
        Atras=findViewById(R.id.Atras);
        GridView gridView = (GridView) findViewById(R.id.galleryGridView);
        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.setEnabled(false);
                Dialog loadingDialog = new Dialog(ImageActivity.this);
                loadingDialog.setContentView(R.layout.dialog_loading);
                loadingDialog.setCancelable(false); // Para evitar que se cierre al tocar fuera
                loadingDialog.show();

                Bitmap bitmap = getBitmapFromDrawable(getResources(), mThumbIds[position]); // Reemplaza con la imagen correcta
                String base64String = convertToBase64(bitmap);
                JSONObject msgJSON=null;
                try {
                    msgJSON = new JSONObject();
                    msgJSON.put("type", "image");
                    msgJSON.put("from", "Android");
                    msgJSON.put("value", base64String);
                    msgJSON.put("name", position+"");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //
                try {
                    Data.client.send(msgJSON.toString());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Cerrar el diálogo después de 3 segundos
                            loadingDialog.dismiss();
                            gridView.setEnabled(true);
                            // Aquí puedes continuar con otras operaciones después de los 3 segundos
                            Toast.makeText(ImageActivity.this, "Imatge enviat amb éxit", Toast.LENGTH_SHORT).show();
                        }
                    }, 6000); // 3000 milisegundos = 3 segundos
                }catch (Exception e){
                    Toast.makeText(ImageActivity.this, "Ha passat alguna cosa al servidor", Toast.LENGTH_SHORT).show();
                }



            }
        });

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this, EnviarActivity.class);
                startActivity(intent);
            }
        });
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


    public Bitmap getBitmapFromDrawable(Resources res, int drawableId) {
        return BitmapFactory.decodeResource(res, drawableId);
    }
    public String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}

 class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images

}
