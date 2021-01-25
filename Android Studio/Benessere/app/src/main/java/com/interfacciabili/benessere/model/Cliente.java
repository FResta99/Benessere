package com.interfacciabili.benessere.model;

// Classe che descrive un cliente con tutti i suoi attributi

import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable {
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

    protected Cliente(Parcel in) {
        username = in.readString();
        password = in.readString();
        email = in.readString();
        nome = in.readString();
        cognome = in.readString();
        eta = in.readInt();
        fotoProfilo = in.readString();
        sesso = in.readString();
        peso = in.readInt();
        altezza = in.readInt();
    }

    public static final Creator<Cliente> CREATOR = new Creator<Cliente>() {
        @Override
        public Cliente createFromParcel(Parcel in) {
            return new Cliente(in);
        }

        @Override
        public Cliente[] newArray(int size) {
            return new Cliente[size];
        }
    };

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
        return nome + " " + cognome + " ("+username+")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeInt(eta);
        dest.writeString(fotoProfilo);
        dest.writeString(sesso);
        dest.writeInt(peso);
        dest.writeInt(altezza);
    }
}
