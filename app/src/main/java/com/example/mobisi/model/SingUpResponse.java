package com.example.mobisi.model;

public class SingUpResponse {
    private int statusCode;
    private UsuarioResponseDto data;

    public SingUpResponse(int statusCode, UsuarioResponseDto data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public UsuarioResponseDto getData() {
        return data;
    }

    public void setData(UsuarioResponseDto data) {
        this.data = data;
    }
}
