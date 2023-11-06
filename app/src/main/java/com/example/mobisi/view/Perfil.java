package com.example.mobisi.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.firebase.Firebase;
import com.example.mobisi.model.Anuncios;
import com.example.mobisi.model.AnunciosFavoritos;
import com.example.mobisi.model.EstabelecimentoFavoritos;
import com.example.mobisi.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Perfil extends AppCompatActivity {

    SqlLiteConnection sql;
    Usuario usuario;
    TextView nome;
    TextView email;
    TextView telefone;
    TextView cep;
    TextView cpf;
    ImageView selected_image;
    ImageView semFoto;



    private static final int CAMERA_PERMISSION_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nome = findViewById(R.id.Nome);
        email = findViewById(R.id.email);
        telefone = findViewById(R.id.telefone);
        cep = findViewById(R.id.cep);
        cpf = findViewById(R.id.cpf);
        selected_image = findViewById(R.id.selected_image);
        semFoto = findViewById(R.id.foto_perfil);
        FrameLayout image_container = findViewById(R.id.image_container);



        sql = new SqlLiteConnection(this);
         usuario = sql.getInfos();

        nome.setText(usuario.getcNome());
        email.setText(usuario.getcEmail());
        telefone.setText(usuario.getcTelefone());
        cep.setText(usuario.getcCep());
        cpf.setText(usuario.getcCpf());

        if (usuario.getcFoto() != null){
            Uri foto = Uri.parse(usuario.getcFoto());
            Glide.with(this)
                    .load(foto)
                    .centerCrop()
                    .into(selected_image);

            selected_image.setVisibility(View.VISIBLE);
            semFoto.setVisibility(View.GONE);
        }



        ActivityResultLauncher<Intent> takePhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");


                        Glide.with(this)
                                .load(imageBitmap)
                                .centerCrop()
                                .into(selected_image);

                        selected_image.setVisibility(View.VISIBLE);
                        semFoto.setVisibility(View.GONE);

                        Uri foto = bitmapToUriConverter(imageBitmap);
                        sql.insertFoto(foto.toString());
                    }
                }
        );

        ActivityResultLauncher<Intent> pickPhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri foto = data.getData();

                        Glide.with(this)
                                .load(foto)
                                .centerCrop()
                                .into(selected_image);

                        selected_image.setVisibility(View.VISIBLE);

                        sql.insertFoto(foto.toString());

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
    }

    public void editar(View view) {

            Bundle envelope = new Bundle();
            envelope.putString("Nome", nome.getText().toString());
            envelope.putString("Email", email.getText().toString());
            envelope.putString("Telefone", telefone.getText().toString());
            envelope.putString("Cep", cep.getText().toString());
            envelope.putString("Cpf", cpf.getText().toString());

            Intent intent = new Intent(this, editarPerfil.class);
            intent.putExtras(envelope);
            startActivity(intent);

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
            uri = FileProvider.getUriForFile(Perfil.this, getApplicationContext().getPackageName() + ".provider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }



    public void voltar(View view){
        Intent intent = new Intent(this, webHome.class);
        Bundle envelope = new Bundle();
        envelope.putBoolean("Internet", true);
        intent.putExtras(envelope);
        startActivity(intent);
    }


    public void anunciosFavoritos(View view) {
        Intent intent = new Intent(this, anunciosFavoritos.class);
        startActivity(intent);
    }

    public void meusAnuncios(View view) {
        Intent intent = new Intent(this, MeusAnuncios.class);
        startActivity(intent);
    }

    public void estabelecimentosFavoritos(View view){
        Intent intent = new Intent(this, estabelecimentoFavorito.class);
        startActivity(intent);
    }


    public void sair(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair da conta");
        builder.setMessage("Tem certeza de que deseja sair da sua conta?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sql.logout();
                Intent intent = new Intent(Perfil.this, CadastroLogin.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alerta = builder.create();
        alerta.show();
    }
}