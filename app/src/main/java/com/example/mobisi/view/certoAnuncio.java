package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import com.example.mobisi.R;
import com.example.mobisi.tools.InternetConnectionReceiver;
import com.example.mobisi.view.webHome;

public class certoAnuncio extends AppCompatActivity implements InternetConnectionReceiver.ConnectionListener {
    private InternetConnectionReceiver connectionReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connectionReceiver = new InternetConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certo_anuncio);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                carregarTela();
                finish();
            }
        }, 3000);
    }


    public void carregarTela(){
        //Verificar Net
        Intent intent = new Intent(this, webHome.class);
        Bundle envelope = new Bundle();
        envelope.putBoolean("Internet", true);
        intent.putExtras(envelope);
        startActivity(intent);
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