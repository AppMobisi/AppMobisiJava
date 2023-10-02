package com.example.mobisi.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCep {
    @GET("{cep}/json/")
    Call<Endereco> buscaEnderecoPor(@Path("cep") String cep);
}
