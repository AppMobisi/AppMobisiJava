package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.model.Usuario;

public class Login extends AppCompatActivity {

    public SqlLiteConnection sql = new SqlLiteConnection(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void voltar(View view){
        Intent intent = new Intent(this, CadastroLogin.class);
        startActivity(intent);
        finish();
    }

    public void Logar(View view) {
        String email  = ((EditText) findViewById(R.id.login_email)).getText().toString();
        String senha = ((EditText) findViewById(R.id.login_senha)).getText().toString();

        if (email.isEmpty() || senha.isEmpty()){
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
        }
        else {
           //chama api;
        }
    }
}