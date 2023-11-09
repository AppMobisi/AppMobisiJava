package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.tools.InternetConnectionReceiver;

public class noInternet extends AppCompatActivity implements InternetConnectionReceiver.ConnectionListener {

    SqlLiteConnection sql;
    private InternetConnectionReceiver connectionReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        sql = new SqlLiteConnection(this);
        connectionReceiver = new InternetConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            Intent intent;
            if(sql.verificarLogin()){
                 intent = new Intent(noInternet.this, webHome.class);
                 Bundle bundle = new Bundle();
                 bundle.putBoolean("Internet", true);
                 intent.putExtras(bundle);
                startActivity(intent);
            }
            else{
                 intent = new Intent(noInternet.this, CadastroLogin.class);
                 startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionReceiver);
    }
}