package com.interfacciabili.benessere.model;

public class Cibo {
    int id;
    String nome;
    int calorie;
    String tipoPasto;

    public Cibo() {
    }

    public Cibo(int id, String nome, int calorie, String tipoPasto) {
        this.id = id;
        this.nome = nome;
        this.calorie = calorie;
        this.tipoPasto = tipoPasto;
    }

    public Cibo( String nome, int calorie, String tipoPasto) {
        this.id = -1;
        this.nome = nome;
        this.calorie = calorie;
        this.tipoPasto = tipoPasto;
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

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getTipoPasto() {
        return tipoPasto;
    }

    public void setTipoPasto(String tipoPasto) {
        this.tipoPasto = tipoPasto;
    }

    @Override
    public String toString() {
        return nome + ", " + calorie + "kcal";
    }
}