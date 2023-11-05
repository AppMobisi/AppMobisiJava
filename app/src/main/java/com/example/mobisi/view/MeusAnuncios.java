package com.example.mobisi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.firebase.Firebase;
import com.example.mobisi.model.AnunciosFavoritos;
import com.example.mobisi.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MeusAnuncios extends AppCompatActivity {
    static ArrayList<AnunciosFavoritos> meusAnuncios;
    SqlLiteConnection sql;
    Usuario usuario;
    ListView lista;
    TextView msgErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);

        sql = new SqlLiteConnection(this);
        usuario = sql.getInfos();
        meusAnuncios = new ArrayList<>();
        lista = findViewById(R.id.lista);
        msgErro = findViewById(R.id.msgErro);
        CarregaArray();




    }

    public void CarregaArray() {
        FirebaseFirestore db = Firebase.getFirebaseFirestore();
        Query query = db.collection("Anuncios").whereEqualTo("iAnuncianteId", usuario.getId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String tituloAnuncio = document.getString("cTitulo");
                        Double preco = document.getDouble("nPreco");
                        String foto = document.getString("cFoto");

                        if (tituloAnuncio != null && preco != null && foto != null) {
                            AnunciosFavoritos anuncio = new AnunciosFavoritos(tituloAnuncio, preco, foto, usuario.getcNome());
                            meusAnuncios.add(anuncio);
                        }
                    }
                }
                chamaLista();
            }
        });



    }

    public void voltar(View view){
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
    }

    public void chamaLista(){
        if (meusAnuncios.isEmpty()) {
            lista.setVisibility(View.GONE);
            msgErro.setVisibility(View.VISIBLE);
        } else{
            AdapterAnuncios adapter = new AdapterAnuncios(this, meusAnuncios);
            lista.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}