package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.model.Endereco;
import com.example.mobisi.model.ApiPostgres;
import com.example.mobisi.model.AuthResponse;
import com.example.mobisi.model.Usuario;
import com.example.mobisi.model.UsuarioDto;
import com.example.mobisi.model.ViaCep;
import com.example.mobisi.tools.InternetConnectionReceiver;
import com.example.mobisi.tools.MaskEnum;
import com.example.mobisi.tools.MaskFormatter;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Cadastro extends AppCompatActivity implements InternetConnectionReceiver.ConnectionListener {

    Usuario usuario = new Usuario();
    primeiro_cadastro pc = primeiro_cadastro.newInstance();
    segundo_cadastro sc = segundo_cadastro.newInstance();
    Endereco endereco;
    String decisao = "continua";

    SqlLiteConnection sql = new SqlLiteConnection(this);
    private InternetConnectionReceiver connectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connectionReceiver = new InternetConnectionReceiver(this);
        registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apipostgres.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiPostgres service = retrofit.create(ApiPostgres.class);
        UsuarioDto post = new UsuarioDto(usuario);
        Call<AuthResponse> singUpResponseCall = service.signUp(post);
        ((ProgressBar) findViewById(R.id.carregarSingup)).setVisibility(View.VISIBLE);
        singUpResponseCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if(response.isSuccessful()){
                    AuthResponse authResponse = response.body();
                    if (authResponse.statusCode == 201) {
                        usuario.setId(authResponse.data.id);
                        ((ProgressBar) findViewById(R.id.carregarSingup)).setVisibility(View.GONE);
                        salvarSqlLIte();

                    } else{
                        Toast.makeText(Cadastro.this, authResponse.message, Toast.LENGTH_SHORT).show();
                        ((ProgressBar) findViewById(R.id.carregarSingup)).setVisibility(View.GONE);
                    }
                } else{

                    Toast.makeText(Cadastro.this,  "Email já cadastrado", Toast.LENGTH_SHORT).show();
                    ((ProgressBar) findViewById(R.id.carregarSingup)).setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(Cadastro.this,"Erro ao chamar api", Toast.LENGTH_SHORT).show();
                ((ProgressBar) findViewById(R.id.carregarSingup)).setVisibility(View.GONE);
            }
        });



    }

    public void salvarSqlLIte(){
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
        String nome = ((EditText) findViewById(R.id.NomeAtualizar)).getText().toString();
        String email = ((EditText) findViewById(R.id.EmailAtualizar)).getText().toString();
        String senha = ((EditText) findViewById(R.id.cadastro_senha2)).getText().toString();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){

            showToast("Todos os campos são obrigatórios");
        } else if (!email.contains("@") || !email.contains(".com")) {
            showToast("Formato de E-mail Inválido");
        } else {
            continuaFluxo(nome, email, senha);
        }
    }

    public void verificarSegunda(){
        String telefone = ((EditText) findViewById(R.id.TelefoneAtualizar)).getText().toString();
        String cpf = ((EditText) findViewById(R.id.CpfAtualizar)).getText().toString().replace(".", "").replace("-", "");
        String cep = ((EditText) findViewById(R.id.CepAtualizar)).getText().toString();
        EditText cpfEditText = findViewById(R.id.CpfAtualizar);

        MaskFormatter maskFormatterCpf = new MaskFormatter(MaskEnum.CPF.getMask(), cpfEditText);



        if(telefone.isEmpty() || cpf.isEmpty() || cep.isEmpty() || usuario.getiTipoDeficiencia() == null) {
            showToast("Todos os campos são obrigatórios");
        } else{
            if (maskFormatterCpf.isMaskMatching()){
                if (validaCPF(cpf)){
                    VerificaCep();
                }else {
                    showToast("Cpf errado");
                }
            }
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
            String telefone = ((EditText) findViewById(R.id.TelefoneAtualizar)).getText().toString();
            String cpf = ((EditText) findViewById(R.id.CpfAtualizar)).getText().toString();

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
        if(decisao == "continua") {
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

    public void setiTipoDeficiencia(int iTipoDeficiencia) {
        usuario.setiTipoDeficiencia(iTipoDeficiencia);
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