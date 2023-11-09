package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import com.example.mobisi.R;
import com.example.mobisi.tools.InternetConnectionReceiver;

public class CadastroLogin extends AppCompatActivity implements InternetConnectionReceiver.ConnectionListener {
    private InternetConnectionReceiver connectionReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_login);

        connectionReceiver = new InternetConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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

    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            // Se a conexão com a internet for perdida, redirecione para a tela de aviso
            Intent intent = new Intent(this, noInternet.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionReceiver);
    }

}