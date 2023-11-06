package com.example.mobisi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.firebase.Firebase;
import com.example.mobisi.model.AnunciosFavoritos;
import com.example.mobisi.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class anunciosFavoritos extends AppCompatActivity {

    static ArrayList<AnunciosFavoritos> anunciosFavoritos;
    SqlLiteConnection sql;
    Usuario usuario;
    ListView lista;
    TextView msgErro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios_favoritos);

        sql = new SqlLiteConnection(this);
        usuario = sql.getInfos();
        anunciosFavoritos= new ArrayList<>();
        lista = findViewById(R.id.lista);
        msgErro = findViewById(R.id.msgErro);
        CarregaArray();

    }


    public void CarregaArray() {
        FirebaseFirestore db = Firebase.getFirebaseFirestore();
        Query query = db.collection("anunciosFavoritos").whereEqualTo("iUsuarioId", usuario.getId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String cAnuncioId = document.getString("cAnuncioId");
                        DocumentReference documentReference = db.collection("Anuncios").document(cAnuncioId);
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String documentoId = document.getId();
                                        String tituloAnuncio = document.getString("cTitulo");
                                        Double preco = document.getDouble("nPreco");
                                        String foto = document.getString("cFoto");

                                        if (tituloAnuncio != null && preco != null && foto != null) {
                                            AnunciosFavoritos anuncio = new AnunciosFavoritos(documentoId, tituloAnuncio, preco, foto, usuario.getcNome());
                                            anunciosFavoritos.add(anuncio);
                                        }
                                    }
                                }
                                chamaLista();
                            }
                        });

                    }
                    chamaLista();

                }
            }
        });


    }


    public void chamaLista(){
        if (anunciosFavoritos.isEmpty()) {
            lista.setVisibility(View.GONE);
            msgErro.setVisibility(View.VISIBLE);
        } else{
            lista.setVisibility(View.VISIBLE);
            msgErro.setVisibility(View.GONE);
            AdapterAnuncios adapter = new AdapterAnuncios(this, anunciosFavoritos);
            lista.setAdapter(adapter);
            adapter.notifyDataSetChanged();

           lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   remover(i, adapter);
               }
           });
        }
    }

    public void voltar(View view){
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
    }

    public void remover(int posicao, AdapterAnuncios adapter){
        AnunciosFavoritos anuncioFavorito = anunciosFavoritos.get(posicao);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desfavoritar");
        builder.setMessage("Deseja mesmo desfavoritar esse anúncio?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletarAnuncio(anuncioFavorito.getDocumentoId(), posicao, adapter);
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

   public void  deletarAnuncio(String cAnuncioId, int posicao, AdapterAnuncios adapter){
        FirebaseFirestore db = Firebase.getFirebaseFirestore();
        Query query = db.collection("anunciosFavoritos").whereEqualTo("cAnuncioId", cAnuncioId).whereEqualTo("iUsuarioId", usuario.getId());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        db.collection("anunciosFavoritos").document(document.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    anunciosFavoritos.remove(posicao);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(com.example.mobisi.view.anunciosFavoritos.this, "Anúncio desfavoritado com sucesso", Toast.LENGTH_LONG).show();

                                    if (anunciosFavoritos.isEmpty()) {
                                        lista.setVisibility(View.GONE);
                                        msgErro.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
   }
}