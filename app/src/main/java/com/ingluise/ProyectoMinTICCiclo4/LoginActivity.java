package com.ingluise.ProyectoMinTICCiclo4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private TextView t1, t2;
    private Button b1;
    private EditText et1, et2;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        settings = getSharedPreferences("id", Context.MODE_PRIVATE);

        t1 =(TextView) findViewById(R.id.textView2);
        t2 = findViewById(R.id.textView14);
        b1 =(Button) findViewById(R.id.button);
        et1 = findViewById(R.id.editTextTextPersonName);
        et2 = findViewById(R.id.editTextTextPassword);
        t1.setClickable(true);
        t2.setClickable(true);
        t1.setLinkTextColor(Color.BLUE);
        t2.setLinkTextColor(Color.BLUE);
//        String texto = "<a href='https://www.google.com/'>Google</a>";
        String texto = "<a href=''>Recordar contraseña</a>";
        String texto2 = "<a href=''>Registrarse</a>";
//        t1.setMovementMethod(LinkMovementMethod.getInstance());
        t1.setText(Html.fromHtml(texto));
        t2.setText(Html.fromHtml(texto2));
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), settings.getString("pass",""), Toast.LENGTH_SHORT).show();
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user", et1.getText().toString());
                editor.putString("pass", et2.getText().toString());
                editor.commit();
                et1.setText("");
                et1.requestFocus();
                et2.setText("");
            }
        });
        et2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == R.id.editTextTextPassword || id == EditorInfo.IME_NULL) {
                    iniciarSesion(textView);
                    return true;
                }
                return false;
            }
        });
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ImageView iv1 =(ImageView) findViewById(R.id.imageView);
            Toast.makeText(this, ""+(iv1.getBottom()-iv1.getTop()), Toast.LENGTH_SHORT).show();
        }
    }

    public void iniciarSesion(View view) {
        String user = et1.getText().toString();
        String pass = et2.getText().toString();
        if(user.equals(settings.getString("user","")) && pass.equals(settings.getString("pass",""))) {
            Intent newIntent = new Intent(this, MainActivity.class);
            startActivity(newIntent);
        }
        else {
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}