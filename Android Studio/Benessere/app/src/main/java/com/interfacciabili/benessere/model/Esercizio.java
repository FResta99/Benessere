package com.interfacciabili.benessere.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Esercizio implements Parcelable {
    private int id;
    private String nome;
    private int ripetizioni;
    private String giorno;
    private String spiegazione;

    public Esercizio() {
    }

    public Esercizio(int id, String nome, int ripetizioni, String giorno, String spiegazione) {
        this.id = id;
        this.nome = nome;
        this.ripetizioni = ripetizioni;
        this.giorno = giorno;
        this.spiegazione = spiegazione;
    }

    public Esercizio(String nome, int ripetizioni, String giorno, String spiegazione) {
        this.id = -1;
        this.nome = nome;
        this.ripetizioni = ripetizioni;
        this.giorno = giorno;
        this.spiegazione = spiegazione;
    }

    protected Esercizio(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        ripetizioni = in.readInt();
        giorno = in.readString();
        spiegazione = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeInt(ripetizioni);
        dest.writeString(giorno);
        dest.writeString(spiegazione);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Esercizio> CREATOR = new Creator<Esercizio>() {
        @Override
        public Esercizio createFromParcel(Parcel in) {
            return new Esercizio(in);
        }

        @Override
        public Esercizio[] newArray(int size) {
            return new Esercizio[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getRipetizioni() {
        return ripetizioni;
    }

    public void setRipetizioni(int ripetizioni) {
        this.ripetizioni = ripetizioni;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public String getSpiegazione() {
        return spiegazione;
    }

    public void setSpiegazione(String spiegazione) {
        this.spiegazione = spiegazione;
    }

    @Override
    public String toString() {
        return nome + " " + ripetizioni + ", " + giorno;
    }


}
