package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.model.Endereco;
import com.example.mobisi.model.Usuario;
import com.example.mobisi.model.ViaCep;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Cadastro extends AppCompatActivity {

    Usuario usuario = new Usuario();
    primeiro_cadastro pc = primeiro_cadastro.newInstance();
    segundo_cadastro sc = segundo_cadastro.newInstance();
    Endereco endereco;
    String decisao = "continua";

    SqlLiteConnection sql = new SqlLiteConnection(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, pc)
                    .commitNow();
        }


    }

    public void cadastrar(){
        //Chama api

        //Coloca usu no banco local
        boolean salvou = sql.salvar(usuario);
        if (salvou){
            boolean internet = sql.verificarLogin();
            Bundle bundle = new Bundle();
            bundle.putBoolean("Internet", internet);
            Intent intent = new Intent(this, webHome.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }
    public void chamarVerifica(View view){
        String botao = ((Button) findViewById(R.id.cadastrar)).getText().toString();
        if (botao.equals("Continuar")) {
            verficarPrimeira();
        } else {
            verificarSegunda();
        }
    }

    public void verficarPrimeira() {
        String nome = ((EditText) findViewById(R.id.cadastro_nome)).getText().toString();
        String email = ((EditText) findViewById(R.id.cadastro_email)).getText().toString();
        String senha = ((EditText) findViewById(R.id.cadastro_senha2)).getText().toString();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){

            showToast("Todos os campos são obrigatórios");
        }
        else {
            continuaFluxo(nome, email, senha);
        }
    }

    public void verificarSegunda(){
        String telefone = ((EditText) findViewById(R.id.cadastro_telefone)).getText().toString();
        String cpf = ((EditText) findViewById(R.id.cadastro_cpf)).getText().toString();
        String cep = ((EditText) findViewById(R.id.cadastro_cep)).getText().toString();

        if(telefone.isEmpty() || cpf.isEmpty() || cep.isEmpty()){
           showToast("Todos os campos são obrigatórios");
        }else if (validaCPF(cpf)) {
            VerificaCep();
        } else{
            showToast("Seu cpf ta errado seu otario");
        }
    }
    public void VerificaCep(){
        String[] inputCep = ((EditText) findViewById(R.id.cadastro_cep)).getText().toString().split("-");
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
                     continuarAposVerifica(true);
                } else {
                   showToast("Não foi possível encontrar o CEP.");
                    continuarAposVerifica(false);
                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
                showToast("Occreu um erro ao buscar o CEP.");
                continuarAposVerifica(false);
            }
        });

    }

    public void continuarAposVerifica(boolean verifica){
        if (verifica){
            String telefone = ((EditText) findViewById(R.id.cadastro_telefone)).getText().toString();
            String cpf = ((EditText) findViewById(R.id.cadastro_cpf)).getText().toString();

            usuario.setcCep(endereco.cep);
            usuario.setcCidade(endereco.localidade);
            usuario.setcEstado(endereco.uf);
            usuario.setcRua(endereco.logradouro);
            usuario.setcTelefone(telefone);
            usuario.setcCpf(cpf);

            System.out.println(usuario);
            cadastrar();
        }
    }

    public void continuaFluxo(String nome, String email, String senha){
        usuario.setcNome(nome);
        usuario.setcEmail(email);
        usuario.setcSenha(senha);

        chamaFragment(decisao);

        Button botao = (Button) findViewById(R.id.cadastrar);
        botao.setText("Finalizar");

        decisao = "voltar";
    }

    public void chamaFragment(String decisao){
        if (decisao == "continua") {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, sc);
            transaction.commit();
        }
        else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, pc);
            transaction.commit();
        }
    }

    public void redirecionLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void voltar(View view) {
        if (decisao != "voltar") {
            Intent intent = new Intent(this, CadastroLogin.class);
            startActivity(intent);
            finish();
        }
        else {
            chamaFragment(decisao);
            Button botao = (Button) findViewById(R.id.cadastrar);
            botao.setText("Continuar");
            decisao = "continua";
        }
    }

    public void showToast(String mensagem){
        Toast.makeText(this,mensagem,Toast.LENGTH_SHORT).show();
    }

    public boolean validaCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches(cpf.charAt(0) + "{11}")) return false;

        int d1, d2;
        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }

        d1 = 11 - (soma % 11);
        if (d1 > 9) d1 = 0;

        soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        soma += d1 * 2;

        d2 = 11 - (soma % 11);
        if (d2 > 9) d2 = 0;

        return d1 == Character.getNumericValue(cpf.charAt(9)) && d2 == Character.getNumericValue(cpf.charAt(10));
    }
}