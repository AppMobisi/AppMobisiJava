package com.example.mobisi.model;

public class AnunciosFavoritos {
    private String Titulo;
    private Double Preco;
    private String Foto;

    public AnunciosFavoritos(String titulo, Double preco, String foto, String anunciante) {
        Titulo = titulo;
        Preco = preco;
        Foto = foto;
        Anunciante = anunciante;
    }

    private String Anunciante;

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public Double getPreco() {
        return Preco;
    }

    public void setPreco(Double preco) {
        Preco = preco;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getAnunciante() {
        return Anunciante;
    }

    public void setAnunciante(String anunciante) {
        Anunciante = anunciante;
    }
}
