package com.ingluise.ProyectoMinTICCiclo4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductoActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
    }

    public void guardarDatos(View view) {
        producto = new Producto("Arroz", 3500);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("producto").push();

        myRef.setValue(producto);
    }

    public void leerDatos(View view) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(producto.getNombre());
        Toast.makeText(this, ""+myRef.toString(), Toast.LENGTH_SHORT).show();
    }
}