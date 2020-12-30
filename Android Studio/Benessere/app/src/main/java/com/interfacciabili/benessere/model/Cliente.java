package com.interfacciabili.benessere.model;

// Classe che descrive un cliente con tutti i suoi attributi

public class Cliente {
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private int eta;
    private String fotoProfilo;
    private String sesso;
    private int peso;
    private int altezza;

    public Cliente(String username, String password, String email, String nome, String cognome, int eta, String fotoProfilo, String sesso, int peso, int altezza) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.fotoProfilo = fotoProfilo;
        this.sesso = sesso;
        this.peso = peso;
        this.altezza = altezza;
    }

    public Cliente(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Cliente(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public String getFotoProfilo() {
        return fotoProfilo;
    }

    public void setFotoProfilo(String fotoProfilo) {
        this.fotoProfilo = fotoProfilo;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getAltezza() {
        return altezza;
    }

    public void setAltezza(int altezza) {
        this.altezza = altezza;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", eta=" + eta +
                ", fotoProfilo='" + fotoProfilo + '\'' +
                ", sesso='" + sesso + '\'' +
                ", peso=" + peso +
                ", altezza=" + altezza +
                '}';
    }
}
