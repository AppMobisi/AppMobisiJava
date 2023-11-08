package com.example.mobisi.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiPostgres {

    @POST("/auth/signup")
    Call<AuthResponse> signUp(@Body UsuarioDto user);

    @POST("/auth/signin")
    Call<AuthResponse> singin(@Body SingInDto singIn);

    @PUT("/users/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body UpdateDto updateDto);
}
