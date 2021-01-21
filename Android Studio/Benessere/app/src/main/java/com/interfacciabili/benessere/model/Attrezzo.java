package com.interfacciabili.benessere.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Attrezzo implements Parcelable {
    private String nome;
    private String descrizione;

    public Attrezzo (String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    protected Attrezzo(Parcel in) {
        nome = in.readString();
        descrizione = in.readString();
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(descrizione);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Attrezzo> CREATOR = new Creator<Attrezzo>() {
        @Override
        public Attrezzo createFromParcel(Parcel in) {
            return new Attrezzo(in);
        }

        @Override
        public Attrezzo[] newArray(int size) {
            return new Attrezzo[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}