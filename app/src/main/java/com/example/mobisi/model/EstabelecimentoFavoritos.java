package com.example.mobisi.model;

import android.content.Intent;

public class EstabelecimentoFavoritos {
    private String DocumentoId;
    private String Nome;
    private String Endereco;
    private String cFoto;
    private Float nNota;

    public EstabelecimentoFavoritos(String DocumentoId, String nome, String endereco, String cFoto, Float nNota) {
        this.DocumentoId = DocumentoId;
        Nome = nome;
        Endereco = endereco;
        this.cFoto = cFoto;
        this.nNota = nNota;
    }

    public String getDocumentoId() {
        return DocumentoId;
    }

    public void setDocumentoId(String documentoId) {
        DocumentoId = documentoId;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public String getcFoto() {
        return cFoto;
    }

    public void setcFoto(String cFoto) {
        this.cFoto = cFoto;
    }

    public Float getnNota() {
        return nNota;
    }

    public void setnNota(Float nNota) {
        this.nNota = nNota;
    }
}
