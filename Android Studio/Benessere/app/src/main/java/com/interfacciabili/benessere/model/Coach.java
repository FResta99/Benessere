package com.interfacciabili.benessere.model;

// Classe che descrive un coach con tutti i suoi attributi


import android.os.Parcel;
import android.os.Parcelable;

public class Coach extends Esperto implements Parcelable {
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private int eta;
    private String sesso;
    private String palestra;

    public Coach(String username, String password, String email, String nome, String cognome, int eta, String sesso, String palestra) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.sesso = sesso;
        this.palestra = palestra;
    }

    public Coach(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Coach(){

    }

    protected Coach(Parcel in) {
        username = in.readString();
        password = in.readString();
        email = in.readString();
        nome = in.readString();
        cognome = in.readString();
        eta = in.readInt();
        sesso = in.readString();
        palestra = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeInt(eta);
        dest.writeString(sesso);
        dest.writeString(palestra);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Coach> CREATOR = new Creator<Coach>() {
        @Override
        public Coach createFromParcel(Parcel in) {
            return new Coach(in);
        }

        @Override
        public Coach[] newArray(int size) {
            return new Coach[size];
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

    public String getPalestra() {
        return palestra;
    }

    public void setPalestra(String studio) {
        this.palestra = studio;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", eta=" + eta +
                ", sesso='" + sesso + '\'' +
                ", palestra='" + palestra + '\'' +
                '}';
    }
}

