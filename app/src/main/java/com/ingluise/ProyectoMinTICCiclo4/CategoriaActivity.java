package com.ingluise.ProyectoMinTICCiclo4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CategoriaActivity extends AppCompatActivity {
    private static final String TAG = CategoriaActivity.class.getSimpleName();

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private TextInputEditText t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        t1 = findViewById(R.id.txt_categoria);
    }

    public void guardarDatos(View view) {
        String categoria = t1.getText().toString();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("categoria").push();

        myRef.setValue(categoria);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void recuperarDatos(View view) {
        // Read from the database

    }
}