package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;

public class MainActivity extends AppCompatActivity {
    
    public SqlLiteConnection sql = new SqlLiteConnection(this);
    public int SPLASH_DURATION = 8000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        boolean logado = sql.verificarLogin();
        splashScreen(logado);
    }

    public void splashScreen(boolean logado){
        
        Handler ha = new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                carregarTela(logado);
                finish();
            }
        }, SPLASH_DURATION);
    }

    public void carregarTela(boolean logado){
        Intent intent;
        if (logado) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null){
                intent = new Intent(this, teste.class);
            }
            else {
                intent = new Intent(this, webHome.class);
            }
        }
        else {
            intent = new Intent(this, CadastroLogin.class);

        }
        startActivity(intent);
    }
}