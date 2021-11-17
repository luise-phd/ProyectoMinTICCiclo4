package com.ingluise.ProyectoMinTICCiclo4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImagenesDBActivity extends AppCompatActivity {
    private EditText t1;
    private ImageView imgView;
    private String mImageFileLocation = "";
    private MyDBSQLiteOpenHelper admin;
    private SQLiteDatabase bd;
    private Cursor fila;
    private ContentValues registro;
    private Bitmap bitmap, bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenes_db);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        admin = new MyDBSQLiteOpenHelper(this, vars.bd, null, vars.ver);
        imgView = findViewById(R.id.imageView);
        imgView.setImageDrawable(null);
        t1 = findViewById(R.id.editText2);
    }

    public void tomarFoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, 0);
        }
    }

    public void deGaleria(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    public void quitarImg(View view) {
        imgView.setImageDrawable(null);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //De la camara
        if(requestCode == 0 && resultCode == RESULT_OK) {
            Toast.makeText(this, "Imagen capturada", Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imgBitmap);
        }

        //De la galería
        if(requestCode == 1 && resultCode == RESULT_OK) {
            Toast.makeText(this, "Imagen cargada de galería", Toast.LENGTH_SHORT).show();
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);

                Bitmap myBitmap  = BitmapFactory.decodeStream(bis);

                imgView.setImageBitmap(myBitmap);
            } catch (FileNotFoundException e) {}
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_imagenes_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.guardar) {
            if (!t1.getText().toString().equals("")) {
                String imgCodificada = "";
                if (imgView.getDrawable() != null) {
                    imgView.buildDrawingCache(true);
                    bitmap = imgView.getDrawingCache(true);
                    bitmap2 = Bitmap.createScaledBitmap(bitmap, imgView.getWidth(), imgView.getHeight(), true);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] imagen = baos.toByteArray();
                    imgCodificada = Base64.encodeToString(imagen, Base64.DEFAULT);

                    bd = admin.getWritableDatabase();
                    registro = new ContentValues();
                    registro.put("descripcion", t1.getText().toString());
                    registro.put("img", imgCodificada);
                    long reg = bd.insert("imagenes", null, registro);
                    if (reg == -1) {
                        Toast.makeText(this, "No se pudo agregar el registro", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Registro agregado", Toast.LENGTH_SHORT).show();
                    }
                    bd.close();

                    t1.setText("");
                    imgView.setImageBitmap(null);
                    imgView.destroyDrawingCache();
                    imgView.setImageDrawable(null);
                } else
                    Toast.makeText(this, "Por favor, tome la fotografía", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "Ingrese la descripción", Toast.LENGTH_LONG).show();

            return true;
        }

        if (id == R.id.buscar) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.inputdialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = promptsView.findViewById(R.id.editText3);

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Bitmap decodedByte = null;
                                byte[] decodeString;
                                bd = admin.getWritableDatabase();
                                fila = bd.rawQuery("SELECT * FROM imagenes WHERE descripcion='"+ userInput.getText() +"'", null);
                                String des = "";
                                if(fila.moveToFirst()) {
                                    des = fila.getString(1);
                                    if(!fila.getString(2).equals("")) {
                                        decodeString = Base64.decode(fila.getString(2), Base64.DEFAULT);
                                        decodedByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                                    }
                                    t1.setText(des);
                                    imgView.setImageBitmap(decodedByte);
                                } else {
                                    Toast.makeText(getApplicationContext(), "El registro no existe", Toast.LENGTH_LONG).show();
                                }
                                bd.close();
                            }
                        })
                    .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                t1.setText("");
                                imgView.setImageBitmap(null);
                                imgView.destroyDrawingCache();
                                imgView.setImageDrawable(null);
                                dialog.cancel();
                            }
                        });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}