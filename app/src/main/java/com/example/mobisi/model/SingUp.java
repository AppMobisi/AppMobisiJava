package com.example.mobisi.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface SingUp {

    @POST("/auth/signup")
    Call<SingUpResponse> signUp(@Body UsuarioDto user);
}
