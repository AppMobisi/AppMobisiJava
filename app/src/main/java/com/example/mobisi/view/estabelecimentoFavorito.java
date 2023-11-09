package com.example.mobisi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import com.example.mobisi.model.EstabelecimentoFavoritos;
import com.example.mobisi.model.Usuario;
import com.example.mobisi.tools.InternetConnectionReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class estabelecimentoFavorito extends AppCompatActivity implements InternetConnectionReceiver.ConnectionListener {
    static ArrayList<EstabelecimentoFavoritos> estabelecimentoFavoritos;
    SqlLiteConnection sql;
    Usuario usuario;
    ListView lista;
    TextView msgErro;
    private InternetConnectionReceiver connectionReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        connectionReceiver = new InternetConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento_favorito);

        sql = new SqlLiteConnection(this);
        usuario = sql.getInfos();
        estabelecimentoFavoritos= new ArrayList<>();
        lista = findViewById(R.id.lista);
        msgErro = findViewById(R.id.msgErro);
        CarregaArray();

    }


    public void CarregaArray() {
        FirebaseFirestore db = Firebase.getFirebaseFirestore();
        Query query = db.collection("estabelecimentosFavoritos").whereEqualTo("iUsuarioId", usuario.getId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot document : querySnapshot){
                        String DocumentoId = document.getId();
                        String nome = document.getString("cNome");
                        Float nota =  Float.parseFloat(document.getDouble("nNota").toString());
                        String foto = document.getString("cFoto");
                        String rua = document.getString("cEndereco");

                        if (nome != null && nota != null && foto != null && rua != null){
                            EstabelecimentoFavoritos estabelecimentoFavorito = new EstabelecimentoFavoritos(DocumentoId, nome, rua, foto, nota);
                            estabelecimentoFavoritos.add(estabelecimentoFavorito);
                        }
                    }
                }
                chamaLista();
            }
        });


    }


    public void chamaLista(){
        if (estabelecimentoFavoritos.isEmpty()) {
            lista.setVisibility(View.GONE);
            msgErro.setVisibility(View.VISIBLE);
        } else{
            AdapterEstabelecimentos adapter = new AdapterEstabelecimentos(this, estabelecimentoFavoritos);
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

    public void remover(int posicao, AdapterEstabelecimentos adapter){
        EstabelecimentoFavoritos estabelecimentoFavoritos1= estabelecimentoFavoritos.get(posicao);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desfavoritar");
        builder.setMessage("Deseja mesmo desfavoritar esse establecimento?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletarEstabelecimento(estabelecimentoFavoritos1.getDocumentoId(), posicao, adapter);
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


    public void  deletarEstabelecimento(String DocumentoId, int posicao, AdapterEstabelecimentos adapter){
        FirebaseFirestore db = Firebase.getFirebaseFirestore();
        db.collection("estabelecimentosFavoritos").document(DocumentoId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    estabelecimentoFavoritos.remove(posicao);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(estabelecimentoFavorito.this, "Estabelecimentos desfavoritado com sucesso", Toast.LENGTH_LONG).show();

                    if (estabelecimentoFavoritos.isEmpty()) {
                        lista.setVisibility(View.GONE);
                        msgErro.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

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