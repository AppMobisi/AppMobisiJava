package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.model.Usuario;

import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.Map;

public class webHome extends AppCompatActivity {

    private final Map<Integer, ImagePair> imagePairs = new HashMap<>();
    private SqlLiteConnection sql;
    private ImageView fotoPerfil;
    private ImageView semFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String redireciona = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle  != null){
            boolean internet = bundle.getBoolean("Internet");
            if (internet){
                redireciona = "https://mobisiweb.onrender.com/";
            }else{
                redireciona = "perfil";
            }
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_home);
        abrirTela(redireciona);

        sql = new SqlLiteConnection(this);
        imagePairs.put(R.id.home_azul, new ImagePair(R.drawable.home_azul, R.drawable.home_cinza));
        imagePairs.put(R.id.anuncio_cinza, new ImagePair(R.drawable.anuncio_azul, R.drawable.anuncio_cinza));
        imagePairs.put(R.id.estabelecimento_cinza, new ImagePair(R.drawable.estabelecimento_azul, R.drawable.estabelecimento_cinza));
        imagePairs.put(R.id.anunciar_cinza, new ImagePair(R.drawable.anunciar_azul, R.drawable.anunciar_cinza));

        for (Integer id : imagePairs.keySet()) {
            ImageView imageView = findViewById(id);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleImageClick((ImageView) v);
                    abrirTela(((ImageView) v).getContentDescription().toString());
                }


            });
        }

        fotoPerfil = findViewById(R.id.perfil_foto);
        semFoto = findViewById(R.id.perfil);

        Usuario usuario =  sql.getInfos();
        if (usuario.getcFoto() != null){
            fotoPerfil.setImageResource(R.drawable.foto_perfil_home);
            Uri foto = Uri.parse(usuario.getcFoto());
            Glide.with(this)
                    .load(foto)
                    .centerCrop()
                    .into(fotoPerfil);

            fotoPerfil.setVisibility(View.VISIBLE);
            semFoto.setVisibility(View.GONE);

        }
    }
    private void handleImageClick(ImageView clickedImage) {
        for (Map.Entry<Integer, ImagePair> entry : imagePairs.entrySet()) {
            ImageView imageView = findViewById(entry.getKey());
            ImagePair pair = entry.getValue();

            if (imageView == clickedImage) {
                imageView.setImageResource(pair.blueImage);
            } else {
                imageView.setImageResource(pair.grayImage);
            }
        }
    }



    public void abrirTela(String descricao){
       if (descricao.contains("http")){
           abrir(descricao);
       }else{
           abrir_off();
       }
    }

    public void abrir(String url){
        WebView safari = (WebView) findViewById(R.id.webView);
        safari.getSettings().setJavaScriptEnabled(true);
        safari.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
            }
        });
        safari.loadUrl(url);
    }

    public void abrir_off(){
        Intent intent = new Intent(this, Anuncio.class);
        startActivity(intent);

    }

    public void abrirPerfil(View view) {
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
    }

    private static class ImagePair {
        int blueImage;
        int grayImage;

        ImagePair(int blueImage, int grayImage) {
            this.blueImage = blueImage;
            this.grayImage = grayImage;
        }
    }
}