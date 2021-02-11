package com.interfacciabili.benessere.model;

public class Prodotto {

    private int id;
    private String nome;
    private int status;
    private String cliente;

    public Prodotto(int id, String nome, int status, String cliente) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.cliente = cliente;
    }

    public Prodotto(String nome, int status, String cliente) {
        this.id = -1;
        this.nome = nome;
        this.status = status;
        this.cliente = cliente;
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return this.nome + " " + (this.status == 1 ? "acquistato" : "da acquistare");
    }
}
