package com.interfacciabili.benessere.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Alimento implements Parcelable {
    private int id;
    private String nome;
    private String porzione;
    private String tipoPorzione;
    private String tipoPasto;
    private String giorno;

    public Alimento() {
    }

    public Alimento(int id, String nome, String porzione, String tipoPorzione, String tipoPasto, String giorno) {
        this.id = id;
        this.nome = nome;
        this.porzione = porzione;
        this.tipoPorzione = tipoPorzione;
        this.tipoPasto = tipoPasto;
        this.giorno = giorno;
    }

    public Alimento(String nome, String porzione, String tipoPorzione, String tipoPasto, String giorno) {
        this.id = -1;
        this.nome = nome;
        this.porzione = porzione;
        this.tipoPorzione = tipoPorzione;
        this.tipoPasto = tipoPasto;
        this.giorno = giorno;
    }

    protected Alimento(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        porzione = in.readString();
        tipoPorzione = in.readString();
        tipoPasto = in.readString();
        giorno = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeString(porzione);
        dest.writeString(tipoPorzione);
        dest.writeString(tipoPasto);
        dest.writeString(giorno);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Alimento> CREATOR = new Creator<Alimento>() {
        @Override
        public Alimento createFromParcel(Parcel in) {
            return new Alimento(in);
        }

        @Override
        public Alimento[] newArray(int size) {
            return new Alimento[size];
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

    public String getPorzione() {
        return porzione;
    }

    public void setPorzione(String porzione) {
        this.porzione = porzione;
    }

    public String getTipoPorzione() {
        return tipoPorzione;
    }

    public void setTipoPorzione(String tipoPorzione) {
        this.tipoPorzione = tipoPorzione;
    }

    public String getTipoPasto() {
        return tipoPasto;
    }

    public void setTipoPasto(String tipoPasto) {
        this.tipoPasto = tipoPasto;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    @Override
    public String toString() {
        return nome + ", " + porzione + tipoPorzione + " " +  giorno;
    }


}