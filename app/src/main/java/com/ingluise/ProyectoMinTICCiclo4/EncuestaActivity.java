package com.ingluise.ProyectoMinTICCiclo4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class EncuestaActivity extends AppCompatActivity {
    private TextView tv1;
    private EditText et1, et2, et3;
    private Spinner sp1;
    private Switch sw1;
    private CheckBox chk1, chk2, chk3;
    private RadioButton rb1, rb2, rb3;
    private SeekBar sb1;
    private String niv_sat;
    private TextInputEditText ti1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        et1 = (EditText) findViewById(R.id.editTextTextPersonName);
        et2 = (EditText) findViewById(R.id.editTextPhone);
        et3 = (EditText) findViewById(R.id.editTextTextEmailAddress);
        sp1 = (Spinner) findViewById(R.id.spinner);
        sw1 = (Switch) findViewById(R.id.switch1);
        chk1 = (CheckBox) findViewById(R.id.checkBox);
        chk2 = (CheckBox) findViewById(R.id.checkBox2);
        chk3 = (CheckBox) findViewById(R.id.checkBox3);
        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        tv1 = (TextView) findViewById(R.id.textView11);
        sb1 = (SeekBar) findViewById(R.id.seekBar);
        ti1 = findViewById(R.id.textInputEditText);
        niv_sat = "Nivel de satisfacción: " + tv1.getText().toString();
        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                Toast.makeText(getApplicationContext(), ""+i, Toast.LENGTH_SHORT).show();
                tv1.setText(""+i);
                niv_sat = "Nivel de satisfacción: " + tv1.getText().toString();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            ti1.setText(data.getStringExtra("tema"));
                            Toast.makeText(getApplicationContext(), "" + data.getStringExtra("pos"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        ti1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(EncuestaActivity.this, ListViewActivity.class);
                mGetContent.launch(newIntent);
            }
        });
    }

    public void obtenerDatos(View view) {
        String nom = "Nombre: " + et1.getText().toString();
        String tel = "Teléfono: " + et2.getText().toString();
        String email = "Email: " + et3.getText().toString();
        String nivel = "Nivel: " + sp1.getSelectedItem().toString();
        String gusta_prog = "¿Te gusta programar?: ";
        if(sw1.isChecked()) {
            gusta_prog += "Si";
        }
        else {
            gusta_prog += "No";
        }
        String leng = "Lenguajes de programación: ";
        if(chk1.isChecked()) {
            leng += chk1.getText().toString() + ", ";
        }
        if(chk2.isChecked()) {
            leng += chk2.getText().toString() + ", ";
        }
        if(chk3.isChecked()) {
            leng += chk3.getText().toString() + ", ";
        }
        String tiempo_exp = "Experiencia en programación: ";
        if(rb1.isChecked()) {
            tiempo_exp += rb1.getText().toString() + " año";
        }
        if(rb2.isChecked()) {
            tiempo_exp += rb2.getText().toString() + " años";
        }
        if(rb3.isChecked()) {
            tiempo_exp += rb3.getText().toString() + " años";
        }
        String tema = "Tema para profundizar: " + ti1.getText();
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Datos")
                .setMessage(nom + "\n" + tel + "\n" + email + "\n" + nivel + "\n" +
                        gusta_prog + "\n" + leng + "\n" + tiempo_exp + "\n" + niv_sat + "\n" + tema)
                .setPositiveButton("Aceptar", null).show();
    }
}