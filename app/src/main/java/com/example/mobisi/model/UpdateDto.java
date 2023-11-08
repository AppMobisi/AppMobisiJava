package com.example.mobisi.model;

public class UpdateDto {
    private String name;
    private String email;
    private String phone;
    private String cep;
    private String city;
    private String neighborhood;
    private String state;

    public UpdateDto(String name, String email, String phone, String cep, String city, String neighborhood, String state) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cep = cep;
        this.city = city;
        this.neighborhood = neighborhood;
        this.state = state;
    }

    public UpdateDto(Usuario usuario){
        this.name = usuario.getcNome();
        this.email = usuario.getcEmail();
        this.phone = usuario.getcTelefone();
        this.cep = usuario.getcCep();
        this.city = usuario.getcCidade();
        this.neighborhood = usuario.getcRua();
        this.state = usuario.getcEstado();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
