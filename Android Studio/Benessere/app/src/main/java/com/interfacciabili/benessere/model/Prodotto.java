package com.interfacciabili.benessere.model;

public class Prodotto {

    private int id;
    private String nome;
    private int status;

    public Prodotto(int id, String nome, int status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
    }

    public Prodotto(String nome, int status) {
        this.id = -1;
        this.nome = nome;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
