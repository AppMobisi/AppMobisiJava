package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobisi.R;

public class ErroAnuncio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erro_anuncio);
    }


    public void redireciona(View view){
        Button button = (Button) view;
        String texto = button.getText().toString();
        Intent intent;

        if (texto.equals("Tentar novamente")) {
            intent = new Intent(this, Anuncio.class);
        } else{
            intent = new Intent(this, webHome.class);
            Bundle envelope = new Bundle();
            envelope.putBoolean("Internet", true);
            intent.putExtras(envelope);
        }

        startActivity(intent);

    }
}