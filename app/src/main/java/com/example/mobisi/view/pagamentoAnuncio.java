package com.example.mobisi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import com.example.mobisi.tools.InternetConnectionReceiver;
import com.example.mobisi.tools.MaskEnum;
import com.example.mobisi.tools.MaskFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class pagamentoAnuncio extends AppCompatActivity implements InternetConnectionReceiver.ConnectionListener{

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

    MaskFormatter maskFormatterNmrCartao;
    MaskFormatter maskFormatterDataValidade;
    MaskFormatter maskFormatterCpf;

    private InternetConnectionReceiver connectionReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        connectionReceiver = new InternetConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        try {
         lerEnvelope = getIntent().getExtras();
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


        maskFormatterNmrCartao = new MaskFormatter(MaskEnum.NUMERO_CARTAO.getMask(), nrm_cartao);
        nrm_cartao.addTextChangedListener(maskFormatterNmrCartao);

        maskFormatterDataValidade = new MaskFormatter(MaskEnum.DATA.getMask(), dataValidade);
        dataValidade.addTextChangedListener(maskFormatterDataValidade);

        maskFormatterCpf = new MaskFormatter(MaskEnum.CPF.getMask(), cpf);
        cpf.addTextChangedListener(maskFormatterCpf);

    }

    public void pagar(View view){
        if (verifica()){


            Intent intent = new Intent(this, ProcessandoPagamento.class);
            intent.putExtras(lerEnvelope);
            startActivity(intent);
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

        if(!maskFormatterCpf.isMaskMatching() || !maskFormatterDataValidade.isMaskMatching() || !maskFormatterNmrCartao.isMaskMatching()){
            return false;
        }

        return true;
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