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
import android.os.Environment;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagenesActivity extends AppCompatActivity {
    private EditText t1;
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private static int SELECCIONAR_IMAGEN = 1;
    private ImageView mPhotoCaptured;
    private String mImageFileLocation = "";
    private MyDBSQLiteOpenHelper admin;
    private SQLiteDatabase bd;
    private Cursor fila;
    private ContentValues registro;
    private Bitmap bitmap, bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenes);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        admin = new MyDBSQLiteOpenHelper(this, vars.bd, null, vars.ver);
        mPhotoCaptured = (ImageView) findViewById(R.id.imageView);
        t1 = (EditText) findViewById(R.id.editText2);
    }

    public void takePhoto(View view) {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    public void deGaleria(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECCIONAR_IMAGEN);
    }

    public void quitarImg(View view) {
        mPhotoCaptured.setImageDrawable(null);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            Toast.makeText(this, "Imagen capturada exitosamente", Toast.LENGTH_SHORT).show();
            Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            mPhotoCaptured.setImageBitmap(photoCapturedBitmap);
            setReducedImageSize();
        }
        if(requestCode == SELECCIONAR_IMAGEN && resultCode == RESULT_OK) {
            Toast.makeText(this, "Imagen cargada de galería", Toast.LENGTH_SHORT).show();
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);

                Bitmap myBitmap  = BitmapFactory.decodeStream(bis);

                mPhotoCaptured.setImageBitmap(myBitmap);
            } catch (FileNotFoundException e) {}
        }
    }

    File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName,".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;
    }

    void setReducedImageSize() {
        int targetImageViewWidth = mPhotoCaptured.getWidth();
        int targetImageViewHeight = mPhotoCaptured.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        mPhotoCaptured.setImageBitmap(photoReducedSizeBitmp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_imagenes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.guardar) {
            if (!t1.getText().toString().equals("")) {
                String imgCodificada = "";
                mPhotoCaptured.buildDrawingCache(true);
                bitmap = mPhotoCaptured.getDrawingCache(true);
                String prueba = "";
                if (bitmap != null) {
                    if(mPhotoCaptured.getWidth() < mPhotoCaptured.getHeight()) {
                        bitmap2 = Bitmap.createScaledBitmap(bitmap, mPhotoCaptured.getWidth(), mPhotoCaptured.getHeight(), true);
                    }
                    else {
                        bitmap2 = Bitmap.createScaledBitmap(bitmap, mPhotoCaptured.getWidth(), mPhotoCaptured.getHeight(), true);
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] imagen = baos.toByteArray();
                    imgCodificada = Base64.encodeToString(imagen, Base64.DEFAULT);
                }

                bd = admin.getWritableDatabase();
                registro = new ContentValues();
                registro.put("descripcion", t1.getText().toString());
                registro.put("img", imgCodificada);
                long reg = bd.insert("ciudad", null, registro);
                if (reg == -1) {
                    Toast.makeText(this, "Error. No se pudo agregar", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Registro agregado", Toast.LENGTH_SHORT).show();
                }
                bd.close();

                t1.setText("");
                mPhotoCaptured.setImageBitmap(null);
                mPhotoCaptured.destroyDrawingCache();
                mPhotoCaptured.setImageDrawable(null);
            }
            else {
                Toast.makeText(this, "Ingrese la descripción", Toast.LENGTH_LONG).show();
            }

            return true;
        }

        if (id == R.id.buscar) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.inputdialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set inputdialog.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView.findViewById(R.id.editText3);

            //set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result edit text
                                    // t2.setText(userInput.getText());
                                    Bitmap decodedByte = null;
                                    byte[] decodeString;
                                    bd = admin.getWritableDatabase();
                                    fila = bd.rawQuery("SELECT * FROM ciudad WHERE idciu='"+ userInput.getText() +"'", null);
                                    String datos = "", _id = "", des = "";
                                    if(fila.moveToFirst()) {
                                        datos = fila.getInt(0) + "--" + fila.getString(1) + "\n";
                                        _id = fila.getString(0);
                                        des = fila.getString(1);
                                        if(!fila.getString(2).equals("")) {
                                            decodeString = Base64.decode(fila.getString(2), Base64.DEFAULT);
                                            decodedByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                                        }
                                        t1.setText(des);
                                        mPhotoCaptured.setImageBitmap(decodedByte);
                                        Toast.makeText(getApplicationContext(), datos, Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "El registro no existe", Toast.LENGTH_LONG).show();
                                    }
                                    bd.close();
                                }
                            })
                    .setNegativeButton("Cancelar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    t1.setText("");
                                    mPhotoCaptured.setImageBitmap(null);
                                    mPhotoCaptured.destroyDrawingCache();
                                    mPhotoCaptured.setImageDrawable(null);
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}