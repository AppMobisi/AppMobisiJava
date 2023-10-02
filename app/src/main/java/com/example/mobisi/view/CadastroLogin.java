package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobisi.R;

public class CadastroLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_login);
    }

    public void Logar(View view) {

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void Cadastrar(View view) {
        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
        finish();
    }
}