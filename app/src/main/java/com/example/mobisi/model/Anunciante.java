package com.example.mobisi.model;

public class Anunciante {
    private String id;
    private String cNome;
    private String cEmail;
    private String cTelefone;
    private String cCpf;
    private String cCep;
    private Double nNota;
    private Integer iUsuarioId;

    public Anunciante( String cNome, String cEmail, String cTelefone, String cCpf, String cCep, Double nNota, Integer iUsuarioId) {
        this.cNome = cNome;
        this.cEmail = cEmail;
        this.cTelefone = cTelefone;
        this.cCpf = cCpf;
        this.cCep = cCep;
        this.nNota = nNota;
        this.iUsuarioId = iUsuarioId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcNome() {
        return cNome;
    }

    public void setcNome(String cNome) {
        this.cNome = cNome;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcTelefone() {
        return cTelefone;
    }

    public void setcTelefone(String cTelefone) {
        this.cTelefone = cTelefone;
    }

    public String getcCpf() {
        return cCpf;
    }

    public void setcCpf(String cCpf) {
        this.cCpf = cCpf;
    }

    public String getcCep() {
        return cCep;
    }

    public void setcCep(String cCep) {
        this.cCep = cCep;
    }

    public Double getnNota() {
        return nNota;
    }

    public void setnNota(Double nNota) {
        this.nNota = nNota;
    }

    public Integer getiUsuarioId() {
        return iUsuarioId;
    }

    public void setiUsuarioId(Integer iUsuarioId) {
        this.iUsuarioId = iUsuarioId;
    }
}
