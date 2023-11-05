package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.model.Endereco;
import com.example.mobisi.model.Usuario;
import com.example.mobisi.model.ViaCep;
import com.google.firestore.bundle.BundledQueryOrBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class editarPerfil extends AppCompatActivity {

    SqlLiteConnection sql;
    Endereco endereco;
    Bundle bundle;
    EditText nome;
    EditText email;
    EditText telefone;
    EditText cep;
    EditText cpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         bundle = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        nome = findViewById(R.id.NomeAtualizar);
        email = findViewById(R.id.EmailAtualizar);
        telefone = findViewById(R.id.TelefoneAtualizar);
        cep = findViewById(R.id.CepAtualizar);
        cpf = findViewById(R.id.CpfAtualizar);

        nome.setText(bundle.getString("Nome"));
        email.setText(bundle.getString("Email"));
        telefone.setText(bundle.getString("Telefone"));
        cep.setText(bundle.getString("Cep"));
        cpf.setText(bundle.getString("Cpf"));

        sql = new SqlLiteConnection(this);


    }

    public void voltar(View view){
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
    }




    public void verifica(View view){
        if (nome.getText().toString().isEmpty() ||
                email.getText().toString().isEmpty() ||
                telefone.getText().toString().isEmpty() ||
                cep.getText().toString().isEmpty() ||
                cpf.getText().toString().isEmpty()){

            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_LONG).show();
        } else if (cep.getText().toString() != bundle.get("Cep")){
             VerificaCep();
        } else{
            continuarAposVerifica();
        }
    }

    public void VerificaCep(){
        String[] inputCep = ((EditText) findViewById(R.id.CepAtualizar)).getText().toString().split("-");
        String cep = "";
        for (String numeros : inputCep){
            cep = cep + numeros;
        }

        // Cria uma instância do Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Cria um objeto de serviço a partir da interface
        ViaCep service = retrofit.create(ViaCep.class);

        // Inicializa a chamada à API
        Call<Endereco> enderecoCall = service.buscaEnderecoPor(cep);


        // Executa a chamada de maneira assíncrona
        enderecoCall.enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                if(response.isSuccessful()) {
                    endereco = response.body();
                    if (!endereco.erro) {
                        continuarAposVerifica();
                    }
                    Toast.makeText(editarPerfil.this, "Não foi possível encontrar o CEP.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(editarPerfil.this, "Não foi possível encontrar o CEP.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
                Toast.makeText(editarPerfil.this, "Occreu um erro ao buscar o CEP", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void continuarAposVerifica(){

            Usuario usuario = new Usuario();
            usuario.setcNome(nome.getText().toString());
            usuario.setcEmail(email.getText().toString());
            usuario.setcTelefone(telefone.getText().toString());
            usuario.setcCep(cep.getText().toString());
            usuario.setcCpf(cpf.getText().toString());

            if (endereco != null){
                usuario.setcCidade(endereco.localidade);
                usuario.setcRua(endereco.logradouro);
                usuario.setcEstado(endereco.uf);
            }

            int linhas = sql.atualizar(usuario);
            if (linhas > 0) {
                Toast.makeText(this, "Usuário atualizado com sucesso", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Algo deu errado ao tentar atualizar o seu usuário", Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(this, Perfil.class);
            startActivity(intent);

    }

}