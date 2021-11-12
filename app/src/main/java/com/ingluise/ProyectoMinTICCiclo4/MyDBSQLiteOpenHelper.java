package com.ingluise.ProyectoMinTICCiclo4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBSQLiteOpenHelper extends SQLiteOpenHelper {

    public MyDBSQLiteOpenHelper(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE imagenes(_id INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT, img BLOB)");
    }

    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionActual) {
        db.execSQL("DROP TABLE IF EXISTS imagenes");
        db.execSQL("CREATE TABLE imagenes(_id INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT, img BLOB)");
    }
}
