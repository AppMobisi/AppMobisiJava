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
                ", cSenha VARCHAR"+
                ", cTelefone VARCHAR" +
                ", cEmail VARCHAR" +
                ", cCep VARCHAR"+
                ", cCpf VARCHAR"+
                ", cRua VARCHAR"+
                ", cEstado VARCHAR"+
                ", cCidade VARCHAR" +
                ", cFoto VARHCAR)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE Usuario");

        sqLiteDatabase.execSQL( "CREATE TABLE Usuario" +
                "( iId INTEGER PRIMARY KEY autoincrement" +
                ", cNome VARCHAR" +
                ", cSenha VARCHAR"+
                ", cTelefone VARCHAR" +
                ", cEmail VARCHAR" +
                ", cCep VARCHAR"+
                ", cCpf VARCHAR"+
                ", cRua VARCHAR"+
                ", cEstado VARCHAR"+
                ", cCidade VARCHAR)"
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
            valores.put("cSenha", usuario.getcSenha());
            valores.put("cTelefone", usuario.getcTelefone());
            valores.put("cEmail", usuario.getcEmail());
            valores.put("cCep", usuario.getcCep());
            valores.put("cCpf", usuario.getcCpf());
            valores.put("cRua", usuario.getcRua());
            valores.put("cEstado", usuario.getcEstado());
            valores.put("cCidade", usuario.getcCidade());

            long id = db.insert("Usuario", null, valores);
            return true;
        }catch (Exception ex){
            db.close();
            return false;
        }

    }

    public Usuario getInfos(){
        SQLiteDatabase db = getReadableDatabase();
        Usuario usuario = new Usuario();

            Cursor cursor = db.rawQuery("SELECT * FROM Usuario", null);
            if (cursor.moveToFirst()){
                usuario.setId(cursor.getInt(0));
                usuario.setcNome(cursor.getString(1));
                usuario.setcSenha(cursor.getString(2));
                usuario.setcTelefone(cursor.getString(3));
                usuario.setcEmail(cursor.getString(4));
                usuario.setcCep(cursor.getString(5));
                usuario.setcCpf(cursor.getString(6));
                usuario.setcRua(cursor.getString(7));
                usuario.setcEstado(cursor.getString(8));
                usuario.setcCidade(cursor.getString(9));
                usuario.setcFoto(cursor.getString(10));
            }
            return usuario;
    }


    public int atualizar(Usuario usuario){
        ContentValues contentValues = new ContentValues();
        contentValues.put("cNome", usuario.getcNome());
        contentValues.put("cEmail", usuario.getcEmail());
        contentValues.put("cTelefone", usuario.getcTelefone());
        contentValues.put("cCep", usuario.getcCep());
        contentValues.put("cCpf", usuario.getcCpf());
        if (usuario.getcEstado() != null){
            contentValues.put("cEstado", usuario.getcEstado());
            contentValues.put("cRua", usuario.getcRua());
            contentValues.put("cCidade", usuario.getcCidade());
        }


        SQLiteDatabase db = getWritableDatabase();

        int linhas = db.update("Usuario", contentValues, null, null);
        return linhas;
    }

    public int insertFoto(String cFoto){
        ContentValues values = new ContentValues();
        values.put("cFoto", cFoto);
        SQLiteDatabase db = getWritableDatabase();

        int linhas = db.update("Usuario", values, null, null);
        return linhas;
    }

    public void logout(){
       SQLiteDatabase db = getWritableDatabase();
       db.delete("Usuario", null, null);
    }
}
