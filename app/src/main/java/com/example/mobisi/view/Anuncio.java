package com.example.mobisi.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.firebase.Firebase;
import com.example.mobisi.model.*;
import com.example.mobisi.tools.InternetConnectionReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Anuncio extends AppCompatActivity implements InternetConnectionReceiver.ConnectionListener{

    SqlLiteConnection sql = new SqlLiteConnection(this);
    private Anuncios anuncios = new Anuncios();
    ImageView selected_image;
    EditText tituloEditText;
    EditText descricaoEditText;
    EditText cepEditText;
    EditText precoEditText;
    Integer iTipoDeficiencia;
    Uri foto;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connectionReceiver = new InternetConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);

        selected_image = findViewById(R.id.selected_image);
        ImageView add_image_icon = findViewById(R.id.add_image_icon);
        TextView incluirFotoTextView = findViewById(R.id.incluir_foto);
        FrameLayout image_container = findViewById(R.id.image_container);
        precoEditText = findViewById(R.id.anuncio_EditPreco);
        Spinner spinner = findViewById(R.id.anuncio_SpinnerCategoria);


        //Fotos
        ActivityResultLauncher<Intent> pickPhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        foto = data.getData();

                        Glide.with(this)
                                .load(foto)
                                .centerCrop()
                                .into(selected_image);

                        selected_image.setVisibility(View.VISIBLE);
                        add_image_icon.setVisibility(View.GONE);
                        incluirFotoTextView.setVisibility(View.GONE);
                    }
                }
        );


        ActivityResultLauncher<Intent> takePhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        foto = bitmapToUriConverter(imageBitmap);

                        Glide.with(this)
                                .load(imageBitmap)
                                .centerCrop()
                                .into(selected_image);

                        selected_image.setVisibility(View.VISIBLE);
                        add_image_icon.setVisibility(View.GONE);
                        incluirFotoTextView.setVisibility(View.GONE);
                    }
                }
        );




        image_container.setOnClickListener(v -> {
            CharSequence[] options = {"Tirar foto", "Escolher existente", "Sair"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Adicionar foto");
            builder.setItems(options, (dialog, item) -> {

                if (options[item].equals("Tirar foto")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        takePhotoLauncher.launch(takePicture);
                    }

                } else if (options[item].equals("Escolher existente")) {

                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhotoLauncher.launch(pickPhoto);

                } else if (options[item].equals("Sair")) {
                    dialog.dismiss();
                }
            });
            builder.show();
        });

        //Fim de fotos

        // Preço formatando

        precoEditText.setText("R$ ");
        precoEditText.setSelection(precoEditText.getText().length());
        precoEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("R$ ")) {
                    precoEditText.setText("R$ ");
                    Selection.setSelection(precoEditText.getText(), precoEditText.getText().length());
                }
            }
        });



        // Tipo deficiência

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorias, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               if (position != 0 ) {
                   iTipoDeficiencia = position;
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }

    public void voltar(View view) {
        Intent intent = new Intent(this, webHome.class);
        Bundle envelope = new Bundle();
        envelope.putBoolean("Internet", true);
        intent.putExtras(envelope);
        startActivity(intent);
    }

    public void anunciar(View view) {
        if (verifica()){
            Bundle envelope = new Bundle();
            envelope.putString("cTitulo", tituloEditText.getText().toString().toLowerCase());
            envelope.putString("cDescricao", descricaoEditText.getText().toString().toLowerCase());
            envelope.putDouble("nPreco",Double.parseDouble(precoEditText.getText().toString().replace("R$", "")));
            envelope.putString("cFoto", foto.toString());
            envelope.putInt("iTipoDeficiencia", iTipoDeficiencia);

            salvarAnuncioFirebase(envelope);
        }
    }

    public boolean verifica(){
        tituloEditText = findViewById(R.id.anuncio_EditTitulo);
        descricaoEditText = findViewById(R.id.anuncio_EditDescricao);
        precoEditText = findViewById(R.id.anuncio_EditPreco);

        if (tituloEditText.getText().toString().isEmpty() ||
            descricaoEditText.getText().toString().isEmpty() ||
            precoEditText.getText().toString().isEmpty() ||
            iTipoDeficiencia == null ){

            Toast.makeText(this, "Ei todos os campos são obrigatórios", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }



    private Uri bitmapToUriConverter(Bitmap mBitmap){
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "tempFile" + System.currentTimeMillis() + ".jpeg");
            FileOutputStream out;
            out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            uri = FileProvider.getUriForFile(Anuncio.this, getApplicationContext().getPackageName() + ".provider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }






    public void salvarAnuncioFirebase(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Preço da anúncio");
        builder.setTitle("Para prosseguir você precisa pagar uma pequena taxa de R$5,00");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Anuncio.this, pagamentoAnuncio.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    private InternetConnectionReceiver connectionReceiver;
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