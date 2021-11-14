package com.ingluise.ProyectoMinTICCiclo4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CategoriaFirebaseActivity extends AppCompatActivity {
    private static final String TAG = CategoriaFirebaseActivity.class.getSimpleName();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TextInputEditText t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_firebase);

        t1 = findViewById(R.id.txt_categoria);
    }

    public void guardarDatos(View view) {
        String categoria = t1.getText().toString();
        if (!categoria.equals("")) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("categoria").push();
            myRef.setValue(categoria);
            t1.setText("");
        } else
            Toast.makeText(this, "Por favor ingrese la categoría", Toast.LENGTH_SHORT).show();
    }

    public void recuperarDatos(View view) {
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("categoria").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String categorias = "", cat = "";
                for(DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    cat = snapShot.getValue(String.class);
                    categorias += cat + "\n";
                }
                new AlertDialog.Builder(CategoriaFirebaseActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Categorías")
                    .setMessage(categorias)
                    .setPositiveButton("Aceptar", null).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}