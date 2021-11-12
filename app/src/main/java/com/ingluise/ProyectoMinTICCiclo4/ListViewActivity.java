package com.ingluise.ProyectoMinTICCiclo4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {
    private ListView lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        lv1 = findViewById(R.id.listView);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Modelo-Vista-Presentador");
        list.add("Diseño de UI");
        list.add("Autenticación de usuario");
        list.add("Gestión de repositorios");
        list.add("Navegación entre actividades");
        list.add("Uso de Widgets");
        list.add("Depuración en Android Studio");
        list.add("Almacenamiento local: SQLite");
        list.add("Almacenamiento remoto: Firebase");
        list.add("Consumo de datos desde una API Rest");
        list.add("Google Maps");
        list.add("Manejo de la camara");
        list.add("Cargar imagenes de la galería");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,
                android.R.layout.simple_list_item_1, list ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                tv.setTextColor(Color.BLUE);
                return view;
            }
        };
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                Toast.makeText(getApplicationContext(), ""+lv1.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                //Retornar datos a la actividad que la invocó
                Intent output = new Intent();
                output.putExtra("tema", lv1.getItemAtPosition(pos).toString());
                output.putExtra("pos", ""+lv1.getItemIdAtPosition(pos));
                setResult(RESULT_OK, output);
                finish();
            }
        });
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                adapter.remove(lv1.getItemAtPosition(pos).toString());
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}