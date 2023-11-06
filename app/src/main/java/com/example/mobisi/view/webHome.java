package com.example.mobisi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.model.InformacoesWeb;
import com.example.mobisi.model.Usuario;

import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class webHome extends AppCompatActivity {

    private final Map<Integer, ImagePair> imagePairs = new HashMap<>();
    private SqlLiteConnection sql;
    private ImageView fotoPerfil;
    private ImageView semFoto;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private int usuarioId;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String redireciona = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean internet = bundle.getBoolean("Internet");
            if (internet) {
                redireciona = "https://mobisiweb.onrender.com/";
            } else {
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

        Usuario usuario = sql.getInfos();
        if (usuario.getcFoto() != null) {
            fotoPerfil.setImageResource(R.drawable.foto_perfil_home);
            Uri foto = Uri.parse(usuario.getcFoto());
            Glide.with(this)
                    .load(foto)
                    .centerCrop()
                    .into(fotoPerfil);

            fotoPerfil.setVisibility(View.VISIBLE);
            semFoto.setVisibility(View.GONE);

        }


        // Inicialize o LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Inicialize o LocationListener
        locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onLocationChanged(@NonNull List<Location> locations) {
                LocationListener.super.onLocationChanged(locations);
            }

            @Override
            public void onFlushComplete(int requestCode) {
                LocationListener.super.onFlushComplete(requestCode);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LocationListener.super.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
            }
        };

        // Verifique e solicite permissão de localização se necessário
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Registre o LocationListener para receber atualizações de localização
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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


    public void abrirTela(String descricao) {
        if (descricao.contains("http")) {
            abrir(descricao);
        } else {
            abrir_off();
        }
    }


    @SuppressLint("JavascriptInterface")
    public void abrir(String url) {
        WebView safari = (WebView) findViewById(R.id.webView);
        safari.getSettings().setJavaScriptEnabled(true);
        InformacoesWeb informacoesWeb = new InformacoesWeb(usuarioId, latitude, longitude);
        safari.setWebViewClient(new WebViewClient() {
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
        safari.addJavascriptInterface(informacoesWeb, "informacoesWeb");
        safari.loadUrl(url);
    }

    public void abrir_off() {
        Intent intent = new Intent(this, Anuncio.class);
        startActivity(intent);

    }

    public void abrirPerfil(View view) {
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
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