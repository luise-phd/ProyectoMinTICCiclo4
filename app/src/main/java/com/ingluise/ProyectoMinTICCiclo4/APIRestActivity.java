package com.ingluise.ProyectoMinTICCiclo4;

import androidx.appcompat.app.AppCompatActivity;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class APIRestActivity extends AppCompatActivity {
    private static final String TAG = APIRestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apirest);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL githubEndpoint = new URL("https://api.github.com/");
                    HttpsURLConnection myConnection = (HttpsURLConnection) githubEndpoint.openConnection();

                    myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
                    myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    myConnection.setRequestProperty("Contact-Me", "usuario@example.com");
                    if (myConnection.getResponseCode() == 200) {
                        Log.e(TAG, "Conexión exitosa: "+myConnection.getResponseCode());
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            String key = jsonReader.nextName();
                            if (key.equals("organization_url")) {
                                String value = jsonReader.nextString();
                                Log.e(TAG, value);
                                break;
                            } else
                                jsonReader.skipValue();
                        }
                        jsonReader.close();
                        myConnection.disconnect();
                    } else
                        Log.e(TAG, "Error de conexión: "+myConnection.getResponseCode());
                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL httpbinEndpoint = new URL("https://httpbin.org/");
                    HttpsURLConnection myConnection = (HttpsURLConnection) httpbinEndpoint.openConnection();
                    String myData = "message=Hello";
                    myConnection.setDoOutput(true);
                    myConnection.getOutputStream().write(myData.getBytes());
                    HttpResponseCache myCache = HttpResponseCache.install(getCacheDir(), 100000L);
                    if (myCache.getHitCount() > 0) {
                        Log.e(TAG, "La caché esta trabajando");
                    }
                    Log.e(TAG, ""+myCache.getHitCount());
                    if (myConnection.getResponseCode() == 200) {
                        myConnection.setRequestMethod("POST");
                    } else
                        Log.e(TAG, "Error de conexión: "+myConnection.getResponseCode());
                    myConnection.disconnect();
                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }
}