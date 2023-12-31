package com.example.mobisi.model;

public class UsuarioDto {
    private String name;
    private String email;
    private String password;
    private String cpf;
    private String phone;
    private String cep;
    private String city;
    private String neighborhood;
    private String state;
    private int disabilityType;

    public UsuarioDto(){}

    public UsuarioDto( String name, String email, String password, String cpf, String phone, String cep, String city, String neighborhood, String state,  int disabilityType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.phone = phone;
        this.cep = cep;
        this.city = city;
        this.neighborhood = neighborhood;
        this.state = state;
        this.disabilityType = disabilityType;
    }

    public UsuarioDto(Usuario usuario){
        this.name = usuario.getcNome();
        this.email = usuario.getcEmail();
        this.password = usuario.getcSenha();
        this.cpf = usuario.getcCpf();
        this.phone = usuario.getcTelefone();
        this.cep = usuario.getcCep();
        this.city = usuario.getcCidade();
        this.neighborhood = usuario.getcRua();
        this.state = usuario.getcEstado();
        this.disabilityType = usuario.getiTipoDeficiencia();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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


    public int getDisabilityType() {
        return disabilityType;
    }

    public void setDisabilityType(int disabilityType) {
        this.disabilityType = disabilityType;
    }
}
