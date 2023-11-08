package com.example.mobisi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobisi.R;
import com.example.mobisi.SqlLite.SqlLiteConnection;
import com.example.mobisi.model.ApiPostgres;
import com.example.mobisi.model.AuthResponse;
import com.example.mobisi.model.SingInDto;
import com.example.mobisi.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    public SqlLiteConnection sql = new SqlLiteConnection(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void voltar(View view){
        Intent intent = new Intent(this, CadastroLogin.class);
        startActivity(intent);
        finish();
    }

    public void Logar(View view) {
        String email  = ((EditText) findViewById(R.id.login_email)).getText().toString();
        String senha = ((EditText) findViewById(R.id.login_senha)).getText().toString();

        if (email.isEmpty() || senha.isEmpty()){
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
        }
        else {
            SingInDto singInDto = new SingInDto(email, senha);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://apipostgres.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPostgres service = retrofit.create(ApiPostgres.class);
            Call<AuthResponse> singInResponseCall = service.singin(singInDto);

            singInResponseCall.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful()){
                        AuthResponse authResponse = response.body();
                        if (authResponse.statusCode == 200) {
                            Usuario usuario = new Usuario(authResponse.data);
                            sql.salvar(usuario);
                            if (sql.verificarLogin()) {
                                Intent intent = new Intent(Login.this, webHome.class);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Internet", true);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(Login.this, authResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Login.this,"Erro ao chamar api", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(Login.this,"Erro ao chamar api", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}