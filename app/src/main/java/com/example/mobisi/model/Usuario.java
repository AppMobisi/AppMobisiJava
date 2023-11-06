package com.example.mobisi.model;

public class Usuario {
    private Integer id;
    private String cNome;
    private String cSenha;
    private String cEmail;
    private String cTelefone;
    private String cCpf;
    private String cCep;
    private String cRua;
    private String cEstado;
    private String cCidade;
    private String cFoto;
    private Integer iTipoDeficiencia;

    public Usuario(){

    }

    public Usuario(Integer id, String cNome, String cSenha, String cEmail, String cTelefone, String cCpf, String cCep, String cRua, String cEstado, String cCidade, String cFoto) {
        this.id = id;
        this.cNome = cNome;
        this.cSenha = cSenha;
        this.cEmail = cEmail;
        this.cTelefone = cTelefone;
        this.cCpf = cCpf;
        this.cCep = cCep;
        this.cRua = cRua;
        this.cEstado = cEstado;
        this.cCidade = cCidade;
        this.cFoto = cFoto;
    }

    public Integer getiTipoDeficiencia() {
        return iTipoDeficiencia;
    }

    public void setiTipoDeficiencia(Integer iTipoDeficiencia) {
        this.iTipoDeficiencia = iTipoDeficiencia;
    }

    public String getcFoto() {
        return cFoto;
    }

    public void setcFoto(String cFoto) {
        this.cFoto = cFoto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getcNome() {
        return cNome;
    }

    public void setcNome(String cNome) {
        this.cNome = cNome;
    }

    public String getcSenha() {
        return cSenha;
    }

    public void setcSenha(String cSenha) {
        this.cSenha = cSenha;
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

    public String getcRua() {
        return cRua;
    }

    public void setcRua(String cRua) {
        this.cRua = cRua;
    }

    public String getcEstado() {
        return cEstado;
    }

    public void setcEstado(String cEstado) {
        this.cEstado = cEstado;
    }

    public String getcCidade() {
        return cCidade;
    }

    public void setcCidade(String cCidade) {
        this.cCidade = cCidade;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "cNome='" + cNome + '\'' +
                ", cSenha='" + cSenha + '\'' +
                ", cEmail='" + cEmail + '\'' +
                ", cTelefone='" + cTelefone + '\'' +
                ", cCpf='" + cCpf + '\'' +
                ", cCep='" + cCep + '\'' +
                ", cRua='" + cRua + '\'' +
                ", cEstado='" + cEstado + '\'' +
                ", cCidade='" + cCidade + '\'' +
                '}';
    }
}
