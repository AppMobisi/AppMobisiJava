package com.example.mobisi.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mobisi.model.Usuario;

public class SqlLiteConnection extends SQLiteOpenHelper {
    public SqlLiteConnection(@Nullable Context context) {
        super(context, "mobisi.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( "CREATE TABLE Usuario" +
                                "( iId INTEGER PRIMARY KEY autoincrement" +
                                ", cNome VARCHAR" +
                                ", cTelefone VARCHAR" +
                                ", cEmail VARCHAR" +
                                ", cCep VARCHAR"+
                                ", cCpf VARCHAR);"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE Usuario");

        sqLiteDatabase.execSQL( "CREATE TABLE Usuario" +
                "( iId INTEGER PRIMARY KEY autoincrement" +
                ", cNome VARCHAR" +
                ", cTelefone VARCHAR" +
                ", cEmail VARCHAR" +
                ", cCep VARCHAR"+
                ", cCpf VARCHAR)"
        );
    }

    public boolean verificarLogin(){
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Usuario", null);
            if (cursor.moveToFirst()){
                return true;
            }
        }catch (Exception ex){
            db.close();
            return  false;
        }
        db.close();
        return false;
    }

    public boolean salvar(Usuario usuario){
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues valores = new ContentValues();
            valores.put("cNome", usuario.getcNome());
            valores.put("cTelefone", usuario.getcTelefone());
            valores.put("cEmail", usuario.getcEmail());
            valores.put("cCep", usuario.getcCep());
            valores.put("cCpf", usuario.getcCpf());


            long id = db.insert("Usuario", null, valores);
            return true;
        }catch (Exception ex){
            db.close();
            return false;
        }

    }
}
