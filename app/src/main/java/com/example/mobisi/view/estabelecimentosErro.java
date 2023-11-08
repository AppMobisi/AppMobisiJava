package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobisi.R;

public class estabelecimentosErro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimentos_erro);
    }

    public void voltar(View view) {
        Intent intent = new Intent(this, webHome.class);
        Bundle envelope = new Bundle();
        envelope.putBoolean("Internet", true);
        intent.putExtras(envelope);
        startActivity(intent);
    }
}