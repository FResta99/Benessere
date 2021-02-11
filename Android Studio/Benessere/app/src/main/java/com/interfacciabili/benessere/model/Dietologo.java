package com.interfacciabili.benessere.model;

// Classe che descrive un dietologo con tutti i suoi attributi

import android.os.Parcel;
import android.os.Parcelable;

public class Dietologo extends Esperto implements Parcelable{
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private String sesso;
    private int eta;
    private String studio;

    public Dietologo(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Dietologo(String username, String password, String email, String nome, String cognome, String sesso, int eta, String studio) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.sesso = sesso;
        this.studio = studio;
    }

    public Dietologo(){

    }

    protected Dietologo(Parcel in) {
        username = in.readString();
        password = in.readString();
        email = in.readString();
        nome = in.readString();
        cognome = in.readString();
        sesso = in.readString();
        eta = in.readInt();
        studio = in.readString();
    }

    public static final Creator<Dietologo> CREATOR = new Creator<Dietologo>() {
        @Override
        public Dietologo createFromParcel(Parcel in) {
            return new Dietologo(in);
        }

        @Override
        public Dietologo[] newArray(int size) {
            return new Dietologo[size];
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

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    @Override
    public String toString() {
        return "Dietologo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", eta=" + eta +
                ", sesso='" + sesso + '\'' +
                ", studio='" + studio + '\'' +
                '}';
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
        dest.writeString(sesso);
        dest.writeInt(eta);
        dest.writeString(studio);
    }
}
