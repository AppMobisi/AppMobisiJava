package com.example.mobisi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.firebase.Firebase;
import com.example.mobisi.model.Anunciante;
import com.example.mobisi.model.Anuncios;
import com.example.mobisi.model.OnImageUploadListener;
import com.example.mobisi.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class pagamentoAnuncio extends AppCompatActivity {

    Anuncios anuncio;
    EditText nrm_cartao;
    EditText dataValidade;
    EditText cpf;
    EditText cvvEdit;
    EditText nmTitular;
    SqlLiteConnection sql;

    Uri foto;

    Spinner spinner;

    Integer tipoCartao;
    Bundle lerEnvelope;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
         lerEnvelope = getIntent().getExtras();

//            anuncio = new Anuncios(lerEnvelope.getString("cTitulo"),
//                                   lerEnvelope.getString("cDescricao"),
//                                   lerEnvelope.getDouble("nPreco"),
//                                   lerEnvelope.getInt("iTipoDeficiencia")
//            );
//
//            foto = Uri.parse(lerEnvelope.getString("cFoto"));

        } catch (Exception ex){

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_anuncio);

        nrm_cartao = findViewById(R.id.nmr_cartao);
        dataValidade = findViewById(R.id.data_validade);
        nmTitular = findViewById(R.id.nomeTitular);
        cpf = findViewById(R.id.CpfCnpj);
        sql = new SqlLiteConnection(this);
        spinner = findViewById(R.id.pagamento_categoria);
        cvvEdit = findViewById(R.id.cvv);




        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cartoes, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0 ){
                    tipoCartao = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nrm_cartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().replaceAll("[^0-9]", ""); // Remove caracteres não numéricos

                StringBuilder formattedText = new StringBuilder();

                for (int i = 0; i < text.length(); i++) {
                    if (i > 0 && i % 4 == 0) {
                        formattedText.append(" "); // Adiciona um espaço a cada 4 dígitos
                    }
                    formattedText.append(text.charAt(i));
                }

                nrm_cartao.removeTextChangedListener(this); // Remova temporariamente o TextWatcher
                nrm_cartao.setText(formattedText.toString());
                nrm_cartao.setSelection(formattedText.length()); // Define a posição do cursor
                nrm_cartao.addTextChangedListener(this); // Reative o TextWatcher

            }
        });

        dataValidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                String text = s.toString().replaceAll("[^0-9]", ""); // Remove caracteres não numéricos

                StringBuilder formattedText = new StringBuilder();

                for (int i = 0; i < text.length(); i++) {
                    if (i == 2) {
                        formattedText.append("/");
                    }
                    formattedText.append(text.charAt(i));
                }


                dataValidade.removeTextChangedListener(this); // Remova temporariamente o TextWatcher
                dataValidade.setText(formattedText.toString());
                dataValidade.setSelection(formattedText.length()); // Move o cursor para o final
                dataValidade.addTextChangedListener(this);
            }
        });

        cpf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                cpf.removeTextChangedListener(this); // Remova temporariamente o TextWatcher

                String text = s.toString().replaceAll("[^0-9]", ""); // Remove caracteres não numéricos

                if (text.length() > 0) {
                    StringBuilder formattedText = new StringBuilder();
                    formattedText.append(text.substring(0, Math.min(text.length(), 3)));
                    if (text.length() > 3) {
                        formattedText.append(".");
                        formattedText.append(text.substring(3, Math.min(text.length(), 6)));
                    }
                    if (text.length() > 6) {
                        formattedText.append(".");
                        formattedText.append(text.substring(6, Math.min(text.length(), 9)));
                    }
                    if (text.length() > 9) {
                        formattedText.append("-");
                        formattedText.append(text.substring(9, Math.min(text.length(), 11)));
                    }

                    cpf.setText(formattedText.toString());
                    cpf.setSelection(formattedText.length()); // Move o cursor para o final
                }

                cpf.addTextChangedListener(this); // Reative o TextWatcher
            }

        });



    }

    public void pagar(View view){
        if (verifica()){
//            uploadImageToFirebaseStorage(foto, new OnImageUploadListener() {
//                @Override
//                public void onUploadSuccess(String imageUri) {
//                    anuncio.setcFoto(imageUri);
//                    salvarAnuncioFirebase();
//                }
//
//                @Override
//                public void onUploadFailed() {
//                    Toast.makeText(pagamentoAnuncio.this, "Algo deu errado", Toast.LENGTH_LONG).show();
//                }
//            });

            Intent intent = new Intent(this, ProcessandoPagamento.class);
            intent.putExtras(lerEnvelope);
            startActivity(intent);
        }
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
                                        Toast.makeText(pagamentoAnuncio.this, "Falha ao criar Anunciante", Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            anuncio.setiAnuncianteId(usuario.getId());
                            adicionarAnuncio(db);
                        }
                    } else {
                        Toast.makeText(pagamentoAnuncio.this, "Falha ao verificar Anunciante", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    private void adicionarAnuncio(FirebaseFirestore db) {

        db.collection("Anuncios").add(anuncio)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(pagamentoAnuncio.this, "Anúncio criado", Toast.LENGTH_LONG).show();
                }).addOnFailureListener(ex -> {
                    Toast.makeText(pagamentoAnuncio.this, "Algo deu errado...", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(pagamentoAnuncio.this, "Erro ao obter URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onUploadFailed();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(pagamentoAnuncio.this, "Erro ao fazer upload: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                listener.onUploadFailed();
            });
        } else {
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show();
            listener.onUploadFailed();
        }
    }


    public boolean verifica(){
        if (nrm_cartao.getText().toString().isEmpty() ||
            tipoCartao == null ||
            dataValidade.getText().toString().isEmpty() ||
            cvvEdit.getText().toString().isEmpty() ||
            cpf.getText().toString().isEmpty()||
            nmTitular.getText().toString().isEmpty()
        ){
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }







}