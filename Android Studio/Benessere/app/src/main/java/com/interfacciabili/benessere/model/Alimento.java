package com.interfacciabili.benessere.model;

public class Alimento {
    //TODO Da usare nella versione della dieta flessibile
    private int id;
    private String nome;
    private String quantita;
    private String tipoPasto;
    private String giorno;

    public Alimento(int id, String nome, String quantita, String tipoPasto, String giorno) {
        this.id = id;
        this.nome = nome;
        this.quantita = quantita;
        this.tipoPasto = tipoPasto;
        this.giorno = giorno;
    }

    public Alimento() {
    }

    @Override
    public String toString() {
        return "Alimento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", quantita='" + quantita + '\'' +
                ", tipoPasto='" + tipoPasto + '\'' +
                ", giorno='" + giorno + '\'' +
                '}';
    }

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

    public String getQuantita() {
        return quantita;
    }

    public void setQuantita(String quantita) {
        this.quantita = quantita;
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
}
