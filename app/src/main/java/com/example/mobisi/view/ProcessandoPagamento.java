package com.example.mobisi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.firebase.Firebase;
import com.example.mobisi.model.Anunciante;
import com.example.mobisi.model.Anuncios;
import com.example.mobisi.model.OnImageUploadListener;
import com.example.mobisi.model.Usuario;
import com.example.mobisi.tools.InternetConnectionReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProcessandoPagamento extends AppCompatActivity implements InternetConnectionReceiver.ConnectionListener {

    Anuncios anuncio;
    Uri foto;
    SqlLiteConnection sql;

    private InternetConnectionReceiver connectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        connectionReceiver = new InternetConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        try {
            Bundle lerEnvelope = getIntent().getExtras();

            anuncio = new Anuncios(lerEnvelope.getString("cTitulo"),
                    lerEnvelope.getString("cDescricao"),
                    lerEnvelope.getDouble("nPreco"),
                    lerEnvelope.getInt("iTipoDeficiencia")
            );

            foto = Uri.parse(lerEnvelope.getString("cFoto"));

        } catch (Exception ex){

        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processando_pagamento);

        ImageView gif = (ImageView) findViewById(R.id.iconeProcessando);

        Glide.with(this)
                .load(R.drawable.carregando_pagamento)
                .centerCrop()
                .into(gif);

        sql = new SqlLiteConnection(this);

        pagar();
    }


    public void pagar(){
            uploadImageToFirebaseStorage(foto, new OnImageUploadListener() {
                @Override
                public void onUploadSuccess(String imageUri) {
                    anuncio.setcFoto(imageUri);
                    salvarAnuncioFirebase();
                }

                @Override
                public void onUploadFailed() {
                    Toast.makeText(ProcessandoPagamento.this, "Algo deu errado", Toast.LENGTH_LONG).show();
                    redirecionaErro();
                }
            });

    }

    public void salvarAnuncioFirebase() {


        Usuario usuario = sql.getInfos();
        FirebaseFirestore db = Firebase.getFirebaseFirestore();

        Query query = db.collection("Anunciantes").whereEqualTo("iUsuarioId", usuario.getId());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        Anunciante anunciante = new Anunciante(usuario.getcNome(),
                                usuario.getcEmail(),
                                usuario.getcTelefone(),
                                usuario.getcCpf(),
                                usuario.getcCep(),
                                0.0,
                                usuario.getId()
                        );

                        db.collection("Anunciantes").add(anunciante)
                                .addOnSuccessListener(documentReference -> {
                                    anuncio.setiAnuncianteId(usuario.getId());
                                    adicionarAnuncio(db);
                                })
                                .addOnFailureListener(ex -> {
                                    Toast.makeText(ProcessandoPagamento.this, "Falha ao criar Anunciante", Toast.LENGTH_LONG).show();
                                    redirecionaErro();
                                });
                    } else {
                        anuncio.setiAnuncianteId(usuario.getId());
                        adicionarAnuncio(db);
                    }
                } else {
                    Toast.makeText(ProcessandoPagamento.this, "Falha ao verificar Anunciante", Toast.LENGTH_LONG).show();
                    redirecionaErro();
                }
            }
        });

    }

    private void adicionarAnuncio(FirebaseFirestore db) {

        db.collection("Anuncios").add(anuncio)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ProcessandoPagamento.this, "Anúncio criado", Toast.LENGTH_LONG).show();
                    redirecionaCerto();
                }).addOnFailureListener(ex -> {
                            Toast.makeText(ProcessandoPagamento.this, "Algo deu errado...", Toast.LENGTH_LONG).show();
                            redirecionaErro();
                        }
                );

    }

    private void uploadImageToFirebaseStorage(Uri imageUri, final OnImageUploadListener listener) {
        if (imageUri != null) {
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads").child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    listener.onUploadSuccess(imageUrl);
                }).addOnFailureListener(e -> {
                    Toast.makeText(ProcessandoPagamento.this, "Erro ao obter URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onUploadFailed();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(ProcessandoPagamento.this, "Erro ao fazer upload: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                listener.onUploadFailed();
            });
        } else {
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show();
            listener.onUploadFailed();
        }
    }

    public void redirecionaErro(){
        Intent intent = new Intent(this, ErroAnuncio.class);
        startActivity(intent);
    }

    public void redirecionaCerto(){
        Intent intent = new Intent(this, certoAnuncio.class);
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