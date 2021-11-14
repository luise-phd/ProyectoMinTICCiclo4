package com.ingluise.ProyectoMinTICCiclo4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductoFirebaseActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Producto producto;
    private TextInputEditText t1, t2;

    String productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_firebase);

        t1 = findViewById(R.id.input_nombre);
        t2 = findViewById(R.id.input_precio);
    }

    public void guardarDatos(View view) {
        String nombre = t1.getText().toString();
        String precio = t2.getText().toString();
        if (!nombre.equals("") && !precio.equals("")) {
            producto = new Producto(nombre, Integer.parseInt(precio));
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference().child("producto").push();
            myRef.setValue(producto);
            t1.setText("");
            t1.requestFocus();
            t2.setText("");
        } else
            Toast.makeText(this, "Por favor, ingrese todos los datos", Toast.LENGTH_SHORT).show();
    }

    public void leerDatos(View view) {
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("producto").orderByChild("nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productos = "";
                String nombre = "";
                int precio = 0;
                for(DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Producto producto = snapShot.getValue(Producto.class);
                    nombre = producto.getNombre();
                    precio = producto.getPrecio();
                    productos += "Nombre: " + nombre + "\n" + "Precio: " + precio + "\n\n";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        new AlertDialog.Builder(ProductoFirebaseActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Productos")
            .setMessage(productos)
            .setPositiveButton("Aceptar", null).show();
    }

    public void buscarDatos(View view) {
        String nom = t1.getText().toString();
        if (!nom.equals("")) {
            myRef = FirebaseDatabase.getInstance().getReference();
            myRef.child("producto").orderByChild("nombre").equalTo(nom).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            Producto producto = snapShot.getValue(Producto.class);
                            String nombre = producto.getNombre();
                            int precio = producto.getPrecio();
                            t1.setText(nombre);
                            t2.setText("" + precio);
                        }
                    } else
                        Toast.makeText(getApplicationContext(), "El producto no existe", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese el nombre del producto", Toast.LENGTH_SHORT).show();
        }
    }

    public void editarDatos(View view) {
        String nom = t1.getText().toString();
        String pre = t2.getText().toString();
        if (!nom.equals("") && !pre.equals("")) {
            myRef = FirebaseDatabase.getInstance().getReference();
            myRef.child("producto").orderByChild("nombre").equalTo(nom).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String key = "";
                    for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                        key = snapShot.getKey();
                    }
                    myRef.child("producto").child(key).child("precio").setValue(Integer.parseInt(pre));
                    t1.setText("");
                    t1.requestFocus();
                    t2.setText("");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        } else
            Toast.makeText(this, "Por favor, ingrese todos los datos", Toast.LENGTH_SHORT).show();
    }

    public void eliminarDatos(View view) {
        String nom = t1.getText().toString();
        if (!nom.equals("")) {
            myRef = FirebaseDatabase.getInstance().getReference();
            myRef.child("producto").orderByChild("nombre").equalTo(nom).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                            snapShot.getRef().removeValue();
                        }
                        t1.setText("");
                        t2.setText("");
                    } else
                        Toast.makeText(getApplicationContext(), "El producto no existe", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese el nombre del producto", Toast.LENGTH_SHORT).show();
        }
    }
}